package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BicycleRepository extends JpaRepository<Bicycle, UUID> {

    List<Bicycle> getBicyclesByStationId(UUID stationId);

    Bicycle getBicycleById(UUID id);

}
