package com.example.slabiak.appointmentscheduler.model;

import java.time.LocalTime;
import java.util.List;

public class Day {

    private LocalTime start;
    private LocalTime end;
    private List<Break> breaks;

    public Day(){

    }

    public Day(LocalTime start, LocalTime end) {
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

    public List<Break> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<Break> breaks) {
        this.breaks = breaks;
    }
}
