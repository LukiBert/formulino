package pl.edu.wat.wcy.edp.bd.formulino.utils;

import pl.edu.wat.wcy.edp.bd.formulino.model.Driver;
import pl.edu.wat.wcy.edp.bd.formulino.model.Lap;
import pl.edu.wat.wcy.edp.bd.formulino.model.Race;
import pl.edu.wat.wcy.edp.bd.formulino.model.RaceMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class FetchService {
    private static final HttpClient client = HttpClient.newHttpClient();
//    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String BASE_URL = "https://api.jolpi.ca/ergast/f1/2025/";
    private static final int MAX_LIMIT = 100;
    private static final int CONCURRENT_REQUESTS = 2;


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

    public static List<Driver> fetchDrivers() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "drivers/"))
                .build();

        System.out.println("Fetching drivers...");
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return RaceMapper.mapJsonToDrivers(response.body());
        } catch (Exception e) {
            System.out.println("Error fetching drivers: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            System.out.println("Fetching drivers ENDED");
        }
    }

    public static List<Lap> fetchAllLaps(String season, String round) {
        System.out.println("Starting to fetch all laps for race " + round + "...");

        try {
            // First, get the total number of laps
            int totalLaps = getTotalLapCount(season, round);
            if (totalLaps == 0) {
                System.out.println("No laps found for race " + round);
                return new ArrayList<>();
            }

            System.out.println("Total laps to fetch: " + totalLaps);

            // Calculate a number of requests needed
            int numRequests = (int) Math.ceil((double) totalLaps / MAX_LIMIT);
            System.out.println("Number of requests needed: " + numRequests);

            // Create executor service for concurrent requests
            ExecutorService executor = Executors.newFixedThreadPool(CONCURRENT_REQUESTS);

            try {
                // Create a list of futures for all requests
                List<CompletableFuture<List<Lap>>> futures = IntStream.range(0, numRequests)
                        .mapToObj(i -> {
                            int offset = i * MAX_LIMIT;
                            return CompletableFuture.supplyAsync(
                                    () -> {
                                        List<Lap> laps = fetchLapsBatch(season, round, offset, MAX_LIMIT);
                                        try {
                                            Thread.sleep(300);
                                            return laps;
                                        } catch (InterruptedException e) {
                                            System.out.println("Sleep interrupted");
                                            return new ArrayList<Lap>();
                                        }
                                    },
                                    executor
                            );
                        }).toList();

                // Wait for all requests to complete and combine results
                List<Lap> allLaps = new ArrayList<>();
                for (CompletableFuture<List<Lap>> future : futures) {
                    try {
                        List<Lap> batchLaps = future.get(30, TimeUnit.SECONDS);
                        allLaps.addAll(batchLaps);
                    } catch (TimeoutException e) {
                        System.err.println("Request timed out: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Error in batch request: " + e.getMessage());
                    }
                }

                // Set race ID for all laps
                String raceId = season + "_" + round;
                allLaps.forEach(lap -> lap.setRace_id(raceId));

                // Sort laps by lap number and position for consistency
                allLaps.sort(Comparator
                        .comparing(Lap::getLap_number)
                        .thenComparing(Lap::getPosition));

                System.out.println("Successfully fetched " + allLaps.size() + " laps");
                return allLaps;

            } finally {
                executor.shutdown();
                try {
                    if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                        executor.shutdownNow();
                    }
                } catch (InterruptedException e) {
                    executor.shutdownNow();
                    Thread.currentThread().interrupt();
                }
            }

        } catch (Exception e) {
            System.err.println("Error fetching all laps: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            System.out.println("Finished fetching all laps for race " + round);
        }
    }

    /**
     * Get the total number of laps for planning pagination
     */
    private static int getTotalLapCount(String season, String round) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + round + "/laps/?limit=1"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int total = RaceMapper.extractTotalFromResponse(response.body());
            System.out.println("Successfully fetched total lap count: " + total);
            return total;
        } catch (Exception e) {
            System.err.println("Error getting total lap count: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Fetch a batch of laps with specific offset and limit
     */
    private static List<Lap> fetchLapsBatch(String season, String round, int offset, int limit) {
        String url = BASE_URL + round + "/laps/?offset=" + offset + "&limit=" + limit;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        System.out.println("Fetching batch: " + url);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return RaceMapper.mapJsonToLaps(response.body());
        } catch (Exception e) {
            System.err.println("Error fetching batch (offset=" + offset + "): " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
