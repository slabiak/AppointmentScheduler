package com.example.slabiak.appointmentscheduler.model;

import java.sql.Time;
import java.time.LocalTime;

public class TimePeroid implements Comparable<TimePeroid> {
    private LocalTime start;
    private LocalTime end;

    public TimePeroid(){

    }

    public TimePeroid(LocalTime start, LocalTime end) {
        this.start = start;
        this.end = end;
    }

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getEnd() {
        return end;
    }

    public void setEnd(LocalTime end) {
        this.end = end;
    }

    @Override
    public int compareTo(TimePeroid o) {
        return this.getStart().compareTo(o.getStart());
    }
}
