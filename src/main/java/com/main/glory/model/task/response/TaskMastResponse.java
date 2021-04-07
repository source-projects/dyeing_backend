package com.main.glory.model.task.response;

import com.main.glory.model.task.TaskData;
import com.main.glory.model.task.TaskMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskMastResponse extends TaskMast {
    String firstName;
    String lastName;
    String departmentName;
    String urlName;
    String formName;

    public TaskMastResponse(TaskMast taskMast) {
        super(taskMast);
    }
}
