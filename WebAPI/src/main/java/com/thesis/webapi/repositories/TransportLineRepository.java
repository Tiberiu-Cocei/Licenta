package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.TransportLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransportLineRepository extends JpaRepository<TransportLine, UUID> {

}
