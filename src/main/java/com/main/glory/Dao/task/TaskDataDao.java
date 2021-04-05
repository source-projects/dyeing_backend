package com.main.glory.Dao.task;

import com.main.glory.model.task.TaskData;
import com.main.glory.model.task.request.TaskDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

public interface TaskDataDao extends JpaRepository<TaskData,Long> {

    @Query("select t from TaskData t where t.controlId=:id")
    List<TaskData> getTaskDataByControlId(Long id);

    @Modifying
    @Transactional
    @Query("delete from TaskData x where x.id=:id")
    void deleteTaskDataById(Long id);


    @Query("select new com.main.glory.model.task.request.TaskDetail(t,(select tt from TaskMast tt where tt.id=t.controlId) as taskMast) from TaskData t where (:status IS NULL OR t.taskStatus=:status) AND (:date IS NULL OR t.taskDate=:date)")
    List<TaskDetail> getTaskDetailByDateAndStatus(Date date, String status);
}
