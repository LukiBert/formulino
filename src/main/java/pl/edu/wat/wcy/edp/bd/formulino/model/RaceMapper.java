package pl.edu.wat.wcy.edp.bd.formulino.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class RaceMapper {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<Race> mapJsonToRaces(String jsonString) throws Exception {
        F1Response response = objectMapper.readValue(jsonString, F1Response.class);
        return response.getMrData().getRaceTable().getRaces();
    }
}
