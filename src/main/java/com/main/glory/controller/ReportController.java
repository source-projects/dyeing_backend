package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.report.ReportMast;
import com.main.glory.model.shade.requestmodals.AddShadeMast;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.ReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReportController extends ControllerConfig {

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll;

    @Autowired
    ReportServiceImpl reportService;

    @PostMapping("/report/add")
    public ResponseEntity<GeneralResponse<Boolean, Object>> addReportMastData(@RequestBody ReportMast reportMast) {
        GeneralResponse<Boolean, Object> result;
        try {
            if (reportMast == null) {
                result = new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, reportMast);
            } else {
                reportService.addReportMast(reportMast);
                result = new GeneralResponse<>(true, ConstantFile.Report_Type_Added, true, System.currentTimeMillis(), HttpStatus.OK, reportMast);
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, reportMast);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @PutMapping("/report/update")
    public ResponseEntity<GeneralResponse<Boolean, Object>> updateReportMastData(@RequestBody ReportMast reportMast) {
        GeneralResponse<Boolean, Object> result;
        try {
            if (reportMast == null) {
                result = new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, reportMast);
            } else {
                reportService.updateReportMast(reportMast);
                result = new GeneralResponse<>(true, ConstantFile.Report_Type_Updated, true, System.currentTimeMillis(), HttpStatus.OK, reportMast);
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, reportMast);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/report/all")
    public ResponseEntity<GeneralResponse<List<ReportMast>, Object>> getAllReportMast() {
        GeneralResponse<List<ReportMast>, Object> result;
        try {

            List<ReportMast> list = reportService.getAllReportMast();
            if (!list.isEmpty())
                result = new GeneralResponse<>(list, ConstantFile.Report_Type_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            else
                result = new GeneralResponse<>(list, ConstantFile.Report_Type_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/report/all/type")
    public ResponseEntity<GeneralResponse<List<String>, Object>> getAllReportMastType() {
        GeneralResponse<List<String>, Object> result;
        try {

            List<String> list = reportService.getAllReportMastType();
            if (!list.isEmpty())
                result = new GeneralResponse<>(list, ConstantFile.Report_Type_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            else
                result = new GeneralResponse<>(list, ConstantFile.Report_Type_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/report/all/byType")
    public ResponseEntity<GeneralResponse<List<ReportMast>, Object>> getAllReportMastByType(@RequestParam("name") String type) {
        GeneralResponse<List<ReportMast>, Object> result;
        try {

            List<ReportMast> list = reportService.getAllReportMastByType(type);
            if (!list.isEmpty())
                result = new GeneralResponse<>(list, ConstantFile.Report_Type_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            else
                result = new GeneralResponse<>(list, ConstantFile.Report_Type_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/report")
    public ResponseEntity<GeneralResponse<ReportMast, Object>> getReportMastById(@RequestParam(name = "id") Long id) {
        GeneralResponse<ReportMast, Object> result;
        try {

            if (id != null) {
                ReportMast list = reportService.getReportMastById(id);
                if (list != null)
                    result = new GeneralResponse<>(list, ConstantFile.Report_Type_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "," + request.getQueryString());
                else
                    result = new GeneralResponse<>(list, ConstantFile.Report_Type_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "," + request.getQueryString());
            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "," + request.getQueryString());
            }

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "," + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @DeleteMapping("/report")
    public ResponseEntity<GeneralResponse<Boolean, Object>> deleteReportMastById(@RequestParam(name = "id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {

            if (id != null) {
                reportService.deleteReportMastById(id);

                result = new GeneralResponse<>(true, ConstantFile.Report_Type_Deleted, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "," + request.getQueryString());

            } else {
                result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "," + request.getQueryString());
            }

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "," + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }


    //ADD DEFAULT REPORT API WITH URL
    @GetMapping("/report/addDefaultApi")
    public ResponseEntity<GeneralResponse<Boolean, Object>> addAllDefaultReportMastWithUrl() {
        GeneralResponse<Boolean, Object> result;
        try {

            reportService.addAllDefaultReportMastWithUrl();

            result = new GeneralResponse<>(true, ConstantFile.Report_Type_Added, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }
}
