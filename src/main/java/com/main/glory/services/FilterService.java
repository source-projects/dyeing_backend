package com.main.glory.services;

import java.util.HashMap;
import java.util.List;

import com.main.glory.Dao.FilterDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.QueryOperator;
import com.main.glory.filters.SpecificationManager;

import com.main.glory.model.machine.request.PaginatedData;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;

@Service
public class FilterService<T,D extends FilterDao<T>> {
    @Autowired
    SpecificationManager specificationManager;
    
    @Autowired
    D dao;

    // public String getWhereClause(List<Filter> parameters,HashMap<String,String> tableConversion){
    //     String whereClause="where ";
    //     for(int i=0;i<parameters.size();i++){
    //         String tableName=parameters.get(i).getTableName();
    //         String field=parameters.get(i).getField();
    //         String value=parameters.get(i).getValue();
    //         String operator=parameters.get(i).getOperation();
    //         if(i!=parameters.size()-1)
    //         whereClause+=tableConversion.get(tableName)+"."+field+" "+operator+" "+value+" AND ";
    //         else
    //         whereClause+=tableConversion.get(tableName)+"."+field+" "+operator+" "+value;
    //     }
    //     return whereClause;
    // }

    public Pageable getPageable(PaginatedData data){
        String sortBy;
        if(data.getSortBy()==null)
        sortBy="id";

        else
        sortBy=data.getSortBy();

        Direction sortOrder;

        if(data.getSortOrder()==null ||data.getSortOrder()=="ASC")
        sortOrder=Direction.ASC;

        else
        sortOrder=Direction.DESC;

        Pageable pageable=PageRequest.of(data.getPageIndex()-1, data.getPageSize()-1, sortOrder, sortBy);
        return pageable;

    }
    public FilterResponse<T> getpaginatedSortedFilteredData(PaginatedData data ){
                
        Specification<T> spec =specificationManager.getSpecificationFromFilters( data.getParameters(),data.isAnd());
        Pageable pageable=getPageable(data);
        
        Page<T> filteredList;
        
        if (spec==null)
        filteredList = dao.findAll(pageable);

        else
        filteredList = dao.findAll(spec, pageable);

        FilterResponse<T> response=new FilterResponse<T>(filteredList.toList(),filteredList.getNumber()+1,filteredList.getSize(),filteredList.getTotalPages());
        return response;
        
    }

    

}
