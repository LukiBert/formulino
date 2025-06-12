package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
class MRData {
    @JsonProperty("RaceTable")
    private RaceTable raceTable;
    @JsonProperty("DriverTable")
    private DriverTable driverTable;
    @JsonProperty("total")
    private String total;

    public String getTotal() { return total; }
    public void setTotal(String total) {this.total = total;}

    public RaceTable getRaceTable() { return raceTable; }
    public void setRaceTable(RaceTable raceTable) { this.raceTable = raceTable; }

    public DriverTable getDriverTable() { return driverTable; }
    public void setDriverTable(DriverTable driverTable) { this.driverTable = driverTable; }
}