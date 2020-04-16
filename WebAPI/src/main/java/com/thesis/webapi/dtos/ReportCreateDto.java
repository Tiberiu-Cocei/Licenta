package com.thesis.webapi.dtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

public class ReportCreateDto {

    @NotNull(message = "User id cannot be null.")
    private UUID userId;

    @NotNull(message = "Bicycle id cannot be null.")
    private UUID bicycleId;

    @Min(value = 1, message = "Severity must be greater or equal to 1 and less or equal to 10")
    @Max(value = 10, message = "Severity must be greater or equal to 1 and less or equal to 10")
    private int severity;

    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters.")
    private String description;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(UUID bicycleId) {
        this.bicycleId = bicycleId;
    }

    public int getSeverity() {
        return severity;
    }

    public void setSeverity(int severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
