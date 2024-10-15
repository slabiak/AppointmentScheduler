package com.example.slabiak.appointmentscheduler.model;

public interface TimePeriodAdjuster {
    TimePeroid adjust(TimePeroid original, TimePeroid breakPeriod);
}