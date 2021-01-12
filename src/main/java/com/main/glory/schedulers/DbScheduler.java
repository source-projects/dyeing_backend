package com.main.glory.schedulers;

import com.smattme.MysqlExportService;
import lombok.experimental.Helper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

@Service
@EnableScheduling
public class DbScheduler {

    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.dbname}")
    private String dbname;

    @Scheduled(cron="0 0 0 * * ?")//at every night at 12am
    public void forDbBackup() throws IOException, SQLException, ClassNotFoundException, InterruptedException {
        File parent =new File("backup");

        if(!parent.exists())
            parent.mkdir();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        File backupFile = new File(parent+"/"+simpleDateFormat.format(new Date())+".sql");


        if(!backupFile.exists())
            backupFile.createNewFile();

        String cmd="mysqldump --user="+user+" --password="+password+" --databases "+dbname+" -r " + backupFile;


        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(cmd);
            int exitValue = process.waitFor();
            /*System.out.println("exit value: " + exitValue);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println("exec response: " + line);
            }*/
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("done exporting");



    }
}
