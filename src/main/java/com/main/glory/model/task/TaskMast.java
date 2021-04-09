package com.main.glory.model.task;

import com.main.glory.model.StockDataBatchData.BatchData;
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
public class TaskMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String taskName;
    String detail;
    String taskPriority;
    String taskType;
    Date startDate;
    Date endDate;
    Long completedDays;
    String remark;
    Long reportId;
    Boolean notify;
    Boolean isCompleted;
    Long departmentId;
    Long assignUserId;
    Long createdBy;
    Date createdDate;
    Date updatedDate;
    Date completedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<TaskImage> taskImageList;

    public TaskMast(TaskMast taskMast) {
        this.id=taskMast.getId();
        this.taskName = taskMast.getTaskName();
        this.detail = taskMast.getDetail();
        this.taskPriority = taskMast.getTaskPriority();
        this.taskType = taskMast.getTaskType();
        this.startDate = taskMast.getStartDate();
        this.endDate=taskMast.getEndDate();
        this.completedDays = taskMast.getCompletedDays();
        this.remark = taskMast.getRemark();
        this.reportId =taskMast.getReportId();
        this.notify = taskMast.getNotify();
        this.isCompleted=taskMast.getIsCompleted();
        this.departmentId=taskMast.getDepartmentId();
        this.assignUserId=taskMast.getAssignUserId();
        this. createdBy=taskMast.getCreatedBy();
        this. createdDate=taskMast.getCreatedDate();
        this. updatedDate=taskMast.getUpdatedDate();
        this. completedDate=taskMast.getCompletedDate();
        this.taskImageList = taskMast.getTaskImageList();
    }


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
