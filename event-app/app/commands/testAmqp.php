<?php

use Illuminate\Console\Command;
use Symfony\Component\Console\Input\InputOption;
use Symfony\Component\Console\Input\InputArgument;
use \PhpAmqpLib\Message\AMQPMessage;

class testAmqp extends Command {

	/**
	 * The console command name.
	 *
	 * @var string
	 */
	protected $name = 'test:amqp';

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
		$AMQP = new ProviderAMQP('testExchange','Message::test');
        $msg= new AMQPMessage('my message', array('content_type' => 'text/plain', 'delivery_mode' => 2));
        $AMQP->basicPublish($msg, 'testExchange');
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
