package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {

    @Query(value = "SELECT u FROM Station u WHERE u.cityId = :cityId AND u.name NOT LIKE 'Warehouse'")
    List<Station> getStationsByCityId(@Param("cityId") UUID cityId);

    Station getStationById(UUID id);

    @Query(value = "SELECT u.id FROM Station u WHERE u.cityId = :cityId AND u.name LIKE 'Warehouse'")
    UUID getWarehouseId(@Param("cityId") UUID cityId);

}
