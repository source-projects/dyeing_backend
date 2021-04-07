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

}
