package com.android.android.dtos;

import com.android.android.entities.User;

import java.util.UUID;

public class TransactionFinishDto {

    private UUID userId;

    private UUID finishStationId;

    public TransactionFinishDto(UUID finishStationId) {
        this.userId = User.getUser().getId();
        this.finishStationId = finishStationId;
    }

    public UUID getUserId() {
        return userId;
    }

    public UUID getFinishStationId() {
        return finishStationId;
    }
}
