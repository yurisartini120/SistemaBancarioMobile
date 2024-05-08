package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ControllerBancoDados controllerBancoDados;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);

        Intent intentTrans = new Intent(MainActivity.this, TransferirActivity.class);
        Intent intent = getIntent();

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
            controllerBancoDados.close();
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
                        Toast.makeText(getApplicationContext(),"Você pagou o seu cheque especial com êxito!", Toast.LENGTH_SHORT).show();
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

                    Double novoSaldo = saldo - valorSaque;
                    Double novoCheque = cheque - valorSaque;

                    Double novoSaldoMais = saldo + valorSaque;

                    if (saldo > 0 && novoSaldo >= 0) {
                        controllerBancoDados.updateSaldo(email, novoSaldo);
                        binding.saldoConta.setText(String.valueOf(novoSaldo));
                    } else if (saldo <= 0 && novoSaldo >= -CHEQUEESPECIAL) {
                        controllerBancoDados.updateSaldo(email, novoSaldo);
                        binding.saldoConta.setText(String.valueOf(novoSaldo));

                        controllerBancoDados.updateCheque(email, novoCheque);
                        binding.chequeEspecialConta.setText(String.valueOf(novoCheque));
                    } else if (saldo <= -CHEQUEESPECIAL) {
                        Toast.makeText(getApplicationContext(),"Você não tem valor disponível", Toast.LENGTH_SHORT).show();
                        controllerBancoDados.updateSaldo(email, -CHEQUEESPECIAL);
                        binding.saldoConta.setText(String.valueOf(-CHEQUEESPECIAL));

                        controllerBancoDados.updateCheque(email, 0);
                        binding.chequeEspecialConta.setText(String.valueOf(0.00));
                    } else {
                        Toast.makeText(getApplicationContext(),"Você não tem saldo para isso!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    controllerBancoDados.close();
                    binding.hintUserValor.setText("");
                }
            }
        });

        binding.btnTransferir.setOnClickListener(v -> {
            startActivity(intentTrans);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        controllerBancoDados.open();
        Intent intent = getIntent();

        String email = intent.getStringExtra("email");
        Double saldo = controllerBancoDados.getSaldoByTitular(email);

        binding.saldoConta.setText(String.valueOf(saldo));
    }
}
