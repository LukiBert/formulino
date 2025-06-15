package pl.edu.wat.wcy.edp.bd.formulino.events;

public class NewLapEvent implements RaceEvent {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private int lap_number;

    public NewLapEvent(String eventId, String eventName, int lap_number) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.lap_number = lap_number;
    }

    public NewLapEvent(String eventId, String eventName, String eventDescription, int lap_number) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.lap_number = lap_number;
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

    public int getLap_number() {
        return lap_number;
    }
}
