package com.main.glory.Dao.task;

import com.main.glory.model.task.TaskDataImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TaskDataImageDao extends JpaRepository<TaskDataImage,Long> {

    @Modifying
    @Transactional
    @Query("delete from TaskDataImage x where x.controlId=:id")
    void deleteTaskDataImageByControlId(Long id);
}
