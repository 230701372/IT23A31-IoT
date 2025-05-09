package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Controller;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service.TruckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/truck")
public class TruckController {

    @Autowired
    private TruckService truckService;

    @PostMapping("/update-location/{truckId}")
    public ResponseEntity<String> updateTruckLocation(@PathVariable String truckId, @RequestParam double latitude, @RequestParam double longitude) {
        truckService.updateLocation(truckId, latitude, longitude);
        return ResponseEntity.ok("Truck location updated");
    }
}