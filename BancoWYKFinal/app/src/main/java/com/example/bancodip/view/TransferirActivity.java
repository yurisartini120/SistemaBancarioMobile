package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bancodip.databinding.ActivityTransferirBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TransferirActivity extends AppCompatActivity {

    private ActivityTransferirBinding binding;
    private DatabaseReference databaseReference;

    private static final String TAG = "TransferirActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransferirBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        databaseReference = FirebaseDatabase.getInstance().getReference("correntistas");

        Intent intent = getIntent();
        String emailUser = intent.getStringExtra("email_trans");

        if (emailUser == null) {
            Log.e(TAG, "Email do usuário não encontrado no Intent");
            Toast.makeText(getApplicationContext(), "Erro ao recuperar email do usuário", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Log.d(TAG, "Email do usuário: " + emailUser);

        binding.btnVoltar.setOnClickListener(v -> finish());

        binding.btnTransferirUser.setOnClickListener(v -> {
            String destinatarioEmail = binding.transUserEmail.getText().toString();
            String valorUser = binding.transUserValor.getText().toString();

            if (!valorUser.isEmpty() && !destinatarioEmail.isEmpty()) {
                try {
                    Double valorTransferencia = Double.parseDouble(valorUser);
                    transferir(emailUser, destinatarioEmail, valorTransferencia);
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Erro ao converter valor da transferência", e);
                    Toast.makeText(getApplicationContext(), "Valor de transferência inválido", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Preencha todos os campos", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void transferir(String emailUser, String destinatarioEmail, Double valorTransferencia) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    DataSnapshot userSnapshot = null;
                    DataSnapshot destinatarioSnapshot = null;

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String email = snapshot.child("email").getValue(String.class);
                        Log.d(TAG, "Email recuperado: " + email); // Log para verificar os emails recuperados

                        if (email != null && emailUser != null && emailUser.equals(email)) {
                            userSnapshot = snapshot;
                            Log.d(TAG, "Usuário encontrado: " + emailUser);
                        }
                        if (email != null && destinatarioEmail != null && destinatarioEmail.equals(email)) {
                            destinatarioSnapshot = snapshot;
                            Log.d(TAG, "Destinatário encontrado: " + destinatarioEmail);
                        }
                    }

                    if (userSnapshot != null && destinatarioSnapshot != null) {
                        Double saldoUser = userSnapshot.child("saldo").getValue(Double.class);
                        Double chequeEspecialUser = userSnapshot.child("cheque_Especial").getValue(Double.class);
                        Double saldoDestinatario = destinatarioSnapshot.child("saldo").getValue(Double.class);

                        if (saldoUser != null && saldoUser > 0) {
                            double saldoUserNew = saldoUser - valorTransferencia;
                            double saldoDestinatarioNew = saldoDestinatario != null ? saldoDestinatario + valorTransferencia : valorTransferencia;

                            userSnapshot.getRef().child("saldo").setValue(saldoUserNew);
                            destinatarioSnapshot.getRef().child("saldo").setValue(saldoDestinatarioNew);

                            if (saldoUserNew < 0) {
                                double novoChequeEspecial = chequeEspecialUser != null ? chequeEspecialUser + saldoUserNew : saldoUserNew;
                                userSnapshot.getRef().child("cheque_Especial").setValue(novoChequeEspecial);
                            }

                            binding.transUserValor.setText("");
                            binding.transUserEmail.setText("");

                            Toast.makeText(getApplicationContext(), "Transferencia executada com sucesso", Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Saldo insuficiente", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (userSnapshot == null) {
                            Log.e(TAG, "Usuário não encontrado: " + emailUser);
                        }
                        if (destinatarioSnapshot == null) {
                            Log.e(TAG, "Destinatário não encontrado: " + destinatarioEmail);
                        }
                        Toast.makeText(getApplicationContext(), "Usuário ou destinatário não encontrados", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Erro durante a transferência", e);
                    Toast.makeText(getApplicationContext(), "Erro durante a transferência", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, "Erro ao acessar o banco de dados", databaseError.toException());
                Toast.makeText(getApplicationContext(), "Erro ao acessar o banco de dados", Toast.LENGTH_LONG).show();
            }
        });
    }
}
