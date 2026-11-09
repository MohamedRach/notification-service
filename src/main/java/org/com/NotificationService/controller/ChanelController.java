package org.com.NotificationService.controller;

import org.com.NotificationService.DTO.EmailChanelCreateDto;
import org.com.NotificationService.DTO.EmailChanelCreateRespDto;
import org.com.NotificationService.DTO.EmailChanelStatusRespDto;
import org.com.NotificationService.service.EmailChanelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChanelController {

    EmailChanelService emailChanelService;

    public ChanelController(EmailChanelService emailChanelService) {
        this.emailChanelService = emailChanelService;
    }

    @PostMapping("/api/v1/chanel/email")
    public ResponseEntity<EmailChanelCreateRespDto> createEmailChanel(@RequestBody EmailChanelCreateDto emailChanelCreateDto) {
        EmailChanelCreateRespDto response = emailChanelService.createEmailChanel(emailChanelCreateDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/v1/chanel/email/{domain}/status")
    public ResponseEntity<EmailChanelStatusRespDto> getEmailChanelStatus(@PathVariable String domain) {
        EmailChanelStatusRespDto response = emailChanelService.getEmailChanelStatus(domain);
        return ResponseEntity.ok(response);
    }
}
