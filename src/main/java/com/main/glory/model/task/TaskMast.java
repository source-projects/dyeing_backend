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
