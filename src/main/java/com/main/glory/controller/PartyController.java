package com.main.glory.controller;

import java.util.List;
import java.util.Map;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.PartyWithMasterName;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.party.request.PartyReport;
import com.main.glory.model.party.request.PartyWithName;
import com.main.glory.model.party.request.PartyWithUserHeadName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.main.glory.model.party.Party;
import com.main.glory.servicesImpl.PartyServiceImp;

@RestController
@RequestMapping("/api")
public class PartyController extends ControllerConfig {

	@Autowired
	private PartyServiceImp partyServiceImp;

	@PostMapping(value="/party")
	public ResponseEntity<GeneralResponse<Boolean>> saveParty(@RequestBody AddParty party, @RequestHeader Map<String, String> headers)
	{
		GeneralResponse<Boolean> result;
		try {
		    partyServiceImp.saveParty(party);
			System.out.println("har::"+headers.get("id"));
			//System.out.println(id);
			result = new GeneralResponse<Boolean>(true, "Party Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/party/isPartyNameIsExist/{name}/{id}")
	public ResponseEntity<GeneralResponse<Boolean>> isPartyNameIsExist(@PathVariable(name = "name") String name,@PathVariable(name="id")Long id)
	{
		GeneralResponse<Boolean> response;
		try {
			Boolean flag = partyServiceImp.isPartyNameIsExist(name,id);

			if(flag)
			{
				response = new GeneralResponse<>(false, "name not found", false, System.currentTimeMillis(), HttpStatus.OK);
			}
			else
			{
				response = new GeneralResponse<>(true, "name found successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}


		}
		catch (Exception e)
		{
			e.printStackTrace();
			response=  new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
	}
	

	@GetMapping(value="/party/all/{getBy}/{id}")
	public ResponseEntity<GeneralResponse<List<PartyWithMasterName>>> getPartyList(@PathVariable(value = "id") Long id, @PathVariable( value = "getBy") String getBy)
	{
		GeneralResponse<List<PartyWithMasterName>> result;

		try{
			switch (getBy) {
				case "own":
					var x = partyServiceImp.getAllPartyDetails(id, getBy);
					if (!x.isEmpty()) {
						result = new GeneralResponse<>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					}
					else {
						result = new GeneralResponse<>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
					}
					break;
				case "group":
					var x1 = partyServiceImp.getAllPartyDetails(id, getBy);
					if (!x1.isEmpty())
					{
						result = new GeneralResponse<>(x1, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					}
					else {
						result = new GeneralResponse<>(x1, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);
					}
					break;
				case "all":
					var x2 = partyServiceImp.getAllPartyDetails(null, null);
					if (!x2.isEmpty()) {
						//throw new ResponseStatusException(HttpStatus.OK,x2.toString());
						result = new GeneralResponse<>(x2, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
						//result = ResponseEntity.status(HttpStatus.OK).body(result);
					}
					else {

						//response.getHeaders().add("status","404");
						result = new GeneralResponse<>(x2, "No party added yet", false, System.currentTimeMillis(), HttpStatus.OK);
						//throw new Exception("no");
						//result = ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
					}
					break;
				default:
					result = new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);

		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

	}

	@GetMapping(value="/party/{id}")
	public ResponseEntity<GeneralResponse<PartyWithUserHeadName>> getPartyDetailsById(@PathVariable(value = "id") Long id) throws Exception {
        GeneralResponse<PartyWithUserHeadName> result;
		if(id!=null)
		   {
			   PartyWithUserHeadName partyObject=partyServiceImp.getPartyDetailById(id);
			   if(partyObject!=null)
			   {
			   	    result = new GeneralResponse<>(partyObject, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK);
			   }
			   else
			   result = new GeneralResponse<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.OK);
		   }
		else {
			result=new GeneralResponse<>(null,"null id passed",false,System.currentTimeMillis(),HttpStatus.OK);
		}

		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}



	@GetMapping(value="/party/allPartyWithName")
	public ResponseEntity<GeneralResponse<List<PartyWithName>>> getAllPartyName(@RequestHeader Map<String, String> headers)
	{
		GeneralResponse<List<PartyWithName>> result;
		List<PartyWithName> partyObject=partyServiceImp.getAllPartyNameWithHeaderId(headers.get("id"));
		if(!partyObject.isEmpty())
		{
			result = new GeneralResponse<>(partyObject, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK);
		}
		else {
			result = new GeneralResponse<>(null, "No Party found!", false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/party/partyCodeExist/{partyCode}/{id}")
	public ResponseEntity<GeneralResponse<Boolean>> getPartyCodeExistOrNot(@PathVariable(name="partyCode") String partyCode,@PathVariable(name="id") Long id)
	{
		GeneralResponse<Boolean> result;
		if(partyCode==null)
		{
			result = new GeneralResponse<>(null, "Code can't be null", true, System.currentTimeMillis(), HttpStatus.OK);
		}

		Boolean partyCodeExistOrNot = partyServiceImp.partyCodeExistOrNot(partyCode,id);
		if(partyCodeExistOrNot==true)
			result = new GeneralResponse<>(true, "Party code not found ", true, System.currentTimeMillis(), HttpStatus.OK);
		else
		result = new GeneralResponse<>(false, "Party code found", false, System.currentTimeMillis(), HttpStatus.OK);

		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@PutMapping(value="/party")
	public ResponseEntity<GeneralResponse<Boolean>> updateParty(@RequestBody Party party) throws Exception
	{
		GeneralResponse<Boolean> result=null;
		try {
			if (party != null) {
				boolean flag = partyServiceImp.editPartyDetails(party);
				if (flag) {
					result = new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
				} else {
					result = new GeneralResponse<Boolean>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK);
				}
			}
			else {
				result=new GeneralResponse<>(false,"NUll id passed",false,System.currentTimeMillis(),HttpStatus.OK);
			}

		}catch(Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
	
	@DeleteMapping(value="/party/{id}")
	public ResponseEntity<GeneralResponse<Boolean>> deletePartyDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
		GeneralResponse<Boolean> result;
		try {
			if (id != null) {
				boolean flag = partyServiceImp.deletePartyById(id);
				if (flag) {
					result =  new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
				} else {
					result= new GeneralResponse<Boolean>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK);
				}
			}
			else {
				result = new GeneralResponse<Boolean>(false, "Null party object", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e )
		{
			e.printStackTrace();
			result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/party/get/report")
	public ResponseEntity<GeneralResponse<PartyReport>> getPartyReportById(@RequestParam(name = "id") Long id,@RequestParam(name = "qualityId")Long qualityId) throws Exception {
		GeneralResponse<PartyReport> result;
		try {
			if (id != null ||qualityId!=null) {
				PartyReport flag = partyServiceImp.getPartyReportById(id,qualityId);
				if (flag!=null) {
					result =  new GeneralResponse<>(flag, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
				} else {
					result= new GeneralResponse<>(null, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK);
				}
			}
			else {
				result = new GeneralResponse<>(null, "Null party object", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e )
		{
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
}