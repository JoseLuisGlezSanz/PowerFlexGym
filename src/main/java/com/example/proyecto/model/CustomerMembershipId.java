package com.example.proyecto.model;

import java.io.Serializable;
import java.util.Objects;

public class CustomerMembershipId implements Serializable{
    private Integer customer;
    private Integer membership;

    public CustomerMembershipId() {}
    
    public CustomerMembershipId(Integer customer, Integer membership) {
        this.customer = customer;
        this.membership = membership;
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public Integer getMembership() {
        return membership;
    } 

    public void setMembership(Integer membership) {
        this.membership = membership;
    } 
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerMembershipId that = (CustomerMembershipId) o;
        return Objects.equals(customer, that.customer)
                && Objects.equals(membership, that.membership);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customer, membership);
    }
}