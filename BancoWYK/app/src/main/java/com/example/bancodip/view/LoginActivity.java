package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

        binding.btnCriarContaLogin.setOnClickListener(v -> {
            startActivity(intentRegister);
        });

        binding.btnEntrarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String nome = binding.hintTxtNomeLogin.getText().toString().trim();
            String numeroContaString = binding.editTextNumeroConta.getText().toString().trim();

            if (nome.isEmpty() || numeroContaString.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int numeroConta = Integer.parseInt(numeroContaString);
                    if (controllerBancoDados.isNomeAndNumeroContaInDatabase(nome, numeroConta)) {
                        intentMain.putExtra("nome", nome);
                        intentMain.putExtra("numeroConta", numeroConta);
                        startActivity(intentMain);
                        finish();
                    } else {
                        Log.e("Informações login", "Os dados são: " + nome + " " + numeroConta);
                        Toast.makeText(getApplicationContext(), "Erro ao fazer login. Verifique suas credenciais.", Toast.LENGTH_LONG).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Número da conta inválido.", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }
}
