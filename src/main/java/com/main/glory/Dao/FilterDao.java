package com.main.glory.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface FilterDao<T> extends  JpaSpecificationExecutor<T>,PagingAndSortingRepository<T,Long>,JpaRepository<T, Long> {

}
