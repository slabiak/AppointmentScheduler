package com.example.slabiak.appointmentscheduler.model;

import java.time.LocalDateTime;

public class TimePeriod  implements Comparable<TimePeriod>{
    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    private LocalDateTime start;
    private LocalDateTime end;

    // Constructors, getters, setters, etc.

    public void adjust(TimePeriodAdjuster adjuster, TimePeriod breakPeriod) {
        TimePeriod adjusted = adjuster.adjust(this, null);
        this.start = adjusted.getStart();
        this.end = adjusted.getEnd();
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }
    @Override
    public int compareTo(TimePeriod other) {
        return this.start.compareTo(other.start);
    }
}
