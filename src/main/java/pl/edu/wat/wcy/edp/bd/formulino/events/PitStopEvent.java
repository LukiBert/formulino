package pl.edu.wat.wcy.edp.bd.formulino.events;

public class PitStopEvent implements RaceEvent {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String driverId;
    private int duration_ms;

    public PitStopEvent(String eventId, String eventName, String eventDescription, String driverId, int duration_ms) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.driverId = driverId;
        this.duration_ms = duration_ms;
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

    public int getDuration_ms() {
        return duration_ms;
    }
}
