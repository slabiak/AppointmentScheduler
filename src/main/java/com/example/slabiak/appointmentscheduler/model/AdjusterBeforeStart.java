package com.example.slabiak.appointmentscheduler.model;


class AdjusterBeforeStart implements TimePeriodAdjuster {
    @Override
    public TimePeriod adjust(TimePeriod original, TimePeriod breakPeriod) {
        if (breakPeriod.getStart().isBefore(original.getStart())) {
            original.setStart(breakPeriod.getStart());
        }
        return original;
    }
}

