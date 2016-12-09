package com.anton.dietpro.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.anton.dietpro.R;
import com.anton.dietpro.models.UserData;
import com.google.gson.Gson;

public class MyDataActivity extends AppCompatActivity {

    private EditText editName;
    private EditText editAge;
    private EditText editHeight;
    private EditText editWeight;
    private EditText editTastePreferences;
    private RadioGroup radioGroupSex;
    private RadioGroup radioGroupBodyType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editName = (EditText) findViewById(R.id.editName);
        editAge = (EditText) findViewById(R.id.editAge);
        editHeight = (EditText) findViewById(R.id.editHeight);
        editWeight = (EditText) findViewById(R.id.editWeight);
        editTastePreferences = (EditText) findViewById(R.id.editTastePreferences);
        radioGroupSex = (RadioGroup) findViewById(R.id.radioGroupSex);
        radioGroupBodyType = (RadioGroup) findViewById(R.id.radioGroupBodyType);

        readPreferences();
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

    private void saveData(){
        int radioSex = radioGroupSex.getCheckedRadioButtonId();
        int radioBodyType = radioGroupBodyType.getCheckedRadioButtonId();
        UserData user = UserData.readPref(getApplicationContext());
        if (radioSex > 0){
            switch (radioSex){
                case R.id.radioSexMale:
                    user.setSex(UserData.SEX_MALE);
                    break;
                case R.id.radioSexFemale:
                    user.setSex(UserData.SEX_FEMALE);
                    break;
                default:
                    break;
            }
        }
        if (radioBodyType > 0){
            switch (radioBodyType){
                case R.id.radioBodyTypeEct:
                    user.setBodyType(UserData.BODY_TYPE_ECTOMORPF);
                    break;
                case R.id.radioBodyTypeMeso:
                    user.setBodyType(UserData.BODY_TYPE_MESOMORPH);
                    break;
                case R.id.radioBodyTypeEndo:
                    user.setBodyType(UserData.BODY_TYPE_ENDOMORPH);
                    break;
                default:
                    break;
            }
        }
        String myName = editName.getText().toString();
        String myTastePreferences = editTastePreferences.getText().toString();

        Integer myAge = 0;
        if (!editAge.getText().toString().isEmpty()){
            myAge = Integer.valueOf(editAge.getText().toString());
        }
        Float myWeight = 0f;
        if(!editWeight.getText().toString().isEmpty()) {
            myWeight = Float.valueOf(editWeight.getText().toString());
        }
        Float myHeight = 0f;
        if (!editWeight.getText().toString().isEmpty()){
           myHeight = Float.valueOf(editHeight.getText().toString());
        }

        if (!myName.isEmpty()) {
            user.setName(myName);
        }
        if (!myTastePreferences.isEmpty()) {
            user.setTastePreferences(myTastePreferences);
        }
        if (myAge != null) {
            user.setAge(myAge);
        }
        if(myWeight != null) {
            user.setWeight(myWeight);
        }
        if(myHeight != null) {
            user.setHeight(myHeight);
        }
        user.savePref(getApplicationContext());
    }

    private void readPreferences() {
        UserData user = UserData.readPref(getApplicationContext());
        if (user == null){
            return;
        }
        if (user.getSex() != null) {
            switch (user.getSex()) {
                case 1:
                    ((RadioButton) findViewById(R.id.radioSexMale)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton) findViewById(R.id.radioSexFemale)).setChecked(true);
                    break;
                default:
                    break;
            }
        }
        if(user.getBodyType() != null) {
            switch (user.getBodyType()) {
                case 1:
                    ((RadioButton) findViewById(R.id.radioBodyTypeEct)).setChecked(true);
                    break;
                case 2:
                    ((RadioButton) findViewById(R.id.radioBodyTypeMeso)).setChecked(true);
                    break;
                case 3:
                    ((RadioButton) findViewById(R.id.radioBodyTypeEndo)).setChecked(true);
                    break;
                default:
                    break;
            }
        }
        if (!user.getName().equals("")){
            editName.setText(user.getName());
        }
        if (!user.getTastePreferences().equals("")){
            editTastePreferences.setText(user.getTastePreferences());
        }
        if (user.getAge() > 0){
            editAge.setText(String.valueOf(user.getAge()));
        }
        if (user.getWeight() > 0){
            editWeight.setText(String.valueOf(user.getWeight()));
        }
        if (user.getHeight() > 0){
            editHeight.setText(String.valueOf(user.getHeight()));
        }
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

    public void onClickSave(View v){
        if(radioGroupSex.getCheckedRadioButtonId()<0) {
            Toast.makeText(this, "Не выбран пол", Toast.LENGTH_SHORT).show();
        }
        if(radioGroupBodyType.getCheckedRadioButtonId()<0) {
            Toast.makeText(this, "Не выбран тип телосложения", Toast.LENGTH_SHORT).show();
        }
        saveData();
        Toast.makeText(this, "Данные сохранены", Toast.LENGTH_SHORT).show();
    }

}
