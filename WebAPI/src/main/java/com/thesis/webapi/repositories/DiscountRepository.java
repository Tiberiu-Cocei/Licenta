package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface DiscountRepository extends JpaRepository<Discount, UUID> {

    @Query(value = "SELECT u FROM Discount u WHERE u.fromStationId = :fromStationId AND u.startTime < :currentTime AND " +
                                                  "u.endTime > :currentTime AND u.discountsLeft > 0")
    List<Discount> getDiscountsByStationAndTime(@Param("fromStationId") UUID fromStationId,
                                                @Param("currentTime") Date currentTime);

    Discount getDiscountById(UUID id);

    @Query(value = "SELECT u.discountValue FROM Discount u WHERE u.id = :discountId")
    Double getDiscountValueById(@Param("discountId") UUID discountId);

}
