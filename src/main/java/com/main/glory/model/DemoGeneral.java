package com.main.glory.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DemoGeneral<T,D> {
    private T data;
    private String msg;
    private Boolean success;
    private Long timeStamp;
    private Integer statusCode;

    @JsonIgnore
    private D requestBody;

    public DemoGeneral(T data, String msg, Boolean success, Long timeStamp, HttpStatus statusCode,D requestBody) {
        this.data = data;
        this.msg = msg;
        this.success = success;
        this.timeStamp = timeStamp;
        this.statusCode = statusCode.value();
        this.requestBody = requestBody;
    }

}
