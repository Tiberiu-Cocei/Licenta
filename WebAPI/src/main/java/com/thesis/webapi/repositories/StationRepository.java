package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface StationRepository extends JpaRepository<Station, UUID> {

    List<Station> getStationsByCityId(UUID cityId);

}
