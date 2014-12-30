package letsbecool.culturalfinderandroidapp;

import android.media.Image;

import java.util.Date;
import java.util.IdentityHashMap;

public class Event {
    private String name;
    private String description;

    private Date date_start;
    private Date date_end;


    public Image getPortada() {
        return portada;
    }

    public void setPortada(Image portada) {
        this.portada = portada;
    }

    private Image portada;

    private Double longitude; //Potser millor agafar classe Location
    private Double latitude;
    private String district;

    public Event(String name, String description, Date date_start, Date date_end, String district, Double latitude, Double longitude) {
        this.name = name;
        this.description = description;
        this.date_start = date_start;
        this.date_end = date_end;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
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