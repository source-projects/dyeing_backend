package com.main.glory.model.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    Long controlId;
    Date date;
    Date inTime;
    Date outTime;
    Date createdDate;
    Long createdBy;
    Long updatedBy;
    Date updatedDate;

    @PrePersist
    public void onCreate()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
    @PreUpdate
    public void onUpdate()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }


}
