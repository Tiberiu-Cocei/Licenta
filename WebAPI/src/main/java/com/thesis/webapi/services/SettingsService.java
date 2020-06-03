package com.thesis.webapi.services;

import com.thesis.webapi.dtos.SettingsUpdateDto;
import com.thesis.webapi.entities.Settings;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface SettingsService {

    ResponseEntity<Settings> getSettingsByCityId(UUID cityId);

    ResponseEntity<String> updateSettings(SettingsUpdateDto settingsUpdateDto);

}
