package com.main.glory.PaginationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RequestBody {

    Map<String,String> parameters;
    Map<String,String> sortBy;
    Long pageIndex;
    Long pageSize;
    Boolean isAnd; //default false and consider as "OR" operation
    Boolean getTotal;//count the number of record or not


}
