package com.back.app.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();


    public Double[] searchLatLon(String query) {
        try {
            if (query == null || query.isBlank()) return null;

            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);

            String url = "https://nominatim.openstreetmap.org/search?format=json&limit=1&countrycodes=hr&q=" + encoded;

            System.out.println("QUERY RAW: [" + query + "]");
            System.out.println("QUERY LEN: " + query.length());
            System.out.println("URL: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("User-Agent", "GearShare/1.0 (contact: leonard.kovac@fer.hr)");
            headers.set("Accept", "application/json");
            headers.set("Accept-Language", "hr,en;q=0.8");



            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<List> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                List.class
            );
        

           
            System.out.println("STATUS: " + response.getStatusCode());
            System.out.println("RAW RESPONSE BODY:");
            System.out.println(response.getBody());

            List<?> results = response.getBody();
            if (results == null || results.isEmpty()) return null;

            Object first = results.get(0);
            if (!(first instanceof Map)) return null;

            Map<?, ?> obj = (Map<?, ?>) first;

            Object latObj = obj.get("lat");
            Object lonObj = obj.get("lon");
            if (latObj == null || lonObj == null) return null;

            double lat = Double.parseDouble(latObj.toString());
            double lon = Double.parseDouble(lonObj.toString());

            return new Double[]{lat, lon};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
