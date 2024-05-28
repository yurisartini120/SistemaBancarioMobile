package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    private static int SPLASH_TIME_OUT = 4000;

    private ControllerBancoDados controllerBancoDados;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        controllerBancoDados = new ControllerBancoDados(this);
        controllerBancoDados.open();



        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(i);
                controllerBancoDados.open();
                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}