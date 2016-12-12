package com.anton.dietpro.models;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import com.anton.dietpro.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 18.11.16.
 */

public class Nutrition {
    private static final String TABLE_NUTRITION_NAME = "nutrition";
    private static final String TABLE_INGESTION_NAME = "ingestion";
    private static final String TABLE_MENU_NAME = "menu";
    private Integer id;
    private Integer day;
    private String Name;
    private ArrayList<Product> products;
    private Date datetime;


    public Nutrition(){
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public Double getAmountKkal() {
        Double amountKkal = 0.0;
        for (Integer i = 0; i<this.products.size() ; i++){
            amountKkal += this.products.get(i).getCalories();
        }
        return amountKkal;
    }

    /**
     * Получает список продуктов для данного приема пищи
     *
     * @param long day
     *           День диеты
     * @param long idIngestion
     *           Номер приема пищи
     * @param Context context
     *           Контекст
     */
    public static ArrayList<Product> getNutritionProducts(long day, long idIngestion, Context context){
        try {
            ArrayList<Product> products = new ArrayList<>();
            Integer idDiet = Diet.getCurrentDietId(context);
            DietDB dbHelper = DietDB.openDB(context);
            if (dbHelper.database == null) {
                Toast.makeText(context, context.getString(R.string.errorDB), Toast.LENGTH_SHORT).show();
                return null;
            }
            String query = "select nutrition.id as nutrition_id " +
                    ", product.id as product_id " +
                    ", product.name as name " +
                    ", product.protein as protein" +
                    ", product.fat as fat" +
                    ", product.carbohydrate as carbohydrate" +
                    ", product.img as img" +
                    " , nutrition.weight as weight " +
                    " from "
                    + Nutrition.TABLE_MENU_NAME
                    + " inner join " + Nutrition.TABLE_NUTRITION_NAME
                    + " on " + Nutrition.TABLE_MENU_NAME + ".id = " + Nutrition.TABLE_NUTRITION_NAME + ".id_menu "
                    + " inner join " + Product.TABLE_NAME
                    + " on " + Nutrition.TABLE_NUTRITION_NAME + ".id_product = " + Product.TABLE_NAME + ".id "
                    + " where " + Nutrition.TABLE_MENU_NAME + ".id_diet = " + idDiet
                    + " and " + Nutrition.TABLE_MENU_NAME + ".day = " + day
                    + " and " + Nutrition.TABLE_MENU_NAME + ".id_ingestion = " + idIngestion;
            Cursor c = dbHelper.database.rawQuery(query, null);
            if (c.moveToFirst()) {
                int nutritionIdColIndex = c.getColumnIndex("nutrition_id");
                int idColIndex = c.getColumnIndex("product_id");
                int nameColIndex = c.getColumnIndex("name");
                int proteinColIndex = c.getColumnIndex("protein");
                int fatColIndex = c.getColumnIndex("fat");
                int carbohydrateColIndex = c.getColumnIndex("carbohydrate");
                int urlColIndex = c.getColumnIndex("img");
                int weightColIndex = c.getColumnIndex("weight");

                do {
                    Product product = new Product();
                    product.setId(Integer.valueOf(c.getString(idColIndex)));
                    product.setName(c.getString(nameColIndex));
                    product.setPfc(new PFC(c.getDouble(proteinColIndex), c.getDouble(fatColIndex)
                            , c.getDouble(carbohydrateColIndex)));
                    product.setUrl(c.getString(urlColIndex));
                    product.setWeight(c.getDouble(weightColIndex));
                    product.setNutritionId(c.getInt(nutritionIdColIndex));
                    products.add(product);
                } while (c.moveToNext());
            } else {
                Product emptyProduct = new Product(context.getString(R.string.listProductsNotFound));
                products.add(emptyProduct);
            }
            c.close();
            dbHelper.close();
            return products;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
    * Выводит информацию о общем количестве калорий за текущий день
    * @param long day
    *           номер дня диеты
    * @param Context context
    *           Контекст
    *
     * @return int
     *          Общее количество калорий
     **/
    public static int getAmountCallories(long day, Context context) {
        if (day < 1){
            return 0;
        }
        double calories = 0;
        ArrayList<Nutrition> nutritions = Nutrition.getNutritionsByDay(day,context);
        for(int i = 0 ; i < nutritions.size() ; i++){
            calories += nutritions.get(i).getAmountKkal();
        }
        return (int)calories;
    }

    /**
    * Получает информацию о приеме пищи в текущий день, по выбранной диете
    * id диеты получать из Diet.getCurrentDietId(); < //static
    * @param long nutrition_id
    *           номер приема пищи
    * @param Context context
    *           Контекст
    *
     * **/

    public static Nutrition getNutritionById(long nutrition_id, Context context) {
        if (nutrition_id < 1){
            return null;
        }
       return Nutrition.getNutritionList(nutrition_id, 0, 0, null, context).get(0);
    }
    /**
    * Получает информацию о приеме пищи в текущий день, по выбранной диете
    * id диеты получать из Diet.getCurrentDietId(); < //static
    * @param long day
    *           День диеты
    * @param long idIngestion
    *           Номер приема пищи
    * @param Context context
    *           Контекст
    *
     * **/
    public static Nutrition getNutritionById(long day, long idIngestion, Context context){
        ArrayList<Nutrition> arr = Nutrition.getNutritionList(0,day,idIngestion,null,context);
        return arr.size()>0?arr.get(0):null;
    }

    public static ArrayList<Nutrition> getNutritionsByDay(long day, Context context) {
        return Nutrition.getNutritionList(0, day, 0, null, context);
    }


    public static Nutrition getNutritionByTime(Date date,Context context){
            ArrayList<Nutrition> arr = Nutrition.getNutritionList(0,0,0,date,context);
        return arr.size()>0?arr.get(0):null;

    }

    private static ArrayList<Nutrition> getNutritionList(long id, long day, long idIngestion,Date date, Context context){

        Integer idDiet = Diet.getCurrentDietId(context);
        ArrayList<Nutrition> nutritions = new ArrayList<>();
        DietDB dbHelper = DietDB.openDB(context);
        if (dbHelper.database == null){
            Toast.makeText(context,context.getString(R.string.errorDB),Toast.LENGTH_SHORT).show();
            return null;
        }
        String where = "";
        String join = "";
        if (day>0){
           where =  " and " + Nutrition.TABLE_MENU_NAME + ".day = " + day;
        }
        if(day>0 && idIngestion>0){
            where = " and " + Nutrition.TABLE_MENU_NAME + ".day = " + day
                    + " and " + Nutrition.TABLE_INGESTION_NAME + ".id = " + idIngestion;
        }
        if (id>0){
            where = " and " + Nutrition.TABLE_NUTRITION_NAME + ".id = " + id;
            join = " inner join " + Nutrition.TABLE_NUTRITION_NAME + " on " + Nutrition.TABLE_MENU_NAME + ".id = " + Nutrition.TABLE_NUTRITION_NAME + ".id_menu";

        }
        if (date!=null){

            where = " and time(" + Nutrition.TABLE_MENU_NAME + ".datetime) between time('now','+0 minutes','localtime') and time('"
                    + SimpleDate.getString(date)
                    +"','+0 minutes')" +
                    " and  " + Nutrition.TABLE_MENU_NAME + ".day = " + Diet.getCurrentDietDay(context);
        }

        String query = "select "
                + Nutrition.TABLE_MENU_NAME + ".id as id ,"
                + Nutrition.TABLE_MENU_NAME + ".day as day ,"
                + Nutrition.TABLE_INGESTION_NAME + ".id as ingestion_id ,"
                + Nutrition.TABLE_INGESTION_NAME + ".name as ingestion_name, "
                + Nutrition.TABLE_MENU_NAME + ".datetime as datetime "
                + " from "
                + Nutrition.TABLE_MENU_NAME
                + " inner join " + Nutrition.TABLE_INGESTION_NAME
                + " on " + Nutrition.TABLE_MENU_NAME + ".id_ingestion = " + Nutrition.TABLE_INGESTION_NAME + ".id " +
                join
                + " where " + Nutrition.TABLE_MENU_NAME + ".id_diet = " + idDiet
                + where
                +" order by "+ Nutrition.TABLE_INGESTION_NAME + ".id asc";
        Cursor c = dbHelper.database.rawQuery(query
                ,null);
        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("ingestion_name");
            int dayColIndex = c.getColumnIndex("day");
            int ingestionIdColIndex = c.getColumnIndex("ingestion_id");
            int datetimeColIndex = c.getColumnIndex("datetime");
            do {
                Nutrition nutrition = new Nutrition();
                nutrition.setId(Integer.valueOf(c.getString(idColIndex)));
                nutrition.setName(c.getString(nameColIndex));
                nutrition.setDay(Integer.valueOf(c.getString(dayColIndex)));
                ArrayList<Product> products = Nutrition.getNutritionProducts(nutrition.getDay(),c.getInt(ingestionIdColIndex),context);
                nutrition.setProducts(products);
                nutrition.setDatetime(SimpleDate.getSimpleDate(c.getString(datetimeColIndex)));
                nutritions.add(nutrition);
            }while(c.moveToNext());

        }
        c.close();
        dbHelper.close();
        return nutritions;
    }

}
