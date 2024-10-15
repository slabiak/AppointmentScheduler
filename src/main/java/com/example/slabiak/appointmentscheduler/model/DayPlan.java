package com.example.slabiak.appointmentscheduler.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DayPlan {

    private TimePeroid workingHours;
    private List<TimePeroid> breaks;

    public DayPlan() {
        breaks = new ArrayList();
    }

    // Decompose conditional Refactoring Technique Applied
    public List<TimePeroid> timePeriodsWithBreaksExcluded() {
        ArrayList<TimePeroid> timePeriodsWithBreaksExcluded = new ArrayList<>();
        timePeriodsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeroid> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            processBreaks(timePeriodsWithBreaksExcluded, breaks);

//            Before:

//            ArrayList<TimePeroid> toAdd = new ArrayList();
//            for (TimePeroid break1 : breaks) {
//                if (break1.getStart().isBefore(workingHours.getStart())) {
//                    break1.setStart(workingHours.getStart());
//                }
//                if (break1.getEnd().isAfter(workingHours.getEnd())) {
//                    break1.setEnd(workingHours.getEnd());
//                }
//                for (TimePeroid peroid : timePeriodsWithBreaksExcluded) {
//                    if (break1.getStart().equals(peroid.getStart()) && break1.getEnd().isAfter(peroid.getStart()) && break1.getEnd().isBefore(peroid.getEnd())) {
//                        peroid.setStart(break1.getEnd());
//                    }
//                    if (break1.getStart().isAfter(peroid.getStart()) && break1.getStart().isBefore(peroid.getEnd()) && break1.getEnd().equals(peroid.getEnd())) {
//                        peroid.setEnd(break1.getStart());
//                    }
//                    if (break1.getStart().isAfter(peroid.getStart()) && break1.getEnd().isBefore(peroid.getEnd())) {
//                        toAdd.add(new TimePeroid(peroid.getStart(), break1.getStart()));
//                        peroid.setStart(break1.getEnd());
//                    }
//                }
//            }
//            timePeriodsWithBreaksExcluded.addAll(toAdd);
//            Collections.sort(timePeriodsWithBreaksExcluded);
        }
        return timePeriodsWithBreaksExcluded;
    }

//    After
    private void processBreaks(List<TimePeroid> periodsWithoutBreaks, List<TimePeroid> breaks) {
        ArrayList<TimePeroid> toAdd = new ArrayList<>();
        for (TimePeroid breakPeriod : breaks) {
            adjustBreakStartAndEnd(breakPeriod);
            updateTimePeriods(periodsWithoutBreaks, breakPeriod, toAdd);
        }
        periodsWithoutBreaks.addAll(toAdd);
        Collections.sort(periodsWithoutBreaks);
    }

    private void adjustBreakStartAndEnd(TimePeroid breakPeriod) {
        if (breakPeriod.getStart().isBefore(workingHours.getStart())) {
            breakPeriod.setStart(workingHours.getStart());
        }
        if (breakPeriod.getEnd().isAfter(workingHours.getEnd())) {
            breakPeriod.setEnd(workingHours.getEnd());
        }
    }

    private void updateTimePeriods(List<TimePeroid> periodsWithoutBreaks, TimePeroid breakPeriod, List<TimePeroid> toAdd) {
        for (TimePeroid period : periodsWithoutBreaks) {
            if (breakPeriod.getStart().equals(period.getStart()) && breakPeriod.getEnd().isAfter(period.getStart()) && breakPeriod.getEnd().isBefore(period.getEnd())) {
                period.setStart(breakPeriod.getEnd());
            }
            if (breakPeriod.getStart().isAfter(period.getStart()) && breakPeriod.getStart().isBefore(period.getEnd()) && breakPeriod.getEnd().equals(period.getEnd())) {
                period.setEnd(breakPeriod.getStart());
            }
            if (breakPeriod.getStart().isAfter(period.getStart()) && breakPeriod.getEnd().isBefore(period.getEnd())) {
                toAdd.add(new TimePeroid(period.getStart(), breakPeriod.getStart()));
                period.setStart(breakPeriod.getEnd());
            }
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

    //For Conditional polymorphism

    public List<TimePeroid> timePeroidsWithBreaksExcluded() {
        List<TimePeroid> timePeriodsWithBreaksExcluded = new ArrayList<>();
        timePeriodsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeroid> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            List<TimePeriodAdjuster> adjusters = Arrays.asList(
                    new AdjusterBeforeStart(),
                    new AdjusterAfterEnd()
            );

            for (TimePeroid breakPeriod : breaks) {
                for (TimePeroid timePeriod : timePeriodsWithBreaksExcluded) {
                    for (TimePeriodAdjuster adjuster : adjusters) {
                        adjuster.adjust(timePeriod, breakPeriod);
                    }
                }
            }

            Collections.sort(timePeriodsWithBreaksExcluded);
        }

        return timePeriodsWithBreaksExcluded;
    }
}