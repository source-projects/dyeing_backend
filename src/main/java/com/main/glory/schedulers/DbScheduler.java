package com.main.glory.schedulers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.main.glory.Dao.database.DatabaseBackupDao;
import com.main.glory.model.database.DatabaseBackup;
import com.smattme.MysqlExportService;
import lombok.experimental.Helper;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Map;
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

    @Autowired
    DatabaseBackupDao databaseBackupDao;

    private Process process;

    @Scheduled(cron="0 0 0 * * ?")//at every night at 12am
    public void forDbBackup() throws IOException, SQLException, ClassNotFoundException, InterruptedException {

        File parent =new File("backup");

        if(!parent.exists())
            parent.mkdir();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");

        File backupFile = new File(parent+"/"+simpleDateFormat.format(new Date())+".sql");

        System.out.println("path:"+backupFile.getAbsolutePath());

        if(!backupFile.exists())
            backupFile.createNewFile();

        String cmd="mysqldump --column-statistics=0 --user="+user+" --password="+password+" --databases "+dbname+" -r " + backupFile;
        //System.out.println(cmd);


        try {

            //runtime = Runtime.getRuntime();
            process = Runtime.getRuntime().exec(cmd);
            int exitValue = process.waitFor();


            if(exitValue==0) {
                //run the cloudinary as well
                //backup on the cloud
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dvvqdgl3s",
                        "api_key", "841845242494588",
                        "api_secret", "E1owKNvkJZa131NBcDYEM6mdZSc"));
                Map uploadResult = cloudinary.uploader().upload(backupFile, ObjectUtils.asMap("resource_type","raw"));
                /*
                ObjectMapper objectMapper = new ObjectMapper();
                uploadResult.get("secure_url") get uploaded url
                uploadResult.get("public_id") get uploaded file name use while we are try to restore the file
                System.out.println(objectMapper.writeValueAsString(uploadResult));
                */

                DatabaseBackup databaseBackup = new DatabaseBackup();
                databaseBackup.setName(uploadResult.get("public_id").toString());
                databaseBackup.setUrl(uploadResult.get("secure_url").toString());

                databaseBackupDao.save(databaseBackup);


            }



        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
            //return false;

        }



    }
}
