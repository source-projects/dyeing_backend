package com.main.glory.filters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinColumnProps {
    private String joinColumnName;
    private Filter parameters;

}
