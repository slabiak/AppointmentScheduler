package com.example.slabiak.appointmentscheduler.model;

public interface TimePeriodAdjuster {
    TimePeriod adjust(TimePeriod original, TimePeriod breakPeriod);
}
