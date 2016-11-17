package com.anton.dietpro.models;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by admin on 17.11.16.
 */

public class UserData {
    public static final Integer SEX_MALE = 1;
    public static final Integer SEX_FEMALE = 2;
    public static final Integer BODY_TYPE_ECTOMORPF = 1;
    public static final Integer BODY_TYPE_MESOMORPH = 2;
    public static final Integer BODY_TYPE_ENDOMORPH = 3;
    public static final String MY_PREF = "MY_PREF";

    private String name;
    private String tastePreferences;
    private Integer age = 0;
    private Float height = 0.0f;
    private Float weight = 0.0f;
    private Integer sex = 0;
    private Integer bodyType = 0;

    public UserData() {
        this.name = "";
        this.tastePreferences = "";
        this.age = 0;
        this.height = 0.0f;
        this.weight = 0.0f;
        this.sex = 0;
        this.bodyType = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getBodyType() {
        return bodyType;
    }

    public void setBodyType(Integer bodyType) {
        this.bodyType = bodyType;
    }

    public String getTastePreferences() {
        return tastePreferences;
    }

    public void setTastePreferences(String tastePreferences) {
        this.tastePreferences = tastePreferences;
    }
    public void savePref(Context context){
        SharedPreferences sPref = context.getSharedPreferences(UserData.MY_PREF, context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        Gson gson = new Gson();
        String data = gson.toJson(this);
        ed.putString("myData", data);
        ed.apply();

    }
    public static UserData readPref(Context context){

        SharedPreferences shared = context.getSharedPreferences(UserData.MY_PREF, context.MODE_PRIVATE);

        String myData = shared.getString("myData", "");
        Gson gson = new Gson();
        UserData user = gson.fromJson(myData,UserData.class);
        return (user != null ? user : new UserData());
    }
}
