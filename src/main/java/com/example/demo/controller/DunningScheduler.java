package com.example.demo.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;
import com.example.demo.service.CustomerService;
import com.example.demo.service.DunningService;
import com.example.demo.service.ServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class DunningScheduler {
    @Autowired
    private DunningService dunningService; 
    @Autowired
    private CustomerService customerService; 
    @Autowired
    private ServiceService serviceService; 

    // Schedule an initial reminder every day at a specific time
    // @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "0 * * * * *")
    public void scheduleInitialReminder() {
        System.out.println("Scheduled Initial Reminder task started");
        List<Customer> overdueCustomers = customerService.findOverdueCustomers();
        System.out.println(overdueCustomers);
        for (Customer customer : overdueCustomers) {
            Service service = serviceService.findServiceForCustomer(customer);
            dunningService.initiateDunning(customer, service,"Initial Remainder");
        }
        System.out.println("Scheduled Initial Reminder task completed");
    }


    // Schedule follow-up reminders every week
    // @Scheduled(cron = "0 0 0 * * 1")
    @Scheduled(cron = "0 */2 * * * *")
    public void scheduleFollowUpReminder() {
        System.out.println("Scheduled Follow-Up Reminder task started");
        List<Customer> customersWithFollowUp = dunningService.findCustomersWithFollowUp();
        System.out.println("foll"+ customersWithFollowUp);
        for (Customer customer : customersWithFollowUp) {
            Service service = serviceService.findServiceForCustomer(customer);
            dunningService.initiateDunning(customer, service, "Follow-Up Reminder");
        }
        System.out.println("Scheduled Follow-Up Reminder task completed");
    }

    // Schedule final notices every month
    // @Scheduled(cron = "0 0 0 1 * ?")
    @Scheduled(cron = "0 */3 * * * *")
    public void scheduleFinalNotice() {
        System.out.println("Scheduled Final Notice task started");
        List<Customer> customersWithFinalNotice = dunningService.findCustomersWithFinalNotice();
        for (Customer customer : customersWithFinalNotice) {
            Service service = serviceService.findServiceForCustomer(customer);
            dunningService.initiateDunning(customer, service, "Final Notice");
            boolean finalNoticeSent = dunningService.isFinalNoticeSent(customer,service);

            if (finalNoticeSent)
                scheduleServiceTermination();
        }
        System.out.println("Scheduled Final Notice task completed");
    }

    public void scheduleServiceTermination() {
       
        long delay = (long) 2 * 60 * 1000;
        Executors.newScheduledThreadPool(1).schedule(() -> {
        System.out.println("Scheduled Service Termination task started");
        List<Customer> customersWithFinalNotice = dunningService.findCustomersWithFinalNotice();
        for (Customer customer : customersWithFinalNotice) {
            Service service = serviceService.findServiceForCustomer(customer);
            if (service.getStatus().equals("Active") && dunningService.isServiceTerminationNeeded(customer)) {
                dunningService.handleFinalNotice(customer, service);
            }
        }
        System.out.println("Scheduled Service Termination task completed");
        }, delay, TimeUnit.MILLISECONDS);
    }

    
}

