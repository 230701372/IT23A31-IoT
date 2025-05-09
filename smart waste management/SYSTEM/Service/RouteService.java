package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.GeoPoint;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class RouteService {

    private final String ORS_API_KEY = "your-api-key";

    public String getOptimizedRoute(List<GeoPoint> points) throws IOException, InterruptedException {
        String orsUrl = "https://api.openrouteservice.org/v2/directions/driving-car/geojson";

        HttpClient client = HttpClient.newHttpClient();
        String body = buildRequestBody(points);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(orsUrl))
                .header("Authorization", ORS_API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body(); // This will be a GeoJSON route
    }

    private String buildRequestBody(List<GeoPoint> points) {
        StringBuilder coords = new StringBuilder("[");
        for (GeoPoint p : points) {
            coords.append("[").append(p.getLongitude()).append(",").append(p.getLatitude()).append("],");
        }
        coords.setLength(coords.length() - 1); // remove last comma
        coords.append("]");

        return "{ \"coordinates\": " + coords.toString() + " }";
    }
}
