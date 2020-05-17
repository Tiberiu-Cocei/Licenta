package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface StaffRepository extends JpaRepository<Staff, UUID> {

    @Query(value = "SELECT u FROM Staff u WHERE u.position = 'Driver' AND u.available = true")
    List<Staff> getAvailableDriversForTransport();

    @Query(value = "SELECT u FROM Staff u WHERE u.position = 'Driver' AND u.available = false")
    List<Staff> getUnavailableDriversForTransport();

}
