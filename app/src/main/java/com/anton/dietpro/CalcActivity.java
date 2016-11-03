package com.anton.dietpro;

import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CalcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
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
        Toast toast = Toast.makeText(getApplicationContext(),"Ваша БЖУ = ",Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

}
