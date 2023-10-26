package com.example.demo.controller;

import org.springframework.stereotype.Component;
import com.example.demo.entity.Customer;
import com.example.demo.entity.Service;
import com.example.demo.service.CustomerService;
import com.example.demo.service.DunningService;
import com.example.demo.service.ServiceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Component
public class DunningScheduler {
    @Autowired
    private DunningService dunningService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ServiceService serviceService;
    @Value("${file.path}")
    String customPath;

    // Schedule an initial reminder every day at a specific time
    // @Scheduled(cron = "0 0 0 * * ?")
    @Scheduled(cron = "*/50 * * * * *") // for demo set the timer for seconds
    public void scheduleInitialReminder() {
        List<Customer> overdueCustomers = customerService.findOverdueCustomers();
        for (Customer customer : overdueCustomers) {
            Service service = serviceService.findServiceForCustomer(customer);
            dunningService.initiateDunningProcess(customer, service, "Initial Reminder");
        }
    }

    // Schedule follow-up reminders every week
    // @Scheduled(cron = "0 0 0 * * 1")
    @Scheduled(cron = "*/200 * * * * *")
    public void scheduleFollowUpReminder() {
        List<Customer> customersWithFollowUp = dunningService.findCustomersWithFollowUp();
        for (Customer customer : customersWithFollowUp) {
            Service service = serviceService.findServiceForCustomer(customer);
            dunningService.initiateDunningProcess(customer, service, "Follow-Up Reminder");
        }
    }

    // Schedule final notices every month
    // @Scheduled(cron = "0 0 0 1 * ?")
    @Scheduled(cron = "*/300 * * * * *")
    public void scheduleFinalNotice() {
        List<Customer> customersWithFinalNotice = dunningService.findCustomersWithFinalNotice();
        for (Customer customer : customersWithFinalNotice) {
            Service service = serviceService.findServiceForCustomer(customer);
            dunningService.initiateDunningProcess(customer, service, "Final Notice");
            boolean finalNoticeSent = dunningService.isFinalNoticeSent(customer, service);

            if (finalNoticeSent)
                scheduleServiceTermination();
        }
    }

    public void scheduleServiceTermination() {

        List<Customer> customersWithFinalNotice = dunningService.findCustomersWithFinalNotice();
        for (Customer customer : customersWithFinalNotice) {
            Service service = serviceService.findServiceForCustomer(customer);
            if (service.getStatus().equals("Active") && dunningService.isServiceTerminationNeeded(customer))
                dunningService.handleFinalNotice(customer, service, customPath);

        }
    }

    public void configSchedulerManually(String schedule) {
        if ("initial".equalsIgnoreCase(schedule))
            scheduleInitialReminder();
        else if ("followup".equalsIgnoreCase(schedule))
            scheduleFollowUpReminder();
        else
            scheduleFinalNotice();
    }

}
