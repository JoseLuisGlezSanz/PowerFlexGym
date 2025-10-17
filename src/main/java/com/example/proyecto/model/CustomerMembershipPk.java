package com.example.proyecto.model;

import java.io.Serializable;
import java.util.Objects;

public class CustomerMembershipPk implements Serializable{
    private Integer customer;
    private Integer membership;
    
    // Constructor por defecto
    public CustomerMembershipPk() {}
    
    // Constructor con parámetros
    public CustomerMembershipPk(Integer customer, Integer membership) {
        this.customer = customer;
        this.membership = membership;
    }
    
    // Getters y Setters
    public Integer getCustomer() { return customer; }
    public void setCustomer(Integer customer) { this.customer = customer; }
    
    public Integer getMembership() { return membership; }
    public void setMembership(Integer membership) { this.membership = membership; }
    
    // equals y hashCode (OBLIGATORIOS)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerMembershipPk that = (CustomerMembershipPk) o;
        return Objects.equals(customer, that.customer) && 
               Objects.equals(membership, that.membership);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customer, membership);
    }
}
