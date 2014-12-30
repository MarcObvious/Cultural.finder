package mock.server.server;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import mock.server.data.Event;

@Path("/Events")
public class Services {

	private static List<Event> events = new ArrayList<Event>();
	
	
	//Agafem la llista d'events inicial
	public Services() {
		events = RestServer.getEvents();
		for (Event item : events) 
			System.out.println(item);
	}

	//Metode ADD algun event
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response create(Event event) {
		events.add(event);
		RestServer.setEvents(events);
		return Response.status(200).entity(event).build();
	}

	//Torna tots els events
	@GET
	@Path("/getEvents")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getALL() {
		return events;
	}

	
	//Torna un event o 404 si no s'ha trobat.
	@GET
	@Path("/get/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event get(@PathParam("id") int id) {
		for (Event event : events) {
			if (event.getId() == id) {
				return event;
			}
		}
		throw new WebApplicationException(404);
	}

}
