package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SettingsRepository extends JpaRepository<Settings, UUID> {

    Settings getSettingsByCityId(UUID cityId);

}
