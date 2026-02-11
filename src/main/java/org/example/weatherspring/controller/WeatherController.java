package org.example.weatherspring.controller;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@RestController
public class WeatherController {

    private final String API_KEY = System.getenv("OPENWEATHER_API_KEY");
    private final String CITY = "Warsaw";

    @GetMapping("/weather")
    public String getWeather() {
        try {
            String url = "https://api.openweathermap.org/data/2.5/weather?q="
                    + CITY + ",PL&appid=" + API_KEY + "&units=metric";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return "Error fetching weather: " + response.body();
            }

            JSONObject json = new JSONObject(response.body());
            double temperature = json.getJSONObject("main").getDouble("temp");
            int humidity = json.getJSONObject("main").getInt("humidity");
            String description = json.getJSONArray("weather")
                    .getJSONObject(0)
                    .getString("description");
            double windSpeed = json.getJSONObject("wind").getDouble("speed");

            return String.format("Current weather in %s, Poland:\nTemperature: %.1f°C\nWeather: %s\nHumidity: %d%%\nWind speed: %.1f m/s",
                    CITY, temperature, description, humidity, windSpeed);

        } catch (Exception e) {
            e.printStackTrace();
            return "An error occurred: " + e.getMessage();
        }
    }
}
