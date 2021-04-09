package com.main.glory.Dao.log;

import com.main.glory.model.log.APILog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface APILogDao extends JpaRepository<APILog,Long> {
}