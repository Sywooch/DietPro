package com.anton.dietpro.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.activity.DietActivity;
import com.anton.dietpro.models.Diet;
import com.anton.dietpro.models.DietDB;

public class DietDetailsActivity extends AppCompatActivity {
    private Intent intent = null;
    private TextView dietName;
    private TextView dietLength;
    private TextView dietDescription;
    private long dietId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dietName = (TextView) findViewById(R.id.dietName);
        dietLength = (TextView) findViewById(R.id.dietLength);
        dietDescription = (TextView) findViewById(R.id.dietDescription);
        intent = getIntent();
        if (intent.getExtras() != null) {
            dietId = Integer.valueOf(intent.getExtras().getString("dietId"));
            this.initView(dietId);
        }
        else {
            Toast.makeText(this, "Произошла ошибка. Диета не найдена", Toast.LENGTH_SHORT).show();
        }

    }

    private void initView(long id){
        if (id == 0){
            dietName.setText("Диета не найдена");
            dietLength.setText("");
            dietDescription.setText("");
            ((Button)findViewById(R.id.acceptDiet)).setVisibility(View.GONE);
        }
        else{
            Diet diet = Diet.getDietById(id, getApplicationContext());
            dietName.setText(diet.getName());
            dietLength.setText("Подолжительность " + diet.getLength() + "дней.");
            dietDescription.setText(Html.fromHtml(diet.getDescription()));
        }
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
            Intent intent = new Intent(this,DietActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void acceptDiet(View view){
        Intent intent = new Intent();
        intent.putExtra("dietId", dietId + "");
        setResult(RESULT_OK, intent);
        finish();
    }
}
