package Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import model.FabricInRecord;

@EnableJpaRepositories
public interface FabDataDao extends JpaRepository<FabricInRecord, Long>{

}
