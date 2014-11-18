<?php
/**
 * Created by PhpStorm.
 * User: paufabregatpappaterra
 * Date: 08/11/14
 * Time: 15:44
 */

use Facebook\FacebookSession;
use Facebook\FacebookRequestException;
use Facebook\FacebookRequest;
use Facebook\Entities\AccessToken;
use Facebook\FacebookAuthorizationException;
use \PhpAmqpLib\Message\AMQPMessage;


class Ingestion_Facebook_Events {

    private $AMQP=null;
    private $queueName = 'Messages::In::Queue';
    private $exchangeName = 'Facebook::API::In';

    private $userToken = 'CAATZCNaBmXHEBAAtjgtYHfjiZBancYLweUk8JCvAJFvdxZBxxpZCV85ZBLR01xZAsL5SvbTnRRLW8iZBMoFfxoExSfFQnZCcsZBeXQm6k18VYVbfxKv5KZASAAdQNUuddJ9yuPvDJZBa5FLZCRFXGm8jGnCF5gAUoF76rZCGwyoPsq5d3IyngmRST2kLqqB9G4eFsFqflTiAeDVYt3QapMcIAjnaZC';
    private $session=null;
    private $appKey = '1406505696255089';
    private $appSecretKey = 'f0bb2f8fed6d2c19b73399dc676417c7';
    private $cityName = null;
    private $pageName = null;
    private $type = null;

    private $minutesExpire = null;
    private $isNextPage = true;

    private $lastCrawlingTime = null;
    private $isFirstTimeCrawl = null;

    public function __construct($type) {

        $this->AMQP = new ProviderAMQP($this->exchangeName, $this->queueName);

        FacebookSession::setDefaultApplication($this->appKey, $this->appSecretKey);

        $this->type = $type;

        if($type == 'page') $this->session = new FacebookSession($this->appKey .'|' . $this->appSecretKey); //access token would look like this: {app-id}|{app-secret} --> it doesn't expire!! :)


        if($type=='event'){
            $longLivedToken = Cache::get('longUserToken::Facebook');

            if($longLivedToken) {
                if($longLivedToken->getExpiresAt()->getTimestamp() < strtotime('+2 days')) {} //TODO send me a warning e-mail
                $this->session = new FacebookSession($longLivedToken);
            }
            else $this->session = new FacebookSession($this->manageTokens());

        }

        $this->validateSession();

        $this->lastCrawlKey = "lastCrawlingTime::Facebook::";
        //$this->minutesExpire = 2880;  //store last crawling time for 2 days
    }

    private function validateSession(){
        try {
            $this->session->validate();
        } catch (FacebookRequestException $ex) {
            echo $ex->getMessage();
        } catch (\Exception $ex) {
            echo $ex->getMessage();
        }
    }

    private function manageTokens(){

        $longLived = $this->getAccessTokenExtend();
        $expiresAt = $longLived->getExpiresAt()->getTimestamp();

        $minutesExpire = ($expiresAt - time())/60;
        Cache::add('longUserToken::Facebook', $longLived, $minutesExpire);

        return $longLived;


    }
    private function getAccessTokenExtend(){

        try{
            $user = new AccessToken($this->userToken);
            $longLivedAccessToken = $user->extend();

            return $longLivedAccessToken;

        }catch(FacebookAuthorizationException $ex){
            print_r($ex->getMessage());
            return null;
        }

    }

    private function buildPayload($record)
    {
        $package = new stdClass();
        $package->header = array(
            'source' => 'facebook::api'
        );

        $package->payload = $record;

        return new AMQPMessage(json_encode($package), null);
    }

    private function setCrawler($city){
        $this->cityName = ucfirst($city);
        $this->lastCrawlKey.= $city;

        $currentTimeCrawling = time(); //I want this value to be the one that will be checked by the next run
        $this->isFirstTimeCrawl = Cache::add($this->lastCrawlKey, time(), $this->minutesExpire); //if I get True it is the first time I run the crawler, false otherwise

        echo '************** LAST CRAWLING TIME **************';
        var_dump($this->isFirstTimeCrawl);
        print_r($this->isFirstTimeCrawl);
        if (!$this->isFirstTimeCrawl) $this->lastCrawlingTime = Cache::get($this->lastCrawlKey);

        echo '************** LAST CRAWLING TIME **************';
        print_r($this->lastCrawlingTime);

        return $currentTimeCrawling;
    }

    private function facebookRequest($request){
        try {
            $response = (new FacebookRequest($this->session, 'GET', $request))->execute();
            $response = $response->getGraphObject();
            return $response;

        } catch (FacebookRequestException $ex) {
            print_r('Facebook Exception' . "\n");
            echo $ex->getMessage();

            if(strpos($ex->getMessage(), 'User request limit reached' ) !== false) { //sleep 40 minutes -> We can make API calls again after 30 minutes
                $sleepuntil = strtotime('40 minutes');

                print_r('Starting sleep...' . "\n");
                time_sleep_until($sleepuntil);

                print_r('...Wake up! Sleep finished!' . "\n");

                return $this->facebookRequest($request);

            }
            else return $this->facebookRequest($request);

        } catch (\Exception $ex) {
            print_r('PHP Exception' . "\n");
            echo $ex->getMessage();

            if(strpos($ex->getMessage(), 'No route to host') !== false) return $this->facebookRequest($request);
            elseif(strpos($ex->getMessage(), 'SSL connection timeout') !== false) return $this->facebookRequest($request);
            elseif(strpos($ex->getMessage(),'Unknown SSL protocol error in connection')!== false) return $this->facebookRequest($request);
        }
    }


