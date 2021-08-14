package com.main.glory.Dao;

import java.io.Serializable;


import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
public interface FilterDao<T> extends  JpaSpecificationExecutor<T>,PagingAndSortingRepository<T,Long> {

}
