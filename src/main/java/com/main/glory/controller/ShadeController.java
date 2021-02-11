package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.AddShadeMast;
import com.main.glory.model.shade.requestmodals.GetAcp;
import com.main.glory.model.shade.requestmodals.GetAllShade;
import com.main.glory.model.shade.requestmodals.GetShadeByPartyAndQuality;
import com.main.glory.model.shade.responsemodals.ShadeMastWithDetails;
import com.main.glory.services.ShadeServicesInterface;
import com.main.glory.servicesImpl.ShadeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ShadeController extends ControllerConfig {

	@Autowired
	ShadeServiceImpl shadeService;

	@PostMapping("/shade")
	public GeneralResponse<Boolean> addShadeData(@RequestBody AddShadeMast shadeMast){
		try {
			if(shadeMast == null){
				return new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			}else {
				shadeService.saveShade(shadeMast);
				return new GeneralResponse<>(true, "Data Inserted Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/shade/getACP")
	public GeneralResponse<GetAcp> getAcpNumber(){
		try {

			GetAcp acp = shadeService.getAcpNumber();
			return new GeneralResponse<>(acp, "Data fetched Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@GetMapping("/shade/acpExist/{number}")
	public GeneralResponse<Boolean> isACPExist(@PathVariable(name = "number")String number){
		try {
			Boolean acp = shadeService.isACPExist(number);
			if(acp)
				return new GeneralResponse<>(acp, "data not found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			else
				return new GeneralResponse<>(acp, "data found", true, System.currentTimeMillis(), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	//GetAll Shade as per format
	@GetMapping("/shade/all/{getBy}/{id}")
	public GeneralResponse<List<GetAllShade>> getAllShadesInfo(@PathVariable(value = "id") Long id,@PathVariable( value = "getBy") String getBy){
		try{
			List<GetAllShade> shadeMast = null;
			switch (getBy) {
				case "own":
					shadeMast = shadeService.getAllShadesInfo(getBy, id);
					if(!shadeMast.isEmpty())
						return new GeneralResponse<List<GetAllShade>>(shadeMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
					else
						return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

				case "group":
					shadeMast = shadeService.getAllShadesInfo(getBy, id);
					if(!shadeMast.isEmpty())
						return new GeneralResponse<List<GetAllShade>>(shadeMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
					else
						return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

				case "all":
					shadeMast = shadeService.getAllShadesInfo(null, null);
					System.out.println(shadeMast);
					if(!shadeMast.isEmpty())
						return new GeneralResponse<List<GetAllShade>>(shadeMast, "Fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
					else
						return new GeneralResponse<>(null, "No shade data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);

				default:
					return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/{id}")
	public GeneralResponse<ShadeMast> getShadesById(@PathVariable(value = "id") Long id){
		try{
			Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(id);
			if(shadeMast != null){
				return new GeneralResponse<ShadeMast>(shadeMast.get(), "fetched successfully", false, System.currentTimeMillis(), HttpStatus.FOUND);
			}else{
				return new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/{qualityId}/{partyId}")
	public GeneralResponse<List<GetShadeByPartyAndQuality>> getShadesByQualityAndPartyId(@PathVariable(value = "qualityId") Long qualityId,@PathVariable(value = "partyId") Long partyId){
		try{
			List<GetShadeByPartyAndQuality> shadeMastList = shadeService.getShadesByQualityAndPartyId(qualityId,partyId);
			if(shadeMastList != null){
				return new GeneralResponse<List<GetShadeByPartyAndQuality>>(shadeMastList, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
			}else{
				return new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(value = "/shade")
	public GeneralResponse<Boolean> updateShadeById(@RequestBody ShadeMast shadeMast) throws Exception {
		if (shadeMast.getId() != null) {
			boolean flag = shadeService.updateShade(shadeMast);
			if (flag) {
				return new GeneralResponse<Boolean>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			}else{
				return new GeneralResponse<Boolean>(false, "No such id found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
		}
		return new GeneralResponse<Boolean>(false, "Null shade Object", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping(value = "/shade/{id}")
	public GeneralResponse<Boolean> deleteshadeById(@PathVariable(value = "id") Long id) {
		try{
			shadeService.deleteShadeById(id);
			return new GeneralResponse<>(true, "deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
	}

}
