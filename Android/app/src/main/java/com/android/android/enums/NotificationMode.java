package com.android.android.enums;

public enum NotificationMode {

    FIVE_MINUTES_BEFORE_ARRIVAL(0),
    ONE_MINUTE_BEFORE_ARRIVAL(1),
    AFTER_PLANNED_ARRIVAL(2),
    FIVE_MINUTES_AFTER_PLANNED_ARRIVAL(3);

    private final int value;

    NotificationMode(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
