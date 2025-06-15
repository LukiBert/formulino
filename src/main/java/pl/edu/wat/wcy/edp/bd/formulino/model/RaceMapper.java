package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class RaceMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Race> mapJsonToRaces(String jsonString) throws Exception {
        F1Response response = objectMapper.readValue(jsonString, F1Response.class);
        return response.getMrData().getRaceTable().getRaces();
    }

    public static List<Driver> mapJsonToDrivers(String jsonString) throws Exception {
        F1Response response = objectMapper.readValue(jsonString, F1Response.class);
        return response.getMrData().getDriverTable().getDrivers();
    }

    public static int extractTotalFromResponse(String jsonString) throws JsonProcessingException {
        F1Response response = objectMapper.readValue(jsonString, F1Response.class);
        String temp = response.getMrData().getTotal();
        return Integer.parseInt(temp);
    }

    public static List<Lap> mapJsonToLaps(String jsonString) throws Exception {
        List<Lap> laps = new ArrayList<>();

        F1Response response = objectMapper.readValue(jsonString, F1Response.class);

        for (Race race : response.getMrData().getRaceTable().getRaces()) {
            if (race.getLaps() != null) {
                for (LapData lapData : race.getLaps()) {
                    if (lapData.getTimings() != null) {
                        for (Timing timing : lapData.getTimings()) {
                            Lap lap = Lap.fromTiming(lapData, timing);
                            laps.add(lap);
                        }
                    }
                }
            }
        }

    return laps;
    }

    public static List<PitStop> mapJsonToPitStops(String jsonString) throws Exception {
        List<PitStop> pitStops = new ArrayList<>();

        F1Response response = objectMapper.readValue(jsonString, F1Response.class);

        for (Race race : response.getMrData().getRaceTable().getRaces()) {
            if (race.getPitStops() != null) {
                for (PitStop pitStop : race.getPitStops()) {
                    PitStop pit = PitStop.fromFetchedPitStop(pitStop);
                    pitStops.add(pit);
                }
            }
        }

        return pitStops;
    }
}
