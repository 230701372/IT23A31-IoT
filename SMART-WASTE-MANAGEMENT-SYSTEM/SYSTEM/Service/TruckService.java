package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Service;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Truck;
import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository.TruckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TruckService {

    @Autowired
    private TruckRepository truckRepo;

    public void updateLocation(String truckId, double lat, double lng) {
        Truck truck = truckRepo.findById(truckId).orElseThrow();
        truck.setLatitude(lat);
        truck.setLongitude(lng);
        truckRepo.save(truck);
    }
}