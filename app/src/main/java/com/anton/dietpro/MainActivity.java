package com.anton.dietpro;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void initDB()
    {
        List<Diet> diets = new ArrayList<Diet>();
        Diet diet = new Diet("Белковая диета",14,"Похудение до 10 кг за 14 дней.\n" +
                "Среднесуточная калорийность 700 Ккал.\n" +
                "Белковая диета по праву является и считается одной из самых эффективных и результативных систем питания - диет для снижения веса. Эта популярная диета рассчитана на активный образ жизни. Свою результативность белковая диета лучше всего показывает при дополнительных тренировках в тренажерном зале фитнесом, аэробикой, шейпингом и т.п. как минимум 3 раза в неделю. Кроме того белковая диета на 14 дней предполагает не менее 6 приемов пищи в день.\n" +
                "Меню белковой диеты полностью исключает все продукты с высоким содержанием углеводов и жестко ограничивает количество жиров. Эти продукты с высоким содержанием белка в меню преобладают, наряду с овощами и фруктами, являющимися источниками клетчатки, минеральных комплексов и основных витаминов.\n" +
                "Белковая диета представлена на vse-diety.com двумя вариантами меню: на 7 дней и на 14 дней. Эффективность и средняя калорийность этих меню полностью идентична, разница лишь в продолжительности диеты.");
        diets.add(diet);

        diet = new Diet("Кето диета",14,"Кетоновая диета была изобретена для снижения массы тела. Можно сказать, что похудение является побочным эффектом ее применения. Ее целью является вызывание кетоза в организме. Это важно для детей и молодежи с эпилепсией и редкими заболеваниями, с метаболическими и генетическими факторами в ситуации, когда медикаментозное лечение не приносит эффекта. Однако такой тип питания стал модным для похудения у мужчин и женщин.\n" +
                "Подобная диета - это потребление жирной пищи с низким содержанием углеводов. В ежедневном рационе потребляется около 35% жиров, 50 % углеводов и 15 % белков. В рационе кетоновой диете жир может составлять 80-90% от потребляемой энергии, а белки углеводы - 10-20 %.\n" +
                "Основным источником энергии в организме являются углеводы. Когда их не хватает, организм начинает искать другое \"топливо\". Ими являются жиры, и, в частности, возникающие в процессе распада жиров кетоновые тела (так называемый кетоз). Однако они обеспечивают организму только 70 % необходимой энергии. После нескольких дней применения кето диеты человек входит в состояние эйфории (так определяют воздействие кетонов врачи) - у него отличное настроение, он веселый, испытывает чувство легкости. Через месяц другой все проходит. Появляются потеря аппетита, сонливость и запоры, запах пота и мочи, меняется дыхание, нарастает жажда.");
        diets.add(diet);

        diet = new Diet("БУЧ диета",14,"Основы БУЧ (белково-углеводное чередование) диеты\n" +
                "\n" +
                "Как видно из расшифровки названия, в данной диете основным принципом является чередование в рационе питания дней с преобладанием белковой или углеводистой пищи. Для чего это нужно?\n" +
                "\n" +
                "Чтобы заставить организм расставаться со своим неприкосновенным жировым запасом, необходимо в первую очередь расходовать весь гликоген (углеводы), содержащийся в мышцах и печени. Этого можно достичь путем снижения поступления углеводов с пищей. Но не все так просто. Ведь после того, как человек чувствует критический уровень их недостатка, организм начинает испытывать стресс и брать энергию, разрушая мышцы, дабы быстро получить энергию. И только после этого переходит к разрушению жировой ткани. Во избегание таких явлений нужно найти правильный баланс между окончанием углеводных запасов и их пополнением. Только в этом случае начинает использоваться энергия от распада жира.");
        diets.add(diet);

        diet = new Diet("Диета 10х10",14,"В последнее время у любительниц экстренного похудения пользуется популярностью диета под названием «10х10». За десять дней, если постараться, можно сбросить до десяти килограммов. Диета очень строгая, с полным исключением углеводов из рациона.");
        diets.add(diet);

        DietDB dbHelper = new DietDB(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        for(int i = 0;i<(diets.size());i++){
            cv.put("name", diets.get(i).getName());
            cv.put("length", diets.get(i).getLength());
            cv.put("description", diets.get(i).getDescription());
            Log.d("myLog","вставили диету");
            // вставляем запись и получаем ее ID
            long rowID = db.insert("diet", null, cv);
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void showCalc(View view)
    {
        Intent intent = new Intent(this,CalcActivity.class);
        startActivity(intent);
        //setContentView(R.layout.activity_calc);
    }
    public void showCalc(MenuItem item){
        Intent intent = new Intent(this,CalcActivity.class);
        startActivity(intent);
    }

    public void showDiet(MenuItem item){
        Intent intent = new Intent(this,DietActivity.class);
        startActivity(intent);
    }





}
