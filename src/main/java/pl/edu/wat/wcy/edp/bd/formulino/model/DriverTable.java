package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DriverTable {
    private String season;

    @JsonProperty("Drivers")
    private List<Driver> drivers;

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public List<Driver> getDrivers() { return drivers; }
    public void setDrivers(List<Driver> drivers) { this.drivers = drivers; }
}
