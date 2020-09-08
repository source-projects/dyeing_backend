package services;
import java.util.List;
import model.Quality;
public interface QualityServiceInterface {

	public int saveQuality(Quality party);
	public List<Quality> getAllQuality();
	public boolean updateQuality(Quality pary) throws Exception;
	public boolean deleteQualityById(Long id);
}
