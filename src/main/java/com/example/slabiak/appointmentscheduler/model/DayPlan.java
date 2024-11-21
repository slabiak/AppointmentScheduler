package com.example.slabiak.appointmentscheduler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayPlan {

    private TimePeroid workingHours;
    private List<TimePeroid> breaks;

    public DayPlan() {
        breaks = new ArrayList();
    }

    public List<TimePeroid> timePeroidsWithBreaksExcluded() {
        ArrayList<TimePeroid> timePeroidsWithBreaksExcluded = new ArrayList<>();
        timePeroidsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeroid> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            adjustBreaksWithinWorkingHours(timePeroidsWithBreaksExcluded);
            Collections.sort(timePeroidsWithBreaksExcluded);
        }

        return timePeroidsWithBreaksExcluded;
    }

    private void adjustBreaksWithinWorkingHours(List<TimePeroid> timePeroidsWithBreaksExcluded) {
        ArrayList<TimePeroid> toAdd = new ArrayList<>();
        for (TimePeroid break1 : getBreaks()) {
            adjustBreakStart(break1);
            adjustBreakEnd(break1);

            for (TimePeroid peroid : timePeroidsWithBreaksExcluded) {
                handleBreakOverlap(break1, peroid, toAdd);
            }
        }
        timePeroidsWithBreaksExcluded.addAll(toAdd);
    }

    private void adjustBreakStart(TimePeroid break1) {
        if (break1.getStart().isBefore(workingHours.getStart())) {
            break1.setStart(workingHours.getStart());
        }
    }

    private void adjustBreakEnd(TimePeroid break1) {
        if (break1.getEnd().isAfter(workingHours.getEnd())) {
            break1.setEnd(workingHours.getEnd());
        }
    }

    private void handleBreakOverlap(TimePeroid break1, TimePeroid peroid, List<TimePeroid> toAdd) {
        if (break1.getStart().equals(peroid.getStart()) && break1.getEnd().isAfter(peroid.getStart()) && break1.getEnd().isBefore(peroid.getEnd())) {
            peroid.setStart(break1.getEnd());
        }
        if (break1.getStart().isAfter(peroid.getStart()) && break1.getStart().isBefore(peroid.getEnd()) && break1.getEnd().equals(peroid.getEnd())) {
            peroid.setEnd(break1.getStart());
        }
        if (break1.getStart().isAfter(peroid.getStart()) && break1.getEnd().isBefore(peroid.getEnd())) {
            toAdd.add(new TimePeroid(peroid.getStart(), break1.getStart()));
            peroid.setStart(break1.getEnd());
        }
    }    

    public TimePeroid getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(TimePeroid workingHours) {
        this.workingHours = workingHours;
    }

    public List<TimePeroid> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<TimePeroid> breaks) {
        this.breaks = breaks;
    }

    public void removeBreak(TimePeroid breakToRemove) {
        breaks.remove(breakToRemove);
    }

    public void addBreak(TimePeroid breakToAdd) {
        breaks.add(breakToAdd);
    }

}
