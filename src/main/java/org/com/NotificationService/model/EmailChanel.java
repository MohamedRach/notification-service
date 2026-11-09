package org.com.NotificationService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailChanel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String emailIdentity;

    private String domain;

    private String emailAddress;

    @ManyToOne
    @JoinColumn(name = "id")
    private App app;


}
