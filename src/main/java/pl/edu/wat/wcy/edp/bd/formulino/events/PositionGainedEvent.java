package pl.edu.wat.wcy.edp.bd.formulino.events;

public class PositionGainedEvent implements RaceEvent {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String driverId;
    private int gainedPositions;

    public PositionGainedEvent(String eventId, String eventName, String eventDescription, String driverId, int gainedPositions) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.driverId = driverId;
        this.gainedPositions = gainedPositions;
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

    public int getGainedPositions() {
        return gainedPositions;
    }
}
