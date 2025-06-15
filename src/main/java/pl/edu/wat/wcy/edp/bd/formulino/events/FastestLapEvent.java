package pl.edu.wat.wcy.edp.bd.formulino.events;

public class FastestLapEvent implements RaceEvent {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String driverId;
    private String duration;
    private int durationMs;

    public FastestLapEvent(String eventId, String eventName, String eventDescription, String driverId, String duration, int durationMs) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.driverId = driverId;
        this.duration = duration;
        this.durationMs = durationMs;
    }

    @Override
    public String getEventId() {
        return eventId;
    }

    @Override
    public String getEventName() {
        return eventName;
    }

    @Override
    public String getEventDescription() {
        return eventDescription;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getDuration() {
        return duration;
    }

    public int getDurationMs() {
        return durationMs;
    }
}
