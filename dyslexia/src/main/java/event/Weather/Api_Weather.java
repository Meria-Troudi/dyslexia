package event.Weather;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
public class Api_Weather {

    private static final String API_KEY = "3d088bec38c00906c9a68fd36d0a98a1"; // Remplace par ta vraie clé API OpenWeatherMap

    public static String getWeather(String city) throws IOException, InterruptedException {
        String url = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY + "&units=metric&lang=fr";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // réponse JSON brute
    }
    }


