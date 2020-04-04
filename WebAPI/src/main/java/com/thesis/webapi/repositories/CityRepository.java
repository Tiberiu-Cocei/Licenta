package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<City, UUID> {

}
