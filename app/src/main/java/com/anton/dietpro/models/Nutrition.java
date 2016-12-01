package com.anton.dietpro.models;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 18.11.16.
 */

public class Nutrition {
    public static final String TABLE_NUTRITION_NAME = "nutrition";
    public static final String TABLE_INGESTION_NAME = "ingestion";
    public static final String TABLE_MENU_NAME = "menu";
    private Integer id;
    private Integer day;
    private String Name;
    private List<Product> products;


    public Nutrition() {
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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
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
            Nutrition nutrition = new Nutrition();
            DietDB dbHelper = new DietDB(context);
            dbHelper.create_db();
            dbHelper.open();
            if (dbHelper.database == null) {
                Toast.makeText(context, "Нет подключения к БД", Toast.LENGTH_SHORT).show();
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
            Log.d("SQL",query);
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
                Product emptyProduct = new Product("Продукты не найдены");
                products.add(emptyProduct);
            }
            c.close();
            dbHelper.close();
            return products;
        }
        catch (Exception e){
            Log.e("DB Error",e.getMessage());
            return null;
        }
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
        Integer idDiet = Diet.getCurrentDietId(context);
        Nutrition nutrition = new Nutrition();
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return null;
        }
        Cursor c = dbHelper.database.rawQuery("select "
                + Nutrition.TABLE_MENU_NAME + ".id as id ,"
                + Nutrition.TABLE_MENU_NAME + ".day as day ,"
                + Nutrition.TABLE_INGESTION_NAME + ".name as ingestion_name "
                + " from "
                + Nutrition.TABLE_MENU_NAME
                + " inner join " + Nutrition.TABLE_INGESTION_NAME
                + " on " + Nutrition.TABLE_MENU_NAME + ".id_ingestion = " + Nutrition.TABLE_INGESTION_NAME + ".id "
                + " where " + Nutrition.TABLE_MENU_NAME + ".id_diet = " + idDiet
                + " and " + Nutrition.TABLE_MENU_NAME + ".day = " + day
                + " and " + Nutrition.TABLE_INGESTION_NAME + ".id = " + idIngestion
                ,null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("ingestion_name");
            int dayColIndex = c.getColumnIndex("day");
            nutrition.setId(Integer.valueOf(c.getString(idColIndex)));
            nutrition.setName(c.getString(nameColIndex));
            nutrition.setDay(Integer.valueOf(c.getString(dayColIndex)));
            List<Product> products = new ArrayList<>();
            nutrition.setProducts(products);

        }
        c.close();
        dbHelper.close();
        return nutrition;
    }

    public static ArrayList<Nutrition> getNutritionsByDay(long day, Context context) {
        Integer idDiet = Diet.getCurrentDietId(context);
        ArrayList<Nutrition> nutritions = new ArrayList<>();
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(context,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return null;
        }
        String query = "select "
                + Nutrition.TABLE_MENU_NAME + ".id as id ,"
                + Nutrition.TABLE_MENU_NAME + ".day as day ,"
                + Nutrition.TABLE_INGESTION_NAME + ".id as ingestion_id ,"
                + Nutrition.TABLE_INGESTION_NAME + ".name as ingestion_name "
                + " from "
                + Nutrition.TABLE_MENU_NAME
                + " inner join " + Nutrition.TABLE_INGESTION_NAME
                + " on " + Nutrition.TABLE_MENU_NAME + ".id_ingestion = " + Nutrition.TABLE_INGESTION_NAME + ".id "
                + " where " + Nutrition.TABLE_MENU_NAME + ".id_diet = " + idDiet
                + " and " + Nutrition.TABLE_MENU_NAME + ".day = " + day;
        Log.d("SQL",query);
        Cursor c = dbHelper.database.rawQuery(query
                ,null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("ingestion_name");
            int dayColIndex = c.getColumnIndex("day");
            int ingestionIdColIndex = c.getColumnIndex("ingestion_id");
            do {
                Nutrition nutrition = new Nutrition();
                nutrition.setId(Integer.valueOf(c.getString(idColIndex)));
                nutrition.setName(c.getString(nameColIndex));
                nutrition.setDay(Integer.valueOf(c.getString(dayColIndex)));
                List<Product> products = Nutrition.getNutritionProducts(nutrition.getDay(),c.getInt(ingestionIdColIndex),context);
                nutrition.setProducts(products);
                nutritions.add(nutrition);
            }while(c.moveToNext());

        }
        c.close();
        dbHelper.close();
        return nutritions;
    }
}
