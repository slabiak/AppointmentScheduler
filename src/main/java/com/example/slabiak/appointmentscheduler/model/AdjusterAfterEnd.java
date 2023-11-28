package com.example.slabiak.appointmentscheduler.model;

class AdjusterAfterEnd implements TimePeriodAdjuster {
    @Override
    public TimePeriod adjust(TimePeriod original, TimePeroid breakPeriod) {
        if (breakPeriod.getEnd().isAfter(original.getEnd())) {
            original.setEnd(breakPeriod.getEnd());
        }
        return original;
    }
}