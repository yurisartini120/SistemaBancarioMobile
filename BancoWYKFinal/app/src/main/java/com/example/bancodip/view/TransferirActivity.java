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
        Double chequeEspecialUser = controllerBancoDados.getChequeByTitular(emailUser);

        binding.btnVoltar.setOnClickListener(v -> finish());

        binding.btnTransferirUser.setOnClickListener(v -> {
            String destinatarioEmail = binding.transUserEmail.getText().toString();
            Double destinatarioSaldo = controllerBancoDados.getSaldoByTitular(destinatarioEmail);
            String valorUser = binding.transUserValor.getText().toString();

            if(controllerBancoDados.isEmailInDatabase(destinatarioEmail) && saldoUser > 0){
                try {
                    Double valorTransferencia = Double.parseDouble(valorUser);
                    Double saldoUserNew = saldoUser - valorTransferencia;
                    Double saldoDestinatarioNew = destinatarioSaldo + valorTransferencia;

                    // Atualiza os saldos do usuário e do destinatário
                    controllerBancoDados.updateSaldo(destinatarioEmail, saldoDestinatarioNew);
                    controllerBancoDados.updateSaldo(emailUser, saldoUserNew);

                    // Atualiza o cheque especial se o saldo do usuário for negativo
                    if (saldoUserNew < 0) {
                        Double novoChequeEspecial = chequeEspecialUser + saldoUserNew;
                        controllerBancoDados.updateCheque(emailUser, novoChequeEspecial);
                    }

                } catch (Exception e){
                    e.printStackTrace();
                } finally {

                    binding.transUserValor.setText("");
                    binding.transUserEmail.setText("");

                    Toast.makeText(getApplicationContext(), "Transferencia executada com sucesso", Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Saldo insuficiente ou email inválido", Toast.LENGTH_LONG).show();
            }
        });
    }
}
