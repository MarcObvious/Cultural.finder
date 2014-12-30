package mock.server.server;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import mock.server.data.Event;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import com.sun.net.httpserver.HttpServer;

/* Servidor, que donada una classe Event i una classe Services, et fa un servidor
 * que posa disponible una serie d'events
 * */

public class RestServer {
	//Llista d'events inicial 
	private static List<Event> events;
	

	public static void main(String[] args) throws IOException {
		
		//Numero aleatori d'Events
		events = new ArrayList<Event>();
		for (int i = 1 + (int) (Math.random() * ((20 - 1))); i > 0; i--) {
			events.add(new Event());
		}
		
		//Definim la URI a localhost, port 15000
		URI baseUri = UriBuilder.fromUri("http://localhost/").port(15000).build();
		
		//Li passem la classe on tindrem els metodes GET i POST
		ResourceConfig config = new ResourceConfig(Services.class);
		
		//Iniciem 
		HttpServer server = JdkHttpServerFactory.createHttpServer(baseUri, config);
		System.out.println("Server started...");
		
		
		//Services service = new Services();
	}
	
	public static List<Event> getEvents() {
		return events;
	}

	public static void setEvents(List<Event> events) {
		RestServer.events = events;
	}
}
