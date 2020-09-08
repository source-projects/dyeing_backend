package services;

import java.util.List;
import model.Fabric;
public interface FabricsServicesInterface {

	public int saveFabrics(Fabric fabrics) throws Exception;
	public List<Fabric> getAllFabricsDetails();
	public boolean editFabricsDetails(Fabric fabs) throws Exception;
	public boolean deleteFabricsById(Long id);
}
