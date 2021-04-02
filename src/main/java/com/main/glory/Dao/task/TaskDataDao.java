package com.main.glory.Dao.task;

import com.main.glory.model.task.TaskData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskDataDao extends JpaRepository<TaskData,Long> {
}
