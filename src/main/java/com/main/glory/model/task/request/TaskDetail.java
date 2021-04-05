package com.main.glory.model.task.request;

import com.main.glory.model.task.TaskData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDetail extends TaskData {

    String detail;
    String taskPriority;
    String taskType;
}
