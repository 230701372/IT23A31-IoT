package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Driver;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DriverRepository extends MongoRepository<Driver, String> {

    // Method to find driver by username and password
    Driver findByUsernameAndPassword(String username, String password);
    Optional<Driver>  findByUsername(String username);
    List<Driver> findByAvailableTrue();
    Optional<Driver> findByTruckId(String truckId);
}
