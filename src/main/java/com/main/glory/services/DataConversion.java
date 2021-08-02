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
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        LocalDate date = LocalDate.parse(s, inputFormatter);
        return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());


    }

}
