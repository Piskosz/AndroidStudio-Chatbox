package com.example.shoot;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity2 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManger;
    SwipeRefreshLayout swipe;
    boolean isAdded=false;
    private MyApi myApi;
    private List<String> Id_list;
    public ItemTouchHelper itemTouchHelper;
    ArrayList<Model> exampleList = new ArrayList<>();
    EditText editText;

    static String JSON_URL = "https://tgryl.pl/shoutbox/messages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        recyclerView = findViewById(R.id.Recycler);
        recyclerView.setHasFixedSize(true);
        mLayoutManger = new LinearLayoutManager(this);
        mAdapter = new Adapter(exampleList);
        swipe = findViewById(R.id.swipeToRefresh);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        EditText editText=findViewById(R.id.send_edit);
        Id_list=new ArrayList<String>();


        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        String message = sharedPreferences.getString("KEY",null);

        recyclerView.setLayoutManager(mLayoutManger);
        recyclerView.setAdapter(mAdapter);
        getdata();

        System.out.println(message);
        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipe.setRefreshing(false);


            }
        });

        findViewById(R.id.send_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSend(editText.getText().toString(),message);
                isAdded=true;
                getdata();
                refresh();
            }
        });
        System.out.println(isAdded);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(isAdded==true) {

            isAdded=false;
        }

        ItemTouchHelper.SimpleCallback Helpercallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               if(message.equals(exampleList.get(viewHolder.getAdapterPosition()).getTex1())){
                   delete_item(Id_list.get(viewHolder.getAdapterPosition()));
                   exampleList.remove(viewHolder.getAdapterPosition());
                   mAdapter.notifyDataSetChanged();
                   refresh();
               }
               else {
                   Toast.makeText(MainActivity2.this, "This is not your post!", Toast.LENGTH_SHORT).show();
                   refresh();

               }
            }
        };
        itemTouchHelper = new ItemTouchHelper(Helpercallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }

    public void getdata(){
        JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest(Request.Method.GET, JSON_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        Model model = new Model();
                        model.setmText1(jsonObject.getString("login").toString());
                        model.setmText2(jsonObject.getString("date").toString());
                        model.setmText3(jsonObject.getString("content").toString());
                        String tmp = jsonObject.getString("id").toString();
                        Id_list.add(tmp);
                        exampleList.add(model);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity2.this));
                recyclerView.setAdapter(mAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse:");

            }

        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity2.this);
        requestQueue.add(jsonArrayRequest1);

    }
    public void postSend(String content,String login){
        MyApi myApi = Retroit.getRetrofitInstance().create(MyApi.class);
        Call<Model>callPost=myApi.itemspost(content, login);
        callPost.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, retrofit2.Response<Model> response) {
                Log.e(TAG,"onResponse "+response.code());
                Log.e(TAG,"onResponse login "+response.body().getTex1());
                Log.e(TAG,"onResponse createdAt"+response.body().getTex2());
                Log.e(TAG,"onResponse content"+response.body().getTex3());
                Log.e(TAG,"onResponse id"+response.body().getId());
                Toast.makeText(MainActivity2.this, "Successfully added!", Toast.LENGTH_SHORT).show();
                refresh();
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG,"Failure");
            }
        });
    }
    public void refresh(){
        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);
    }
    public void delete_item(String id){
        MyApi myApi = Retroit.getRetrofitInstance().create(MyApi.class);
        Call<Model>delete= myApi.itemsdelete(id);
        delete.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, retrofit2.Response<Model> response) {

                Toast.makeText(MainActivity2.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<Model> call, Throwable t) {
                Log.e(TAG,"Failure");
            }
        });
    }
    public void shoutbox(MenuItem item) {
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        drawerLayout.closeDrawer(GravityCompat.START, false);
    }
    public void settings(MenuItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        finish();
    }
}