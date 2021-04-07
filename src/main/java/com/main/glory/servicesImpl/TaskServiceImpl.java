package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.DepartmentDao;
import com.main.glory.Dao.task.*;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.admin.Department;
import com.main.glory.model.task.ReportType;
import com.main.glory.model.task.TaskData;
import com.main.glory.model.task.TaskMast;
import com.main.glory.model.task.TaskStatus;
import com.main.glory.model.task.request.TaskDetail;
import com.main.glory.model.task.request.TaskFilter;
import com.main.glory.model.task.response.TaskResponse;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import org.aspectj.internal.lang.annotation.ajcPrivileged;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("taskServiceImpl")
public class TaskServiceImpl {

    @Autowired
    ReportTypeDao reportTypeDao;

    @Autowired
    TaskDataImageDao taskDataImageDao;


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




    public void saveTask(TaskMast record) throws Exception {


        //check the user and department is exit aur not

        Department departmentExist =departmentDao.getDepartmentById(record.getDepartmentId());
        UserData userDataExist = userService.getUserById(record.getAssignUserId());

        if(departmentExist==null || userDataExist==null)
            throw new Exception("no department or user found");


        TaskMast taskMast = taskMastDao.save(record);

        //List<TaskData> taskDataList=new ArrayList<>();

        //create the task as per the days
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        switch(record.getTaskType())
        {
            case "Daily":
                Calendar cal = Calendar.getInstance();
                cal.setTime(record.getStartDate());
                Date date=cal.getTime();
                for(;date.getTime()<=record.getEndDate().getTime();)
                {
                    System.out.println(simpleDateFormat.format(date));
                    TaskData taskData =new TaskData(taskMast);
                    taskData.setTaskDate(date);
                    taskData.setTaskStatus("NotStarted");
                    taskDataDao.save(taskData);

                    cal.add(Calendar.DATE,1);
                    date=cal.getTime();
                }
                break;

            case "Monthly":
                cal = Calendar.getInstance();
                cal.setTime(record.getStartDate());
                date=cal.getTime();
                for(;date.getTime()<=record.getEndDate().getTime();)
                {
                    System.out.println("max day of month:"+simpleDateFormat.format(date));
                    if(record.getStartDate().getDate() <= cal.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                        TaskData taskData = new TaskData(taskMast);
                        taskData.setTaskDate(date);
                        taskData.setTaskStatus("NotStarted");
                        taskDataDao.save(taskData);

                    }
                    cal.add(Calendar.MONTH,1);

                    date=cal.getTime();
                    date.setDate(record.getStartDate().getDate());



                }
                break;

            case "Weekly":
                cal = Calendar.getInstance();
                cal.setTime(record.getStartDate());
                date=cal.getTime();
                for(;date.before(record.getEndDate());)
                {
                    System.out.println(simpleDateFormat.format(date));
                    TaskData taskData =new TaskData(taskMast);
                    taskData.setTaskDate(date);
                    taskData.setTaskStatus("NotStarted");
                    taskDataDao.save(taskData);

                    cal.add(Calendar.DATE,7);
                    date=cal.getTime();

                }
                break;
            case "Once":
                    TaskData taskData =new TaskData(taskMast);
                    taskData.setTaskDate(record.getStartDate());
                    taskData.setTaskStatus("NotStarted");
                    taskDataDao.save(taskData);
                break;
            default:
                throw new Exception("task type not found");

        }




    }

    public TaskResponse getTaskById(Long id) {
        TaskMast taskMast = taskMastDao.getTaskMastById(id);

        TaskResponse taskResponse =null;
        if(taskMast!=null) {

            List<TaskData> taskDataList = taskDataDao.getTaskDataByControlId(taskMast.getId());
             taskResponse = new TaskResponse(taskMast, taskDataList);

        }
        return taskResponse;
    }

    public List<TaskResponse> getAllTask() {
        List<TaskMast> taskMastList = taskMastDao.getAllTask();
        List<TaskResponse> taskResponseList = new ArrayList<>();
        for(TaskMast taskMast:taskMastList)
        {
            TaskResponse taskResponse = getTaskById(taskMast.getId());
            if(taskResponse==null)
                continue;
            taskResponseList.add(taskResponse);

        }
        return taskResponseList;


    }

