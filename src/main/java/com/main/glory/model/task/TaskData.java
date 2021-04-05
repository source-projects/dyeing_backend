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
    @Column(columnDefinition = "varchar(255) default 'NotStarted'")
    String taskStatus;
    Boolean isCompleted;
    Boolean approved;
    Date taskDate;
    Date taskCompletedDate;
    Date createdDate;
    String reportUrl;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<TaskDataImage> taskDataImageList;

    public TaskData(TaskMast taskMast) {
        this.controlId = taskMast.getId();
        this.assignUserId =taskMast.assignUserId;
        this.isCompleted=false;
        this.approved=false;

    }

    public TaskData(TaskData taskData) {
        this.id=taskData.getId();
        this.controlId = taskData.getControlId();
        this.assignUserId = taskData.getAssignUserId();
        this.remark=taskData.getRemark();
        this.taskStatus=taskData.getTaskStatus();
        this.isCompleted = taskData.getIsCompleted();
        this.approved=taskData.getApproved();
        this.taskDate = taskData.getTaskDate();
        this.taskCompletedDate = taskData.getTaskCompletedDate();
        this.createdDate = taskData.getCreatedDate();
        this.reportUrl = taskData.getReportUrl();
    }

    @PrePersist
    public void onCreate()
    {
        this.createdDate=new Date(System.currentTimeMillis());
    }
}
