package com.main.glory.Dao;

import com.main.glory.FabInMasterLookUp.MasterLookUpWithRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import com.main.glory.model.FabricInRecord;

import java.util.Collection;
import java.util.List;

public interface FabDataDao extends JpaRepository<FabricInRecord, Long>{


}
