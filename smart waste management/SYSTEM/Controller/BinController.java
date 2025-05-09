package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Controller;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Bin;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.BinStatusRequest;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service.BinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/bins")
public class BinController {

    @Autowired
    private BinService binService;

    @PostMapping("/status")
    public ResponseEntity<String> updateBinStatus(@RequestBody BinStatusRequest request) {
        binService.updateBinStatus(request);
        return ResponseEntity.ok("Bin status updated and assignment checked");
    }
    @PutMapping("/collected/{binId}")
    public ResponseEntity<String> markBinAsCollected(@PathVariable String binId) {
        boolean success = binService.markAsCollected(binId);
        if (success) {
            return ResponseEntity.ok("Bin marked as collected and resources freed.");
        } else {
            return ResponseEntity.badRequest().body("Bin not found or not assigned.");
        }
    }

    //@PreAuthorize("hasRole('DRIVER')")
    @GetMapping("/driver/assigned-bins")
    public ResponseEntity<List<Bin>> getAssignedBinsForDriver(@RequestParam String driverId) {

        List<Bin> assignedBins = binService.getAssignedBinsForDriver(driverId);
        if (assignedBins.isEmpty()) {
            return ResponseEntity.status(404).body(List.of());  // Return an empty list with a 404 status.
        }

        return ResponseEntity.ok(assignedBins);

    }


}