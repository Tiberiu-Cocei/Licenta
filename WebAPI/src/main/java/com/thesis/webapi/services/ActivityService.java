package com.thesis.webapi.services;

import java.util.UUID;

public interface ActivityService {

    void incrementTimesClickedWhileEmpty(UUID stationId);

    void incrementTimesClickedWhileFull(UUID stationId);

    void generateRowsForActivity();

    void onScheduleCallGenerateRowsForActivity();

    void onStartupCallGenerateRowsForActivity();

}
