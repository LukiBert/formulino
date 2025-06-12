package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class Timing {
    @JsonProperty("driverId")
    private String driverId;

    @JsonProperty("position")
    private String position;

    @JsonProperty("time")
    private String time;

    // Getters and setters
    public String getDriverId() { return driverId; }
    public void setDriverId(String driverId) { this.driverId = driverId; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    // Helper method to get position as integer
    public int getPositionAsInt() {
        try {
            return Integer.parseInt(position);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}