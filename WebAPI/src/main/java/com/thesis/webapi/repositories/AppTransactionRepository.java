package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.AppTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AppTransactionRepository extends JpaRepository<AppTransaction, UUID> {

    List<AppTransaction> getAppTransactionsByUserId(UUID userId);

    @Query(value = "SELECT u FROM AppTransaction u WHERE u.userId = :userId AND u.finishStationId IS NULL AND u.finishTime IS NULL")
    List<AppTransaction> getUnfinishedTransactionsByUser(@Param("userId") UUID userId);

    @Query(value = "SELECT u FROM AppTransaction u WHERE u.finishStationId IS NULL AND u.finishTime IS NULL")
    List<AppTransaction> getAllUnfinishedTransactions();

    @Query(value = "SELECT u FROM AppTransaction u WHERE u.bicycleId = :bicycleId AND u.finishStationId IS NULL AND u.finishTime IS NULL")
    AppTransaction getUnfinishedTransactionByBicycleId(@Param("bicycleId") UUID bicycleId);

    @Query(value = "SELECT * FROM App_Transaction WHERE start_station_id = ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<AppTransaction> getTransactionsByStartStationWithLimitAndOffset(UUID startStationId, int limit, int offset);

    @Query(value = "SELECT * FROM App_Transaction WHERE finish_station_id = ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<AppTransaction> getTransactionsByFinishStationWithLimitAndOffset(UUID finishStationId, int limit, int offset);

}
