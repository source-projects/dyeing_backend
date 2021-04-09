package com.main.glory.model.task.request;

import com.main.glory.model.admin.Department;
import com.main.glory.model.task.ReportType;
import com.main.glory.model.task.TaskData;
import com.main.glory.model.task.TaskDataImage;
import com.main.glory.model.task.TaskMast;
import com.main.glory.model.user.UserData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDetail extends TaskData {

    String detail;
    String taskPriority;
    String taskType;
    String firstName;
    String lastName;
    String departmentName;
    String formName;
    String taskName;
    Long completedDays;
    Boolean assignBySameUser;



    public TaskDetail(TaskData taskData, String detail, String taskPriority, String taskType) {
        super(taskData);
        this.detail = detail;
        this.taskPriority = taskPriority;
        this.taskType = taskType;
    }

    public TaskDetail(TaskData taskData,TaskMast taskMast) {
        super(taskData);
        this.detail = taskMast.getDetail();
        this.taskPriority = taskMast.getTaskPriority();
        this.taskType = taskMast.getTaskType();
    }

    public TaskDetail(TaskDetail taskData) {
        this.setId(taskData.getId());
        this.setControlId(taskData.getControlId());
        this.setAssignUserId(taskData.getAssignUserId());
        this.setRemark(taskData.getRemark());
        this.setTaskStatus(taskData.getTaskStatus());
        //this.isCompleted = taskData.getIsCompleted();
        this.setApproved(taskData.getApproved());
        this.setTaskDate(taskData.getTaskDate());
        this.setTaskCompletedDate(taskData.getTaskCompletedDate());
        this.setCreatedDate(taskData.getCreatedDate());
        this.setReportUrl(taskData.getReportUrl());
    }
}
