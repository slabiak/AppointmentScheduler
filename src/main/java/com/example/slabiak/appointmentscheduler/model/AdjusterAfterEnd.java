package com.example.slabiak.appointmentscheduler.model;

class AdjusterAfterEnd implements TimePeriodAdjuster {


    @Override
    public TimePeroid adjust(TimePeroid original, TimePeroid breakPeriod) {
        if (breakPeriod.getEnd().isAfter(original.getEnd())) {
            original.setEnd(breakPeriod.getEnd());
        }
        return original;
    }
}