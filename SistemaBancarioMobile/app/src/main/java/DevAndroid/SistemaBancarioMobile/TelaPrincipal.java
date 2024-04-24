package DevAndroid.SistemaBancarioMobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipal extends AppCompatActivity {

    private TextView textViewAccountNumber;
    private TextView textViewBalance;
    private TextView textViewName;
    private TextView textViewUsername;
    private Button buttonDeposito;
    private Button buttonSaque;
    private Button buttonTransferencia;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        textViewAccountNumber = findViewById(R.id.textViewNumeroConta);
        textViewBalance = findViewById(R.id.textViewSaldo);
        textViewName = findViewById(R.id.textViewNome);
        textViewUsername = findViewById(R.id.textViewUsername);
        buttonDeposito = findViewById(R.id.buttonDeposito);
        buttonSaque = findViewById(R.id.buttonSaque);
        buttonTransferencia = findViewById(R.id.buttonTransferencia);

        // Recebendo dados do usuário
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String accountNumber = intent.getStringExtra("accountNumber");

        double saldo = intent.getDoubleExtra("balance", 0.0);

        // Exibindo os dados na tela
        textViewAccountNumber.setText("Número da conta: " + accountNumber);

        textViewBalance.setText("Saldo: R$ " + saldo);
        textViewUsername.setText("Usuário: " + username);

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
