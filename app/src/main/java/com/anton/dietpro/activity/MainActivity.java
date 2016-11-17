package com.anton.dietpro.activity;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.annotation.NonNull;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.anton.dietpro.R;
import com.anton.dietpro.activity.CalcActivity;
import com.anton.dietpro.activity.DietActivity;
import com.anton.dietpro.models.DietDB;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DietDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new DietDB(this);
        db.create_db();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return (id == R.id.action_settings) || super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showCalc(View view)
    {
        Intent intent = new Intent(this,CalcActivity.class);
        startActivity(intent);
    }
    public void showMyData(MenuItem item){
        Intent intent = new Intent(this, MyDataActivity.class);
        startActivity(intent);
    }
    public void showMyDiary(MenuItem item){
        Intent intent = new Intent(this, MyDiaryActivity.class);
        startActivity(intent);
    }
    public void showCalc(MenuItem item){
        Intent intent = new Intent(this,CalcActivity.class);
        startActivity(intent);
    }

    public void showDiet(MenuItem item){
        Intent intent = new Intent(this,DietActivity.class);
        startActivity(intent);
    }
    public void showDiet(View v){
        Intent intent = new Intent(this,DietActivity.class);
        startActivity(intent);
    }
    public void showProduct(View v){
        Intent intent = new Intent(this,ProductActivity.class);
        startActivity(intent);
    }

}
