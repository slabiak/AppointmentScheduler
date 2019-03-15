package com.example.slabiak.appointmentscheduler.security;

import com.example.slabiak.appointmentscheduler.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private AppointmentService appointmentService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        CustomUserDetails currentUser = (CustomUserDetails) authentication.getPrincipal();
        /*
        * if admin logged in, update all appointments statuses, otherwise update only user related statuses
        * */
        if(currentUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))){
            appointmentService.updateAllAppointmentsStatuses();
        }else {
            appointmentService.updateUserAppointmentsStatuses(currentUser.getId());
        }
        response.sendRedirect(request.getContextPath() + "/");
    }

}
