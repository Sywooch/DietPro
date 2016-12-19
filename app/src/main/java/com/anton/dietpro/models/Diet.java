package com.anton.dietpro.models;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.widget.Toast;

import com.anton.dietpro.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by admin on 02.11.16.
 */

public class Diet extends Item {
    public static final String TABLE_NAME = "diet";


    private int length;
    private double effect;
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

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public double getEffect() {
        return effect;
    }

    public void setEffect(double effect) {
        this.effect = effect;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
            Toast.makeText(context,context.getString(R.string.errorDontAcceptDiet),Toast.LENGTH_LONG).show();
            return 0;
        }
        long diff = today.getTime() - beginDay.getTime();
        long day = diff / (24 * 60 * 60 * 1000);
        day++;
        return day;
    }

    public static void clearInfoDiet(Context applicationContext /* , long dietId */) {
        DietDB dbHelper = DietDB.openDB(applicationContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        db.delete(Diary.TABLE_DIARY_NAME,null,null); //TODO добавить удаление только выбранной диеты(хотя и так сойдет)
        db.setTransactionSuccessful();
        db.endTransaction();
        dbHelper.close();
    }

    public static ArrayList<Diet> getDietList(Context context){
        return Diet.getDietList(context,0,null,null);
    }

    public static Diet getDietById(long id, Context context){
        return Diet.getDietList(context,id,null,null).get(0);
    }

    public static ArrayList<Diet> getSearchDietList(Context context, String query) {
        return Diet.getDietList(context,0,query,null);
    }

    public static ArrayList<Diet> getDietListSorted(Context context,boolean orderBy){

        return orderBy?getDietList(context,0,null,"name asc"):getDietList(context,0,null,"name desc");
    }

    /**
    * Метод производит поиск по базе данных с учетом вкусовых предпочтений пользователя
    * @return номер самой подходящей по вкусам диеты
     **/
    public static long generateDiet(Context context) {
        UserData user = UserData.readPref(context);
        long idDiet = 0;
        String taste = user.getTastePreferences();
        taste = taste.trim();
        if (taste.length()<2){
            return 0;
        }

        String[] arrTaste = taste.split("(\\s|\n|,|\\.)");
        for (int i=0;i < arrTaste.length ;i++){
            arrTaste[i] = arrTaste[i].substring(0,1).toUpperCase(new Locale("ru","RU")) +arrTaste[i].substring(1);
        }
        //ищем совпадения по продуктам в БД(без последних двух симоволов(окончания))
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrTaste.length; i++) {
            arrTaste[i] = arrTaste[i].trim();
            if (!arrTaste[i].matches(" *") && (arrTaste[i].length()>1)) {
                if(arrTaste[i].length() > 2) {
                    arrTaste[i] = arrTaste[i].substring(0, arrTaste[i].length() - 1);
                }
                sb.append("or name like '" + arrTaste[i] + "%' ");
            }
        }
        String where = sb.substring(2);
        String query = "";
        DietDB db = DietDB.openDB(context);
        Cursor c = db.getReadableDatabase().query(true,DietDB.TABLE_PRODUCT,new String[]{"id","name","protein","fat","carbohydrate","img"}
                ,where,null,null,null,null,"10");
        if(c.moveToFirst()) {
            String productsName="";
            do{
                productsName += "or nutrition.id_product=" + c.getString(0)+" ";
            }while(c.moveToNext());
            productsName = productsName.substring(2);
            //ищем диету с максимальным приемом данных продуктов(в сумме)

            query = " select diet.id,diet.name, count(*) " +
                    " from diet inner join menu on diet.id = menu.id_diet " +
                    " inner join nutrition on menu.id = nutrition.id_menu " +
                    " where " + productsName +
                    " group by diet.id " +
                    " order by count(*) desc ";
            c.close();
            c = db.database.rawQuery(query,null);
            if(c.moveToFirst()){
                idDiet = c.getInt(0);
            }
            c.close();
        }
        db.close();
        return idDiet;
    }

    private static ArrayList<Diet> getDietList(Context context,long id,String query, String orderBy){

        ArrayList<Diet> diets = new ArrayList<Diet>();
        DietDB dbHelper = DietDB.openDB(context);
        if (dbHelper.database == null){
            Toast.makeText(context,context.getString(R.string.errorDB),Toast.LENGTH_SHORT).show();
            return null;
        }
        String selection = null;
        String[] selectionArgs = null;
        String limit = null;
        if(query != null ){
            if(!query.isEmpty()) {
                selection = "name like ?";
                selectionArgs = new String[]{"%"+query+"%"};
                limit ="10";
            }
        }
        else if(id>0){
            selection = "id = ?";
            selectionArgs = new String[]{String.valueOf(id)};
            limit = "1";
        }
        Cursor c = dbHelper.database.query(true,Diet.TABLE_NAME,new String[]{"id","name","description","length","efficitnce"}
                ,selection,selectionArgs,null,null,orderBy,limit);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int descriptionColIndex = c.getColumnIndex("description");
            int lengthColIndex = c.getColumnIndex("length");
            int effectColIndex = c.getColumnIndex("efficitnce");
            do {
                Diet diet = new Diet(
                        Integer.valueOf(c.getString(idColIndex))
                        ,c.getString(nameColIndex)
                        ,Integer.valueOf(c.getString(lengthColIndex))
                        ,c.getString(descriptionColIndex)
                );
                diet.setEffect(c.getDouble(effectColIndex));
                diets.add(diet);
            } while (c.moveToNext());
        } else {
            Diet emptyDiet = new Diet(context.getString(R.string.listDietsNotFound),0,null);
            diets.add(emptyDiet);
        }
        c.close();
        dbHelper.close();
        return diets;
    }

}
