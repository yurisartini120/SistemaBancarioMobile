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
                    Double chequeEspecial_fixo = dataSnapshot.child("cheque_Especial_Fixo").getValue(Double.class);

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
                            // Verificar se o cheque especial foi usado e agora o saldo está positivo novamente
                            Double chequeEspecialAtual = dataSnapshot.child("cheque_Especial").getValue(Double.class);
                            if (novoSaldo >= 0 && chequeEspecialAtual != 0.0) {
                                // Zerar o cheque especial, já que o saldo está positivo novamente
                                Double chequeEspecialFixo = dataSnapshot.child("cheque_Especial_Fixo").getValue(Double.class);
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("cheque_Especial").setValue(chequeEspecialFixo);
                                binding.chequeEspecialConta.setText(String.valueOf(chequeEspecialFixo));
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


        binding.btnSacar.setOnClickListener(v -> {
            String valorCliente = binding.hintUserValor.getText().toString();
            if (!valorCliente.isEmpty()) {
                Double valorSaque = Double.parseDouble(valorCliente);
                referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Double saldoAtual = dataSnapshot.child("saldo").getValue(Double.class);
                            Double chequeEspecialAtual = dataSnapshot.child("cheque_Especial").getValue(Double.class);

                            if (saldoAtual >= valorSaque) {
                                // Se o saldo é suficiente para o saque, apenas diminua o saldo
                                Double novoSaldo = saldoAtual - valorSaque;
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                                binding.saldoConta.setText(String.valueOf(novoSaldo));
                            } else if (saldoAtual + chequeEspecialAtual >= valorSaque) {
                                // Se o saldo + cheque especial for suficiente para o saque, use o cheque especial
                                Double saqueDoSaldo = saldoAtual;
                                Double saqueDoChequeEspecial = valorSaque - saldoAtual;
                                Double novoSaldo = saldoAtual - saqueDoSaldo - saqueDoChequeEspecial;
                                Double novoChequeEspecial = chequeEspecialAtual - saqueDoChequeEspecial;

                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("saldo").setValue(novoSaldo);
                                referencia.child("correntistas").child(String.valueOf(numeroConta)).child("cheque_Especial").setValue(novoChequeEspecial);

                                // Atualize o saldo e o cheque especial
                                binding.saldoConta.setText(String.valueOf(novoSaldo));
                                binding.chequeEspecialConta.setText(String.valueOf(novoChequeEspecial));
                            } else {
                                // Se o saldo + cheque especial não for suficiente, informe ao usuário
                                Toast.makeText(MainActivity.this, "Saldo e cheque especial insuficientes para este saque", Toast.LENGTH_SHORT).show();
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
