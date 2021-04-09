package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.request.PartyReport;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.*;
import com.main.glory.servicesImpl.ShadeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShadeController extends ControllerConfig {

	@Autowired
	ShadeServiceImpl shadeService;

	@PostMapping("/shade")
	public ResponseEntity<GeneralResponse<Boolean>> addShadeData(@RequestBody AddShadeMast shadeMast ,@RequestHeader Map<String, String> headers){
		GeneralResponse<Boolean> result;
		try {
			if(shadeMast == null){
				result =  new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.OK);
			}else {
				shadeService.saveShade(shadeMast,headers.get("id"));
				result = new GeneralResponse<>(true, "Data Inserted Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
	@GetMapping("/shade/getAPC")
	public GeneralResponse<GetAPC> getApcNumber(){
		try {

			GetAPC acp = shadeService.getAPCNumber();
			return new GeneralResponse<>(acp, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
	}
	@GetMapping("/shade/apcExist/{number}")
	public ResponseEntity<GeneralResponse<Boolean>> isAPCExist(@PathVariable(name = "number")String number){

		GeneralResponse<Boolean> result;
		try {
			Boolean acp = shadeService.isAPCExist(number);
			if(acp)
				result = new GeneralResponse<>(acp, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);
			else
				result = new GeneralResponse<>(acp, "data found", true, System.currentTimeMillis(), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	//GetAll Shade as per format
	@GetMapping("/shade/all/{getBy}/{id}")
	public ResponseEntity<GeneralResponse<List<GetAllShade>>> getAllShadesInfo(@PathVariable(value = "id") Long id, @PathVariable( value = "getBy") String getBy){
		GeneralResponse<List<GetAllShade>> result ;
		try{
			List<GetAllShade> shadeMast = null;
			switch (getBy) {
				case "own":
					shadeMast = shadeService.getAllShadesInfo(getBy, id);
					if(!shadeMast.isEmpty())
						result = new GeneralResponse<List<GetAllShade>>(shadeMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					else
						result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

					break;
				case "group":
					shadeMast = shadeService.getAllShadesInfo(getBy, id);
					if(!shadeMast.isEmpty())
						result = new GeneralResponse<List<GetAllShade>>(shadeMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					else
					result =new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

					break;
				case "all":
					shadeMast = shadeService.getAllShadesInfo(null, null);
					//System.out.println(shadeMast);
					if(!shadeMast.isEmpty())
						result =  new GeneralResponse<List<GetAllShade>>(shadeMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					else
						result = new GeneralResponse<>(null, "No shade data added yet", false, System.currentTimeMillis(), HttpStatus.OK);

					break;
				default:
					result = new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			result =  new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result ,HttpStatus.valueOf(result .getStatusCode()));
	}

	@GetMapping("/shade/{id}")
	public ResponseEntity<GeneralResponse<ShadeMast>> getShadesById(@PathVariable(value = "id") Long id){
		GeneralResponse<ShadeMast> result;
		try{
			Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(id);
			if(shadeMast != null){
				result = new GeneralResponse<ShadeMast>(shadeMast.get(), "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}else{
				result = new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	//get All pending shade list
	@GetMapping("/shade/allPendingAPC")
	public ResponseEntity<GeneralResponse<List<GetAllPendingShade>>> getAllPendingShade(){
		GeneralResponse<List<GetAllPendingShade>> result;

		try{
			List<GetAllPendingShade> shadeMast = shadeService.getAllPendingShade();
			if(shadeMast.isEmpty()){
				result= new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.OK);
			}
			else{
				result= new GeneralResponse<>(shadeMast, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping("/shade/{qualityId}/{partyId}")
	public ResponseEntity<GeneralResponse<List<GetShadeByPartyAndQuality>>> getShadesByQualityAndPartyId(@PathVariable(value = "qualityId") String qualityId,@PathVariable(value = "partyId") String partyId,@RequestHeader Map<String, String> headers){
		GeneralResponse<List<GetShadeByPartyAndQuality>> result;
		try{
			List<GetShadeByPartyAndQuality> shadeMastList = shadeService.getShadesByQualityAndPartyId(qualityId,partyId, headers.get("id"));
			if(shadeMastList != null){
				result= new GeneralResponse<List<GetShadeByPartyAndQuality>>(shadeMastList, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}else{
				result= new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}


	@PutMapping(value = "/shade")
	public ResponseEntity<GeneralResponse<Boolean>> updateShadeById(@RequestBody ShadeMast shadeMast) throws Exception {
		GeneralResponse<Boolean> result;
		if (shadeMast.getId() != null) {
			boolean flag = shadeService.updateShade(shadeMast);
			if (flag) {
				result= new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}else{
				result= new GeneralResponse<Boolean>(false, "No such id found", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}
		else
		result= new GeneralResponse<Boolean>(false, "Null shade Object", false, System.currentTimeMillis(), HttpStatus.OK);
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@DeleteMapping(value = "/shade/{id}")
	public ResponseEntity<GeneralResponse<Boolean>> deleteshadeById(@PathVariable(value = "id") Long id) {
		GeneralResponse<Boolean> result;
		try{
			shadeService.deleteShadeById(id);
			result= new GeneralResponse<>(true, "deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value="/shade/all")
	public ResponseEntity<GeneralResponse<List<GetShadeByPartyAndQuality>>> getShadeByPartyAndWithAndWithoutQuality(@RequestParam(name = "partyId") Long partyId, @RequestParam(name = "qualityId")Long qualityId) throws Exception {
		GeneralResponse<List<GetShadeByPartyAndQuality>> result;
		try {
			if (partyId != null) {
				List<GetShadeByPartyAndQuality> flag = shadeService.getShadeByPartyAndWithAndWithoutQuality(partyId,qualityId);
				if (!flag.isEmpty()) {
					result =  new GeneralResponse<>(flag, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
				} else {
					result= new GeneralResponse<>(null, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK);
				}
			}
			else {
				result = new GeneralResponse<>(null, "Null object", false, System.currentTimeMillis(), HttpStatus.OK);
			}
		}catch (Exception e )
		{
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

}
