package com.example.bancodip.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.backup.BackupManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ControllerBancoDados controllerBancoDados;
    private static final int REQUEST_TRANSFERIR = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);

        Intent intentTrans = new Intent(MainActivity.this, TransferirActivity.class);
        Intent intent = getIntent();

        String nome = intent.getStringExtra("nome");
        String email = intent.getStringExtra("email");

        intentTrans.putExtra("email_trans", email);

        try {
            controllerBancoDados.open();

            Double saldoBanco = controllerBancoDados.getSaldoByTitular(email);
            Double chequeBanco = controllerBancoDados.getChequeByTitular(email);
            String saldoString = String.valueOf(saldoBanco);
            String chequeString = String.valueOf(chequeBanco);

            binding.saldoConta.setText("R$ " + saldoString);
            binding.chequeEspecialConta.setText(chequeString);

        } catch (Exception e){
            e.printStackTrace();
        } finally {
        }

        binding.btnDepositar.setOnClickListener(v -> {
            controllerBancoDados.open();

            String valorCliente = binding.hintUserValor.getText().toString();

            if(!valorCliente.isEmpty()){
                try {

                    Double cheque = controllerBancoDados.getChequeByTitular(email);
                    Double valorSaldo = controllerBancoDados.getSaldoByTitular(email);
                    Double CHEQUEESPECIAL = controllerBancoDados.getChequeDEFIByTitular(email);

                    Double novoSaldo = Double.parseDouble(valorCliente) + valorSaldo ;
                    Double novoCheque = cheque + Double.parseDouble(valorCliente);

                    controllerBancoDados.updateSaldo(email, novoSaldo);
                    binding.saldoConta.setText(String.valueOf(novoSaldo));

                    if(valorSaldo < 0 ){
                        controllerBancoDados.updateCheque(email, novoCheque);
                        binding.chequeEspecialConta.setText(String.valueOf(novoCheque));
                    }
                    if(novoSaldo >= 0 && cheque < CHEQUEESPECIAL){
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Você pagou o seu cheque especial com êxito!");
                        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // nada aqui
                            }
                        });

                        AlertDialog alerta = builder.create();
                        alerta.show();

                        controllerBancoDados.updateCheque(email, CHEQUEESPECIAL);
                        binding.chequeEspecialConta.setText(String.valueOf(CHEQUEESPECIAL));

                    }

                }catch (Exception e){
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.hintUserValor.setText("");
                }
            }

        });

        binding.btnSacar.setOnClickListener(v -> {
            controllerBancoDados.open();

            String valorCliente = binding.hintUserValor.getText().toString();

            if (!valorCliente.isEmpty()) {
                try {
                    Double saldo = controllerBancoDados.getSaldoByTitular(email);
                    Double cheque = controllerBancoDados.getChequeByTitular(email);
                    Double CHEQUEESPECIAL = controllerBancoDados.getChequeDEFIByTitular(email);

                    Double valorSaque = Double.parseDouble(valorCliente);

                    if (saldo >= valorSaque) {
                        // Se o saldo for suficiente, apenas atualize o saldo
                        Double novoSaldo = saldo - valorSaque;
                        controllerBancoDados.updateSaldo(email, novoSaldo);
                        binding.saldoConta.setText(String.valueOf(novoSaldo));
                    } else {
                        // Se o saldo não for suficiente, calcule o quanto do cheque especial será utilizado
                        Double saldoRestante = valorSaque - saldo;
                        Double novoSaldo = 0 - saldoRestante;
                        Double novoCheque = cheque - saldoRestante;

                        if (novoCheque < 0) {
                            // Se o cheque especial não for suficiente, atualize para zero
                            novoCheque = 0.0;
                            novoSaldo = saldo - (valorSaque - cheque);
                        }

                        // Atualize o saldo e o cheque
                        controllerBancoDados.updateSaldo(email, novoSaldo);
                        binding.saldoConta.setText(String.valueOf(novoSaldo));
                        controllerBancoDados.updateCheque(email, novoCheque);
                        binding.chequeEspecialConta.setText(String.valueOf(novoCheque));
                    }
                } catch (NumberFormatException e) {
                    // Tratar erro de formato inválido
                    e.printStackTrace();
                }
            } else {
                // Tratar entrada de valor vazia
            }

        });

        binding.btnTransferir.setOnClickListener(v -> {
            startActivityForResult(intentTrans, REQUEST_TRANSFERIR);
        });

    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TRANSFERIR && resultCode == RESULT_OK) {
            String email = getIntent().getStringExtra("email");
            controllerBancoDados.open();
            try {
                Double saldoBanco = controllerBancoDados.getSaldoByTitular(email);
                Double chequeBanco = controllerBancoDados.getChequeByTitular(email);
                binding.saldoConta.setText("R$ " + saldoBanco);
                binding.chequeEspecialConta.setText(String.valueOf(chequeBanco));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }
    }
}
