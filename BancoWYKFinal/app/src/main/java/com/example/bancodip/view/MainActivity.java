package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.bancodip.databinding.ActivityMainBinding;
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
    private String emailUser; // Variável para armazenar o email do usuário

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        referencia = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        numeroConta = intent.getIntExtra("numeroConta", 0);

        // Adicionar um listener para buscar o email do usuário baseado no número da conta
        referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emailUser = dataSnapshot.child("email").getValue(String.class);
                    Double saldo = dataSnapshot.child("saldo").getValue(Double.class);
                    Double chequeEspecial = dataSnapshot.child("cheque_Especial").getValue(Double.class);

                    binding.saldoConta.setText(String.valueOf(saldo));
                    binding.chequeEspecialConta.setText(String.valueOf(chequeEspecial));
                } else {
                    Toast.makeText(MainActivity.this, "Conta não encontrada", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnDepositar.setOnClickListener(v -> {
            String valorCliente = binding.hintUserValor.getText().toString();
            if (!valorCliente.isEmpty()) {
                Double valorDeposito = Double.parseDouble(valorCliente);
                referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Double saldoAtual = dataSnapshot.child("saldo").getValue(Double.class);
                            Double novoSaldo = saldoAtual + valorDeposito;
                            referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                            binding.saldoConta.setText(String.valueOf(novoSaldo));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Por favor, insira um valor", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnSacar.setOnClickListener(v -> {
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
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                                binding.saldoConta.setText(String.valueOf(novoSaldo));
                            } else {
                                Double chequeEspecialAtual = dataSnapshot.child("cheque_Especial").getValue(Double.class);
                                Double saldoRestante = valorSaque - saldoAtual;
                                Double novoChequeEspecial = chequeEspecialAtual - saldoRestante;
                                if (novoChequeEspecial < 0) {
                                    novoChequeEspecial = 0.0;
                                    novoSaldo = 0.0;
                                }
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("cheque_Especial").setValue(novoChequeEspecial);
                                binding.saldoConta.setText(String.valueOf(novoSaldo));
                                binding.chequeEspecialConta.setText(String.valueOf(novoChequeEspecial));
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(MainActivity.this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(MainActivity.this, "Por favor, insira um valor", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnTransferir.setOnClickListener(v -> {
            if (emailUser != null) {
                Intent intentTrans = new Intent(MainActivity.this, TransferirActivity.class);
                intentTrans.putExtra("email_trans", emailUser);
                startActivityForResult(intentTrans, REQUEST_TRANSFERIR);
            } else {
                Toast.makeText(MainActivity.this, "Erro ao recuperar email do usuário", Toast.LENGTH_SHORT).show();
            }
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
                        Double saldo = dataSnapshot.child("saldo").getValue(Double.class);
                        Double chequeEspecial = dataSnapshot.child("cheque_Especial").getValue(Double.class);
                        binding.saldoConta.setText(String.valueOf(saldo));
                        binding.chequeEspecialConta.setText(String.valueOf(chequeEspecial));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, "Erro ao acessar o banco de dados", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
