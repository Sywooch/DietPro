package com.anton.dietpro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.anton.dietpro.adapter.DietAdapter;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.DietDB;

import java.util.ArrayList;

public class DietActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.listView);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayList<Diet> diets = new ArrayList<>();
        DietDB dbHelper = new DietDB(this);
        dbHelper.create_db();
        dbHelper.open();
        if (dbHelper.database == null){
            Toast.makeText(this,"Нет подключения к БД",Toast.LENGTH_SHORT).show();
            return;
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
        DietAdapter adapterDiet = new DietAdapter(this,diets);
        listView.setAdapter(adapterDiet);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DietDetailsActivity.class);
                intent.putExtra("dietId",id + "");
                startActivityForResult(intent,1);
            }
        });
        dbHelper.close();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String dietId = data.getStringExtra("dietId");
        //сохраняем ИД выбранной диеты в преференцес( но мб переделаем на БД )
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("acceptDietId",dietId);
        ed.apply();
        Toast.makeText(this, "Вы сели на диету " + dietId, Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
