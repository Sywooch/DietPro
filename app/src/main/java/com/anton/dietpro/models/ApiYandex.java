package com.anton.dietpro.models;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by admin on 01.12.16.
 */

public interface ApiYandex {

    @FormUrlEncoded
    @POST("/services/spellservice.json/checkText")
    Call<Object> translate(@FieldMap Map<String,String> map);

}
