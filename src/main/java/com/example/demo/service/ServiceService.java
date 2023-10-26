package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;

@org.springframework.stereotype.Service
public class ServiceService {

    @Autowired
    private ServiceRepository serviceRepository;

    public List<Service> serviceList() {
        return serviceRepository.findAll();
    }

    public Service findServiceForCustomer(Customer customer) {
        return serviceRepository.findByCustomer(customer);
    }

    public Service findId(Long customerId, String serviceName) {
        return serviceRepository.findByCustomerIdAndServiceName(customerId, serviceName);
    }

    public Service createService(Service service) {
        return serviceRepository.save(service);
    }

    public Service getService(Long serviceId) {
        return serviceRepository.findById(serviceId).orElse(null);
    }

    public Service getCustomerAndService(String name, String serviceName) {
        return serviceRepository.findByCustomerNameAndServiceName(name, serviceName);
    }
}
