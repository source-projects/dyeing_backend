package com.main.glory.Dao.task;

import com.main.glory.model.task.TaskImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskImageDao extends JpaRepository<TaskImage,Long> {
}
