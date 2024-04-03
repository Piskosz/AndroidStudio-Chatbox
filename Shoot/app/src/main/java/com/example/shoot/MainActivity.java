package com.example.shoot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        editText = findViewById(R.id.login);

        SharedPreferences sharedPreferences=getSharedPreferences("shared preferences",MODE_PRIVATE);
        String message=sharedPreferences.getString("KEY",null);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isOnline()==true){
                    editor.putString("KEY", editText.getText().toString());
                    editor.apply();
                    openActivity2();
                    Toast.makeText(MainActivity.this, "Welcome "+editText.getText().toString(), Toast.LENGTH_SHORT).show();
                }

                else{

                }

            }
        });

    }
    public void openActivity2(){

        Intent intent = new Intent(this,MainActivity2.class);
        startActivity(intent);

    }


    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;

        } else {
            Context context = getApplicationContext();
            CharSequence text = "Nie ma połączenia z internetem!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            return false;
        }
    }




}