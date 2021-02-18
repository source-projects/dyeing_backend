package com.main.glory.servicesImpl;

import com.main.glory.Dao.*;
import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.Dao.admin.ApproveByDao;
import com.main.glory.Dao.batch.BatchDataDao;
import com.main.glory.Dao.color.ColorBoxDao;
import com.main.glory.Dao.color.ColorDataDao;
import com.main.glory.Dao.color.ColorMastDao;
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
import com.main.glory.Dao.qualityProcess.ChemicalDao;
import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.Dao.user.UserPermissionDao;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.supplier.SupplierRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    ApproveByDao approveByDao;

    @Autowired
    APCDao apcDao;



    @Value("${spring.datasource.username}")
    private String user;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.dbname}")
    private String dbname;

    private Runtime runtime;
    private Process process;
    public Boolean restoreDb(String name) throws IOException, InterruptedException {

    //restore db
        try {
            File parent =new File("backup");

            if(!parent.exists())
                parent.mkdir();

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            File backupFile = new File(parent+"/"+name);


            if(!backupFile.exists())
                throw new Exception("no file found");


            //String command = "mysql --user="+user+" --password="+password+"  "+dbname+" < " + backupFile;
            //String command = "mysql -u"+user+" -p < " + backupFile;
            //String command="mysqldump --user="+user+" --password="+password+" --databases "+dbname+" -r " + backupFile;

            String[] command = new String[]{"mysql", "--user=" + user, "--password=" + password, "-e", "source " + backupFile};


            process = Runtime.getRuntime().exec(command);
            int exitValue = process.waitFor();
            //System.out.println("exit value: " + exitValue);
            if (exitValue == 0)
                return true;
            else
                return false;

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    public Boolean backupDb() throws IOException {
        File parent =new File("backup");

        if(!parent.exists())
            parent.mkdir();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        File backupFile = new File(parent+"/"+simpleDateFormat.format(new Date())+".sql");


        if(!backupFile.exists())
            backupFile.createNewFile();
        String cmd="mysqldump --user="+user+" --password="+password+" --databases "+dbname+" -r " + backupFile;


        try {

            //runtime = Runtime.getRuntime();
            process = Runtime.getRuntime().exec(cmd);
            int exitValue = process.waitFor();


            if(exitValue==0)
                return true;
            else return false;


            /*System.out.println("exit value: " + exitValue);
            BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = "";
            while ((line = buf.readLine()) != null) {
                System.out.println("exec response: " + line);
            }*/


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        }

        return false;

    }

    public Boolean clearDb() {

        //clear by all the table payment type
        //partyDao.trucateRecord();
       // partyDao.dropCommand(dbname);

        return true;
    }
}
