package com.example.slabiak.appointmentscheduler.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DayPlan {

    private TimePeroid workingHours;
    private List<TimePeroid> breaks;

    public DayPlan() {
        breaks = new ArrayList();
    }


/*    public List<TimePeroid> timePeroidsWithBreaksExcluded() {
        ArrayList<TimePeroid> timePeroidsWithBreaksExcluded = new ArrayList<>();
        timePeroidsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeroid> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            ArrayList<TimePeroid> toAdd = new ArrayList();
            for (TimePeroid break1 : breaks) {
                if (break1.getStart().isBefore(workingHours.getStart())) {
                    break1.setStart(workingHours.getStart());
                }
                if (break1.getEnd().isAfter(workingHours.getEnd())) {
                    break1.setEnd(workingHours.getEnd());
                }
                for (TimePeroid peroid : timePeroidsWithBreaksExcluded) {
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
            }
            timePeroidsWithBreaksExcluded.addAll(toAdd);
            Collections.sort(timePeroidsWithBreaksExcluded);
        }


        return timePeroidsWithBreaksExcluded;
    }*/

/*
    public List<TimePeroid> timePeroidsWithBreaksExcluded() {
        ArrayList<TimePeroid> timePeroidsWithBreaksExcluded = new ArrayList<>();
        timePeroidsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeroid> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            processBreaks(timePeroidsWithBreaksExcluded, breaks);
        }

        return timePeroidsWithBreaksExcluded;
    }

    private void processBreaks(List<TimePeroid> timePeroidsWithBreaksExcluded, List<TimePeroid> breaks) {
        ArrayList<TimePeroid> toAdd = new ArrayList<>();
        for (TimePeroid break1 : breaks) {
            adjustBreakStartAndEnd(break1);
            updateTimePeroids(timePeroidsWithBreaksExcluded, break1, toAdd);
        }
        timePeroidsWithBreaksExcluded.addAll(toAdd);
        Collections.sort(timePeroidsWithBreaksExcluded);
    }

    private void adjustBreakStartAndEnd(TimePeroid break1) {
        if (break1.getStart().isBefore(workingHours.getStart())) {
            break1.setStart(workingHours.getStart());
        }
        if (break1.getEnd().isAfter(workingHours.getEnd())) {
            break1.setEnd(workingHours.getEnd());
        }
    }

    private void updateTimePeroids(List<TimePeroid> timePeroidsWithBreaksExcluded, TimePeroid break1, List<TimePeroid> toAdd) {
        for (TimePeroid peroid : timePeroidsWithBreaksExcluded) {
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
    }
*/


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

    public List<TimePeroid> timePeroidsWithBreaksExcluded() {
        List<TimePeroid> timePeriodsWithBreaksExcluded = new ArrayList<>();
        timePeriodsWithBreaksExcluded.add(getWorkingHours());
        List<TimePeroid> breaks = getBreaks();

        if (!breaks.isEmpty()) {
            List<TimePeriodAdjuster> adjusters = Arrays.asList(
                    new AdjusterBeforeStart(),
                    new AdjusterAfterEnd()
                    // Add more adjusters as needed
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
