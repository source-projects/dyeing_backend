package com.main.glory.model.task.response;

import com.main.glory.model.task.TaskData;
import com.main.glory.model.task.TaskMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@NoArgsConstructor
@Getter
@Setter
public class TaskResponse {

    TaskMast taskMast;
    List<TaskData> taskDataList;

    public TaskResponse(TaskMast taskMast, List<TaskData> taskDataList) {
        this.taskMast = taskMast;
        this.taskDataList = taskDataList;
    }
}
