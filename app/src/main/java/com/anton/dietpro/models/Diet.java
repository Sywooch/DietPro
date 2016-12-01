package com.anton.dietpro.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.Toast;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by admin on 02.11.16.
 */

public class Diet {
    public static final String TABLE_NAME = "diet";

    private int id;
    private String name;
    private int length;
    private String description;
    public Diet(int id, String name, int length, String description) {
        this.id = id;
        this.name = name;
        this.length = length;
        this.description = description;
    }
    public Diet() {
    }

    public Diet(String name, int length, String description) {
        this.name = name;
        this.length = length;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public static ArrayList<Diet> getDietList(Context context){

        ArrayList<Diet> diets = new ArrayList<>();
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return null;
        }
        Cursor c = dbHelper.database.rawQuery("select * from " + DietDB.TABLE_DIET,null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int descriptionColIndex = c.getColumnIndex("description");
            int lengthColIndex = c.getColumnIndex("length");

            do {
                Diet diet = new Diet(
                        Integer.valueOf(c.getString(idColIndex))
                        ,c.getString(nameColIndex)
                        ,Integer.valueOf(c.getString(lengthColIndex))
                        ,c.getString(descriptionColIndex)
                );
                diets.add(diet);
            } while (c.moveToNext());
        } else {
            Diet emptyDiet = new Diet("Диеты не найдены",0,null);
            diets.add(emptyDiet);
        }
        c.close();
        dbHelper.close();
        return diets;
    }

    public static Diet getDietById(long id, Context context){
        Diet diet;
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return null;
        }
        Cursor c = dbHelper.database.rawQuery("select * from " + DietDB.TABLE_DIET +
                " where id = " + id + " limit 1",null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int descriptionColIndex = c.getColumnIndex("description");
            int lengthColIndex = c.getColumnIndex("length");
            diet = new Diet(
                    Integer.valueOf(c.getString(idColIndex))
                    ,c.getString(nameColIndex)
                    ,Integer.valueOf(c.getString(lengthColIndex))
                    ,c.getString(descriptionColIndex)
            );
        } else {
            diet = new Diet("Диеты не найдены",0,null);
        }
        c.close();
        dbHelper.close();
        return diet;
    }

    public static Integer getCurrentDietId(Context context) {
        UserData data = UserData.readPref(context);
        return data.getDietId();
    }
    public static void setCurrentDietId(Context context, Integer dietId){
        UserData data = UserData.readPref(context);
        data.setDietId(dietId);
        data.savePref(context);

    }

    public static Date getCurrentDietDate(Context context) {
        UserData data = UserData.readPref(context);
        return data.getDietDateStart();
    }
    public static void setCurrentDietDate(Context context, Date dietDate){
        UserData data = UserData.readPref(context);
        data.setDietDateStart(dietDate);
        data.savePref(context);

    }

    public static long getCurrentDietDay(Context context) {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        Date beginDay = Diet.getCurrentDietDate(context);
        if (beginDay == null ){
            Toast.makeText(context,"Вы не выбрали диету.",Toast.LENGTH_LONG).show();
            return 0;
        }
        long diff = today.getTime() - beginDay.getTime();
        long day = diff / (24 * 60 * 60 * 1000);
        long hours = diff / ( 60 * 60 * 1000);
        day++;
        return day;
    }
}
