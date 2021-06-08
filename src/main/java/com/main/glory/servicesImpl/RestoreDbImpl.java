package com.main.glory.servicesImpl;

import com.cloudinary.Cloudinary;

import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.main.glory.Dao.*;
import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.Dao.batch.BatchDataDao;
import com.main.glory.Dao.color.ColorBoxDao;
import com.main.glory.Dao.color.ColorDataDao;
import com.main.glory.Dao.color.ColorMastDao;
import com.main.glory.Dao.database.DatabaseBackupDao;
import com.main.glory.Dao.designation.DesignationDao;
import com.main.glory.Dao.dispatch.DispatchDataDao;
import com.main.glory.Dao.dispatch.DispatchMastDao;
import com.main.glory.Dao.dyeingProcess.DyeingChemicalDataDao;
import com.main.glory.Dao.dyeingProcess.DyeingProcessDataDao;
import com.main.glory.Dao.dyeingProcess.DyeingProcessMastDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipItemDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipMastDao;
import com.main.glory.Dao.paymentTerm.AdvancePaymentDao;
import com.main.glory.Dao.paymentTerm.PaymentDataDao;
import com.main.glory.Dao.paymentTerm.PaymentMastDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.Dao.user.UserPermissionDao;
import com.main.glory.model.database.DatabaseBackup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service("restoreDbServiceImp")
public class RestoreDbImpl {

    @Autowired
    PartyDao partyDao;

    @Autowired
    QualityDao qualityDao;

    @Autowired
    ProgramDao programDao;

    @Autowired
    QualityProcessMastDao qualityProcessMastDao;

    @Autowired
    UserDao userDao;

    @Autowired
    UserPermissionDao userPermissionDao;

    @Autowired
    ShadeMastDao shadeMastDao;

    @Autowired
    ShadeDataDao shadeDataDao;

    @Autowired
    JetMastDao jetMastDao;

    @Autowired
    JetDataDao jetDataDao;

    @Autowired
    ProductionPlanDao productionPlanDao;

    @Autowired
    DispatchMastDao dispatchMastDao;

    @Autowired
    DispatchDataDao dispatchDataDao;

    @Autowired
    AdvancePaymentDao advancePaymentDao;

    @Autowired
    PaymentMastDao paymentMastDao;

    @Autowired
    PaymentDataDao paymentDataDao;

    @Autowired
    SupplierDao supplierDao;

    @Autowired
    SupplierRateDao supplierRateDao;

    @Autowired
    DyeingSlipMastDao dyeingSlipMastDao;

    @Autowired
    DyeingSlipDataDao dyeingSlipDataDao;

    @Autowired
    DyeingSlipItemDataDao dyeingSlipItemDataDao;

    @Autowired
    DyeingProcessMastDao dyeingProcessMastDao;

    @Autowired
    DyeingProcessDataDao dyeingProcessDataDao;

    @Autowired
    DyeingChemicalDataDao dyeingChemicalDataDao;

    @Autowired
    DesignationDao designationDao;

    @Autowired
    StockMastDao stockMastDao;

    @Autowired
    BatchDataDao batchDataDao;

    @Autowired
    ColorMastDao colorMastDao;

    @Autowired
    ColorDataDao colorDataDao;

    @Autowired
    ColorBoxDao colorBoxDao;


    @Autowired
    DatabaseBackupDao databaseBackupDao;
   // private final Cloudinary cloudinary = Singleton.getCloudinary();




    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.dbname}")
    private String dbname;

    private Runtime runtime;
    private Process process;

    public Boolean restoreDb(String date) throws Exception {

    //restore db

            File parent =new File("backup");

            if(!parent.exists())
                parent.mkdir();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<DatabaseBackup> databaseBackupList = databaseBackupDao.getDatabaseByDate(simpleDateFormat.parse(date));


            if(databaseBackupList.isEmpty())
                throw new Exception("no backup found");



            File backupFile = new File(parent+"/"+"demo.sql");
            backupFile.createNewFile();


            URL url = new URL(databaseBackupList.get(databaseBackupList.size()-1).getUrl());
            BufferedReader read = new BufferedReader(new InputStreamReader(url.openStream()));
            String i;
            while ((i = read.readLine()) != null) {
                i+="\n";
                Files.write(Paths.get(String.valueOf(backupFile)), i.getBytes(), StandardOpenOption.APPEND);
            }
                //System.out.println(i);
            read.close();


            if(!backupFile.exists())
                throw new Exception("no file found");


            //String command = "mysql --user="+user+" --password="+password+"  "+dbname+" < " + backupFile;
            //String command = "mysql -u"+user+" -p < " + backupFile;
            //String command="mysqldump --user="+user+" --password="+password+" --databases "+dbname+" -r " + backupFile;

            String[] command = new String[]{"mysql", "--user=" + user, "--password=" + password, "-e", "source " + backupFile};


            process = Runtime.getRuntime().exec(command);
            int exitValue = process.waitFor();

            //System.out.println("exit value: " + exitValue);
            if (exitValue == 0) {

                return true;
            }
            else
                return false;



    }

    public Boolean backupDb() throws IOException {
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
            //System.out.println("value:"+exitValue);

            if(exitValue==0) {
                //run the cloudinary as well
                //backup on the cloud
                Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                        "cloud_name", "dvvqdgl3s",
                        "api_key", "841845242494588",
                        "api_secret", "E1owKNvkJZa131NBcDYEM6mdZSc"));

                Map uploadResult = cloudinary.uploader().upload(backupFile, ObjectUtils.asMap("resource_type","raw"));
                //thread sleep because api is taking time for upload
                //Thread.sleep(50000);

                ObjectMapper objectMapper = new ObjectMapper();
                //uploadResult.get("secure_url"); //get uploaded url
                //uploadResult.get("public_id"); //get uploaded file name use while we are try to restore the file
                System.out.println(objectMapper.writeValueAsString(uploadResult));

                DatabaseBackup databaseBackup = new DatabaseBackup();
                databaseBackup.setName(uploadResult.get("public_id").toString());
                databaseBackup.setUrl(uploadResult.get("secure_url").toString());

                databaseBackupDao.save(databaseBackup);


            }else {

                System.out.println("exit value: " + exitValue);
                BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = "";
                while ((line = buf.readLine()) != null) {
                    System.out.println("exec response: " + line);
                }
            }


            //remove the file using cmd which are 7 days old
            File[] files = new File(String.valueOf(parent)).listFiles();

            Date currentDate = new Date(System.currentTimeMillis());
            for (File file : files) {

                if (file.isFile()) {
                    long time = file.lastModified();
                    //Date fileDate = simpleDateFormat.parse(new Date(file.getName()).toString());
                    long diff = currentDate.getTime() - time;

                    if(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS)>7)
                    {
                        file.delete();
                    }

                    System.out.println(TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS));
                    System.out.println(file.getName());
                }
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);

        }
        return false;


    }

    public Boolean clearDb() {

        //clear by all the table payment type
        //partyDao.trucateRecord();
       /* partyDao.dropCommand(dbname);*/



        return true;
    }
}
