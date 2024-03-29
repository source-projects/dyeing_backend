package com.main.glory.services;

import com.main.glory.Dao.FilterDao;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.machine.request.PaginatedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class FilterService<T,D extends FilterDao<T>> {
    @Autowired
    SpecificationManager specificationManager;
    
    @Autowired
    D dao;


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

        Pageable pageable=PageRequest.of(data.getPageIndex(), data.getPageSize(), sortOrder, sortBy);
        return pageable;

    }
    public FilterResponse<T> getpaginatedSortedFilteredData(PaginatedData data ){
                
        Specification<T> spec =specificationManager.getSpecificationFromFilters( data.getParameters(),data.isAnd(),null);
        Pageable pageable=getPageable(data);
        
        Page<T> filteredList;
        
        if (spec==null)
        filteredList = dao.findAll(pageable);

        else
        filteredList = dao.findAll(spec, pageable);

        FilterResponse<T> response=new FilterResponse<T>(filteredList.toList(),filteredList.getNumber()+1,filteredList.getSize(),filteredList.getTotalPages());
        return response;
        
    }


    public Pageable getPageableForInvoice(PaginatedData data) {
        String sortBy;
        if(data.getSortBy()==null)
            sortBy="postfix";

        else
            sortBy=data.getSortBy();

        Direction sortOrder;

        if(data.getSortOrder()==null ||data.getSortOrder()=="ASC")
            sortOrder=Direction.ASC;

        else
            sortOrder=Direction.DESC;

        Pageable pageable=PageRequest.of(data.getPageIndex(), data.getPageSize(), sortOrder, sortBy);
        return pageable;
    }

}
