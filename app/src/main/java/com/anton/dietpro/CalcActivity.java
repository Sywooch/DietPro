package com.anton.dietpro;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anton.dietpro.models.CalcCalories;

public class CalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    public void actionCalc(View v)
    {

        String age = ((EditText)findViewById(R.id.editText6)).getText().toString();
        String weight = ((EditText)findViewById(R.id.editText5)).getText().toString();
        String height = ((EditText)findViewById(R.id.editText11)).getText().toString();
        if (age.isEmpty()){
            return;
        }
        if (weight.isEmpty()){
            return;
        }
        if (height.isEmpty()){
            return;
        }
        int x = Integer.parseInt(age);
        int y = Integer.parseInt(weight);
        int z = Integer.parseInt(height);
        CalcCalories calc = new CalcCalories(x,y,z);
        calc.setAge(x);
        calc.setWeight(y);
        calc.setHeight(z);
        double cal = calc.calcMifflin();
        TextView textResult = ((TextView)findViewById(R.id.textResult));
        textResult.setText(String.valueOf("Ваша дневная норма БЖУ: " + cal));

    }

}
