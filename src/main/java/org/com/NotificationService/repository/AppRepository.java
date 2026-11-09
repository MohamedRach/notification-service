package org.com.NotificationService.repository;

import org.com.NotificationService.model.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppRepository extends JpaRepository<App, String> {
    Optional<App> findByName(String appName);
}
