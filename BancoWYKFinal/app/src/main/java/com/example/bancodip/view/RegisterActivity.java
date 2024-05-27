package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.controller.Util;
import com.example.bancodip.databinding.ActivityRegisterBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ControllerBancoDados controllerBancoDados;
    private Util util;

    private DatabaseReference referencia = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        controllerBancoDados = new ControllerBancoDados(this);
        util = new Util();
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

        binding.btnCriarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String  nome = binding.hintTxtRegisterNome.getText().toString().trim();
            String email = binding.hintTxtRegisterEmail.getText().toString().trim();
            String saldo = binding.hintTxtRegisterSaldo.getText().toString().trim();

            int ativo = 1;
            float saldoreal = Float.parseFloat(saldo);

            float cheque_especial = saldoreal*4;

            int conta = 6;

            String emailreal = email;

            int idade = 49;

            String nomereal = nome;

            int tipo = 1;

            if(!nome.isEmpty() && !email.isEmpty() && !saldo.isEmpty() && util.isValidEmail(email) && !controllerBancoDados.isEmailInDatabase(email) ){

                double saldoDouble = Double.parseDouble(saldo);
                double chequeEspecial = saldoDouble * 4;

                try {
                    controllerBancoDados.insertData(nome, email, saldoDouble, chequeEspecial, chequeEspecial);
                    intent.putExtra("nome", nome);
                    intent.putExtra("email", email);
                    intent.putExtra("saldo", saldoDouble);
                    intent.putExtra("cheque", chequeEspecial);

                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
            }





        });




    }
}