package com.main.glory.schedulers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.main.glory.Dao.database.DatabaseBackupDao;
import com.main.glory.model.database.DatabaseBackup;
import com.main.glory.servicesImpl.RestoreDbImpl;
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

    @Autowired
    RestoreDbImpl restoreDbService;

    private Process process;

    //pattern second ,minute, hour, day, month, weekday
    @Scheduled(cron="0 0 0 * * *")//at every night at 12am
    public void forDbBackup() throws IOException, SQLException, ClassNotFoundException, InterruptedException {

        //System.out.println("called db");
        restoreDbService.backupDb();

    }
}
