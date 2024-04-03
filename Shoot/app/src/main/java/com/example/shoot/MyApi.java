package com.example.shoot;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface MyApi {

    @GET("shoutbox/messages")
    Call<ArrayList<Model>>itemscall();

    @FormUrlEncoded
    @POST("shoutbox/message")
    Call<Model>itemspost(@Field("content")String content,@Field("login") String login);

    @PUT("shoutbox/message/{id}")
    Call<Model>itemsput(@Field("content")String content,@Field("login") String login);

    @DELETE("shoutbox/message/{id}")
    Call<Model>itemsdelete(@Path("id")String id);



}
