package com.thesis.webapi.services;

public interface ActivityService {

    void generateRowsForActivity();

    void onScheduleCallGenerateRowsForActivity();

    void onStartupCallGenerateRowsForActivity();

}
