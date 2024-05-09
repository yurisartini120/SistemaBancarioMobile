package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ControllerBancoDados controllerBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);

        controllerBancoDados = new ControllerBancoDados(this);

        binding.btnCriarContaLogin.setOnClickListener(v ->{
            startActivity(intentRegister);
        });

        binding.btnEntrarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String nome = binding.hintTxtNomeLogin.getText().toString().trim().toUpperCase();
            String email = binding.hintTxtId.getText().toString().trim().toUpperCase();

            long id = controllerBancoDados.getIdByNameAndEmail(nome, email);
            if (id != -1) {
                try {
                    intentMain.putExtra("nome", nome);
                    intentMain.putExtra("email", email);
                    intentMain.putExtra("id", id);
                    startActivity(intentMain);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Nome ou email inválido", Toast.LENGTH_LONG).show();
            }
        });
    }
}