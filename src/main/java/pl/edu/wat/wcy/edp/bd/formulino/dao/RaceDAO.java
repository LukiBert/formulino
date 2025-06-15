package pl.edu.wat.wcy.edp.bd.formulino.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.model.*;

import java.util.List;

public class RaceDAO {
    private final Sql2o sql2o;

    public RaceDAO(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    public void saveDriver(Driver driver) {
        String sql = """
            INSERT OR IGNORE INTO drivers (driver_id, url, given_name, family_name, date_of_birth, nationality, permanent_number, code)
            VALUES (:driverId, :url, :givenName, :familyName, :dateOfBirth, :nationality, :permanentNumber, :code)
            ON CONFLICT (driver_id) DO UPDATE SET
                url = EXCLUDED.url,
                given_name = EXCLUDED.given_name,
                family_name = EXCLUDED.family_name,
                date_of_birth = EXCLUDED.date_of_birth,
                nationality = EXCLUDED.nationality,
                permanent_number = EXCLUDED.permanent_number,
                code = EXCLUDED.code
            """;

        try (Connection conn = sql2o.open()) {
            conn.createQuery(sql)
                    .addParameter("driverId", driver.getDriverId())
                    .addParameter("url", driver.getUrl())
                    .addParameter("givenName", driver.getGivenName())
                    .addParameter("familyName", driver.getFamilyName())
                    .addParameter("dateOfBirth", driver.getDateOfBirth())
                    .addParameter("nationality", driver.getNationality())
                    .addParameter("permanentNumber", driver.getPermanentNumber())
                    .addParameter("code", driver.getCode())
                    .executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    public List<Driver> getAllDrivers() {
        String sql = """
            SELECT driver_id as driverId, url, given_name as givenName, family_name as familyName,
                   date_of_birth as dateOfBirth, nationality, permanent_number as permanentNumber, code
            FROM drivers
            ORDER BY permanent_number
            """;

        try (Connection conn = sql2o.open()) {
            List<DriverResult> results = conn.createQuery(sql).executeAndFetch(DriverResult.class);

            return results.stream()
                    .map(this::mapToDriver)
                    .toList();
        }
    }

    /**
     * Save a Race with its Circuit and Location to the database
     */
    public void saveRace(Connection conn, Race race) {
        try {
            if (race.getCircuit() != null && race.getCircuit().getLocation() != null) {
                saveLocation(conn, race.getCircuit().getLocation());
                saveCircuit(conn, race.getCircuit());
            }

            String insertRaceSql = """
                INSERT OR IGNORE INTO races (season, round, url, race_name, date, time, circuit_id)
                VALUES (:season, :round, :url, :raceName, :date, :time, :circuitId)
                """;

            conn.createQuery(insertRaceSql)
                    .addParameter("season", race.getSeason())
                    .addParameter("round", race.getRound())
                    .addParameter("url", race.getUrl())
                    .addParameter("raceName", race.getRaceName())
                    .addParameter("date", race.getDate())
                    .addParameter("time", race.getTime())
                    .addParameter("circuitId", race.getCircuit() != null ? race.getCircuit().getCircuitId() : null)
                    .executeUpdate();

            conn.commit();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Save Location to a database
     */
    private void saveLocation(Connection conn, Location location) {
        String insertLocationSql = """
            INSERT INTO locations (lat, longitude, locality, country)
            VALUES (:lat, :longitude, :locality, :country)
            ON CONFLICT (locality, country) DO NOTHING
            """;

        try {
            conn.createQuery(insertLocationSql)
                    .addParameter("lat", location.getLat())
                    .addParameter("longitude", location.getLongitude())
                    .addParameter("locality", location.getLocality())
                    .addParameter("country", location.getCountry())
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Save Circuit to a database
     */
    private void saveCircuit(Connection conn, Circuit circuit) {
        String insertCircuitSql = """
            INSERT INTO circuits (circuit_id, url, circuit_name, locality, country)
            VALUES (:circuitId, :url, :circuitName, :locality, :country)
            ON CONFLICT (circuit_id) DO UPDATE SET
                url = EXCLUDED.url,
                circuit_name = EXCLUDED.circuit_name,
                locality = EXCLUDED.locality,
                country = EXCLUDED.country
            """;

        try {
            Location location = circuit.getLocation();
            conn.createQuery(insertCircuitSql)
                    .addParameter("circuitId", circuit.getCircuitId())
                    .addParameter("url", circuit.getUrl())
                    .addParameter("circuitName", circuit.getCircuitName())
                    .addParameter("locality", location != null ? location.getLocality() : null)
                    .addParameter("country", location != null ? location.getCountry() : null)
                    .executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find race by season and round
     */
    public Race findRace(String season, String round) {
        String sql = """
            SELECT r.season, r.round, r.url, r.race_name as raceName, r.date, r.time,
                   c.circuit_id as circuitId, c.url as circuitUrl, c.circuit_name as circuitName,
                   l.lat, l.longitude, l.locality, l.country
            FROM races r
            LEFT JOIN circuits c ON r.circuit_id = c.circuit_id
            LEFT JOIN locations l ON c.locality = l.locality AND c.country = l.country
            WHERE r.season = :season AND r.round = :round
            """;

        try (Connection conn = sql2o.open()) {
            List<RaceResult> results = conn.createQuery(sql)
                    .addParameter("season", season)
                    .addParameter("round", round)
                    .executeAndFetch(RaceResult.class);

            if (results.isEmpty()) {
                return null;
            }

            return mapToRace(results.get(0));
        }
    }

    /**
     * Find all races for a season
     */
    public List<Race> findRacesBySeason(String season) {
        String sql = """
            SELECT r.season, r.round, r.url, r.race_name as raceName, r.date, r.time,
                   c.circuit_id as circuitId, c.url as circuitUrl, c.circuit_name as circuitName,
                   l.lat, l.longitude, l.locality, l.country
            FROM races r
            LEFT JOIN circuits c ON r.circuit_id = c.circuit_id
            LEFT JOIN locations l ON c.locality = l.locality AND c.country = l.country
            WHERE r.season = :season
            ORDER BY CAST(r.round AS INTEGER)
            """;

        try (Connection conn = sql2o.open()) {
            List<RaceResult> results = conn.createQuery(sql)
                    .addParameter("season", season)
                    .executeAndFetch(RaceResult.class);

            return results.stream()
                    .map(this::mapToRace)
                    .toList();
        }
    }

    /**
     * Save laps
     */
    public void saveLaps(List<Lap> laps) {
        String sql = "INSERT OR IGNORE INTO laps " +
                     "(race_id, lap_number, driver_id, position, lap_time, lap_time_ms) " +
                     "VALUES (:raceId, :lapNumber, :driverId, :position, :time, :timeMs)";

        try (Connection con = sql2o.open()) {
            for (Lap lap : laps) {
                con.createQuery(sql)
                        .addParameter("raceId", lap.getRace_id())
                        .addParameter("lapNumber", lap.getLap_number())
                        .addParameter("driverId", lap.getDriver_id())
                        .addParameter("position", lap.getPosition())
                        .addParameter("time", lap.getLap_time())
                        .addParameter("timeMs", lap.getLap_time_ms())
                        .executeUpdate();
            }
        }
    }

    /**
     * Find all timings in a given lap in a given race
     */
    public List<Lap> getAllTimingsFromLap(String raceId, int lapNumber) {
        String sql = "SELECT * FROM laps WHERE race_id = :raceId AND lap_number = :lapNumber ORDER BY position";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("raceId", raceId)
                    .addParameter("lapNumber", lapNumber)
                    .executeAndFetch(Lap.class);
        }
    }

    public void savePitStops(List<PitStop> pitStops) {
        String sql = """
            INSERT OR IGNORE INTO pitstops 
            (race_id, stop_number, driver_id, lap_number, stop_time, duration, duration_ms)
            VALUES (:race_id, :stop_number, :driver_id, :lap_number, :stop_time, :duration, :duration_ms)
        """;

        try (Connection con = sql2o.beginTransaction()) {
            for (PitStop ps : pitStops) {
                con.createQuery(sql)
                        .addParameter("race_id", ps.getRace_id())
                        .addParameter("stop_number", ps.getStop_number())
                        .addParameter("driver_id", ps.getDriver_id())
                        .addParameter("lap_number", ps.getLap_number())
                        .addParameter("stop_time", ps.getStop_time())
                        .addParameter("duration", ps.getDuration())
                        .addParameter("duration_ms", ps.getDuration_ms())
                        .executeUpdate();
            }
            con.commit();
        } catch (Exception e) {
            System.err.println("Error saving pit stops: " + e.getMessage());
        }
    }

    public List<PitStop> getAllPitStops(String raceId) {
        String sql = "SELECT * FROM pitstops WHERE race_id = :raceId ORDER BY lap_number, stop_number";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("raceId", raceId)
                    .executeAndFetch(PitStop.class);
        }
    }

    public List<PitStop> getPitStopsFromLap(String raceId,  int lapNumber) {
        String sql = "SELECT * FROM pitstops WHERE race_id = :raceId AND lap_number = :lapNumber ORDER BY stop_time";

        try (Connection con = sql2o.open()) {
            return con.createQuery(sql)
                    .addParameter("raceId", raceId)
                    .addParameter("lapNumber", lapNumber)
                    .executeAndFetch(PitStop.class);
        }
    }

    /**
     * Helper method to map a database result to Race object
     */
    private Race mapToRace(RaceResult result) {
        Race race = new Race();
        race.setSeason(result.getSeason());
        race.setRound(result.getRound());
        race.setUrl(result.getUrl());
        race.setRaceName(result.getRaceName());
        race.setDate(result.getDate());
        race.setTime(result.getTime());

        if (result.getCircuitId() != null) {
            Circuit circuit = new Circuit();
            circuit.setCircuitId(result.getCircuitId());
            circuit.setUrl(result.getCircuitUrl());
            circuit.setCircuitName(result.getCircuitName());

            if (result.getLocality() != null) {
                Location location = new Location();
                location.setLat(result.getLat());
                location.setLongitude(result.getLongitude());
                location.setLocality(result.getLocality());
                location.setCountry(result.getCountry());
                circuit.setLocation(location);
            }

            race.setCircuit(circuit);
        }

        return race;
    }

    private Driver mapToDriver(DriverResult result) {
        Driver driver = new Driver();
        driver.setDriverId(result.getDriverId());
        driver.setUrl(result.getUrl());
        driver.setGivenName(result.getGivenName());
        driver.setFamilyName(result.getFamilyName());
        driver.setDateOfBirth(result.getDateOfBirth());
        driver.setNationality(result.getNationality());
        driver.setPermanentNumber(result.getPermanentNumber());
        driver.setCode(result.getCode());
        return driver;
    }

    private static class RaceResult {
        private String season;
        private String round;
        private String url;
        private String raceName;
        private String date;
        private String time;
        private String circuitId;
        private String circuitUrl;
        private String circuitName;
        private String lat;
        private String longitude;
        private String locality;
        private String country;

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

        public String getCircuitId() { return circuitId; }
        public void setCircuitId(String circuitId) { this.circuitId = circuitId; }

        public String getCircuitUrl() { return circuitUrl; }
        public void setCircuitUrl(String circuitUrl) { this.circuitUrl = circuitUrl; }

        public String getCircuitName() { return circuitName; }
        public void setCircuitName(String circuitName) { this.circuitName = circuitName; }

        public String getLat() { return lat; }
        public void setLat(String lat) { this.lat = lat; }

        public String getLongitude() { return longitude; }
        public void setLongitude(String longitude) { this.longitude = longitude; }

        public String getLocality() { return locality; }
        public void setLocality(String locality) { this.locality = locality; }

        public String getCountry() { return country; }
        public void setCountry(String country) { this.country = country; }
    }

    private static class DriverResult {
        private String driverId;
        private String url;
        private String givenName;
        private String familyName;
        private String dateOfBirth;
        private String nationality;
        private String permanentNumber;
        private String code;

        // Getters and setters
        public String getDriverId() { return driverId; }
        public void setDriverId(String driverId) { this.driverId = driverId; }

        public String getUrl() { return url; }
        public void setUrl(String url) { this.url = url; }

        public String getGivenName() { return givenName; }
        public void setGivenName(String givenName) { this.givenName = givenName; }

        public String getFamilyName() { return familyName; }
        public void setFamilyName(String familyName) { this.familyName = familyName; }

        public String getDateOfBirth() { return dateOfBirth; }
        public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

        public String getNationality() { return nationality; }
        public void setNationality(String nationality) { this.nationality = nationality; }

        public String getPermanentNumber() { return permanentNumber; }
        public void setPermanentNumber(String permanentNumber) { this.permanentNumber = permanentNumber; }

        public String getCode() { return code; }
        public void setCode(String code) { this.code = code; }
    }
}