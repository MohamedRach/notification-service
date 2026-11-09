package org.com.NotificationService.service;

import org.com.NotificationService.DTO.AppCreateDto;
import org.com.NotificationService.exception.AppAlreadyExistsException;
import org.com.NotificationService.exception.AppNotFoundException;
import org.com.NotificationService.model.App;
import org.com.NotificationService.repository.AppRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AppService {

    private final AppRepository appRepository;

    public AppService(AppRepository appRepository) {
        this.appRepository = appRepository;
    }

    public App findById(String id) {
        if (appRepository.findById(id).isEmpty()) {
            throw new AppNotFoundException("App with id " + id + " does not exist.");
        }
        return appRepository.findById(id).orElse(null);
    }

    public App findByName(String appName) {
        if (appRepository.findByName(appName).isEmpty()) {
            throw new AppNotFoundException("App with name " + appName + " does not exist.");
        }
        return appRepository.findByName(appName).orElse(null);
    }

    public App createApp(AppCreateDto app) {
        if (appRepository.findByName(app.getName()).isPresent()) {
            throw new AppAlreadyExistsException("App with name " + app.getName() + " already exists.");
        }
        UUID id = UUID.randomUUID();
        App newApp = new App();
        newApp.setName(app.getName());
        newApp.setId(id.toString());
        return appRepository.save(newApp);
    }
}
