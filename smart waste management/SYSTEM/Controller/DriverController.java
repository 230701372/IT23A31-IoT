package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Driver;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/driver")
public class DriverController {
    // Allows requests from any frontend

    @Autowired
    private DriverService driverService;
    @CrossOrigin(origins = "*")
    @PostMapping("/register")
    public ResponseEntity<String> registerDriver(@RequestBody Driver driver) {
        Optional<Driver> existing = driverService.getDriverByUsername(driver.getUsername());
        if (existing.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        driverService.registerDriver(driver);
        return ResponseEntity.ok("Driver registered successfully");
    }

    // Test endpoint to check login (secured)
    @GetMapping("/profile")
    public ResponseEntity<String> getProfile(Principal principal) {
        return ResponseEntity.ok("Welcome, " + principal.getName());
    }
}
