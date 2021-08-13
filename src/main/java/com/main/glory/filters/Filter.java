package com.main.glory.filters;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
public class Filter {
    private String field;
    private QueryOperator operator;
    private String value;
    private List<String> values;//Used in case of IN operator
}
