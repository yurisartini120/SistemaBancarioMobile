package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.bancodip.databinding.ActivityRegisterBinding;
import com.example.bancodip.controller.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private Util util;
    private DatabaseReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        util = new Util();
        referencia = FirebaseDatabase.getInstance().getReference();

        binding.btnCriarConta.setOnClickListener(v -> {
            String nome = binding.hintTxtRegisterNome.getText().toString().trim();
            String email = binding.hintTxtRegisterEmail.getText().toString().trim();
            String saldo = binding.hintTxtRegisterSaldo.getText().toString().trim();
            String idade = binding.hintTxtRegisterIdade.getText().toString().trim();

            if (!nome.isEmpty() && !email.isEmpty() && !saldo.isEmpty() && util.isValidEmail(email)) {
                double saldoDouble = Double.parseDouble(saldo);
                double chequeEspecial = saldoDouble * 4;
                int idadereal = Integer.parseInt(idade);

                verificarEmailERegistrar(nome, email, saldoDouble, chequeEspecial, idadereal);
            } else {
                Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void verificarEmailERegistrar(String nome, String email, double saldo, double chequeEspecial, int idade) {
        referencia.child("correntistas").orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(RegisterActivity.this, "Email já cadastrado.", Toast.LENGTH_LONG).show();
                } else {
                    obterNumeroDeCorrentistasEAdicionarNovo(nome, email, saldo, chequeEspecial, idade);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Erro ao verificar email", databaseError.toException());
            }
        });
    }

    private void obterNumeroDeCorrentistasEAdicionarNovo(String nome, String email, double saldo, double chequeEspecial, int idade) {
        referencia.child("correntistas").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long numeroDeCorrentistas = dataSnapshot.getChildrenCount();
                int numeroDaConta = (int) (numeroDeCorrentistas + 1);

                // Crie um novo correntista no Firebase
                DatabaseReference novaContaRef = referencia.child("correntistas").child(String.valueOf(numeroDaConta));
                novaContaRef.child("ativo").setValue(1);
                novaContaRef.child("cheque_Especial").setValue(chequeEspecial);
                novaContaRef.child("conta").setValue(numeroDaConta);
                novaContaRef.child("email").setValue(email);
                novaContaRef.child("idade").setValue(idade);
                novaContaRef.child("nome").setValue(nome);
                novaContaRef.child("saldo").setValue(saldo);
                novaContaRef.child("tipo").setValue(1);

                // Log para verificar se está registrando corretamente
                Log.d("Firebase", "Novo correntista adicionado: " + nome + ", Conta: " + numeroDaConta);

                // Redirecione para a MainActivity
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("nome", nome);
                intent.putExtra("email", email);
                intent.putExtra("saldo", saldo);
                intent.putExtra("cheque", chequeEspecial);
                startActivity(intent);
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Firebase", "Erro ao obter dados", databaseError.toException());
            }
        });
    }
}
