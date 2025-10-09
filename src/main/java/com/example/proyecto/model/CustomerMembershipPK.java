package com.example.proyecto.model;

import java.io.Serializable;
import java.util.Objects;

public class CustomerMembershipPK implements Serializable{
    private Integer idCustomer;
    private Integer idMembership;

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
        CustomerMembershipPK that = (CustomerMembershipPK) o;
        return Objects.equals(idCustomer, that.idCustomer)
                && Objects.equals(idMembership, that.idMembership);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCustomer, idMembership);
    }
}