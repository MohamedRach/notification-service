package org.com.NotificationService.controller;

import org.com.NotificationService.DTO.AppCreateDto;
import org.com.NotificationService.model.App;
import org.com.NotificationService.service.AppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AppController {

    private AppService appService;

    public AppController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping("/api/v1/app/{appId}")
    public ResponseEntity<App> getAppById(@PathVariable String appId) {
        return ResponseEntity.ok(appService.findById(appId));
    }

    @GetMapping("/api/v1/app/name/{appName}")
    public ResponseEntity<App> getAppByName(@PathVariable String appName) {
        return ResponseEntity.ok(appService.findByName(appName));
    }

    @PostMapping("/api/v1/app")
    public ResponseEntity<App> createApp(@RequestBody AppCreateDto app) {
        return ResponseEntity.ok(appService.createApp(app));
    }
}
