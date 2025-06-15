package pl.edu.wat.wcy.edp.bd.formulino.events;

public class PositionDroppedEvent implements RaceEvent {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String driverId;
    private int lostPositions;

    public PositionDroppedEvent(String eventId, String eventName, String eventDescription, String driverId, int lostPositions) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.driverId = driverId;
        this.lostPositions = lostPositions;
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

    public int getLostPositions() {
        return lostPositions;
    }
}
