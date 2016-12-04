package com.anton.dietpro.models;

import android.os.AsyncTask;
import java.lang.reflect.Type;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by admin on 04.11.16.
 * Класс для работы с API
 * Исправляет орфографические ошибки в словах
 */

public class API {
    private final String URL_API = "http://speller.yandex.net";
    private Gson gson = new GsonBuilder().create();
    public API() {

    }

    public String doCorrection(String text){
        try {
            String response = new MyRetroTask().execute(text).get();
            return response;
        }
        catch (Exception e){}
        return "Пусто";
    }

    class MyRetroTask extends AsyncTask<String,String,String>{
        @Override
        protected String doInBackground(String... strings) {

            String myString = strings[0];
            Retrofit retr = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(URL_API)
                    .build();
            ApiYandex apiYandex = retr.create(ApiYandex.class);
            Map<String, String> mapXml = new HashMap<String, String>();
            mapXml.put("text",myString);
            mapXml.put("lang","ru");
            Call<Object> call = apiYandex.translate(mapXml);
            try {
                Response<Object> response = call.execute();
                Type listType = new TypeToken<List<SpellResult>>() {}.getType();
                List<SpellResult> responseList = gson.fromJson(response.body().toString(),listType);
                for(int i = 0; i<responseList.size() ;i++) {
                    myString = myString.replaceFirst(responseList.get(i).getWord(),responseList.get(i).getS()[0]);
                }
                Log.d("RESPONSE", "Было: " + strings[0]);
                Log.d("RESPONSE", "Стало: " + myString);
                return myString;

            }catch (Exception ex){
                Log.d("RESPONSE" , "Ошибка " + ex);
            }
            return null;
        }

    }

}
