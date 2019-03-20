package com.example.slabiak.appointmentscheduler.entity.user.provider;

import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.*;
import com.example.slabiak.appointmentscheduler.entity.user.Role;
import com.example.slabiak.appointmentscheduler.entity.user.User;
import com.example.slabiak.appointmentscheduler.entity.WorkingPlan;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="providers")
@PrimaryKeyJoinColumn(name = "id_provider")
public class Provider extends User {

    @OneToMany(mappedBy = "provider")
    private List<Appointment> appointments;

    @ManyToMany
    @JoinTable(name="works_providers", joinColumns=@JoinColumn(name="id_user"), inverseJoinColumns=@JoinColumn(name="id_work"))
    private List<Work> works;

    @OneToOne(mappedBy="provider", cascade = {CascadeType.ALL})
    private WorkingPlan workingPlan;

    public Provider(){
    }

    public Provider(UserFormDTO userForm, String encryptedPassword, Collection<Role> roles, WorkingPlan workingPlan) {
        super(userForm,encryptedPassword,roles);
        this.workingPlan = workingPlan;
        workingPlan.setProvider(this);
        this.works = userForm.getWorks();
    }

    @Override
    public void update(UserFormDTO updateData) {
        super.update(updateData);
        this.works = updateData.getWorks();
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<Work> getWorks() {
        return works;
    }

    public void setWorks(List<Work> works) {
        this.works = works;
    }

    public WorkingPlan getWorkingPlan() {
        return workingPlan;
    }

    public void setWorkingPlan(WorkingPlan workingPlan) {
        this.workingPlan = workingPlan;
    }
}
