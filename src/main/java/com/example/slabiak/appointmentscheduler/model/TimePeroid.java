package com.example.slabiak.appointmentscheduler.model;

import java.time.LocalTime;
import java.util.Objects;

public class TimePeroid implements Comparable<TimePeroid> {

    private LocalTime start;
    private LocalTime end;

    public TimePeroid() {

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimePeroid peroid = (TimePeroid) o;
        return this.start.equals(peroid.getStart()) &&

                this.end.equals(peroid.getEnd());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public String toString() {
        return "TimePeroid{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
