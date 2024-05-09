package com.example.bancodip.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.controller.Util;
import com.example.bancodip.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ControllerBancoDados controllerBancoDados;
    private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);
        util = new Util();

        binding.btnCriarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String  nome = binding.hintTxtRegisterNome.getText().toString().trim();
            String Email = binding.hintTxtRegisterEmail.getText().toString().trim();
            String saldo = binding.hintTxtRegisterSaldo.getText().toString().trim();

            if(!nome.isEmpty() && !Email.isEmpty() && !saldo.isEmpty() && util.isValidEmail(Email)){

                double saldoDouble = Double.parseDouble(saldo);
                double chequeEspecial = saldoDouble * 4;

                try {
                    long idLong = controllerBancoDados.insertData(Email, nome, saldoDouble, chequeEspecial, chequeEspecial);
                    Toast.makeText(getApplicationContext(), "Conta criada com sucesso! O número da conta é: " + idLong, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("Email", Email);
                    intent.putExtra("nome", nome);
                    intent.putExtra("saldo", saldoDouble);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Preencha corretamente todos os campos", Toast.LENGTH_LONG).show();
            }
        });
    }
}