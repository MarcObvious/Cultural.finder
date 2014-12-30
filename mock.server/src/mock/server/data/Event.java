package mock.server.data;

import java.awt.Image;
import java.util.Date;

/*Classe base d'events a la banda del servidor els passarem mitjançant json a l'aplicació*/

public class Event {
	private int id; //ID HAURIA DE SER UNIC I EXCLUSIU CUIDAO.
   
	private String name;
    private String description;

    private Date date_start;
    private Date date_end;

    private Image portada;

    private Double longitude; //Potser millor agafar classe Location
    private Double latitude;
    private String district;
    
    //Metode que crea un Event aleatori
    public void MockEventConstructor() {
    	this.id = 1 + (int)(Math.random() * ((1000 - 1)));
    	this.name = Integer.toString(id);
    	this.description = name + name + name;
    	
    	
    	this.date_start =  new Date(Math.abs(System.currentTimeMillis() + (long) Math.random()) );
    	this.date_end =  new Date(Math.abs(System.currentTimeMillis() + (long) Math.random()) );
    	while (date_end.before(date_start))
    		this.date_end =  new Date(Math.abs(System.currentTimeMillis() + (long) Math.random()) );
    	
    	this.portada = null;
    	
    	this.longitude = 1 + (double)(Math.random() * ((1000 - 1)));
    	this.latitude = 1 + (double)(Math.random() * ((1000 - 1)));
    	
    	this.district = null;
    	
    	
    }
    
    //Tots els events son aleatoris si no s'especifica el constructor.
    public Event() {
    	MockEventConstructor();
    }

    public Event(int id, String name, String description, Date date_start, Date date_end, String district, Double latitude, Double longitude) {
        this.id = id;
    	this.name = name;
        this.description = description;
        
        this.date_start = date_start;
        this.date_end = date_end;
        
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
        
        this.portada = null;
    }

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


    public Image getPortada() {
        return portada;
    }

    public void setPortada(Image portada) {
        this.portada = portada;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistricte(String districte) {
        this.district = districte;
    }

    @Override
    public String toString() {
        return "Event{" +
        		"id='" + id + '\''+
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", date_start=" + date_start +
                ", date_end=" + date_end +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", district='" + district + '\'' +
                '}';
    }
}
