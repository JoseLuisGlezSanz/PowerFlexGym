package com.example.proyecto.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.proyecto.dto.CustomerRequest;
import com.example.proyecto.dto.CustomerResponse;
import com.example.proyecto.mapper.CustomerMapper;
import com.example.proyecto.mapper.EmergencyContactMapper;
import com.example.proyecto.model.Customer;
import com.example.proyecto.model.EmergencyContact;
import com.example.proyecto.model.Gym;
import com.example.proyecto.repository.CustomerRepository;
import com.example.proyecto.repository.EmergencyContactRepository;
import com.example.proyecto.repository.GymRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final GymRepository gymRepository;
    private final EmergencyContactRepository emergencyContactRepository;

    @Override
    public List<CustomerResponse> findAll() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper::toResponse)
                .toList();
    }

    @Override
    public CustomerResponse findById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
                
        return CustomerMapper.toResponse(customer);
    }

    @Override
    public CustomerResponse create(CustomerRequest customerRequest) {
        Gym gym = gymRepository.findById(customerRequest.getGymId())
            .orElseThrow(() -> new RuntimeException("Gimnasio no encontrado con ID: " + customerRequest.getGymId()));

        Customer customer = CustomerMapper.toEntity(customerRequest, gym, null);
        Customer savedCustomer = customerRepository.save(customer);

        EmergencyContact emergencyContact = null;
        if (customerRequest.getContactName() != null && customerRequest.getContactPhone() != null) {
            emergencyContact = EmergencyContactMapper.toEntity(
                customerRequest.getContactName(), 
                customerRequest.getContactPhone()
            );
            
            emergencyContact.setCustomer(savedCustomer);
            
            emergencyContact = emergencyContactRepository.save(emergencyContact);
            
            savedCustomer.setEmergencyContact(emergencyContact);
            
            savedCustomer = customerRepository.save(savedCustomer);
        }
        return CustomerMapper.toResponse(savedCustomer);
    }

    @Override
    public CustomerResponse update(Long id, CustomerRequest customerRequest) {
        Customer excistingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer no encontrado con ID: " + id));
        
        Gym gym = gymRepository.findById(customerRequest.getGymId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID" + customerRequest.getGymId()));
        
        EmergencyContact emergencyContact = excistingCustomer.getEmergencyContact();

        emergencyContact.setContactName(customerRequest.getContactName());
        emergencyContact.setContactPhone(customerRequest.getContactPhone());

        CustomerMapper.copyToEntity(customerRequest, excistingCustomer, gym, emergencyContact);

        Customer updateCustomer = customerRepository.save(excistingCustomer);
        return CustomerMapper.toResponse(updateCustomer);
    }

     // @Override
     // public void delete(Long id){
     //      customerRepository.deleteById(id);
     // }

    @Override
    public List<CustomerResponse> getAll(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Customer> customers = customerRepository.findAll(pageReq);
        return customers.getContent().stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerResponse> findByName(String name) {
          List<Customer> customers = customerRepository.findByName(name);
          return customers.stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerResponse> findByVerifiedNumberTrue() {
          List<Customer> customers = customerRepository.findByVerifiedNumberTrue();
          return customers.stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerResponse> getByVerifiedNumberTrue(int page, int pageSize) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Customer> customers = customerRepository.findByVerifiedNumberTrue(pageReq);
        return customers.getContent().stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerResponse> findAllCustomersByGymId(Long gymId) {
          List<Customer> customers = customerRepository.findAllCustomersByGymId(gymId);
          return customers.stream().map(CustomerMapper::toResponse).toList();
    }

    @Override
    public List<CustomerResponse> getAllCustomersByGymId(int page, int pageSize, Long gymId) {
        PageRequest pageReq = PageRequest.of(page, pageSize);
        Page<Customer> customers = customerRepository.getAllCustomersByGymId(gymId, pageReq);
        return customers.getContent().stream().map(CustomerMapper::toResponse).toList();
    }
}   