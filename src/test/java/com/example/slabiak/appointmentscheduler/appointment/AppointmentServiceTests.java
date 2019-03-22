package com.example.slabiak.appointmentscheduler.appointment;

import com.example.slabiak.appointmentscheduler.dao.AppointmentRepository;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.service.impl.AppointmentServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Appointment appointment;
    private Optional<Appointment> optionalAppointment;
    private List<Appointment> appointments;

/*    @Before
    public void initObjects(){
        appointment = new Appointment(LocalDateTime.now(),LocalDateTime.now(),new User(),new User(), new Work());
        optionalAppointment = Optional.of(appointment);
        appointments = new ArrayList<>();
        appointments.add(appointment);
    }*/

    /*@Test
    public void shouldSaveAppointment(){
        appointmentService.createNewAppointment(appointment);
        verify(appointmentRepository).createNewAppointment(appointment);
    }*/

    @Test
    public void shouldFindById() {
        when(appointmentRepository.findById(1)).thenReturn(optionalAppointment);
        assertEquals(optionalAppointment.get(), appointmentService.getAppointmentById(1));
        verify(appointmentRepository).findById(1);
    }

    @Test
    public void shouldFindAllAppointments(){
        when(appointmentRepository.findAll()).thenReturn(appointments);
        assertEquals(appointments,appointmentService.getAllAppointments());
        verify(appointmentRepository).findAll();
    }

    @Test
    public void shouldDeleteById() {
        appointmentService.deleteAppointmentById(1);
        verify(appointmentRepository).deleteById(1);
    }



}
