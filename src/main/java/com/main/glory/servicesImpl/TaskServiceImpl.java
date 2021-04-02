package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.DepartmentDao;
import com.main.glory.Dao.task.TaskDataDao;
import com.main.glory.Dao.task.TaskDataImageDao;
import com.main.glory.Dao.task.TaskImageDao;
import com.main.glory.Dao.task.TaskMastDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.admin.Department;
import com.main.glory.model.task.TaskData;
import com.main.glory.model.task.TaskMast;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("taskServiceImpl")
public class TaskServiceImpl {

    @Autowired
    UserServiceImpl userService;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    TaskMastDao taskMastDao;

    @Autowired
    TaskDataDao taskDataDao;

    @Autowired
    TaskImageDao taskImageDao;

    @Autowired
    TaskDataImageDao taskDataImageDao;


    public void saveTask(TaskMast record) throws Exception {


        //check the user and department is exit aur not

        Department departmentExist =departmentDao.getDepartmentById(record.getDepartmentId());
        UserData userDataExist = userService.getUserById(record.getId());

        if(departmentExist==null || userDataExist==null)
            throw new Exception("no department or user found");


        TaskMast taskMast = taskMastDao.save(record);

        List<TaskData> taskDataList = new ArrayList<>();
        //Long differenceInDays = record.getEndDate() - record.getStartDate();









    }
}
