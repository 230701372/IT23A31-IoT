package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Truck;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface TruckRepository extends MongoRepository<Truck, String> {
    // We can add custom query methods later if needed
    Optional<Truck> findById(String id);
    List<Truck> findByAvailableTrue();

    Truck findByDriverId(String driverId);


    List<Truck> findByAvailable(boolean b);
}
