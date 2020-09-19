package com.main.glory.controller;

import java.util.List;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import com.main.glory.model.Party;
import com.main.glory.servicesImpl.PartyServiceImp;

@RestController
@RequestMapping("/api")
public class PartyController  extends ControllerConfig {

	@Autowired 
	private PartyServiceImp partyServiceImp;
	
	@PostMapping(value="/party")
	public GeneralResponse<Boolean> saveParty(@RequestBody Party party)
	{
		int flag=partyServiceImp.saveParty(party);
		if(flag!=1)
		{
			return new GeneralResponse<Boolean>(null, "Please Enter Valid Data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}else
			return new GeneralResponse<Boolean>(null, "Party Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
	}
	
	@GetMapping(value="/get-party-list")
	public GeneralResponse<List<Party>> getQualityList()
	{
		try{
			var x = partyServiceImp.getAllPartyDetails();
			return new GeneralResponse<List<Party>>(x, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return new GeneralResponse<List<Party>>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(value="/get-party-by-id/{id}")
	public GeneralResponse<Party> getPartyDetailsById(@PathVariable(value = "id") Long id)
	{
           if(id!=null)
		   {
			   Party partyObject=partyServiceImp.getPartyDetailById(id);
			   if(partyObject!=null)
			   {
			   	    return new GeneralResponse<Party>(partyObject, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
			   }
			   return new GeneralResponse<Party>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
		   }
           return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
	}

	@PutMapping(value="/update-party")
	public GeneralResponse<Boolean> updateQuality(@RequestBody Party party) throws Exception
	{
		if(party!=null)
		{
			 boolean flag=partyServiceImp.editPartyDetails(party);
			 if(flag) {
				 return new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			 }
			return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new GeneralResponse<Boolean>(false, "Null Party Object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
	}
	
	@DeleteMapping(value="/delete-party/{id}")
	public GeneralResponse<Boolean> deletePartyDetailsByID(@PathVariable(value = "id") Long id)
	{
		if(id!=null)
		{
			boolean flag=partyServiceImp.deletePartyById(id);
			if(flag)
			{
				return new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}
			return new GeneralResponse<Boolean>(false, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new GeneralResponse<Boolean>(false, "Null id passed", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
	}
}