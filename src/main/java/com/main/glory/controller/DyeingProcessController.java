package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingProcess.TagDyeingProcess.TagDyeingProcessMast;
import com.main.glory.model.dyeingProcess.request.GetAllDyeingProcessList;
import com.main.glory.servicesImpl.DyeingProcessServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
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
public class DyeingProcessController extends ControllerConfig {


    ConstantFile constantFile;

    @Autowired
    LogServiceImpl logService;

    @Autowired
    HttpServletRequest request;

    @Value("${spring.application.debugAll}")
    Boolean debugAll = true;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @PostMapping("/dyeingProcess")
    public ResponseEntity<GeneralResponse<Boolean, Object>> addDyeingProcess(@RequestBody DyeingProcessMast data) {
        GeneralResponse<Boolean, Object> result;
        try {
            if (data == null) {
                result = new GeneralResponse<>(false, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, data);
            } else {
                dyeingProcessService.addDyeingProcess(data);
                result = new GeneralResponse<>(true, constantFile.DyeingProcess_Added, true, System.currentTimeMillis(), HttpStatus.OK, data);
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, data);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @PutMapping("/dyeingProcess")
    public ResponseEntity<GeneralResponse<Boolean, Object>> updateDyeingProcess(@RequestBody DyeingProcessMast data) {
        GeneralResponse<Boolean, Object> result;
        try {
            if (data == null) {
                result = new GeneralResponse<>(false, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, data);
            } else {
                dyeingProcessService.updateDyeingProcess(data);
                result = new GeneralResponse<>(true, constantFile.DyeingProcess_Updated, true, System.currentTimeMillis(), HttpStatus.OK, data);
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, data);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/all")
    public ResponseEntity<GeneralResponse<List<GetAllDyeingProcessList>, Object>> getAllDyeingProcess(@RequestHeader Map<String, String> headers) {
        GeneralResponse<List<GetAllDyeingProcessList>, Object> result = null;
        try {
            List<GetAllDyeingProcessList> list = dyeingProcessService.getAllDyeingProcess(headers.get("id"));
            if (!list.isEmpty()) {
                result = new GeneralResponse<>(list, constantFile.DyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(list, constantFile.DyeingProcess_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/{id}")
    public ResponseEntity<GeneralResponse<DyeingProcessMast, Object>> getDyeingProcessById(@PathVariable(name = "id") Long id) {
        GeneralResponse<DyeingProcessMast, Object> result = null;
        try {
            if (id == null)
                throw new Exception(constantFile.Null_Record_Passed);

            DyeingProcessMast data = dyeingProcessService.getDyeingProcessById(id);
            if (data != null) {
                result = new GeneralResponse<>(data, constantFile.DyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/dyeingProcess/isProcessExistWithName/{processName}/{id}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> isDyeingProcessExist(@PathVariable(name = "processName") String name, @PathVariable(name = "id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {

            if (name == null)
                throw new Exception(constantFile.Null_Record_Passed);

            Boolean data = dyeingProcessService.dyeingProcessExistWithName(name, id);
            if (data) {
                result = new GeneralResponse<>(false, constantFile.DyeingProcess_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(true, constantFile.DyeingProcess_Name_Exist, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @DeleteMapping("/dyeingProcess/{id}")
    public ResponseEntity<GeneralResponse<Boolean, Object>> deleteProcessById(@PathVariable(name = "id") Long id) {

        GeneralResponse<Boolean, Object> result;
        try {
            if (id == null)
                throw new Exception(constantFile.Null_Record_Passed);

            Boolean flag = dyeingProcessService.deleteByProcessId(id);
            if (flag) {
                result = new GeneralResponse<>(true, constantFile.DyeingProcess_Deleted, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else
                result = new GeneralResponse<>(null, constantFile.DyeingProcess_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }


    //tag dyeing process api's
    @PostMapping("/dyeingProcess/add/tagProcess")
    public ResponseEntity<GeneralResponse<Boolean, Object>> addTagDyeingProcess(@RequestBody TagDyeingProcessMast data) {
        GeneralResponse<Boolean, Object> result;
        try {
            if (data == null) {
                result = new GeneralResponse<>(false, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, data);
            } else {
                dyeingProcessService.addTagDyeingProcess(data);
                result = new GeneralResponse<>(true, constantFile.TagDyeingProcess_Added, true, System.currentTimeMillis(), HttpStatus.OK, data);
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, data);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @PutMapping("/dyeingProcess/update/tagProcess")
    public ResponseEntity<GeneralResponse<Boolean, Object>> updateTagDyeingProcess(@RequestBody TagDyeingProcessMast data) {
        GeneralResponse<Boolean, Object> result;
        try {
            if (data == null) {
                result = new GeneralResponse<>(false, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK, data);
            } else {
                dyeingProcessService.updateTagDyeingProcess(data);
                result = new GeneralResponse<>(true, constantFile.TagDyeingProcess_Updated, true, System.currentTimeMillis(), HttpStatus.OK, data);
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, data);
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/dyeingProcess/get/tagProcess")
    public ResponseEntity<GeneralResponse<TagDyeingProcessMast, Object>> getTagDyeingProcessById(@RequestParam(name = "id") Long id) {
        GeneralResponse<TagDyeingProcessMast, Object> result;
        try {
            if (id == null) {
                throw new Exception(ConstantFile.Null_Record_Passed);
                //result = new GeneralResponse<>(false, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                TagDyeingProcessMast tagDyeingProcessMast = dyeingProcessService.getTagDyeignProcessById(id);

                if (tagDyeingProcessMast != null) {
                    result = new GeneralResponse<>(tagDyeingProcessMast, constantFile.TagDyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                } else {
                    result = new GeneralResponse<>(tagDyeingProcessMast, constantFile.TagDyeingProcess_Not_Exist, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
                }
            }
            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/dyeingProcess/all/tagProcess")
    public ResponseEntity<GeneralResponse<List<TagDyeingProcessMast>, Object>> getAllTagDyeingProcess() {
        GeneralResponse<List<TagDyeingProcessMast>, Object> result;
        try {

            List<TagDyeingProcessMast> list = dyeingProcessService.getAllTagDyeignProcess();

            if (!list.isEmpty()) {
                result = new GeneralResponse<>(list, constantFile.TagDyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            } else {
                result = new GeneralResponse<>(list, constantFile.TagDyeingProcess_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            }

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @GetMapping("/dyeingProcess/exist/tagProcess")
    public ResponseEntity<GeneralResponse<Boolean, Object>> getTagProcessExitWithName(@RequestParam(name = "name") String name, @RequestParam("id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {

            TagDyeingProcessMast list = dyeingProcessService.getTagDyeingProcessExist(id, name);

            if (list != null) {
                result = new GeneralResponse<>(true, constantFile.TagDyeingProcess_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            } else {
                result = new GeneralResponse<>(false, constantFile.TagDyeingProcess_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());
            }

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

    @DeleteMapping("/dyeingProcess/delete/tagProcess")
    public ResponseEntity<GeneralResponse<Boolean, Object>> deleteTagDyeingProcessById(@RequestParam(name = "id") Long id) {
        GeneralResponse<Boolean, Object> result;
        try {

            dyeingProcessService.deleteTagDyeignProcessById(id);

            result = new GeneralResponse<>(true, constantFile.TagDyeingProcess_Deleted, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI() + "?" + request.getQueryString());

            logService.saveLog(result, request, debugAll);
        } catch (Exception e) {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI() + "?" + request.getQueryString());
            logService.saveLog(result, request, true);
        }
        return new ResponseEntity<>(result, HttpStatus.valueOf(result.getStatusCode()));

    }

}
