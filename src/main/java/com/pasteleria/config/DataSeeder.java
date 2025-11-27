package com.pasteleria.config;

import com.pasteleria.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private LocationService locationService;

    @Override
    public void run(String... args) throws Exception {
        locationService.seedData();
    }
}
