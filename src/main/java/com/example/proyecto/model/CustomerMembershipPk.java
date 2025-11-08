package com.example.proyecto.model;

import java.io.Serializable;
import java.util.Objects;

public class CustomerMembershipPk implements Serializable{
    private Long customer;
    private Long membership;
    
    public CustomerMembershipPk() {}
    
    public CustomerMembershipPk(Long customerId, Long membershipId) {
        this.customer = customerId;
        this.membership = membershipId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) 
            return true;
        if (!(o instanceof CustomerMembershipPk))
            return false;
        CustomerMembershipPk that = (CustomerMembershipPk) o;
        return Objects.equals(customer, that.customer) && 
               Objects.equals(membership, that.membership);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(customer, membership);
    }

    public Long getCustomer() {
        return customer;
    }

    public void setCustomer(Long customer) {
        this.customer = customer;
    }

    public Long getMembership() {
        return membership;
    }

    public void setMembership(Long membership) {
        this.membership = membership;
    }  
}
