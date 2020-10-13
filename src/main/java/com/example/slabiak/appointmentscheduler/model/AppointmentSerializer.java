package com.example.slabiak.appointmentscheduler.model;

import com.example.slabiak.appointmentscheduler.entity.Appointment;
import com.example.slabiak.appointmentscheduler.entity.AppointmentStatus;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.ZoneOffset;

public class AppointmentSerializer extends StdSerializer<Appointment> {

    public AppointmentSerializer() {
        this(null);
    }

    public AppointmentSerializer(Class<Appointment> t) {
        super(t);
    }

    @Override
    public void serialize(Appointment appointment, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", appointment.getId());
        gen.writeStringField("title", appointment.getWork().getName());
        gen.writeNumberField("start", appointment.getStart().toInstant(ZoneOffset.UTC).toEpochMilli());
        gen.writeNumberField("end", appointment.getEnd().toInstant(ZoneOffset.UTC).toEpochMilli());
        gen.writeStringField("url", "/appointments/" + appointment.getId());
        gen.writeStringField("color", appointment.getStatus().equals(AppointmentStatus.SCHEDULED) ? "#28a745" : "grey");
        gen.writeEndObject();
    }
}
