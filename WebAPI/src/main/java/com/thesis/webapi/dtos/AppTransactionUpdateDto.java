package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class AppTransactionUpdateDto {

    @NotNull(message = "User id cannot be null.")
    private UUID userId;

    @NotNull(message = "Finish station id cannot be null.")
    private UUID finishStationId;

    public UUID getUserId() {
        return userId;
    }

    public UUID getFinishStationId() {
        return finishStationId;
    }
}
