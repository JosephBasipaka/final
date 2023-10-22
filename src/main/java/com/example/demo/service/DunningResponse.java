package com.example.demo.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DunningResponse {
    private boolean initialReminder;
    private long followUpReminderCount;
    private boolean finalReminder;
}
