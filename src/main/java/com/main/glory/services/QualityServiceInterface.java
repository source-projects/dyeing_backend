package com.main.glory.services;
import java.util.List;
import java.util.Optional;

import com.main.glory.model.quality.request.AddQualityRequest;
import com.main.glory.model.quality.request.UpdateQualityRequest;
import com.main.glory.model.quality.response.GetQualityResponse;

public interface QualityServiceInterface {

	public List<GetQualityResponse> getAllQuality();
	public int saveQuality(AddQualityRequest obj) throws Exception;
	public boolean updateQuality(UpdateQualityRequest obj) throws Exception;
	public boolean deleteQualityById(Long id);
	public GetQualityResponse getQualityByID(Long id);
	public String isQualityAlreadyExist(String qualityId);
	public String getPartyNameByPartyId(Long partyName);
}
