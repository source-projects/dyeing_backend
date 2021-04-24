package com.main.glory.controller;

import java.util.List;
import java.util.Map;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.PartyWithMasterName;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.party.request.PartyReport;
import com.main.glory.model.party.request.PartyWithName;
import com.main.glory.model.party.request.PartyWithUserHeadName;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.main.glory.model.party.Party;
import com.main.glory.servicesImpl.PartyServiceImp;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class PartyController extends ControllerConfig {


	@Autowired
	LogServiceImpl logService;

	@Autowired
	HttpServletRequest request;

	@Value("${spring.application.debugAll}")
	Boolean debugAll=true;

	@Autowired
	private PartyServiceImp partyServiceImp;

	@PostMapping(value="/party")
	public ResponseEntity<GeneralResponse<Boolean,Object>> saveParty(@RequestBody AddParty party, @RequestHeader Map<String, String> headers)
	{
		GeneralResponse<Boolean,Object> result;
		try {
		    partyServiceImp.saveParty(party);
			//System.out.println("har::"+headers.get("id"));
			//System.out.println(id);
			result = new GeneralResponse<>(true, CommonMessage.Party_Added, true, System.currentTimeMillis(), HttpStatus.OK,party);
			logService.saveLog(result,request,debugAll);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,party);
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/party/isPartyNameIsExist/{name}/{id}")
	public ResponseEntity<GeneralResponse<Boolean,Object>> isPartyNameIsExist(@PathVariable(name = "name") String name,@PathVariable(name="id")Long id)
	{
		GeneralResponse<Boolean,Object> result;
		try {
			Boolean flag = partyServiceImp.isPartyNameIsExist(name,id);

			if(flag)
			{
				result = new GeneralResponse<>(false, CommonMessage.Party_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			}
			else
			{
				result = new GeneralResponse<>(true, CommonMessage.Party_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			}

			logService.saveLog(result,request,debugAll);

		}
		catch (Exception e)
		{
			e.printStackTrace();
			result=  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
	

	@GetMapping(value="/party/all/{getBy}/{id}")
	public ResponseEntity<GeneralResponse<List<PartyWithMasterName>,Object>> getPartyList(@PathVariable(value = "id") Long id, @PathVariable( value = "getBy") String getBy)
	{
		GeneralResponse<List<PartyWithMasterName>,Object> result;

		try{
			switch (getBy) {
				case "own":
					var x = partyServiceImp.getAllPartyDetails(id, getBy);
					if (!x.isEmpty()) {
						result = new GeneralResponse<>(x, CommonMessage.Party_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}
					else {
						result = new GeneralResponse<>(x, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}
					break;
				case "group":
					var x1 = partyServiceImp.getAllPartyDetails(id, getBy);
					if (!x1.isEmpty())
					{
						result = new GeneralResponse<>(x1, CommonMessage.Party_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}
					else {
						result = new GeneralResponse<>(x1, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}
					break;
				case "all":
					var x2 = partyServiceImp.getAllPartyDetails(null, null);
					if (!x2.isEmpty()) {
						//throw new ResponseStatusException(HttpStatus.OK,x2.toString());
						result = new GeneralResponse<>(x2, CommonMessage.Party_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
						//result = ResponseEntity.status(HttpStatus.OK).body(result);
					}
					else {

						//response.getHeaders().add("status","404");
						result = new GeneralResponse<>(x2, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
						//throw new Exception("no");
						//result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
					}
					break;
				default:
					result = new GeneralResponse<>(null, CommonMessage.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());


			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);

		}
		logService.saveLog(result,request,debugAll);
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

	}

	@GetMapping(value="/party/{id}")
	public ResponseEntity<GeneralResponse<PartyWithUserHeadName,Object>> getPartyDetailsById(@PathVariable(value = "id") Long id) throws Exception {

		GeneralResponse<PartyWithUserHeadName, Object> result;
		try {


			if (id != null) {
				PartyWithUserHeadName partyObject = partyServiceImp.getPartyDetailById(id);
				if (partyObject != null) {
					result = new GeneralResponse<>(partyObject, CommonMessage.Party_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
				} else
					result = new GeneralResponse<>(null, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());

				logService.saveLog(result, request, debugAll);
			} else {
				throw new Exception(CommonMessage.Null_Record_Passed);

			}
		}catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
			logService.saveLog(result, request, true);
		}

		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}



	@GetMapping(value="/party/allPartyWithName")
	public ResponseEntity<GeneralResponse<List<PartyWithName>,Object>> getAllPartyName(@RequestHeader Map<String, String> headers)
	{
		GeneralResponse<List<PartyWithName>,Object> result;
		try {
			List<PartyWithName> partyObject = partyServiceImp.getAllPartyNameWithHeaderId(headers.get("id"));
			if (!partyObject.isEmpty()) {
				result = new GeneralResponse<>(partyObject, CommonMessage.Party_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
			} else {
				result = new GeneralResponse<>(null, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
			}
			logService.saveLog(result, request, debugAll);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
			logService.saveLog(result, request, true);

		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/party/partyCodeExist/{partyCode}/{id}")
	public ResponseEntity<GeneralResponse<Boolean,Object>> getPartyCodeExistOrNot(@PathVariable(name="partyCode") String partyCode,@PathVariable(name="id") Long id)
	{
		GeneralResponse<Boolean,Object> result;
		try {
			if (partyCode == null) {
				throw new Exception(CommonMessage.Null_Record_Passed);
				//result = new GeneralResponse<>(null, "Code can't be null", true, System.currentTimeMillis(), HttpStatus.OK);
			}

			Boolean partyCodeExistOrNot = partyServiceImp.partyCodeExistOrNot(partyCode, id);
			if (partyCodeExistOrNot == true)
				result = new GeneralResponse<>(true, CommonMessage.Party_Not_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			else
				result = new GeneralResponse<>(false, CommonMessage.Party_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

			logService.saveLog(result, request, debugAll);
		}catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result, request, true);


		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@PutMapping(value="/party")
	public ResponseEntity<GeneralResponse<Boolean,Object>> updateParty(@RequestBody Party party) throws Exception
	{
		GeneralResponse<Boolean,Object> result=null;
		try {
			if (party != null) {
				boolean flag = partyServiceImp.editPartyDetails(party);
				if (flag) {
					result = new GeneralResponse<>(true, CommonMessage.Party_Updated, true, System.currentTimeMillis(), HttpStatus.OK,party);
				} else {
					result = new GeneralResponse<>(false, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,party);
				}
			}
			else {
				result=new GeneralResponse<>(false,CommonMessage.Null_Record_Passed,false,System.currentTimeMillis(),HttpStatus.OK,party);
			}
			logService.saveLog(result, request, debugAll);

		}catch(Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,party);
			logService.saveLog(result, request, true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
	
	@DeleteMapping(value="/party/{id}")
	public ResponseEntity<GeneralResponse<Boolean,Object>> deletePartyDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
		GeneralResponse<Boolean,Object> result;
		try {
			if (id != null) {
				boolean flag = partyServiceImp.deletePartyById(id);
				if (flag) {
					result =  new GeneralResponse<>(true, CommonMessage.Party_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
				} else {
					result= new GeneralResponse<>(false, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
				}
			}
			else {
				result = new GeneralResponse<>(false, CommonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			}
			logService.saveLog(result, request, debugAll);
		}catch (Exception e )
		{
			e.printStackTrace();
			result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result, request, true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/party/all/report")
	public ResponseEntity<GeneralResponse<PartyReport,Object>> getPartyReportById(@RequestParam(name = "id") Long id,@RequestParam(name = "qualityId")Long qualityId) throws Exception {
		GeneralResponse<PartyReport,Object> result;
		try {
			if (id != null ||qualityId!=null) {
				PartyReport flag = partyServiceImp.getPartyReportById(id,qualityId);
				if (flag!=null) {
					result =  new GeneralResponse<>(flag, CommonMessage.Party_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
				} else {
					result= new GeneralResponse<>(null, CommonMessage.Party_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
				}
			}
			else {
				result = new GeneralResponse<>(null, CommonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			}
			logService.saveLog(result, request, debugAll);
		}catch (Exception e )
		{
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result, request, true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
}