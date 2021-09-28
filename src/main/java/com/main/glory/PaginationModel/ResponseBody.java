package com.main.glory.PaginationModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponseBody {

    Object pageContent;
    Long pageIndex;
    Long pageSize;
    Long total;

}
