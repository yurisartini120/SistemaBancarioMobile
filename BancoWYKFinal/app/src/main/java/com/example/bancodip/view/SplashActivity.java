package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.bancodip.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {
    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    private static int SPLASH_TIME_OUT = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int ativo = 1;
        float saldo = 100.0F;
        float cheque_especial = saldo*4;
        int conta = 6;
        String email = "wesley@gmail.com";
        int idade = 49;
        String nome = "wesley";
        int tipo = 1;


        DatabaseReference referencia = FirebaseDatabase.getInstance().getReference().child("correntistas").child(String.valueOf(conta));
        referencia.child("ativo").setValue(ativo);
        referencia.child("cheque_Especial").setValue(cheque_especial);
        referencia.child("conta").setValue(conta);
        referencia.child("email").setValue(email);
        referencia.child("idade").setValue(idade);
        referencia.child("nome").setValue(nome);
        referencia.child("saldo").setValue(saldo);
        referencia.child("tipo").setValue(tipo);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,
                        LoginActivity.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);


    }
}