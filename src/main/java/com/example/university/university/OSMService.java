package com.example.university.university;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OSMService {

    private final RestTemplate restTemplate;

    @Autowired
    public OSMService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Location fetchLocationData(String address) {
        String startUrl = "https://nominatim.openstreetmap.org/search?q=";
        String[] split = address.split(" ");

        for (int i = 0; i < split.length; i++) {
            startUrl += split[i];
            if (i < (split.length - 1)) {
                startUrl += "+";
            }
        }

        startUrl += "&format=json&addressdetails=1";

        Location[] responses = restTemplate.getForObject(startUrl, Location[].class);

        if (responses.length == 0)
            return null;

        return responses[0];
    }

    public Location fetchLocation(String address) {
        if(fetchLocationData(address) == null){
            return fetchLocationData(address.substring(address.indexOf(",") + 1));
        }
        else {
            return fetchLocationData(address);
        }
    }
}
