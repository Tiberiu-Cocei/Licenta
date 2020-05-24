package com.android.android.dtos;

import java.util.UUID;

public class ReportCreateDto {

    private UUID userId;

    private UUID bicycleId;

    private int severity;

    private String description;

    public ReportCreateDto(UUID userId, UUID bicycleId, int severity, String description) {
        this.userId = userId;
        this.bicycleId = bicycleId;
        this.severity = severity;
        this.description = description;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public int getSeverity() {
        return severity;
    }

    public String getDescription() {
        return description;
    }
}
