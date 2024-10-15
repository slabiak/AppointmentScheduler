package com.example.slabiak.appointmentscheduler.model;


import java.time.LocalDateTime;

class AdjusterBeforeStart implements TimePeriodAdjuster {


    @Override
    public TimePeroid adjust(TimePeroid original, TimePeroid breakPeriod) {
        if (breakPeriod.getStart().isBefore(original.getStart())) {
            original.setStart((breakPeriod.getStart()));
        }
        return original;
    }
}