package com.main.glory.Dao.database;

import com.main.glory.model.database.DatabaseBackup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface DatabaseBackupDao extends JpaRepository<DatabaseBackup,Long> {

    @Query("select x from DatabaseBackup x where Date(x.createdDate)=Date(:parse)")
    List<DatabaseBackup> getDatabaseByDate(Date parse);
}
