package com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Repository;

import com.project.SMART.WASTE.MANAGEMENT.SYSTEM.Model.Bin;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;
public interface BinRepository extends MongoRepository<Bin, String> {
    List<Bin> findByTruckIdAndAssignedTrue(String truckId);


}
