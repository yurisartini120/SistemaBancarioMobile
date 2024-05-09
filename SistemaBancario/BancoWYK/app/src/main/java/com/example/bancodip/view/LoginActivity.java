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

        controllerBancoDados = new ControllerBancoDados(this);

        binding.btnCriarContaLogin.setOnClickListener(v -> {
            startActivity(intentRegister);
        });

        binding.btnEntrarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String nome = binding.hintTxtNomeLogin.getText().toString().trim();
            String idString = binding.hintTxtId.getText().toString().trim();


            if (nome.isEmpty() || idString.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, insira o nome e o ID", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                int id = Integer.parseInt(idString);

                if (controllerBancoDados.isNomeInDatabase(nome) && controllerBancoDados.getIdByNome(nome) == id) {
                    Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                    intentMain.putExtra("nome", nome);
                    intentMain.putExtra("id", id);
                    startActivity(intentMain);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Nome ou ID inválido", Toast.LENGTH_LONG).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getApplicationContext(), "ID inválido", Toast.LENGTH_LONG).show();
            } finally {
                controllerBancoDados.close();
            }
        });

    }
}
