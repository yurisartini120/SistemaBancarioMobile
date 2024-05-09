package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityTransferirBinding;

public class TransferirActivity extends AppCompatActivity {

    private ActivityTransferirBinding binding;
    private ControllerBancoDados controllerBancoDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);

        controllerBancoDados.open();

        Intent intent = getIntent();
        String emailUser = intent.getStringExtra("email_trans");
        Double saldoUser = controllerBancoDados.getSaldoByTitular(emailUser);

        binding.btnTransferirUser.setOnClickListener(v -> {

            String destinatarioEmail = binding.transUserEmail.getText().toString();
            Double destinatarioSaldo = controllerBancoDados.getSaldoByTitular(destinatarioEmail);
            String valorUser = binding.transUserValor.getText().toString();

            if(controllerBancoDados.isEmailInDatabase(destinatarioEmail) && saldoUser > 0){
                try {

                    Double saldoUserNew = saldoUser - Double.parseDouble(valorUser);
                    Double saldoDestinatarioNew = destinatarioSaldo + Double.parseDouble(valorUser);

                    controllerBancoDados.updateSaldo(destinatarioEmail, saldoDestinatarioNew);
                    controllerBancoDados.updateSaldo(emailUser, saldoUserNew);

                    Toast.makeText(getApplicationContext(),"Transferência executada com sucesso", Toast.LENGTH_SHORT).show();

                } catch (Exception e){
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.transUserValor.setText("");
                    binding.transUserEmail.setText("");
                }
            } else {
                Toast.makeText(getApplicationContext(),"Saldo insuficiente ou email inválido", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnVoltar.setOnClickListener(v -> {
            finish();
        });
    }
}
