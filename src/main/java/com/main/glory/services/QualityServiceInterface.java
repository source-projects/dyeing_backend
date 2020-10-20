package com.main.glory.services;
import java.util.List;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityWithPartyName;

public interface QualityServiceInterface {

	public List<QualityWithPartyName> getAllQuality();
	public int saveQuality(Quality obj) throws Exception;
	public boolean updateQuality(Quality obj) throws Exception;
	public boolean deleteQualityById(Long id);
	public Quality getQualityByID(Long id);
	public String isQualityAlreadyExist(String qualityId);
	public String getPartyNameByPartyId(Long partyName);
}
