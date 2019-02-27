package com.example.slabiak.appointmentscheduler.model;

import com.example.slabiak.appointmentscheduler.entity.Appointment;

import java.util.List;

public class ApiResponse {

    int success;
    List<Appointment> result;

    public ApiResponse(int success, List<Appointment> result) {
        this.success = success;
        this.result = result;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<Appointment> getResult() {
        return result;
    }

    public void setResult(List<Appointment> result) {
        this.result = result;
    }
}
