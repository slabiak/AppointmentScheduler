package com.example.slabiak.appointmentscheduler.service.impl;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.service.JwtTokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Component
public class JwtTokenServiceImpl implements JwtTokenService {

    @Value(value = "${app.jwtSecret}")
    private String jwtSecret;


    @Override
    public String generateAppointmentRejectionToken(Appointment appointment){
        ZoneId zoneId = ZoneId.of("Europe/Warsaw");
        Date expiryDate = convertLocalDateTimeToDate(appointment.getEnd().plusHours(24));
        return Jwts.builder()
                .claim("appointmentId",appointment.getId())
                .claim("customerId",appointment.getCustomer().getId())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Override
    public String generateAcceptRejectionToken(Appointment appointment){
        return Jwts.builder()
                .claim("appointmentId",appointment.getId())
                .claim("providerId",appointment.getProvider().getId())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }


    @Override
    public boolean validateToken(String token){
           try {
               Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
               return true;
           } catch (JwtException e) {
               //don't trust the JWT!
           }
           return false;

    }

    @Override
    public int getAppointmentIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("appointmentId");
    }

    @Override
    public int getCustomerIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("customerId");
    }

    @Override
    public int getProviderIdFromToken(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();
        return (int) claims.get("providerId");
    }

    @Override
    public Date convertLocalDateTimeToDate(LocalDateTime localDateTime){
        ZoneId zone = ZoneId.of("Europe/Warsaw");
        ZoneOffset zoneOffSet = zone.getRules().getOffset(localDateTime);
        Instant instant = localDateTime.toInstant(zoneOffSet);
        return Date.from(instant);
    }
}
