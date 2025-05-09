package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Driver;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Truck;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.DriverRepository;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.TruckRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;
    @Autowired
    private TruckRepository truckRepo;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void registerDriver(Driver driver) {
        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        driver.setAvailable(true); // default to available

        // 2. Save the driver first (this will generate the driver ID)
        Driver savedDriver = driverRepository.save(driver);

        // 3. Link driver to the truck (if truckId is provided)
        if (savedDriver.getTruckId() != null) {
            Truck truck = truckRepo.findById(savedDriver.getTruckId()).orElse(null);
            if (truck != null) {
                truck.setDriverId(savedDriver.getId()); // Link driver to truck
                truckRepo.save(truck);           // Save the updated truck
            }
        }
    }

    public Optional<Driver> getDriverByUsername(String username) {
        return driverRepository.findByUsername(username);
    }
}