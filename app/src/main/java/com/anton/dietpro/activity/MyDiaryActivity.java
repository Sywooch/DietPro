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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

public class MyDiaryActivity extends AppCompatActivity implements View.OnTouchListener {

    final private String TAG_DAY = "SWIPE_DAY";
    final private String TAG_ACTION = "SWIPE_DAY";
    private long swipe_day = 0;
    private TextView header;
    private float fromPositionX;
    private float fromPositionY;
    private ListView listNutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        long day = Diet.getCurrentDietDay(getApplicationContext());
        swipe_day = day;
        if ((day<1) && (Diet.getCurrentDietId(getApplicationContext())<1) ){
            Intent backIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(backIntent);
        }


        setContentView(R.layout.activity_my_diary);

        RelativeLayout v= (RelativeLayout) findViewById(R.id.content_my_diary);
        v.setOnTouchListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
/*
        Toast.makeText(getApplicationContext() , "Начало диеты "+ df.format(beginDay.getTime()) ,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext() , "ID диеты "+ Diet.getCurrentDietId(getApplicationContext()) ,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext() , "День диеты "+ day ,Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext() , "Час диеты "+ hours ,Toast.LENGTH_SHORT).show();
*/

        String formattedDate = getDate(1);
        header = (TextView) findViewById(R.id.headerDay);
        header.setText(String.format(getString(R.string.headerDay), formattedDate,day));
        listNutrition = (ListView)findViewById(R.id.listNutrition);
        initList(day);
        listNutrition.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                fromPositionX = event.getX();
                fromPositionY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float toPositionX = event.getX();
                float toPositionY = event.getY();
                if( (Math.abs(fromPositionY - toPositionY)<300) && (Math.abs(fromPositionX - toPositionX)>400) ) {
                    if (fromPositionX > toPositionX) {
                        if (swipe_day+1 > 0 && swipe_day+1 <= (Diet.getDietById(Diet.getCurrentDietId(getApplicationContext()), getApplicationContext())).getLength()) {
                            swipe_day++;
                            String formattedDate = getDate(Math.abs(swipe_day-Diet.getCurrentDietDay(getApplicationContext())));
                            header.setText(String.format(getString(R.string.headerDay), formattedDate, swipe_day));
                            initList(swipe_day);
                        } else {
                            Toast.makeText(getApplicationContext(), "Конец диеты.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (fromPositionX < toPositionX) {
                        if (swipe_day-1 > 0 && swipe_day-1 <= (Diet.getDietById(Diet.getCurrentDietId(getApplicationContext()), getApplicationContext())).getLength()) {
                            swipe_day--;
                            String formattedDate = getDate(-Math.abs(swipe_day-Diet.getCurrentDietDay(getApplicationContext())));
                            header.setText(String.format(getString(R.string.headerDay), formattedDate, swipe_day));
                            initList(swipe_day);
                        } else {
                            Toast.makeText(getApplicationContext(), "Начало диеты.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            default:
                break;
        }
        return false;
    }

    private void initList(long day) {
        ArrayList<Nutrition> nutritions = Nutrition.getNutritionsByDay(day, getApplicationContext());
        NutritionAdapter adapterNutrition = new NutritionAdapter(this, nutritions);
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

    private String getDate(long countDays) {
        if (countDays==0){
            countDays = 1;//сегодня
        }
        Date today = new Date(System.currentTimeMillis() + ((24 * 60 * 60 * 1000)*countDays));
        SimpleDateFormat df = new SimpleDateFormat("dd.MM");
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return df.format(today.getTime());
    }

}
