package mock.server.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import mock.server.data.Event;

/* Client per a comprobar la recepció d'events desde el servidor,
 * Això es el que s'haurà d'implementar a l'aplicació
 */

public class RestClient {
	public static void main(String[] args) {
		// Items

		Client client = ClientBuilder.newClient();
		//WebTarget targetAdd = client.target("http://192.168.1.33:15000/").path("item/init");
		// Add items
		//targetAdd.request(MediaType.APPLICATION_JSON_TYPE).get();


		//WebTarget targetGetEvent = client.target("http:")
		WebTarget targetGetall = client.target("http://localhost:15000/").path("Events/getEvents");

		//String item = targetGetall.request(MediaType.APPLICATION_JSON_TYPE).get(new GenericType<String>(){} );
		List<Event> events = targetGetall.request(MediaType.APPLICATION_JSON_TYPE).get(  new GenericType<List<Event>>(){} );
		for (Event item : events) 
			System.out.println(events);
		
		//WebTarget targetGetone = client.target("http://localhost:15000/").path("Events/get/389");
		//Event event = targetGetone.request(MediaType.APPLICATION_JSON_TYPE).get(  new GenericType<Event>(){} );
		//System.out.println(event);

	}
}
