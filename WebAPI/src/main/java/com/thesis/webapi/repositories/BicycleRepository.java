package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface BicycleRepository extends JpaRepository<Bicycle, UUID> {

    List<Bicycle> getBicyclesByStationId(UUID stationId);

    Bicycle getBicycleById(UUID id);

    @Query(value = "SELECT u FROM Bicycle u WHERE u.stationId IN (SELECT id FROM Station WHERE cityId = :cityId) AND u.status = 'Damaged'")
    List<Bicycle> getDamagedBicycles(@Param("cityId") UUID cityId);

}
