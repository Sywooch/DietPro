package com.anton.dietpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.anton.dietpro.R;
import com.anton.dietpro.adapter.DietAdapter;
import com.anton.dietpro.adapter.NutritionAdapter;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Nutrition;

import java.util.ArrayList;

public class MyDiaryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        ArrayList<Nutrition> nutritions = new ArrayList<>();
        nutritions.add(Nutrition.getNutritionById(1,1,getApplicationContext()));
        nutritions.add(Nutrition.getNutritionById(1,2,getApplicationContext()));
        nutritions.add(Nutrition.getNutritionById(1,3,getApplicationContext()));
        NutritionAdapter adapterNutrition = new NutritionAdapter(this,nutritions);
        ListView listNutrition = (ListView)findViewById(R.id.listNutrition);
        listNutrition.setAdapter(adapterNutrition);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
