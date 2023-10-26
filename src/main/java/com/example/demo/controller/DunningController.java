package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.DunningResponse;
import com.example.demo.service.DunningService;

@RestController
@RequestMapping("/api/dunning")
@CrossOrigin(origins = "*")
public class DunningController {

    @Autowired
    private DunningScheduler dunningScheduler;
    @Autowired
    private DunningService dunningService;

    @PostMapping("/intReminder")
    public ResponseEntity<String> executeInitialReminder(@RequestParam String reminderType) {
        dunningScheduler.configSchedulerManually(reminderType);
        return ResponseEntity.ok("Scheduled " + reminderType + " reminder executed immediately.");
    }

    @PostMapping("/termination")
    public ResponseEntity<String> executeTermination() {
        dunningScheduler.scheduleServiceTermination();
        return ResponseEntity.ok("Scheduled Termination executed immediately.");
    }

    @GetMapping("/dunningDetails")
    public List<DunningResponse> getDunningData(@RequestParam Long customerId, @RequestParam Long serviceId) {
        return dunningService.getDunningDetails(customerId, serviceId);
    }
}
