package pl.edu.wat.wcy.edp.bd.formulino.utils;

import pl.edu.wat.wcy.edp.bd.formulino.model.Race;
import pl.edu.wat.wcy.edp.bd.formulino.model.RaceMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class FetchService {
    private static final HttpClient client = HttpClient.newHttpClient();
//    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "https://api.jolpi.ca/ergast/f1/2025/"; // example

    public static List<Race> fetchSeasonRaces() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "races/"))
                .build();

        System.out.println("Fetching season races...");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return RaceMapper.mapJsonToRaces(response.body());
        } catch (Exception e) {
            System.out.println("Error occurred while fetching:");
            return new ArrayList<>();
        } finally {
            System.out.println("Fetching season races ENDED");
        }
    }
}
