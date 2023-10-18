package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.service.DunningService;

@RestController
@RequestMapping("/api/dunning")
@CrossOrigin(origins = "*")
public class DunningController {

    // @Autowired
    // private DunningService dunningService;
    @Autowired
    private DunningScheduler dunningScheduler;


    @PostMapping("/intReminder")
    public ResponseEntity<String> executeInitialReminder() {
        dunningScheduler.scheduleFollowUpReminder();
        return ResponseEntity.ok("Scheduled Initial Reminder executed immediately.");
    }
}

