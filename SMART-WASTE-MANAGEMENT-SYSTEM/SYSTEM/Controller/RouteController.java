package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Controller;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.GeoPoint;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service.BinService;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/route")
public class RouteController {

    @Autowired private RouteService routeService;
    @Autowired
    private BinService binService;
    @PreAuthorize("hasAnyRole('DRIVER', 'ADMIN')")
    @GetMapping("/optimized/{truckId}")
    public ResponseEntity<String> getRoute(@PathVariable String truckId) throws IOException, InterruptedException {
        List<GeoPoint> points = binService.getWaypointsForTruck(truckId);

        if (points.isEmpty()) {
            return ResponseEntity.badRequest().body("No waypoints found for the truck.");
        }

        String routeGeoJson = routeService.getOptimizedRoute(points);
        return ResponseEntity.ok(routeGeoJson);
    }
}
