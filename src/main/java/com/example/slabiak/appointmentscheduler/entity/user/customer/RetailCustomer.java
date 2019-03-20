package com.example.slabiak.appointmentscheduler.entity.user.customer;

import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.user.Role;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="retail_customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class RetailCustomer extends Customer {

    public RetailCustomer(){
    }

    public RetailCustomer(UserFormDTO newUserForm, String password, Collection<Role> roles){
        super(newUserForm,password,roles);
    }


}
