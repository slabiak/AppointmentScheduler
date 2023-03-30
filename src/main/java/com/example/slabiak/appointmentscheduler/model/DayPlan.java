package com.example.slabiak.appointmentscheduler.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DayPlan {

    private TimePeriod workingHours;
    private List<TimePeriod> breaks;

    public DayPlan() {
        breaks = new ArrayList();
    }

    public List<TimePeriod> timePeriodsWithBreaksExcluded() {
        ArrayList<TimePeriod> timePeriodsWithBreaksExcluded = new ArrayList<>();
        timePeriodsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeriod> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            ArrayList<TimePeriod> toAdd = new ArrayList();
            for (TimePeriod break1 : breaks) {
                if (break1.getStart().isBefore(workingHours.getStart())) {
                    break1.setStart(workingHours.getStart());
                }
                if (break1.getEnd().isAfter(workingHours.getEnd())) {
                    break1.setEnd(workingHours.getEnd());
                }
                for (TimePeriod period : timePeriodsWithBreaksExcluded) {
                    if (break1.getStart().equals(period.getStart()) && break1.getEnd().isAfter(period.getStart()) && break1.getEnd().isBefore(period.getEnd())) {
                        period.setStart(break1.getEnd());
                    }
                    if (break1.getStart().isAfter(period.getStart()) && break1.getStart().isBefore(period.getEnd()) && break1.getEnd().equals(period.getEnd())) {
                        period.setEnd(break1.getStart());
                    }
                    if (break1.getStart().isAfter(period.getStart()) && break1.getEnd().isBefore(period.getEnd())) {
                        toAdd.add(new TimePeriod(period.getStart(), break1.getStart()));
                        period.setStart(break1.getEnd());
                    }
                }
            }
            timePeriodsWithBreaksExcluded.addAll(toAdd);
            Collections.sort(timePeriodsWithBreaksExcluded);
        }


        return timePeriodsWithBreaksExcluded;
    }

    public TimePeriod getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(TimePeriod workingHours) {
        this.workingHours = workingHours;
    }

    public List<TimePeriod> getBreaks() {
        return breaks;
    }

    public void setBreaks(List<TimePeriod> breaks) {
        this.breaks = breaks;
    }

    public void removeBreak(TimePeriod breakToRemove) {
        breaks.remove(breakToRemove);
    }

    public void addBreak(TimePeriod breakToAdd) {
        breaks.add(breakToAdd);
    }

}
