package com.example.slabiak.appointmentscheduler.kapoor98ak_test;

import com.example.slabiak.appointmentscheduler.controller.ExchangeController;
import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.security.CustomUserDetails;
import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import com.example.slabiak.appointmentscheduler.service.ExchangeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class ExchangeControllerTest {

    private ExchangeController exchangeController;

    @Mock
    private ExchangeService exchangeService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private CustomUserDetails currentUser;

    @Mock
    private Model model;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        exchangeController = new ExchangeController(exchangeService, appointmentService);
    }

    @Test
    public void testShowEligibleAppointmentsToExchange() {
        // Arrange
        int oldAppointmentId = 1;
        List<Appointment> eligibleAppointments = new ArrayList<>();
        when(exchangeService.getEligibleAppointmentsForExchange(oldAppointmentId)).thenReturn(eligibleAppointments);

        // Act
        String viewName = exchangeController.showEligibleAppointmentsToExchange(oldAppointmentId, model);

        // Assert
        assertEquals("exchange/listProposals", viewName);
        verify(model).addAttribute("appointmentId", oldAppointmentId);
        verify(model).addAttribute("numberOfEligibleAppointments", eligibleAppointments.size());
        verify(model).addAttribute("eligibleAppointments", eligibleAppointments);
    }

    @Test
    public void testShowExchangeSummaryScreen_ExchangePossible() {
        // Arrange
        int oldAppointmentId = 1;
        int newAppointmentId = 2;
        when(exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, currentUser.getId())).thenReturn(true);

        // Act
        String viewName = exchangeController.showExchangeSummaryScreen(oldAppointmentId, newAppointmentId, model, currentUser);

        // Assert
        verify(model).addAttribute("oldAppointment", appointmentService.getAppointmentByIdWithAuthorization(oldAppointmentId));
        verify(model).addAttribute("newAppointment", appointmentService.getAppointmentById(newAppointmentId));
        assertEquals("exchange/exchangeSummary", viewName);
    }

    @Test
    public void testShowExchangeSummaryScreen_ExchangeNotPossible() {
        // Arrange
        int oldAppointmentId = 1;
        int newAppointmentId = 2;
        when(exchangeService.checkIfExchangeIsPossible(oldAppointmentId, newAppointmentId, currentUser.getId())).thenReturn(false);

        // Act
        String viewName = exchangeController.showExchangeSummaryScreen(oldAppointmentId, newAppointmentId, model, currentUser);

        // Assert
        verify(model, never()).addAttribute("oldAppointment", appointmentService.getAppointmentByIdWithAuthorization(oldAppointmentId));
        verify(model, never()).addAttribute("newAppointment", appointmentService.getAppointmentById(newAppointmentId));
        assertEquals("redirect:/appointments/all", viewName);
    }



    @Test
    public void testProcessExchangeRequest_Failure() {
        // Arrange
        int oldAppointmentId = 1;
        int newAppointmentId = 2;
        when(exchangeService.requestExchange(oldAppointmentId, newAppointmentId, currentUser.getId())).thenReturn(false);

        // Act
        String viewName = exchangeController.processExchangeRequest(oldAppointmentId, newAppointmentId, model, currentUser);

        // Assert
        verify(model).addAttribute("message", "Error! Exchange not sent!");
        assertEquals("exchange/requestConfirmation", viewName);
    }

    @Test
    public void testProcessExchangeAcceptation() {
        // Arrange
        int exchangeId = 1;

        // Act
        String viewName = exchangeController.processExchangeAcceptation(exchangeId, model, currentUser);

        // Assert
        verify(exchangeService).acceptExchange(exchangeId, currentUser.getId());
        assertEquals("redirect:/appointments/all", viewName);
    }

    @Test
    public void testProcessExchangeRejection() {
        // Arrange
        int exchangeId = 1;

        // Act
        String viewName = exchangeController.processExchangeRejection(exchangeId, model, currentUser);

        // Assert
        verify(exchangeService).rejectExchange(exchangeId, currentUser.getId());
        assertEquals("redirect:/appointments/all", viewName);
    }
}
