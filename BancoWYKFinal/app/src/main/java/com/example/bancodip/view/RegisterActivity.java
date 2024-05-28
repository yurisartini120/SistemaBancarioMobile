package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.example.bancodip.R;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.controller.Util;
import com.example.bancodip.databinding.ActivityRegisterBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private ControllerBancoDados controllerBancoDados;
    private Util util;
    private DatabaseReference referencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);
        util = new Util();
        referencia = FirebaseDatabase.getInstance().getReference();

        binding.btnCriarConta.setOnClickListener(v -> {
            controllerBancoDados.open();
            String nome = binding.hintTxtRegisterNome.getText().toString().trim();
            String email = binding.hintTxtRegisterEmail.getText().toString().trim();
            String saldo = binding.hintTxtRegisterSaldo.getText().toString().trim();
            String idade = binding.hintTxtRegisterIdade.getText().toString().trim();

            if (!nome.isEmpty() && !email.isEmpty() && !saldo.isEmpty() && util.isValidEmail(email) && !controllerBancoDados.isEmailInDatabase(email)) {
                double saldoDouble = Double.parseDouble(saldo);
                double chequeEspecial = saldoDouble * 4;
                int idadereal = Integer.parseInt(idade);

                obterNumeroDeCorrentistasEAdicionarNovo(nome, email, saldoDouble, chequeEspecial, idadereal);
            } else {
                Toast.makeText(getApplicationContext(), "Erro", Toast.LENGTH_LONG).show();
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

                // Adicione o novo correntista no banco de dados local
                controllerBancoDados.insertData(nome, email, saldo, chequeEspecial, chequeEspecial);

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
