package com.example.bancodip.view;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.example.bancodip.controller.ControllerBancoDados;
import com.example.bancodip.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ControllerBancoDados controllerBancoDados;
    private String nome; // Adicione esta linha para armazenar o nome do usuário
    private int id; // Adicione esta linha para armazenar o ID do usuário

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        controllerBancoDados = new ControllerBancoDados(this);

        Intent intent = getIntent();
        nome = intent.getStringExtra("nome"); // Recupere o nome do usuário do intent
        id = intent.getIntExtra("id", -1); // Recupere o ID do usuário do intent

        // Verifique se o nome e o ID foram passados corretamente
        if (nome == null || id == -1) {
            Toast.makeText(getApplicationContext(), "Erro: Nome ou ID inválido", Toast.LENGTH_LONG).show();
            finish(); // Encerre a atividade se os dados não estiverem corretos
        }

        // Continue com o restante do seu código aqui...

        Intent intentTrans = new Intent(MainActivity.this, TransferirActivity.class);
        intentTrans.putExtra("email_trans", nome); // Envie o nome do usuário para a atividade de transferência

        // Restante do seu código...

        binding.btnTransferir.setOnClickListener(v -> {
            startActivity(intentTrans);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        controllerBancoDados.open();
        Double saldoBanco = controllerBancoDados.getSaldoByTitular(nome); // Use o nome do usuário para recuperar o saldo
        Double chequeBanco = controllerBancoDados.getChequeByTitular(nome); // Use o nome do usuário para recuperar o cheque especial
        Double chequeDefiBanco = controllerBancoDados.getChequeDEFIByTitular(nome); // Use o nome do usuário para recuperar o cheque especial definido
        String saldoString = String.valueOf(saldoBanco);
        String chequeString = String.valueOf(chequeBanco);
        String chequeDefiString = String.valueOf(chequeDefiBanco);

        binding.saldoConta.setText("R$ " + saldoString);
        binding.chequeEspecialConta.setText(chequeString);
    }
}
