package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class RaceTable {
    private String season;

    @JsonProperty("Races")
    private List<Race> races;

    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }
    public List<Race> getRaces() { return races; }
    public void setRaces(List<Race> races) { this.races = races; }
}