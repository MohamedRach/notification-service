package org.com.NotificationService.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailChanelCreateDto {

    private String name;

    private String emailIdentity;

    private String domain;

    private String emailAddress;
}
