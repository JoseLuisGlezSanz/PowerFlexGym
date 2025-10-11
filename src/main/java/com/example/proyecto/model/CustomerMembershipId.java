package com.example.proyecto.model;

import java.io.Serializable;
import java.util.Objects;

public class CustomerMembershipId implements Serializable{
    private Integer idCustomer;
    private Integer idMembership;

    public CustomerMembershipId() {}
    
    public CustomerMembershipId(Integer idCustomer, Integer idMembership) {
        this.idCustomer = idCustomer;
        this.idMembership = idMembership;
    }

    public void setIdCustomer(Integer idCustomer) {
        this.idCustomer = idCustomer;
    }

    public void setIdMembership(Integer idMembership) {
        this.idMembership = idMembership;
    }

    public Integer getIdCustomer() {
        return idCustomer;
    }

    public Integer getIdMembership() {
        return idMembership;
    }  
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerMembershipId that = (CustomerMembershipId) o;
        return Objects.equals(idCustomer, that.idCustomer)
                && Objects.equals(idMembership, that.idMembership);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCustomer, idMembership);
    }
}