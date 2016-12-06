package com.anton.dietpro.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
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
import java.util.List;
import java.util.TimeZone;

public class DietActivity extends AppCompatActivity {

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (ListView) findViewById(R.id.listViewDiet);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
            doSearch(query);
            //doMySearch(query);
        }else {
            ArrayList<Diet> diets = Diet.getDietList(getApplicationContext());
            DietAdapter adapterDiet = new DietAdapter(this, diets);
            listView.setAdapter(adapterDiet);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),DietDetailsActivity.class);
                intent.putExtra("dietId",id + "");
                startActivityForResult(intent,1);
            }
        });


    }

    private void doSearch(String query) {
        ArrayList<Diet> diets = Diet.getSearchDietList(getApplicationContext(),query);
        DietAdapter adapter = new DietAdapter(getApplicationContext(),diets);
        listView.setAdapter(adapter);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        String dietId = data.getStringExtra("dietId");

        Date date = new Date();
        java.text.DateFormat dateFormat = DateFormat.getDateFormat(getApplicationContext());
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));
        //очищаем график приемов пищи по текущей диете
        Diet.clearInfoDiet(getApplicationContext());
        //сохраняем ИД выбранной диеты в преференцес( но мб переделаем на БД )
        Diet.setCurrentDietId(getApplicationContext(), Integer.valueOf(dietId));
        Diet.setCurrentDietDate(getApplicationContext(), date);
        Toast.makeText(this, "Вы сели на диету " + dietId, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Дата " + dateFormat.format(date), Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        //searchView.setOnSearchClickListener();
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
        if (id == R.id.action_search){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
