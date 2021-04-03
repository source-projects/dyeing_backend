package com.main.glory.Dao.task;

import com.main.glory.model.task.TaskData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TaskDataDao extends JpaRepository<TaskData,Long> {

    @Query("select t from TaskData t where t.controlId=:id")
    List<TaskData> getTaskDataByControlId(Long id);

    @Modifying
    @Transactional
    @Query("delete from TaskData x where x.id=:id")
    void deleteTaskDataById(Long id);
}
