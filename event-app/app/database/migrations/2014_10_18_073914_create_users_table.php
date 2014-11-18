<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateEventsTable extends Migration {

	/**
	 * Run the migrations.
	 *
	 * @return void
	 */
	public function up()
	{
        Schema::create('events', function($table)
        {
            $table->increments('id');
            $table->string('title');
            $table->text('description');
            $table->string('category');
            $table->time('time_start');
            $table->time('time_end')->nullable();
            $table->date('date_start');
            $table->date('date_end')->nullable();
            $table->float('latitude')->nullable();
            $table->float('longitude')->nullable();
            $table->string('venue')->nullable();
            $table->string('price')->nullable();
            $table->string('district')->nullable();
        });
	}

	/**
	 * Reverse the migrations.
	 *
	 * @return void
	 */
	public function down()
	{
        Schema::drop('events');
	}

}
