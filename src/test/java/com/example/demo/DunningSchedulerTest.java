package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import com.example.demo.controller.DunningScheduler;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;
import com.example.demo.service.CustomerService;
import com.example.demo.service.DunningService;
import com.example.demo.service.ServiceService;

@SpringBootTest
@DirtiesContext
@TestPropertySource(properties = {
        "spring.scheduled.enabled=false"
})
class DunningSchedulerTest {

    @MockBean
    private DunningService dunningService;

    @Autowired
    private DunningScheduler dunningScheduler;
    @MockBean
    private CustomerService customerService;
    @MockBean
    private ServiceService serviceService;
    // @Test
    // void scheduleInitialReminderTest() {
    // System.out.println("Scheduled Initial Reminder task started");
    // List<Customer> overdueCustomers = new ArrayList<>();
    // overdueCustomers.add(new Customer(1L, "John", "john@gmail.com", null,
    // null,null,null));
    // overdueCustomers.add(new Customer(2L, "Joseph", "joseph@gmail.com", null,
    // null,null,null));
    // System.out.println(overdueCustomers);
    // for (Customer customer : overdueCustomers) {
    // Service service = new Service(1L, "basic" , 1000, "Active", null, null);
    // dunningService.initiateDunning(customer, service,"Initial Remainder");
    // }
    // System.out.println("Scheduled Initial Reminder task completed");
    // }

    @Test
    void testScheduleInitialReminder() {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "john@gmail.com", null, null, null, null));
        customers.add(new Customer(2L, "Joseph", "joseph@gmail.com", null, null, null, null));
        when(customerService.findOverdueCustomers()).thenReturn(customers);

        Service service = new Service(1L, "basic", 1000, "Active", null, null);
        when(serviceService.findServiceForCustomer(any(Customer.class))).thenReturn(service);
        for (Customer customer : customers) {
            doNothing().when(dunningService).initiateDunning(customer, service, "Intial Remainder");
        }
        dunningScheduler.scheduleInitialReminder();

        verify(customerService, times(1)).findOverdueCustomers();
        verify(serviceService, times(customers.size())).findServiceForCustomer(any(Customer.class));
        // verify(dunningService,
        // times(customers.size())).initiateDunning(any(Customer.class),
        // any(Service.class), anyString());

    }

    @Test
    void testFollowUpReminder() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "john@gmail.com", null, null, null, null));
        customers.add(new Customer(2L, "Joseph", "joseph@gmail.com", null, null, null, null));
        when(dunningService.findCustomersWithFollowUp()).thenReturn(customers);
        Service service = new Service(1L, "basic", 1000, "Active", null, null);
        when(serviceService.findServiceForCustomer(any(Customer.class))).thenReturn(service);
        for (Customer customer : customers) {
            doNothing().when(dunningService).initiateDunning(customer, service, "Follow-Up Remainder");
            when(dunningService.getTempDirectory()).thenReturn(Path.of("/test"));
        }
        dunningScheduler.scheduleFollowUpReminder();

        verify(dunningService, times(1)).findCustomersWithFollowUp();
        verify(serviceService, times(customers.size())).findServiceForCustomer(any(Customer.class));
        // verify(dunningService,
        // times(customers.size())).initiateDunning(any(Customer.class),
        // any(Service.class),
        // anyString());

    }

    @Test
    void testScheduleFinalNotice() {

        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1L, "John", "john@gmail.com", null, null, null, null));
        customers.add(new Customer(2L, "Joseph", "joseph@gmail.com", null, null, null, null));
        when(dunningService.findCustomersWithFinalNotice()).thenReturn(customers);

        Service service = new Service(1L, "basic", 1000, "Active", null, null);
        when(serviceService.findServiceForCustomer(any(Customer.class))).thenReturn(service);
        for (Customer customer : customers) {
            doNothing().when(dunningService).initiateDunning(customer, service, "Final Notice");
            when(dunningService.isFinalNoticeSent(customer, service)).thenReturn(true);
        }
        dunningScheduler.scheduleFinalNotice();

        verify(dunningService, times(3)).findCustomersWithFinalNotice();
        verify(serviceService, times(customers.size() * 3)).findServiceForCustomer(any(Customer.class));
        // verify(dunningService,
        // times(customers.size())).initiateDunning(any(Customer.class),
        // any(Service.class),
        // anyString());

    }
}
