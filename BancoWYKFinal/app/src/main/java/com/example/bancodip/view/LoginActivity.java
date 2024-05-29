package com.example.bancodip.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private ControllerBancoDados controllerBancoDados;

    private FirebaseAuth mAuth;
    private DatabaseReference referencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);

        controllerBancoDados = new ControllerBancoDados(this);

        referencia = FirebaseDatabase.getInstance().getReference();


        binding.btnCriarContaLogin.setOnClickListener(v -> {
            startActivity(intentRegister);
        });

        binding.btnEntrarConta.setOnClickListener(v -> {
            String nome = binding.hintTxtNomeLogin.getText().toString().trim();
            String numeroContaString = binding.editTextNumeroConta.getText().toString().trim();

            if (nome.isEmpty() || numeroContaString.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int numeroConta = Integer.parseInt(numeroContaString);

                    // Consulta ao Firebase para verificar as credenciais
                    referencia.child("correntistas").child(String.valueOf(numeroConta)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // Verifica se o nome corresponde ao nome no Firebase
                                String nomeFirebase = dataSnapshot.child("nome").getValue(String.class);
                                if (nomeFirebase != null && nomeFirebase.equals(nome)) {
                                    // Nome e número da conta correspondem, faça o login
                                    Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                                    intentMain.putExtra("numeroConta", numeroConta);
                                    startActivity(intentMain);

                                    startActivity(intentMain);
                                    finish();
                                } else {
                                    // Nome não corresponde ao nome no Firebase
                                    Toast.makeText(LoginActivity.this, "Nome ou número da conta incorretos.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // Usuário não encontrado
                                Toast.makeText(LoginActivity.this, "Usuário não encontrado.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Tratar erro de consulta
                        }
                    });

                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Número da conta inválido.", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
 }
