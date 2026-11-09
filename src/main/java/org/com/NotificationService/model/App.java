package org.com.NotificationService.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class App {
    @Id
    private String id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    List<EmailChanel> emailChanels;

}
