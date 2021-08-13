package com.main.glory.services;

import java.util.Date;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;  


public class DataConversion {
    

    public static Date stringToDate(String s) throws ParseException{
        if (s==null){
            return null ;
        }
        
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(s, inputFormatter);
        System.out.println(s);
        System.out.println(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString());
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());


    }

}
