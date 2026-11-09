package org.com.NotificationService.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.com.NotificationService.DTO.EmailNotificationDTO;
import org.com.NotificationService.DTO.SmsnDTO;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.Target;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class SchedulerService {
    private final String EMAIL_SNS_TOPIC = "arn:aws:sns:us-east-1:211125423661:emailNotification";
    private final String SMS_SNS_TOPIC = "arn:aws:sns:us-east-1:211125423661:smsNotification";
    private final SchedulerClient schedulerClient;

    public SchedulerService(SchedulerClient schedulerClient) {
        this.schedulerClient = schedulerClient;
    }


    public String scheduleNotification(String name, String cronExpression, Object notificationDTO, String targetArn, String roleArn, boolean deleteAfterCompletion,
                                       boolean useFlexibleTimeWindow) {
        Target target = Target.builder()
                .arn(targetArn)
                .roleArn(roleArn)
                .input(notificationDTO.toString())
                .build();

        CreateScheduleRequest scheduleRequest = CreateScheduleRequest.builder()
                .name(name)
                .scheduleExpression("cron(" + cronExpression + ")")
                .flexibleTimeWindow(FlexibleTimeWindow.builder()
                        .mode(useFlexibleTimeWindow ? FlexibleTimeWindowMode.FLEXIBLE : FlexibleTimeWindowMode.OFF)
                        .build())
                .target(target)
                .build();


        try {
            schedulerClient.createSchedule(scheduleRequest);
            return "Schedule created successfully: " + name;
        } catch (Exception e) {
            log.error("Error creating schedule: {}", e.getMessage());
            return "Error creating schedule: " + e.getMessage();
        }

    }




}
