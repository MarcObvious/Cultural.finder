<?php

use Illuminate\Console\Command;
use Symfony\Component\Console\Input\InputOption;
use Symfony\Component\Console\Input\InputArgument;
use Facebook\Entities\AccessToken;
use Facebook\FacebookSession;
use Facebook\FacebookAuthorizationException;

class testFB extends Command {

    private $userToken = 'CAATZCNaBmXHEBAAtjgtYHfjiZBancYLweUk8JCvAJFvdxZBxxpZCV85ZBLR01xZAsL5SvbTnRRLW8iZBMoFfxoExSfFQnZCcsZBeXQm6k18VYVbfxKv5KZASAAdQNUuddJ9yuPvDJZBa5FLZCRFXGm8jGnCF5gAUoF76rZCGwyoPsq5d3IyngmRST2kLqqB9G4eFsFqflTiAeDVYt3QapMcIAjnaZC';
    private $appKey = '1406505696255089';
    private $appSecretKey = 'f0bb2f8fed6d2c19b73399dc676417c7';

    /**
	 * The console command name.
	 *
	 * @var string
	 */
	protected $name = 'test:fb';

	/**
	 * The console command description.
	 *
	 * @var string
	 */
	protected $description = 'Command description.';

	/**
	 * Create a new command instance.
	 *
	 * @return void
	 */
	public function __construct()
	{
		parent::__construct();
	}

	/**
	 * Execute the console command.
	 *
	 * @return mixed
	 */
	public function fire()
	{
		//$facebook = new Ingestion_Facebook_Events('event');
        //$facebook->getData('barcelona');

        Redis::hmset('longUserToken::Facebook::test', 'token', 'sometoken', 'expire', 1234);
        /*FacebookSession::setDefaultApplication($this->appKey, $this->appSecretKey);

        try{
            $user = new AccessToken($this->userToken);
            $longLivedAccessToken = $user->extend();

        }catch(FacebookAuthorizationException $ex){
            print_r($ex->getMessage());
        }



        var_dump($longLivedAccessToken->isValid());

        print_r($longLivedAccessToken->getExpiresAt()->getTimestamp());*/

        /*$city = 'Barcelona';
        $tryStreet = false;
        $published = false;
        $event = (object) array
        (
            'description' => 'Disfruta del apasionante clásico del astillero y a conquistar esos tres puntos que nos acerquen más a la final',
        'end_time' => '2014-11-19T22:00:00-0500',
        'location' => 'Barcelona La Casa Grande',

        'venue' => (object) array
                (
                        'country' => 'Spain',
                        'street' => 'Roger de Flor 229'
                    )

        );
        if(isset($event->description)){

            if(isset($event->venue)){
                if(isset($event->venue->latitude)){
                    $isCity = $this->getDistanceFromLatLonInKm($event->venue->latitude, $event->venue->longitude);
                    var_dump($isCity);
                    if($isCity){

                        if(isset( $event->venue->city)) {

                            if (strpos(strtolower($event->venue->city), strtolower($city)) !== false) {

                                echo '************RECORD PUBLISHED 1****************' . "\n";
                                print_r($event);
                                $published = true;
                            }
                        }
                        elseif (isset($event->timezone)) {
                            if ($event->timezone == 'Europe/Madrid') {
                                echo '************RECORD PUBLISHED 2****************' . "\n";
                                print_r($event);
                                $published = true;
                            } // publish

                        }
                        elseif (isset($event->location)) {

                            if(strpos(strtolower($event->location), strtolower($city)) !== false) {
                                echo '************RECORD PUBLISHED 3****************' . "\n";
                                print_r($event);
                                $published = true;
                            }
                        }
                        else $tryStreet = true;

                    }

                }else $tryStreet = true;

                if(!$published){
                    if(isset($event->venue->street) and $tryStreet){
                        //do the same as in butxaca crawler and get the location using the google maps api
                        $coordinates = $this->getCoordinates($event->venue->street);
                        $isCity = $this->getDistanceFromLatLonInKm($coordinates[0], $coordinates[1]);

                        if($isCity){
                            echo '************RECORD PUBLISHED ****************' . "\n";
                            print_r($event);//publish
                        }
                        else echo '**********No Barcelona event************' . "\n";
                    }
                    else{
                        echo '**********No Barcelona event************' . "\n";
                        print_r($event);
                    }
                }

            }
        }
        else{
            print_r('************THIS EVENT WAS DELETED*************' . "\n");
        }*/






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

	/**
	 * Get the console command arguments.
	 *
	 * @return array
	 */
	protected function getArguments()
	{
		return array(
			//array('example', InputArgument::REQUIRED, 'An example argument.'),
		);
	}

	/**
	 * Get the console command options.
	 *
	 * @return array
	 */
	protected function getOptions()
	{
		return array(
			array('example', null, InputOption::VALUE_OPTIONAL, 'An example option.', null),
		);
	}

}
