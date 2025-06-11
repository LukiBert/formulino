package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Race {
    private String season;
    private String round;
    private String url;
    private String raceName;
    private String date;
    private String time;

    @JsonProperty("Circuit")
    private Circuit circuit;

    // Getters and setters
    public String getSeason() { return season; }
    public void setSeason(String season) { this.season = season; }

    public String getRound() { return round; }
    public void setRound(String round) { this.round = round; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getRaceName() { return raceName; }
    public void setRaceName(String raceName) { this.raceName = raceName; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public Circuit getCircuit() { return circuit; }
    public void setCircuit(Circuit circuit) { this.circuit = circuit; }

    public LocalDate getRaceDate() {
        return date != null ? LocalDate.parse(date) : null;
    }

    public LocalTime getRaceTime() {
        return time != null ? LocalTime.parse(time.replace("Z", "")) : null;
    }

    @Override
    public String toString() {
        return "Race{" +
                "season='" + season + '\'' +
                ", round='" + round + '\'' +
                ", url='" + url + '\'' +
                ", raceName='" + raceName + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", circuit=" + circuit +
                '}';
    }
}
