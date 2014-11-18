<?php
/**
 * Created by PhpStorm.
 * User: paufabregatpappaterra
 * Date: 15/11/14
 * Time: 15:25
 */

use PhpAmqpLib\Connection\AMQPConnection;

class ProviderAMQP {

    private $channel = null;
    private $connection = null;

    public function __construct($exchangeQueue, $queueName){

        $this->connection = new AMQPConnection('localhost', 5672, 'guest', 'guest');
        $this->channel = $this->connection->channel();

        $this->setExchange($exchangeQueue);
        $this->setQueue($queueName);
        $this->setBinding($exchangeQueue, $queueName);
    }

    /**
    name: $exchange
    type: direct
    passive: false // don't check if a exchange with the same name exists
    durable: false // the exchange will not survive server restarts
    auto_delete: true //the exchange will be deleted once the channel is closed.
**/

    private function setExchange($exchangeQueue){
        $this->channel->exchange_declare($exchangeQueue, 'fanout', false, true, false);
    }

    /**
    name: $queue    // should be unique in fanout exchange.
    passive: false  // don't check if a queue with the same name exists
    durable: false // the queue will not survive server restarts
    exclusive: false // the queue might be accessed by other channels
    auto_delete: true //the queue will be deleted once the channel is closed.
    **/

    private function setQueue($queueName){
        $this->channel->queue_declare($queueName,  false, true, false, false);
    }

    private function setBinding($exchangeQueue, $queueName){
        $this->channel->queue_bind($queueName, $exchangeQueue);
    }

    /**
    queue: Queue from where to get the messages
    consumer_tag: Consumer identifier
    no_local: Don't receive messages published by this consumer.
    no_ack: Tells the server if the consumer will acknowledge the messages.
    exclusive: Request exclusive consumer access, meaning only this consumer can access the queue
    nowait: don't wait for a server response. In case of error the server will raise a channel
            exception
    callback: A PHP Callback
    **/

    /** When creating the consumer, the callback has to knowledge the messages and create all the logic to process each message depending on the source.
     Moreover we have to make sure that all callbacks are called so implement:
     * while(count($channel->callbacks)) {
        $channel->wait();
        }**/
    public function basicConsume($queueName, $callback){

        $this->channel->basic_consume($queueName, '', false, true, false, false, $callback);
    }

    public function basicPublish($msg, $exchangeQueue){  /** When publishing the msg has to be wrapped with new AMQPMessage**/
        $this->channel->basic_publish($msg, $exchangeQueue);
    }

    public function  shutdown()
    {
        $this->channel->close();
        $this->connection->close();
    }


} 