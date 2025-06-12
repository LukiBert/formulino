package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lap {
    private int lapNumber;
    private String driverId;
    private int position;
    private String time;
    private String raceId;

    // Constructors
    public Lap() {}

    public Lap(int lapNumber, String driverId, int position, String time) {
        this.lapNumber = lapNumber;
        this.driverId = driverId;
        this.position = position;
        this.time = time;
    }

    // Static factory method to create from Jackson objects
    public static Lap fromTiming(LapData lapData, Timing timing) {
        Lap lap = new Lap();
        lap.setLapNumber(lapData.getLapNumberAsInt());
        lap.setDriverId(timing.getDriverId());
        lap.setPosition(timing.getPositionAsInt());
        lap.setTime(timing.getTime());
        return lap;
    }

    // Getters and setters
    public int getLapNumber() { return lapNumber; }
    public void setLapNumber(int lapNumber) { this.lapNumber = lapNumber; }

    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getRaceId() { return raceId; }
    public void setRaceId(String raceId) { this.raceId = raceId; }

    @Override
    public String toString() {
        return "Lap{" +
                "lapNumber=" + lapNumber +
                ", driverId='" + driverId + '\'' +
                ", position=" + position +
                ", time='" + time + '\'' +
                ", raceId='" + raceId + '\'' +
                '}';
    }
}