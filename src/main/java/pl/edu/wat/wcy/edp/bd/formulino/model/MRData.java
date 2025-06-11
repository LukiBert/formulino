package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
class MRData {
    @JsonProperty("RaceTable")
    private RaceTable raceTable;

    public RaceTable getRaceTable() { return raceTable; }
    public void setRaceTable(RaceTable raceTable) { this.raceTable = raceTable; }
}