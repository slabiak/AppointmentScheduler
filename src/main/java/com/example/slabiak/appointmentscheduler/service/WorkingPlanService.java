package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeriod;

public interface WorkingPlanService {
    void updateWorkingPlan(WorkingPlan workingPlan);

    void addBreakToWorkingPlan(TimePeriod breakToAdd, int planId, String dayOfWeek);

    void deleteBreakFromWorkingPlan(TimePeriod breakToDelete, int planId, String dayOfWeek);

    WorkingPlan getWorkingPlanByProviderId(int providerId);
}
