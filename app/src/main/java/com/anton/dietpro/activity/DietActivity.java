package com.anton.dietpro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.adapter.DietAdapter;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.DietDB;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DietActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listView = (ListView) findViewById(R.id.listViewDiet);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayList<Diet> diets = Diet.getDietList(getApplicationContext());
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

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String dietId = data.getStringExtra("dietId");

        Date date = new Date();
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(getApplicationContext());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        //сохраняем ИД выбранной диеты в преференцес( но мб переделаем на БД )
        Diet.setCurrentDietId(getApplicationContext(), Integer.valueOf(dietId));
        Diet.setCurrentDietDate(getApplicationContext(), date);
        Toast.makeText(this, "Вы сели на диету " + dietId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Дата " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
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
