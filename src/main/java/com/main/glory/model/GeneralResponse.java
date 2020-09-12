package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GeneralResponse {
	Object data;
	String msg;
	Boolean success;
	Long timeStamp;
	Integer statusCode;
}
