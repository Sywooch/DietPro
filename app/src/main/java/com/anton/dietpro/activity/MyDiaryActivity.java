package com.anton.dietpro.activity;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.anton.dietpro.R;
import com.anton.dietpro.adapter.DietAdapter;
import com.anton.dietpro.adapter.NutritionAdapter;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Nutrition;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MyDiaryActivity extends AppCompatActivity {

    TextView header;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat("dd.MM");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        Date beginDay = Diet.getCurrentDietDate(getApplicationContext());
        if (beginDay == null ){
            Toast.makeText(getApplicationContext(),"Вы не выбрали диету.",Toast.LENGTH_LONG).show();
            return;
        }
        long diff = today.getTime() - beginDay.getTime();
        long day = diff / (24 * 60 * 60 * 1000);
        long hours = diff / ( 60 * 60 * 1000);
        day++;

/*
        Toast.makeText(getApplicationContext() , "Начало диеты "+ df.format(beginDay.getTime()) ,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext() , "ID диеты "+ Diet.getCurrentDietId(getApplicationContext()) ,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext() , "День диеты "+ day ,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext() , "Час диеты "+ hours ,Toast.LENGTH_SHORT).show();
*/

        String formattedDate = df.format(today.getTime());
        header = (TextView) findViewById(R.id.headerDay);
        header.setText(String.format(header.getText().toString(), formattedDate,day));
        ArrayList<Nutrition> nutritions = Nutrition.getNutritionsByDay(day,getApplicationContext());
        NutritionAdapter adapterNutrition = new NutritionAdapter(this,nutritions);
        ListView listNutrition = (ListView)findViewById(R.id.listNutrition);
        listNutrition.setAdapter(adapterNutrition);

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
