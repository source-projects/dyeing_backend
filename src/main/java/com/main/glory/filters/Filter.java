package com.main.glory.filters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Filter {
    private List<String> field;
    private QueryOperator operator;
    private String operation;
    private String value;
    private List<String> values;//Used in case of IN operator


    public Filter(List<String> field, QueryOperator operator, String value) {
        this.field = field;
        this.operator = operator;
        this.value = value;
    }




}
