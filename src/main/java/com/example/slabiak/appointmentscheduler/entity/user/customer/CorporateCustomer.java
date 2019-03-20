package com.example.slabiak.appointmentscheduler.entity.user.customer;

import com.example.slabiak.appointmentscheduler.model.UserFormDTO;
import com.example.slabiak.appointmentscheduler.entity.user.Role;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name="corporate_customers")
@PrimaryKeyJoinColumn(name = "id_customer")
public class CorporateCustomer extends Customer {

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "vat_number")
    private String vatNumber;


    public CorporateCustomer(){
    }

    public CorporateCustomer(UserFormDTO newUserForm, String encryptedPassword, Collection<Role> roles){
        super(newUserForm,encryptedPassword,roles);
        this.companyName = newUserForm.getCompanyName();
        this.vatNumber = newUserForm.getVatNumber();
    }

    @Override
    public void update(UserFormDTO updateData) {
        super.update(updateData);
        this.companyName = updateData.getCompanyName();
        this.vatNumber = updateData.getVatNumber();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

}
