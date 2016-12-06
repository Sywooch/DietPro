package com.anton.dietpro.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.anton.dietpro.activity.DietActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by admin on 24.11.16.
 */

public class Diary {
    public static final String TABLE_DIARY_NAME = "diary";

    private long id;
    private long idNutrition;
    private boolean complete;
    private boolean active;
    private String datetime;

    public Diary() {
    }
    public Diary(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdNutrition() {
        return idNutrition;
    }

    public void setIdNutrition(long idNutrition) {
        this.idNutrition = idNutrition;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public static boolean insertProductComplete(Context context, long nutrition_id) {
        if (nutrition_id<1){
            return false;
        }
        Integer idDiet = Diet.getCurrentDietId(context);
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return false;
        }
        ContentValues cv = new ContentValues();
        cv.put("id_nutrition", nutrition_id);
        cv.put("complete", true);
        cv.put("active", true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        cv.put("datetime", df.format(new Date()));
        dbHelper.database.beginTransaction();
        long rowID = dbHelper.database.insert(Diary.TABLE_DIARY_NAME, null,cv);
        dbHelper.database.setTransactionSuccessful();
        dbHelper.database.endTransaction();
        dbHelper.close();
        if (rowID>0){
            Log.d("SQL_INSERT" , "insert " + rowID + " row completed.");
            return true;
        }
        return false;

    }

    public static boolean removeNutrition(Context context, long nutrition_id) {
        if (nutrition_id<1){
            return false;
        }
        Integer idDiet = Diet.getCurrentDietId(context);
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return false;
        }
        dbHelper.database.beginTransaction();
        int count = dbHelper.database.delete(Diary.TABLE_DIARY_NAME, "id_nutrition = " + nutrition_id,null);
        Log.d("SQL",Diary.TABLE_DIARY_NAME+  "; id_nutrition = " + nutrition_id);
        dbHelper.database.setTransactionSuccessful();
        dbHelper.database.endTransaction();
        dbHelper.close();
        if (count>0){
            return true;
        }
        return false;
    }

    public static void printDiary(Context context) {
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor c = dbHelper.database.rawQuery("select id,id_nutrition,datetime,complete,active from " + Diary.TABLE_DIARY_NAME,null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int idNutritionColIndex = c.getColumnIndex("id_nutrition");
            int datetimeColIndex = c.getColumnIndex("datetime");
            int completeColIndex = c.getColumnIndex("complete");
            int activeColIndex = c.getColumnIndex("active");

            do {

                Log.d("SQL_TEST",
                 "row -- id:"+c.getInt(idColIndex)
                + " id_nutrition:"+c.getInt(idNutritionColIndex)
                + " datetime:" + c.getString(datetimeColIndex)
                + " complete:" + c.getInt(completeColIndex)
                + " active:" + c.getInt(activeColIndex)
                );
            } while (c.moveToNext());
        } else {
            Log.d("SQL_TEST","Нет записей.");
        }
        c.close();
        dbHelper.close();
    }

    public static boolean isCompleteNutrition(Context context, Integer nutritionId) {
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return false;
        }
        Cursor c = dbHelper.database.rawQuery("select id from " + Diary.TABLE_DIARY_NAME + " where id_nutrition = " + nutritionId + "" +
                " and complete = 1" +
                " and active = 1" +
                " limit 1",null);
        if (c.moveToFirst()) {
            if (c.getInt(c.getColumnIndex("id")) > 0 ) {
                dbHelper.close();
                return true;
            }
        }
        dbHelper.close();
        return false;

    }

    public static boolean isCompleteMenu(Context context, long itemId) {
        if (itemId <1 ){
            return false;
        }
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return false;
        }
        String query = "select count_plan as plan" +
                ", count_fakt as fakt " +
                " from (select count(*) as count_plan from nutrition where nutrition.id_menu = " + itemId + " ) as plan_table" +
                ", (select count(*) as count_fakt from nutrition inner join diary on nutrition.id = diary.id_nutrition" +
                " where nutrition.id_menu = " + itemId + " ) as fakt_table " +
                " limit 1";
        Log.d("INGESTION2",query);
        Cursor c = dbHelper.database.rawQuery(query,null);
        if (c.moveToFirst()) {
            int planColIndex = c.getColumnIndex("plan");
            int faktColIndex = c.getColumnIndex("fakt");
            if (c.getInt(planColIndex) == c.getInt(faktColIndex)){
                c.close();
                dbHelper.close();
                return true;
            }
        }
        c.close();
        dbHelper.close();
        return false;
    }
}
