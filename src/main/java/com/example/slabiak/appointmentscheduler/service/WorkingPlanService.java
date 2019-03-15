package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;
import com.example.slabiak.appointmentscheduler.model.TimePeroid;

public interface WorkingPlanService {
    void update(WorkingPlan workingPlan);
    void addBreak(TimePeroid breakToAdd, int planId, String dayOfWeek);
    void deleteBreak(TimePeroid breakToDelete, int planId, String dayOfWeek);
    WorkingPlan generateDefaultWorkingPlan();
}
