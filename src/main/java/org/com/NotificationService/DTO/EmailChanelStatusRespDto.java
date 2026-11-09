package org.com.NotificationService.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailChanelStatusRespDto {
    String domain;
    String verificationStatus; // e.g. "SUCCESS", "PENDING", "FAILED", "NOT_STARTED"
    String dkimStatus;         // e.g. "SUCCESS", "PENDING", "FAILED", "NOT_STARTED"
}
