package com.anton.dietpro.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by admin on 12.12.16.
 */

public class SimpleDate extends Date {

    public static String getString(Date date){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("ru","RU"));
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return df.format(date);
    }

    public static Date getSimpleDate(String stringDate){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",new Locale("ru","RU"));
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return convertedDate;
    }
}
