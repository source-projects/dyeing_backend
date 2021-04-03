package com.main.glory.Dao.task;

import com.main.glory.model.task.TaskMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskMastDao extends JpaRepository<TaskMast,Long> {


    @Query("select x from TaskMast x where x.id=:id")
    TaskMast getTaskMastById(Long id);

    @Query("select x from TaskMast x")
    List<TaskMast> getAllTask();
}