    public boolean deleteTaskById(Long id) throws Exception {
        TaskMast taskMastExist = taskMastDao.getTaskMastById(id);
        if(taskMastExist==null)
            throw new Exception("no record found");


        List<TaskData> taskDataList = taskDataDao.getTaskDataByControlId(taskMastExist.getId());

        //check the any task is completed
        for(TaskData taskData:taskDataList)
        {
            if(taskData.getTaskStatus().equals(TaskStatus.Completed))
            {
                throw new Exception("remove the task which are completed");
            }
        }

        for(TaskData taskData:taskDataList)
        {
            taskDataDao.deleteTaskDataById(taskData.getId());
            //remove the taskData image as well if the condition is satisfied
            taskDataImageDao.deleteTaskDataImageByControlId(taskData.getId());

        }

        return true;

    }

    public List<TaskMast> getTaskByReportId(Long reportId) {
        return taskMastDao.getTaskByReportId(reportId);
    }

    public List<TaskDetail> getAllTaskByDateAndStatus(TaskFilter record) {
        List<TaskDetail> taskDetailList = null;
        if(record.getDate()==null && record.getStatus().isEmpty())
            taskDetailList =taskDataDao.getTaskDetail();
        else if(!record.getStatus().isEmpty() && record.getDate()!=null)
            taskDetailList =taskDataDao.getTaskDetailByDateAndStatus(record.getDate(),record.getStatus());
        else if(record.getDate()!=null)
            taskDetailList =taskDataDao.getTaskDetailByDate(record.getDate());
        else
            taskDetailList =  taskDataDao.getTaskDetailByStatus( record.getStatus());

        taskDetailList = taskDetailResponse(taskDetailList);
        return taskDetailList;

    }

    private List<TaskDetail> taskDetailResponse(List<TaskDetail> taskDetailList) {
        taskDetailList.forEach(e->{

            TaskMast taskMast = taskMastDao.getTaskMastById(e.getControlId());
            UserData userData = userService.getUserById(e.getAssignUserId());
            Department department = departmentDao.getDepartmentById(userData.getDepartmentId());
            ReportType reportType =reportTypeDao.getReportTypeById(taskMast.getReportId());

            e.setFormName(userData.getFirstName());
            e.setLastName(userData.getLastName());
            e.setDepartmentName(department.getName());
            if(reportType!=null)
            e.setFormName(reportType.getFormName());

        });
        return taskDetailList;
    }

    public List<TaskDetail> getAllTaskDetail(String getBy, Long id, String headerId) throws Exception {
        List<TaskDetail> taskDetailList = null;

        UserData userData = userService.getUserById(Long.parseLong(headerId));

        UserPermission userPermission = userData.getUserPermissionData();
        Permissions permissions = new Permissions(userPermission.getTt().intValue());

        if(id==null)
        {
            taskDetailList = taskDataDao.getTaskDetail();
        }
        else if(getBy.equals("assignAndCreated"))
        {
            taskDetailList = taskDataDao.getTaskDetailByCreatedByAssignById(id,id);
        }
        else if (getBy.equals("assign"))
        {
            taskDetailList = taskDataDao.getTaskDetailAssignBy(id);
        }
        taskDetailList =taskDetailResponse(taskDetailList);
        return taskDetailList;
    }

    public List<TaskDetail> getAllApprovedOrNot(Long id, Boolean approvedFlag) {
        List<TaskDetail> taskDetailList = null;


        if(id==null && approvedFlag==null) {
            taskDetailList = taskDataDao.getTaskDetail();
        }
        else if(id==null && approvedFlag!=null)
        {
            taskDetailList = taskDataDao.getTaskDetailByApproved(approvedFlag);
        }
        else if(id!=null && approvedFlag==null)
        {
            taskDetailList = taskDataDao.getTaskDetailAssignBy(id);
        }
        else
        {
            //id is not null and flag is not null
            taskDetailList = taskDataDao.getTaskDetailByApprovedAndAssignId(id,approvedFlag);
        }

        taskDetailList =taskDetailResponse(taskDetailList);
        return taskDetailList;
    }

    public void updateTaskByIdAndFlag(Long id, Boolean approvedFlag) throws Exception {
        TaskData taskDataExist = taskDataDao.getTaskDetailById(id);
        if(taskDataExist==null) {
            throw new Exception("no record found");
        }

        taskDataDao.updateTaskWithIdAndFlag(id,approvedFlag);

    }
}