    public function getData($city){
        //TODO Optimize queries -> just get the fields we care
        $currentTimeCrawling = $this->setCrawler($city);
        $request = "/search?q=". $this->cityName . "&type=event&limit=250";
        $response = $this->facebookRequest($request);

        while($this->isNextPage) {
            if($events = $response->getProperty('data')){ //check if we receive an object from the response
                $events = $events->asArray();

                foreach ($events as $event) {
                    $this->getEvent($event);
                }

                if($next = $response->getProperty('paging')->getProperty('next')){

                    $patternUntil = '/&until=[a-zA-z0-9=&-]+/';
                    preg_match($patternUntil, $next, $matches);

                    $patternUnix = '/[0-9]+/';
                    preg_match($patternUnix, $matches[0], $unixTimeStamp);

                    if ($this->type == 'page') $request = "/". $this->pageName . "/feed?limit=250" . $matches[0];
                    if ($this->type == 'event') $request = "/search?limit=250until&type=event&q=". $this->cityName . $matches[0];

                    if($unixTimeStamp < time()) $this->isNextPage = false; //we stop retrieving post when we already got events related to the current date or one week after
                    else{
                        print_r('*********** NEW PAGE REQUEST **********' . "\n");
                        print_r($request . "\n\n\n\n");
                        $response = $this->facebookRequest($request);
                    }


                } else $this->isNextPage = false;

            }
            else $this->isNextPage = false;


        }

        Cache::put($this->lastCrawlKey, $currentTimeCrawling, $this->minutesExpire);

    }

    private function getEvent($eventShort){
        $request = '/' . $eventShort->id;
        $tryStreet = false;
        $isPublished = false;

        //TODO store new events in redis as key->value and check if they were already stored
        if(strtotime($eventShort->start_time) < strtotime('+8 days') and strtotime($eventShort->start_time) > time()){
            $response = $this->facebookRequest($request);
            $event = (object) $response->asArray();

            if(isset($event->name)){

                if(isset($event->venue)){
                    if(isset($event->venue->latitude)){
                        $isCity = $this->getDistanceFromLatLonInKm($event->venue->latitude, $event->venue->longitude);

                        if($isCity){

                            if(isset( $event->venue->city)) {

                                if (strpos(strtolower($event->venue->city), strtolower($this->cityName)) !== false) {

                                    echo '************RECORD PUBLISHED 1****************' . "\n";
                                    $this->AMQP->basicPublish($this->buildPayload($event), $this->exchangeName);
                                    $isPublished = true;
                                }
                            }
                            elseif (isset($event->timezone)) {
                                if ($event->timezone == 'Europe/Madrid') {
                                    echo '************RECORD PUBLISHED 2****************' . "\n";
                                    $this->AMQP->basicPublish($this->buildPayload($event), $this->exchangeName);
                                    $isPublished = true;
                                } // publish

                            }
                            elseif (isset($event->location)) {

                                if(strpos(strtolower($event->location), strtolower($this->cityName)) !== false) {
                                    echo '************RECORD PUBLISHED 3****************' . "\n";
                                    $this->AMQP->basicPublish($this->buildPayload($event), $this->exchangeName);
                                    $isPublished = true;
                                }
                            }
                            else $tryStreet = true;

                        }else echo '**********NO BARCELONA EVENT ************' . "\n";

                    }else $tryStreet = true;

                    if(!$isPublished){
                        if(isset($event->venue->street) and $tryStreet){
                            //do the same as in butxaca crawler and get the location using the google maps api
                            $coordinates = $this->getCoordinates($event->venue->street);
                            $isCity = $this->getDistanceFromLatLonInKm($coordinates[0], $coordinates[1]);

                            if($isCity){
                                echo '************RECORD PUBLISHED AFTER GET COORDINATES****************' . "\n";
                                $this->AMQP->basicPublish($this->buildPayload($event), $this->exchangeName);
                            }
                            else {
                                echo '**********NO BARCELONA EVENT AFTER GET COORDINATES************' . "\n";
                                //print_r($event->start_time . "\n\n");
                            }

                        }
                        else{
                            echo '**********NO BARCELONA EVENT ************' . "\n";
                            //print_r($event->start_time . "\n\n");
                        }
                    }

                }
            }
            else{
                print_r('************THIS EVENT WAS DELETED*************' . "\n");
            }

        }
        else{
            print_r('************THIS EVENT IS NOT IN TIME*************' . "\n");
            //print_r($eventShort->start_time . "\n\n");
        }



    }


    private function getDistanceFromLatLonInKm($lat1,$lon1){

        $lat2 = 41.38;
        $lon2 = 2.16;

        $R = 6371; #Radius of the earth in km
        $dLat = deg2rad($lat2 - $lat1);  #deg2rad below
        $dLon = deg2rad($lon2 - $lon1);
        $a = sin($dLat / 2) * sin($dLat / 2) + cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * sin($dLon / 2) * sin($dLon / 2);

        $c = 2 * atan2(sqrt($a), sqrt(1 - $a));
        $d = $R * $c; # Distance in km

        if ($d < 20)return True;
        else return False;
    }

    private function getCoordinates($address){

        $address = str_replace(" ", "+", $address); // replace all the white space with "+" sign to match with google search pattern

        $url = "http://maps.google.com/maps/api/geocode/json?sensor=false&address=$address";

        $response = file_get_contents($url);

        $json = json_decode($response,TRUE); //generate array object from the response from the web

        try{

            $lat = $json['results'][0]['geometry']['location']['lat'];
            $lon = $json['results'][0]['geometry']['location']['lng'];



            return array($lat,$lon);
        }
        catch(Exception $ex){
            echo $ex->getMessage();
            print_r($json);
        }


    }
} 