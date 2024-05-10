package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

        binding.btnCriarConta.setOnClickListener(v -> {
            controllerBancoDados.open();

            String  nome = binding.hintTxtRegisterNome.getText().toString().trim();
            String email = binding.hintTxtRegisterEmail.getText().toString().trim();
            String saldo = binding.hintTxtRegisterSaldo.getText().toString().trim();

            if(!nome.isEmpty() && !email.isEmpty() && !saldo.isEmpty() && util.isValidEmail(email) && !controllerBancoDados.isEmailInDatabase(email) ){

                double saldoDouble = Double.parseDouble(saldo);
                double chequeEspecial = saldoDouble * 4;

                try {
                    long id = controllerBancoDados.insertData(nome, email, saldoDouble, chequeEspecial, chequeEspecial);
                    intent.putExtra("nome", nome);
                    intent.putExtra("email", email);
                    intent.putExtra("saldo", saldoDouble);
                    intent.putExtra("cheque", chequeEspecial);

                    Toast.makeText(getApplicationContext(), "Conta criada com sucesso. Seu ID é: " + id, Toast.LENGTH_SHORT).show();
                    Log.e("Confirmação registro", ("as informações foram adicionadas com sucesso! elas são: " + nome + email + saldoDouble + chequeEspecial )  );

                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    startActivity(intent);
                    finish();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Preencha corretamente todos os campos", Toast.LENGTH_LONG).show();
            }
        });
    }
}
