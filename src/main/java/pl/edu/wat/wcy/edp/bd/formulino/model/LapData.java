package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
class LapData {
    @JsonProperty("number")
    private String number;

    @JsonProperty("Timings")
    private List<Timing> timings;

    // Getters and setters
    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public List<Timing> getTimings() { return timings; }
    public void setTimings(List<Timing> timings) { this.timings = timings; }

    // Helper method to get lap number as integer
    public int getLapNumberAsInt() {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}