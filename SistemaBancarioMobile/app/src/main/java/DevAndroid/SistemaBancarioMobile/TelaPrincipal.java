package DevAndroid.SistemaBancarioMobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipal extends AppCompatActivity {

    private TextView textViewAccountNumber;
    private TextView textViewBalance;
    private TextView textViewName;
    private Button buttonDeposito;
    private Button buttonSaque;
    private Button buttonTransferencia;

    private double saldo = 1000.00;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        textViewAccountNumber = findViewById(R.id.textViewNumeroConta);
        textViewBalance = findViewById(R.id.textViewSaldo);
        textViewName = findViewById(R.id.textViewNome);
        buttonDeposito = findViewById(R.id.buttonDeposito);
        buttonSaque = findViewById(R.id.buttonSaque);
        buttonTransferencia = findViewById(R.id.buttonTransferencia);

        // Simulando dados do cliente
        String accountNumber = "123456789";
        String name = "Fulano de Tal";

        textViewAccountNumber.setText("Número da conta: " + accountNumber);
        textViewName.setText("Nome: " + name);
        textViewBalance.setText("Saldo: R$ " + saldo);


        buttonDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, DepositotActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonSaque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, SaqueActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonTransferencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, TransferenciaActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }








}


