package com.anton.dietpro.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.anton.dietpro.R;
import com.anton.dietpro.models.CalcCalories;
import com.anton.dietpro.models.UserData;

public class CalcActivity extends AppCompatActivity {

    private EditText editAge;
    private EditText editWeight;
    private EditText editHeight;
    private TextView textResult;
    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        editAge = ((EditText)findViewById(R.id.editText6));
        editWeight = ((EditText)findViewById(R.id.editText5));
        editHeight = ((EditText)findViewById(R.id.editText11));
        textResult = ((TextView)findViewById(R.id.textResult));

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();


        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setIndicator(getString(R.string.labelLoseWeight));
        tabSpec.setContent(R.id.tab1);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setIndicator(getString(R.string.labelUpWeight));
        tabSpec.setContent(R.id.tab2);
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("tag3");
        tabSpec.setIndicator(getString(R.string.labelIMT));
        tabSpec.setContent(R.id.tab3);
        tabHost.addTab(tabSpec);
        tabHost.setCurrentTabByTag("tag1");

        TabWidget tw = (TabWidget)tabHost.findViewById(android.R.id.tabs);
        int n = tw.getChildCount();
        for (int i = 0; i < n; i++) {
            View tabView = tw.getChildTabViewAt(i);
            TextView tv = (TextView) tabView.findViewById(android.R.id.title);
            tv.setTextSize(13);
        }

        // обработчик переключения вкладок
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            public void onTabChanged(String tabId) {
                textResult.setText("");//Toast.makeText(getApplicationContext(), "tabId = " + tabId, Toast.LENGTH_SHORT).show();
            }
        });

        readPreferences();
    }

    private void readPreferences() {

        UserData user = UserData.readPref(getApplicationContext());
        if (user != null) {
            FrameLayout tabContent = (FrameLayout)((LinearLayout)tabHost.getChildAt(0)).getChildAt(1);// (FrameLayout)findViewById(R.id.tabcontent));
            int n = tabContent.getChildCount();
            //Toast.makeText(getApplicationContext(),"n="+n,Toast.LENGTH_LONG).show();


            for(int i =0;i<n;i++) {
                View v = tabContent.getChildAt(i);
                if (user.getAge() > 0) {
                    editAge = (EditText) v.findViewById(R.id.editText6);
                    editAge.setText(String.valueOf(user.getAge()));
                }
                if (user.getWeight() > 0) {
                    editWeight = ((EditText)v.findViewById(R.id.editText5));
                    editWeight.setText(String.valueOf(user.getWeight()));
                }
                if (user.getHeight() > 0) {
                    editHeight = ((EditText)v.findViewById(R.id.editText11));
                    editHeight.setText(String.valueOf(user.getHeight()));
                }
            }

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
            saveData();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actionCalc(View v)
    {
        int id = v.getId();
        v = (View)v.getParent();
        editAge = ((EditText)v.findViewById(R.id.editText6));
        editWeight = ((EditText)v.findViewById(R.id.editText5));
        editHeight = ((EditText)v.findViewById(R.id.editText11));
        saveData();
        String age = editAge.getText().toString();
        String weight = editWeight.getText().toString();
        String height = editHeight.getText().toString();
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
        Float y = Float.parseFloat(weight);
        Float z = Float.parseFloat(height);
        CalcCalories calc = new CalcCalories(x,y,z);
        double cal = 0;

        switch (id){
            case R.id.button:
                cal = calc.calcMifflin();
                textResult.setText(String.format(getString(R.string.PFCnorm),cal)); //String.valueOf("Ваша дневная норма БЖУ: " + Math.round(cal) + " ккал."));
                break;
            case R.id.button2:
                cal = calc.calcMass();
                textResult.setText(String.format(getString(R.string.PFCnorm),cal));
                break;
            case R.id.button3:
                cal = calc.calcIMT();
                int resIMT = calc.calcIMTRes();
                String res = String.format(getString(R.string.IMT),cal);
                switch (resIMT){
                    case 0:
                        res += " " + getString(R.string.IMTlower);
                        break;
                    case 1:
                        res += " " + getString(R.string.IMTnorm);
                        break;
                    case 2:
                        res += " " + getString(R.string.IMTupper);
                        break;
                    default:
                        break;
                }

                textResult.setText(res);
                break;
            default:
                break;
        }
        saveData();
        readPreferences();

    }

    private void saveData(){
        Integer myAge = 0;
        Float myWeight = 0f;
        Float myHeight = 0f;

         UserData user = UserData.readPref(getApplicationContext());
        if (editAge.getText().length() > 0) {
            myAge = Integer.valueOf(editAge.getText().toString());
        }
        if (editWeight.getText().length() > 0) {
            myWeight = Float.valueOf(editWeight.getText().toString());
        }
        if (editHeight.getText().length() > 0) {
            myHeight = Float.valueOf(editHeight.getText().toString());
        }
        user.setAge(myAge);
        user.setWeight(myWeight);
        user.setHeight(myHeight);
        user.savePref(getApplicationContext());
    }

    @Override
    protected void onPause() {
        saveData();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
