package com.main.glory.model.database;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class DatabaseBackup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String name;//public id which is coming after storing record
    String url;
    Date createdDate;

    @PrePersist
    public void create()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
}
