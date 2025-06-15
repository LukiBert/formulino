package pl.edu.wat.wcy.edp.bd.formulino.weather;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;
import org.sql2o.Sql2o;
import pl.edu.wat.wcy.edp.bd.formulino.dao.DBConnection;
import pl.edu.wat.wcy.edp.bd.formulino.dao.RaceDAO;
import pl.edu.wat.wcy.edp.bd.formulino.events.EventBus;
import pl.edu.wat.wcy.edp.bd.formulino.events.NewLapEvent;
import pl.edu.wat.wcy.edp.bd.formulino.events.NewRaceEvent;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;

public class WeatherViewController {

    @FXML private Label temperatureLabel;
    @FXML private Label conditionLabel;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @FXML
    public void initialize() {
        Sql2o sql2o = DBConnection.createSql2o();
        RaceDAO raceDao = RaceDAO.getInstance(sql2o);

        EventBus.getInstance().subscribe(NewRaceEvent.class, e -> {
                String season = e.getSeason();
                int round = e.getRound();

                Race race = raceDao.findRace(season, String.valueOf(round));

                String raceDate = race.getDate();
                Double lat = race.getCircuit().getLocation().getLatitudeAsDouble();
                Double lng = race.getCircuit().getLocation().getLongitudeAsDouble();

                fetchWeather(lat, lng, raceDate);
            }
        );
    }

    public void fetchWeather(double latitude, double longitude, String date) {
        String url = String.format(
                "https://historical-forecast-api.open-meteo.com/v1/forecast?latitude=%.2f&longitude=%.2f&start_date=%s&end_date=%s&daily=temperature_2m_max,weathercode&timezone=auto",
                latitude, longitude, date, date
        );

        System.out.println("Fetching Weather for " + url);

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws IOException, InterruptedException {
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .build();

                HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                JSONObject json = new JSONObject(response.body());

                double temp = json.getJSONObject("daily")
                        .getJSONArray("temperature_2m_max").getDouble(0);
                int weatherCode = json.getJSONObject("daily")
                        .getJSONArray("weathercode").getInt(0);

                String condition = switch (weatherCode) {
                    case 0 -> "Clear Sky";
                    case 1 -> "Mainly Clear";
                    case 2 -> "Partly Cloudy";
                    case 3 -> "Overcast";
                    case 45, 48 -> "Fog";
                    case 51, 53, 55 -> "Drizzle";
                    case 56, 57 -> "Freezing Drizzle";
                    case 61, 63, 65 -> "Rain";
                    case 66, 67 -> "Freezing Rain";
                    case 71, 73, 75 -> "Snowfall";
                    case 77 -> "Snow Grains";
                    case 80, 81, 82 -> "Rain Showers";
                    case 85, 86 -> "Snow Showers";
                    case 95 -> "Thunderstorm";
                    case 96, 99 -> "Heavy Thunderstorm";
                    default -> "Unknown Weather Code";
                };

                Platform.runLater(() -> {
                    temperatureLabel.setText("Temperature: " + temp + "°C");
                    conditionLabel.setText("Condition: " + condition);
                });

                System.out.println("Weather fetched from " + url);

                return null;
            }
        };

        new Thread(task).start();
    }
}

