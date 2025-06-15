package pl.edu.wat.wcy.edp.bd.formulino.events;

public class NewRaceEvent implements RaceEvent {
    private String eventId;
    private String eventName;
    private String eventDescription;
    private String season;
    private int round;

    public NewRaceEvent(String eventId, String eventName, String eventDescription, String season, int round) {
        this.eventId = eventId;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.season = season;
        this.round = round;
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

    public String getSeason() {
        return season;
    }

    public int getRound() {
        return round;
    }
}
