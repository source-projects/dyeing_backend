package com.main.glory.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class TaskData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    Long assignUserId;
    String remark;
    Boolean isCompleted;
    Boolean approved;
    Date taskDate;
    Date taskCompletedDate;
    Date createdDate;
    String reportUrl;

    @PrePersist
    public void onCreate()
    {
        this.createdDate=new Date(System.currentTimeMillis());
    }
}
