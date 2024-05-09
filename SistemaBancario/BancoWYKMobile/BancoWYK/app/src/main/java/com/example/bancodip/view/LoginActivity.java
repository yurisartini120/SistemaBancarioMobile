package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.R;
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

        controllerBancoDados = new ControllerBancoDados(this);

        Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
        Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);

        binding.btnCriarContaLogin.setOnClickListener(v -> {
            startActivity(intentRegister);
        });

        // Dentro do método onCreate da LoginActivity

        binding.btnEntrarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String numeroConta = binding.hintTxtNumeroConta.getText().toString().trim();

            if (controllerBancoDados.isNumeroContaInDatabase(numeroConta)) {
                String nome = controllerBancoDados.getNomeByNumeroConta(numeroConta);
                String email = controllerBancoDados.getEmailByNumeroConta(numeroConta);

                // Faça algo com o nome e o email obtidos, como iniciar a MainActivity
            } else {
                Toast.makeText(getApplicationContext(), "Número de conta inválido", Toast.LENGTH_SHORT).show();
            }

            controllerBancoDados.close();
        });

    }
}
