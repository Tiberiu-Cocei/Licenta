package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransportRepository extends JpaRepository<Transport, UUID> {

}
