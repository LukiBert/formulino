package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Location {
    private String lat;
    @JsonProperty("long")
    private String longitude;
    private String locality;
    private String country;

    public String getLat() { return lat; }
    public void setLat(String lat) { this.lat = lat; }

    public String getLongitude() { return longitude; }
    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getLocality() { return locality; }
    public void setLocality(String locality) { this.locality = locality; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getLatitudeAsDouble() {
        return lat != null ? Double.parseDouble(lat) : null;
    }

    public Double getLongitudeAsDouble() {
        return longitude != null ? Double.parseDouble(longitude) : null;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat='" + lat + '\'' +
                ", longitude='" + longitude + '\'' +
                ", locality='" + locality + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
