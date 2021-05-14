package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.*;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.ShadeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShadeController extends ControllerConfig {

	@Autowired
	ShadeServiceImpl shadeService;

	@Autowired
	LogServiceImpl logService;

	@Autowired
	HttpServletRequest request;

	@Value("${spring.application.debugAll}")
	Boolean debugAll=true;

	@PostMapping("/shade")
	public ResponseEntity<GeneralResponse<Boolean,Object>> addShadeData(@RequestBody AddShadeMast shadeMast ,@RequestHeader Map<String, String> headers){
		GeneralResponse<Boolean,Object> result;
		try {
			if(shadeMast == null){
				result =  new GeneralResponse<>(false, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,shadeMast);
			}else {
				shadeService.saveShade(shadeMast,headers.get("id"));
				result = new GeneralResponse<>(true, ConstantFile.Shade_Added, true, System.currentTimeMillis(), HttpStatus.OK,shadeMast);
			}
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,shadeMast);
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
	@GetMapping("/shade/getAPC")
	public GeneralResponse<GetAPC,Object> getApcNumber(){
		try {

			GetAPC acp = shadeService.getAPCNumber();
			return new GeneralResponse<>(acp, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.CREATED,request.getRequestURI()+"?"+request.getQueryString());

		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
		}
	}
	@GetMapping("/shade/apcExist/{number}")
	public ResponseEntity<GeneralResponse<Boolean,Object>> isAPCExist(@PathVariable(name = "number")String number){

		GeneralResponse<Boolean,Object> result;
		try {
			Boolean acp = shadeService.isAPCExist(number);
			if(acp)
				result = new GeneralResponse<>(acp, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			else
				result = new GeneralResponse<>(acp, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	//GetAll Shade as per format
	@GetMapping("/shade/all/{getBy}/{id}")
	public ResponseEntity<GeneralResponse<List<GetAllShade>,Object>> getAllShadesInfo(@PathVariable(value = "id") Long id, @PathVariable( value = "getBy") String getBy){
		GeneralResponse<List<GetAllShade>,Object> result ;
		try{
			List<GetAllShade> shadeMast = null;
			switch (getBy) {
				case "own":
					shadeMast = shadeService.getAllShadesInfo(getBy, id);
					if(!shadeMast.isEmpty())
						result = new GeneralResponse<>(shadeMast, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
					else
						result = new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

					break;
				case "group":
					shadeMast = shadeService.getAllShadesInfo(getBy, id);
					if(!shadeMast.isEmpty())
						result = new GeneralResponse<>(shadeMast, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
					else
					result =new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

					break;
				case "all":
					shadeMast = shadeService.getAllShadesInfo(null, null);
					//System.out.println(shadeMast);
					if(!shadeMast.isEmpty())
						result =  new GeneralResponse<>(shadeMast, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
					else
						result = new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

					break;
				default:
					result = new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

			}
		}catch (Exception e){
			e.printStackTrace();
			result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,true);
		}
		logService.saveLog(result,request,debugAll);
		return new ResponseEntity<>(result ,HttpStatus.valueOf(result .getStatusCode()));
	}

	@GetMapping("/shade/{id}")
	public ResponseEntity<GeneralResponse<ShadeMast,Object>> getShadesById(@PathVariable(value = "id") Long id){
		GeneralResponse<ShadeMast,Object> result;
		try{
			Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(id);
			if(shadeMast != null){
				result = new GeneralResponse<>(shadeMast.get(), ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}else{
				result = new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}
			logService.saveLog(result,request,debugAll);
		}catch (Exception e){
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	//get All pending shade list
	@GetMapping("/shade/allPendingAPC")
	public ResponseEntity<GeneralResponse<List<GetAllPendingShade>,Object>> getAllPendingShade(){
		GeneralResponse<List<GetAllPendingShade>,Object> result;

		try{
			List<GetAllPendingShade> shadeMast = shadeService.getAllPendingShade();
			if(shadeMast.isEmpty()){
				result= new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}
			else{
				result= new GeneralResponse<>(shadeMast, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}
			logService.saveLog(result,request,debugAll);
		}catch (Exception e){
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping("/shade/{qualityId}/{partyId}")
	public ResponseEntity<GeneralResponse<List<GetShadeByPartyAndQuality>,Object>> getShadesByQualityAndPartyId(@PathVariable(value = "qualityId") String qualityId,@PathVariable(value = "partyId") String partyId,@RequestHeader Map<String, String> headers){
		GeneralResponse<List<GetShadeByPartyAndQuality>,Object> result;
		try{
			List<GetShadeByPartyAndQuality> shadeMastList = shadeService.getShadesByQualityAndPartyId(qualityId,partyId, headers.get("id"));
			if(shadeMastList != null){
				result= new GeneralResponse<>(shadeMastList, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}else{
				result= new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}
			logService.saveLog(result,request,debugAll);
		}catch (Exception e){
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}


	@PutMapping(value = "/shade")
	public ResponseEntity<GeneralResponse<Boolean,Object>> updateShadeById(@RequestBody ShadeMast shadeMast) throws Exception {
		GeneralResponse<Boolean,Object> result;

		try {
			if (shadeMast.getId() != null) {
				boolean flag = shadeService.updateShade(shadeMast);
				if (flag) {
					result = new GeneralResponse<>(true, ConstantFile.Shade_Updated, true, System.currentTimeMillis(), HttpStatus.OK, shadeMast);
				} else {
					result = new GeneralResponse<>(false, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, shadeMast);
				}
				logService.saveLog(result, request, debugAll);
			} else
				throw new Exception(ConstantFile.Null_Record_Passed);
				//result = new GeneralResponse<>(false, CommonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,shadeMast);

			logService.saveLog(result,request,debugAll);
		}catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,shadeMast);
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@DeleteMapping(value = "/shade/{id}")
	public ResponseEntity<GeneralResponse<Boolean,Object>> deleteshadeById(@PathVariable(value = "id") Long id) {

		GeneralResponse<Boolean,Object> result;

		try{
			shadeService.deleteShadeById(id);
			result= new GeneralResponse<>(true, ConstantFile.Shade_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/shade/all")
	public ResponseEntity<GeneralResponse<List<GetShadeByPartyAndQuality>,Object>> getShadeByPartyAndWithAndWithoutQuality(@RequestParam(name = "partyId") Long partyId, @RequestParam(name = "qualityId")Long qualityId) throws Exception {
		GeneralResponse<List<GetShadeByPartyAndQuality>,Object> result;
		try {
			if (partyId != null) {
				List<GetShadeByPartyAndQuality> flag = shadeService.getShadeByPartyAndWithAndWithoutQuality(partyId,qualityId);
				if (!flag.isEmpty()) {
					result =  new GeneralResponse<>(flag, ConstantFile.Shade_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
				} else {
					result= new GeneralResponse<>(null, ConstantFile.Shade_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
				}
			}
			else {
				throw new Exception(ConstantFile.Null_Record_Passed);
				//result = new GeneralResponse<>(null, CommonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
			}
			logService.saveLog(result,request,debugAll);
		}catch (Exception e )
		{
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

}
