package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.DTO.NotificationMessage;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.*;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.BinRepository;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.DriverRepository;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BinService {
    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BinRepository binRepo;
    @Autowired private TruckRepository truckRepo;
    @Autowired private DriverRepository driverRepo;


    public void updateBinStatus(BinStatusRequest request) {
        Bin bin = binRepo.findById(request.getBinId()).orElseThrow();
        bin.setFull("FULL".equals(request.getStatus()));
        bin.setAssigned(false);
        binRepo.save(bin);

        if (bin.isFull()) {
            assignTruckToBin(bin);
        }
    }
    public List<GeoPoint> getWaypointsForTruck(String truckId) {
        List<GeoPoint> waypoints = new ArrayList<>();

        // Bins assigned to this truck
        List<Bin> bins = binRepo.findByTruckIdAndAssignedTrue(truckId);
        for (Bin bin : bins) {
            waypoints.add(new GeoPoint(bin.getLatitude(), bin.getLongitude()));
        }


        return waypoints;
    }

    @Transactional
    public void assignTruckToBin(Bin bin) {
        List<Truck> availableTrucks = truckRepo.findByAvailable(true);
        Truck nearest = null;
        double minDist = Double.MAX_VALUE;

        for (Truck t : availableTrucks) {
            double dist = GeoUtil.distance(t.getLatitude(), t.getLongitude(), bin.getLatitude(), bin.getLongitude());
            if (dist < minDist) {
                minDist = dist;
                nearest = t;
            }
        }

        if (nearest != null) {
            Driver driver = driverRepo.findByTruckId(nearest.getId()).orElseThrow();
            nearest.setAvailable(false);
            truckRepo.save(nearest);

            driver.setAvailable(false);
            driverRepo.save(driver);

            bin.setAssigned(true);
            binRepo.save(bin);

            // Send optimized route to driver
            NotificationMessage message = new NotificationMessage();
            message.setMessage("New bin assigned near your location.");
            message.setType("BIN_ASSIGNED");

            notificationService.notifyDriver(driver.getId(), message);

        }
    }
    public boolean markAsCollected(String binId) {
        Optional<Bin> optionalBin = binRepo.findById(binId);
        if (optionalBin.isEmpty()) return false;

        Bin bin = optionalBin.get();

        if (!bin.isAssigned() || bin.getTruckId() == null) {
            return false;
        }

        String truckId = bin.getTruckId();
        Truck truck = truckRepo.findById(truckId).orElse(null);
        if (truck == null) return false;

        // Set truck as available
        truck.setAvailable(true);
        truckRepo.save(truck);

        // Set driver as available
        Driver driver = driverRepo.findByTruckId(truckId).orElse(null);
        if (driver != null) {
            driver.setAvailable(true);
            driverRepo.save(driver);
        }

        // Unassign bin
        bin.setAssigned(false);
        bin.setTruckId(null); // optional
        binRepo.save(bin);

        return true;
    }
    public List<Bin> getAssignedBinsForDriver(String driverId) {
        // Step 1: Find the truck assigned to this driver
        Truck assignedTruck = truckRepo.findByDriverId(driverId);
        if (assignedTruck == null) {
            return Collections.emptyList(); // No truck assigned to driver
        }

        // Step 2: Fetch bins assigned to this truck
        return binRepo.findByTruckIdAndAssignedTrue(assignedTruck.getId());
    }


}
