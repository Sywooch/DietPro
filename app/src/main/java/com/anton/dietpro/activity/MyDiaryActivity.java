package com.anton.dietpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.adapter.NutritionAdapter;
import com.anton.dietpro.models.Diary;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.Nutrition;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyDiaryActivity extends AppCompatActivity implements View.OnTouchListener {
    private long swipe_day = 0; //хранит день диеты при свайпах
    private TextView header;
    private TextView headerCallories;
    private float fromPositionX;
    private float fromPositionY;
    private ListView listNutrition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        long day = Diet.getCurrentDietDay(getApplicationContext());
        this.swipe_day = day;
        if ((day<1) && (Diet.getCurrentDietId(getApplicationContext())<1) ){
            Intent backIntent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(backIntent);
        }
        setContentView(R.layout.activity_my_diary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        String formattedDate = getDate(1);
        this.header = (TextView) findViewById(R.id.headerDay);
        this.headerCallories = (TextView) findViewById(R.id.headerDayCallories);
        this.header.setText(String.format(getString(R.string.headerDay), formattedDate,day));
        this.headerCallories.setText(String.format(getString(R.string.headerDayCallories), Diary.getCurrentCallories(day,this),Nutrition.getAmountCallories(day,this)));

        this.listNutrition = (ListView)findViewById(R.id.listNutrition);
        initList(day);

        RelativeLayout v= (RelativeLayout) findViewById(R.id.content_my_diary);
        this.listNutrition.setOnTouchListener(this);

    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        String formattedDate = getDate(1);
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                this.fromPositionX = event.getX();
                this.fromPositionY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                float toPositionX = event.getX();
                float toPositionY = event.getY();
                if( (Math.abs(this.fromPositionY - toPositionY)<300) && (Math.abs(this.fromPositionX - toPositionX)>400) ) {
                    if (this.fromPositionX > toPositionX) {
                        if (this.swipe_day+1 > 0 && this.swipe_day+1 <= (Diet.getDietById(Diet.getCurrentDietId(getApplicationContext()), getApplicationContext())).getLength()) {
                            this.swipe_day++;
                            formattedDate = getDate(Math.abs(this.swipe_day-Diet.getCurrentDietDay(getApplicationContext())));
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.endDiet), Toast.LENGTH_SHORT).show();
                        }
                    } else if (fromPositionX < toPositionX) {
                        if (this.swipe_day-1 > 0 && this.swipe_day-1 <= (Diet.getDietById(Diet.getCurrentDietId(getApplicationContext()), getApplicationContext())).getLength()) {
                            this.swipe_day--;
                            formattedDate = getDate(-Math.abs(this.swipe_day-Diet.getCurrentDietDay(getApplicationContext())));
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.beginDiet), Toast.LENGTH_SHORT).show();
                        }
                    }
                    this.header.setText(String.format(getString(R.string.headerDay), formattedDate, this.swipe_day));
                    this.headerCallories.setText(String.format(getString(R.string.headerDayCallories), Diary.getCurrentCallories(this.swipe_day,this),Nutrition.getAmountCallories(this.swipe_day,this)));
                    initList(this.swipe_day);
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
        this.listNutrition.setAdapter(adapterNutrition);
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
        SimpleDateFormat df = new SimpleDateFormat("dd.MM",new Locale("ru","RU"));
        df.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        return df.format(today.getTime());
    }

}
