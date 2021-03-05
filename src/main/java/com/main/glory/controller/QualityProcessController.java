package com.main.glory.controller;

import com.main.glory.Dao.qualityProcess.QualityProcessMastDao;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.qualityProcess.QualityProcessMast;
import com.main.glory.model.qualityProcess.request.UpdateRequestQualityProcess;
import com.main.glory.model.qualityProcess.response.ListResponse;
import com.main.glory.servicesImpl.QualityProcessImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.main.glory.config.ControllerConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class QualityProcessController extends ControllerConfig {

	@Autowired
	QualityProcessImpl qualityProcess;

	@Autowired
	QualityProcessMastDao qualityProcessDao;

	@Autowired
	ModelMapper modelMapper;

	@PostMapping("/qualityprocess")
	public GeneralResponse<Boolean> addQualityProcess(@RequestBody QualityProcessMast qualityProcessMast){
		try{
			if(qualityProcessMast == null){
				return new GeneralResponse<>(false, "Null data passed", false, System.currentTimeMillis(), HttpStatus.OK);
			}
			qualityProcess.saveQualityProcessMast(qualityProcessMast);
			return new GeneralResponse<>(true, "Process Added Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
	}

	@GetMapping("/qualityprocess/all/{getBy}/{id}")
	public GeneralResponse<List<ListResponse>> getQualityProcessList(@PathVariable(value = "getBy")String getBy, @PathVariable(value = "id")Long id){
		try {
			List<QualityProcessMast> qualityProcessMasts = null;
			List<ListResponse> res = new ArrayList<>();
			switch (getBy) {
				case "own":
					qualityProcessMasts = qualityProcess.qualityProcessMasts(getBy, id);
					if(!qualityProcessMasts.isEmpty()){
						qualityProcessMasts.forEach(e -> {
							res.add(modelMapper.map(e, ListResponse.class));
						});
						return new GeneralResponse<>(res, "Fetched Successfully", true, System.currentTimeMillis(),HttpStatus.OK);
					}else
						return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

				case "group":
					qualityProcessMasts = qualityProcess.qualityProcessMasts(getBy, id);
					if(!qualityProcessMasts.isEmpty()){
						qualityProcessMasts.forEach(e -> {
							res.add(modelMapper.map(e, ListResponse.class));
						});
						return new GeneralResponse<>(res, "Fetched Successfully", true, System.currentTimeMillis(),HttpStatus.OK);
					}else
						return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

				case "all":
					qualityProcessMasts = qualityProcess.qualityProcessMasts(null, null);
					if(!qualityProcessMasts.isEmpty()){
						qualityProcessMasts.forEach(e -> {
							res.add(modelMapper.map(e, ListResponse.class));
						});
						return new GeneralResponse<>(res, "Fetched Successfully", true, System.currentTimeMillis(),HttpStatus.OK);
					}else
						return new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.OK);

				default:
					return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
	}

	@GetMapping("/qualityprocess/{id}")
	public GeneralResponse<QualityProcessMast> getQualityProcessList(@PathVariable("id") Long id){
		try {
			QualityProcessMast qualityProcessMasts = qualityProcess.findById(id);
			return new GeneralResponse<>(qualityProcessMasts, "Fetched Successfully", true, System.currentTimeMillis(),HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
	}

	@PutMapping("/qualityprocess")
	public GeneralResponse<Boolean> updateQualityProcess(@RequestBody UpdateRequestQualityProcess qualityProcessMast){
		try{
			if(qualityProcessMast == null){
				return new GeneralResponse<>(false, "Null data passed", false, System.currentTimeMillis(), HttpStatus.OK);
			}
			Optional<QualityProcessMast> q = qualityProcessDao.findById(qualityProcessMast.getId());
			if(!q.isPresent())
				return new GeneralResponse<>(false, "No process found with id: "+qualityProcessMast.getId(), false, System.currentTimeMillis(), HttpStatus.OK);
			qualityProcess.update(qualityProcessMast);
			return new GeneralResponse<>(true, "updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
	}

	@DeleteMapping("qualityProcess/{id}")
	public GeneralResponse<Boolean> deleteQualityProcessById(@PathVariable("id") Long id){
		try{
			Optional<QualityProcessMast> q = qualityProcessDao.findById(id);
			if(!q.isPresent())
				return new GeneralResponse<>(false, "No process found with id: "+id, false, System.currentTimeMillis(), HttpStatus.OK);
			qualityProcess.deleteQualityProcess(id);
			return new GeneralResponse<>(true, "Process deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		}catch(Exception e){
			return  new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
	}

}
