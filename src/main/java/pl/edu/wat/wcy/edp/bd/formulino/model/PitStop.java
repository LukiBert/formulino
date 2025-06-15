package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PitStop {

    private String race_id;

    @JsonProperty("driverId")
    private String driver_id;

    @JsonProperty("lap")
    private int lap_number;

    @JsonProperty("stop")
    private int stop_number;

    @JsonProperty("duration")
    private String duration;

    private int duration_ms;

    @JsonProperty("time")
    private String stop_time;

    public PitStop() {}

    public PitStop(String race_id, String driver_id, int lap_number, int stop_number, String duration, int duration_ms, String stop_time) {
        this.race_id = race_id;
        this.driver_id = driver_id;
        this.lap_number = lap_number;
        this.stop_number = stop_number;
        this.duration = duration;
        this.duration_ms = duration_ms;
        this.stop_time = stop_time;
    }

    public String getRace_id() {
        return race_id;
    }

    public void setRace_id(String race_id) {
        this.race_id = race_id;
    }

    public String getDriver_id() {
        return driver_id;
    }

    public void setDriver_id(String driver_id) {
        this.driver_id = driver_id;
    }

    public int getLap_number() {
        return lap_number;
    }

    public void setLap_number(int lap_number) {
        this.lap_number = lap_number;
    }

    public int getStop_number() {
        return stop_number;
    }

    public void setStop_number(int stop_number) {
        this.stop_number = stop_number;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getDuration_ms() {
        return duration_ms;
    }

    public void setDuration_ms(int duration_ms) {
        this.duration_ms = duration_ms;
    }

    public String getStop_time() {
        return stop_time;
    }

    public void setStop_time(String stop_time) {
        this.stop_time = stop_time;
    }

    @Override
    public String toString() {
        return "PitStop{" +
                "race_id='" + race_id + '\'' +
                ", driver_id='" + driver_id + '\'' +
                ", lap_number=" + lap_number +
                ", stop_number=" + stop_number +
                ", duration='" + duration + '\'' +
                ", stop_time='" + stop_time + '\'' +
                '}';
    }

    // Static factory method to create from Jackson objects
    public static PitStop fromFetchedPitStop(PitStop pitStop) {
        PitStop pit = new PitStop();
        pit.setDriver_id(pitStop.getDriver_id());
        pit.setLap_number(pitStop.getLap_number());
        pit.setStop_number(pitStop.getStop_number());

        String duration  = pitStop.getDuration();
        pit.setDuration(duration);
        pit.setDuration_ms(parseTimeToMs(duration));
        pit.setStop_time(pitStop.getStop_time());

        return pit;
    }

    private static int parseTimeToMs(String timeStr) {
        // Supports format like  "59.845"
        String[] parts = timeStr.split("[.]");
        int seconds = Integer.parseInt(parts[0]);
        int millis = Integer.parseInt(parts[1]);
        return seconds * 1000 + millis;
    }
}
