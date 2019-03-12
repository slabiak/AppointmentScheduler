package com.example.slabiak.appointmentscheduler.service;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Date;

@Component
public class JwtTokenService {

    @Value(value = "${app.jwtSecret}")
    private String jwtSecret;


    public String generateDenyTokenForAppointment(Appointment appointment){
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        Date expiryDate = convertLocalDateTimeToDate(appointment.getEnd().plusHours(24));
        return Jwts.builder()
                .claim("appointmentId",appointment.getId())
                .claim("customerId",appointment.getCustomer().getId())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token){
           try {
               Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
               return true;
           } catch (JwtException e) {
               //don't trust the JWT!
           }
           return false;

    }

    public int getAppointmentIdFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("appointmentId");
    }

    public int getCustomerIdFromJWT(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("customerId");
    }

    public Date convertLocalDateTimeToDate(LocalDateTime localDateTime){
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(localDateTime);
        Instant instant = localDateTime.toInstant(zoneOffSet);
        return Date.from(instant);
    }
}
