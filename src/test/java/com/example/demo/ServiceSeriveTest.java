package com.example.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;
import com.example.demo.repositories.ServiceRepository;
import com.example.demo.service.ServiceService;


@SpringBootTest
public class ServiceSeriveTest {
    
    @Autowired
    private ServiceService serviceService;

    @MockBean
    private ServiceRepository serviceRepository;

    @Test
    public void testServiceList_Success() {
        List<Service> services = new ArrayList<>();
        services.add(new Service(1L, "basic" , 1000, "Active", null, null));
        services.add(new Service(2L, "premium" , 5000, "Terminated", null, null));
        Mockito.when(serviceRepository.findAll()).thenReturn(services);

        List<Service> result = serviceService.serviceList();
        assertEquals(2, result.size());
    }
    @Test
    public void testServiceList_Fail() {
        List<Service> services = new ArrayList<>();
        services.add(new Service(1L, "basic" , 1000, "Active", null, null));
        Mockito.when(serviceRepository.findAll()).thenReturn(services);

        List<Service> result = serviceService.serviceList();
        assertNotEquals(2, result.size());
    }

    @Test
    public void testAddService_Success() {
        Service Service = new Service(1L, "basic" , 1000, "Active", null, null);
        Mockito.when(serviceRepository.save(Service)).thenReturn(Service);

        Service createdService = serviceService.createService(Service);
        assertEquals(Service, createdService);
    }
    @Test
    public void testAddService_Fail() {
       Service Service = new Service(1L, "basic" , 1000, "Active", null, null);
        Mockito.when(serviceRepository.save(Service)).thenReturn(Service);

        Service test = new Service();
        Service createdService = serviceService.createService(test);
        assertNotEquals(Service, createdService);
    }

     @Test
    public void testFindServiceForCustomer_Success() {
        List<Service> services = new ArrayList<>();
        Service service = new Service(1L, "basic" , 1000, "Active", null, null);
        services.add(service);
        Customer customer = new Customer(1L, "Joseph", "joseph@gmail.com", null, services);

        Mockito.when(serviceRepository.findByCustomer(customer)).thenReturn(service);

        Service result = serviceService.findServiceForCustomer(customer);
        assertEquals(service, result);
    }
}
