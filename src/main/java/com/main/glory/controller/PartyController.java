package com.main.glory.controller;

import java.util.List;
import java.util.Map;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.request.AddParty;
import com.main.glory.model.party.request.PartyWithName;
import com.main.glory.model.party.request.PartyWithPartyCode;
import com.main.glory.model.party.request.PartyWithUserHeadName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.main.glory.model.party.Party;
import com.main.glory.servicesImpl.PartyServiceImp;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api")
public class PartyController extends ControllerConfig {

	@Autowired
	private PartyServiceImp partyServiceImp;

	@PostMapping(value="/party")
	public GeneralResponse<Boolean> saveParty(@RequestBody AddParty party, @RequestHeader Map<String, String> headers)
	{
		try {
		    partyServiceImp.saveParty(party);
			System.out.println("har::"+headers.get("id"));
			//System.out.println(id);
			return new GeneralResponse<Boolean>(true, "Party Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return new GeneralResponse<Boolean>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping(value="/party/all/{getBy}/{id}")
	public GeneralResponse<List<Party>> getPartyList(@PathVariable(value = "id") Long id,@PathVariable( value = "getBy") String getBy)
	{
		try{
			switch (getBy) {
				case "own":
					var x = partyServiceImp.getAllPartyDetails(id, getBy);
					if (!x.isEmpty())
						return new GeneralResponse<List<Party>>(x, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
					else
						return new GeneralResponse<List<Party>>(x, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

				case "group":
					var x1 = partyServiceImp.getAllPartyDetails(id, getBy);
					if (!x1.isEmpty())
						return new GeneralResponse<List<Party>>(x1, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
					else
						return new GeneralResponse<List<Party>>(x1, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

				case "all":
					var x2 = partyServiceImp.getAllPartyDetails(null, null);
					if (!x2.isEmpty())
						return new GeneralResponse<List<Party>>(x2, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
					else
						return new GeneralResponse<List<Party>>(x2, "No party added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

				default:
					return new GeneralResponse<List<Party>>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<List<Party>>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value="/party/{id}")
	public GeneralResponse<PartyWithUserHeadName> getPartyDetailsById(@PathVariable(value = "id") Long id) throws Exception {
           if(id!=null)
		   {
			   PartyWithUserHeadName partyObject=partyServiceImp.getPartyDetailById(id);
			   if(partyObject!=null)
			   {
			   	    return new GeneralResponse<>(partyObject, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
			   }
			   return new GeneralResponse<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
		   }
           return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
	}



	@GetMapping(value="/party/allPartyWithName")
	public GeneralResponse<List<PartyWithName>> getAllPartyName(@RequestHeader Map<String, String> headers)
	{
		List<PartyWithName> partyObject=partyServiceImp.getAllPartyNameWithHeaderId(headers.get("id"));
		if(!partyObject.isEmpty())
		{
			return new GeneralResponse<>(partyObject, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
		}

		return new GeneralResponse<>(null, "No Party found!", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
	}

	@GetMapping(value="/party/partyCodeExist/{partyCode}")
	public GeneralResponse<Boolean> getPartyCodeExistOrNot(@PathVariable(name="partyCode") String partyCode)
	{
		if(partyCode==null)
		{
			return new GeneralResponse<>(null, "Code can't be null", true, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}

		Boolean partyCodeExistOrNot = partyServiceImp.partyCodeExistOrNot(partyCode);
		if(partyCodeExistOrNot==true)
			return new GeneralResponse<>(true, "Party code is available to use ", true, System.currentTimeMillis(), HttpStatus.OK);
		else
		return new GeneralResponse<>(false, "Party code is already exist", false, System.currentTimeMillis(), HttpStatus.FOUND);
	}

	@PutMapping(value="/party")
	public GeneralResponse<Boolean> updateParty(@RequestBody Party party) throws Exception
	{
		try {
			if (party != null) {
				boolean flag = partyServiceImp.editPartyDetails(party);
				if (flag) {
					return new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
				} else {
					return new GeneralResponse<Boolean>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
				}
			}

		}catch(Exception e)
		{
			return new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
		return null;
	}
	
	@DeleteMapping(value="/party/{id}")
	public GeneralResponse<Boolean> deletePartyDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
		GeneralResponse<Boolean> result;
		try {
			if (id != null) {
				boolean flag = partyServiceImp.deletePartyById(id);
				if (flag) {
					result =  new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
				} else {
					result= new GeneralResponse<Boolean>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
				}
			}
			result= new GeneralResponse<Boolean>(false, "Null party object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}catch (Exception e )
		{
			result= new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
		return result;
	}
}