package pl.edu.wat.wcy.edp.bd.formulino.model;

public class Lap {
    private int lap_number;
    private String driver_id;
    private int position;
    private String lap_time;
    private int lap_time_ms;
    private String race_id;

    // Constructors
    public Lap() {}

    public Lap(int lapNumber, String driverId, int position, String time) {
        this.lap_number = lapNumber;
        this.driver_id = driverId;
        this.position = position;
        this.lap_time = time;
    }

    // Static factory method to create from Jackson objects
    public static Lap fromTiming(LapData lapData, Timing timing) {
        Lap lap = new Lap();
        lap.setLap_number(lapData.getLapNumberAsInt());
        lap.setDriver_id(timing.getDriverId());
        lap.setPosition(timing.getPositionAsInt());

        String time = timing.getTime();
        lap.setLap_time(time);
        lap.setLap_time_ms(parseTimeToMs(time));
        return lap;
    }

    private static int parseTimeToMs(String timeStr) {
        // Supports format like "1:57.099" or "0:59.845"
        String[] parts = timeStr.split("[:.]");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        int millis = Integer.parseInt(parts[2]);
        return (minutes * 60 + seconds) * 1000 + millis;
    }

    // Getters and setters
    public int getLap_number() { return lap_number; }
    public void setLap_number(int lap_number) { this.lap_number = lap_number; }

    public String getDriver_id() { return driver_id; }
    public void setDriver_id(String driver_id) { this.driver_id = driver_id; }

    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }

    public String getLap_time() { return lap_time; }
    public void setLap_time(String lap_time) { this.lap_time = lap_time; }

    public String getRace_id() { return race_id; }
    public void setRace_id(String race_id) { this.race_id = race_id; }

    public int getLap_time_ms() { return lap_time_ms; }
    public void setLap_time_ms(int time_ms) { this.lap_time_ms = time_ms; }

    @Override
    public String toString() {
        return "Lap{" +
                "lapNumber=" + lap_number +
                ", driverId='" + driver_id + '\'' +
                ", position=" + position +
                ", time='" + lap_time + '\'' +
                ", time_ms='" + lap_time_ms + '\'' +
                ", raceId='" + race_id + '\'' +
                '}';
    }
}