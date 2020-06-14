package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Bicycle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface BicycleRepository extends JpaRepository<Bicycle, UUID> {

    List<Bicycle> getBicyclesByStationId(UUID stationId);

    Bicycle getBicycleById(UUID id);

    @Query(value = "SELECT u FROM Bicycle u WHERE u.stationId IN (SELECT id FROM Station WHERE cityId = :cityId) AND u.status = 'Damaged'")
    List<Bicycle> getDamagedBicycles(@Param("cityId") UUID cityId);

    @Query(value = "SELECT u FROM Bicycle u WHERE u.arrivalTime < :currentTime AND u.status = 'User'")
    List<Bicycle> getLateBicycles(@Param("currentTime") LocalTime currentTime);

    @Query(value = "SELECT u FROM Bicycle u WHERE u.arrivalTime < :currentTime AND u.status = 'Transport'")
    List<Bicycle> getArrivedTransportBicycles(@Param("currentTime") LocalTime currentTime);

    @Query(value = "SELECT u FROM Bicycle u WHERE u.stationId = :stationId AND u.status = 'Station'")
    List<Bicycle> getAvailableBicycles(@Param("stationId") UUID stationId);

    @Query(value = "SELECT COUNT(u) FROM Bicycle u WHERE u.status = :status")
    Integer getBicycleCountByStatus(@Param("status") String status);

    @Query(value = "SELECT COUNT(u) FROM Bicycle u WHERE u.status = :status AND u.stationId = :stationId")
    Integer getBicycleCountByStatusAndStationId(@Param("status") String status, @Param("stationId") UUID stationId);

    @Query(value = "SELECT * FROM Bicycle LIMIT ?1 OFFSET ?2", nativeQuery = true)
    List<Bicycle> getBicyclesWithLimitAndOffset(int limit, int offset);

    @Query(value = "SELECT * FROM Bicycle WHERE station_id = ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Bicycle> getBicyclesWithLimitAndOffsetAndStationId(UUID stationId, int limit, int offset);

    @Query(value = "UPDATE Bicycle SET status = ?1::bicycle_status WHERE id = ?2", nativeQuery = true)
    void setStatus(String status, UUID id);

}
