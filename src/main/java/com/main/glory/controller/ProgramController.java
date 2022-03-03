package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.servicesImpl.ProgramServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProgramController extends ControllerConfig {

    @Autowired
    private ProgramServiceImpl programServiceImpl;

   /* @PostMapping(value="/program")
    public GeneralResponse<Boolean> saveProgram(@RequestBody AddProgramWithProgramRecord program) throws Exception {
        if(program==null)
        {
            return                                                                                                                                                                    new GeneralResponse<Boolean>(false, "Program id is null", true, System.currentTimeMillis(), HttpStatus.OK);
        }

        try {
                programServiceImpl.saveProgram(program);
                return new GeneralResponse<Boolean>(true, "Program added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch(Exception e)
        {
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @GetMapping(value="/program/all/{getBy}/{id}")
    public GeneralResponse<List<GetAllProgram>> getProgramList(@PathVariable(value = "id") Long id,@PathVariable( value = "getBy") String getBy) throws Exception {
        try {
            switch (getBy) {
                case "own":
                    var data = programServiceImpl.getAllProgram(getBy, id);
                    if (!data.isEmpty())
                        return new GeneralResponse<>(data, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    else
                        return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

                case "group":
                    var data1 = programServiceImpl.getAllProgram(getBy, id);
                    if (!data1.isEmpty())
                        return new GeneralResponse<>(data1, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    else
                        return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

                case "all":
                    var data2 = programServiceImpl.getAllProgram(null, null);
                    if (!data2.isEmpty())
                        return new GeneralResponse<>(data2, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                    else
                        return new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.OK);

                default:
                    return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }

    }

    //getStock Quality wise Stock and it's qty
    @GetMapping(value="/program/StockQuality/{id}")
    public GeneralResponse<List<StockQualityWise>> getStockListByQualityId(@PathVariable(value = "id") Long id) throws Exception {

        try {
            if (id == null) {
                throw new Exception("Id can't be null");
            }
            else
            {
                List<StockQualityWise> stockQualityWiseList =  programServiceImpl.getAllStockByQuality(id);
                return new GeneralResponse<>(stockQualityWiseList, "Data fetched Succesully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        }
        catch (Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);

        }

       // return programServiceImpl.getAllProgram();
    }

    @GetMapping(value="/program/{id}")
    public GeneralResponse<Program> getProgramDetailsById(@PathVariable(value = "id") Long id) throws Exception {
        if(id!=null)
        {
            Program programObject=programServiceImpl.getProgramById(id);
            if(programObject!=null)
            {
                return new GeneralResponse<>(programObject,"Data Get successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        }
        return new GeneralResponse<>(null, "Data not found", false, System.currentTimeMillis(), HttpStatus.OK);
    }

    //get partshade no with respected shadeId
    @GetMapping(value="/program/PartyShadeDetailPartyWise")
    public GeneralResponse<List<ShadeIdwithPartyShadeNo>> getShadeDetail() throws Exception {
        try {
            List<ShadeIdwithPartyShadeNo> listData = programServiceImpl.getShadeDetail();
            if (listData != null) {
                return new GeneralResponse<>(listData, "Data Get successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new GeneralResponse<>(null, "Data not found", false, System.currentTimeMillis(), HttpStatus.OK);
        }catch (Exception e)
        {
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }


    @PutMapping(value="/program")
    public GeneralResponse<Boolean> updateProgram(@RequestBody UpdateProgramWithProgramRecord program) throws Exception
    {
        try
        {
            programServiceImpl.updateProgramByID(program);
            return new GeneralResponse<Boolean>(true, "Data updated sucessfully !", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch(Exception e) {
            e.printStackTrace();
            return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
    }

    @DeleteMapping(value="/program/{id}")
    public GeneralResponse<Boolean> deleteProgramDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        if(id!=null)
        {
            boolean flag=programServiceImpl.deleteProgramById(id);
            if(flag)
            {
                return new GeneralResponse<>(true,"Deleted Successfully",true,System.currentTimeMillis(),HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(false,"Data not found",false,System.currentTimeMillis(),HttpStatus.OK);
            }
        }
        return new GeneralResponse<>(false,"Data not deleted",false,System.currentTimeMillis(),HttpStatus.OK);
    }

    @GetMapping(value="/program/BatchData/{id}")
    public GeneralResponse<List<GetAllBatchResponse>> getBatchDetailByQualityId(@PathVariable(value = "id") Long qualityEntryId) throws Exception {
       try
       {
           if(qualityEntryId!=null)
           {
               List<GetAllBatchResponse> flag=programServiceImpl.getAllBatchByQuality(qualityEntryId);
               if(flag!=null)
               {
                   return new GeneralResponse<>(flag,"Batch Fetched Successfully",true,System.currentTimeMillis(),HttpStatus.OK);
               }
               else
               {
                   return new GeneralResponse<>(null,"Data not found",false,System.currentTimeMillis(),HttpStatus.OK);
               }
           }


       }
       catch (Exception e)
       {
           return new GeneralResponse<>(null,e.getMessage(),false,System.currentTimeMillis(),HttpStatus.OK);
       }
        return null;
    }
*/


}
