package com.example.bancodip.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private DatabaseReference referencia;
    private static final int REQUEST_TRANSFERIR = 123;

    private int numeroConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        referencia = FirebaseDatabase.getInstance().getReference();

        Intent intentTrans = new Intent(MainActivity.this, TransferirActivity.class);
        Intent intent = getIntent();

        numeroConta = intent.getIntExtra("numeroConta", 0);
        intentTrans.putExtra("numeroConta_trans", numeroConta);

        referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Recuperar os dados de saldo e cheque especial do Firebase
                    Double saldo = dataSnapshot.child("saldo").getValue(Double.class);
                    Double chequeEspecial = dataSnapshot.child("cheque_especial").getValue(Double.class);

                    // Atualizar a interface do usuário com os dados recuperados
                    binding.saldoConta.setText(String.valueOf(saldo));
                    binding.chequeEspecialConta.setText(String.valueOf(chequeEspecial));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Tratar erro de consulta''''''''''''''''''''''''''''''''
            }
        });

        binding.btnDepositar.setOnClickListener(v -> {
            // Lógica de depósito
            String valorCliente = binding.hintUserValor.getText().toString();

            if (!valorCliente.isEmpty()) {
                Double valorDeposito = Double.parseDouble(valorCliente);

                referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Double saldoAtual = dataSnapshot.child("saldo").getValue(Double.class);
                            Double novoSaldo = saldoAtual + valorDeposito;

                            // Atualizar saldo no Firebase
                            referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);

                            // Atualizar a interface do usuário
                            binding.saldoConta.setText(String.valueOf(novoSaldo));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Tratar erro de consulta
                    }
                });
            } else {
                // Tratar entrada de valor vazia
            }
        });

        binding.btnSacar.setOnClickListener(v -> {
            // Lógica de saque
            String valorCliente = binding.hintUserValor.getText().toString();

            if (!valorCliente.isEmpty()) {
                Double valorSaque = Double.parseDouble(valorCliente);

                referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Double saldoAtual = dataSnapshot.child("saldo").getValue(Double.class);
                            Double novoSaldo = saldoAtual - valorSaque;

                            if (novoSaldo >= 0) {
                                // Se o saldo for suficiente, atualizar saldo no Firebase
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                                binding.saldoConta.setText(String.valueOf(novoSaldo));
                            } else {
                                // Se o saldo não for suficiente, calcular quanto do cheque especial será utilizado
                                Double chequeEspecialAtual = dataSnapshot.child("cheque_especial").getValue(Double.class);
                                Double saldoRestante = valorSaque - saldoAtual;
                                Double novoChequeEspecial = chequeEspecialAtual - saldoRestante;

                                if (novoChequeEspecial < 0) {
                                    novoChequeEspecial = 0.0;
                                    novoSaldo = 0.0;
                                }

                                // Atualizar saldo e cheque especial no Firebase
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("cheque_especial").setValue(novoChequeEspecial);

                                // Atualizar a interface do usuário
                                binding.saldoConta.setText(String.valueOf(novoSaldo));
                                binding.chequeEspecialConta.setText(String.valueOf(novoChequeEspecial));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Tratar erro de consulta
                    }
                });
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
            referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Recuperar os dados de saldo e cheque especial do Firebase
                        Double saldo = dataSnapshot.child("saldo").getValue(Double.class);
                        Double chequeEspecial = dataSnapshot.child("cheque_especial").getValue(Double.class);

                        // Atualizar a interface do usuário com os dados recuperados
                        binding.saldoConta.setText(String.valueOf(saldo));
                        binding.chequeEspecialConta.setText(String.valueOf(chequeEspecial));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Tratar erro de consulta
                }
            });

        }
    }
}
