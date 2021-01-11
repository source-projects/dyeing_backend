package com.main.glory.Scheduler;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ScheduledTasks {
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @Value("${spring.datasource.dbName}")
    private String dbName;

    @Value("${spring.datasource.username}")
    private String userName;

    @Value("${spring.datasource.password}")
    private String password;

    @Scheduled(fixedRate = 60000)//invoke every 60 second
    public void scheduleTaskWithFixedRate() throws IOException, InterruptedException {
/*
        //take a backup of db
        String executeCmd = "";
        executeCmd = "mysqldump -u"+userName+" -p"+password+" --add-drop-database -B "+dbName+" -r backup.sql";

        Process runtimeProcess =Runtime.getRuntime().exec(executeCmd);
        int processComplete = runtimeProcess.waitFor();
        if(processComplete == 0){
                System.out.println("Backup taken successfully");

            } else {

                System.out.println("Could not take mysql backup");

            }
*/
    }





    public void scheduleTaskWithFixedDelay() {}

    public void scheduleTaskWithInitialDelay() {}

    public void scheduleTaskWithCronExpression() {}
}
