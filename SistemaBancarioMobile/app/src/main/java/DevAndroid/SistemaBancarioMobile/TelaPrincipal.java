package DevAndroid.SistemaBancarioMobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipal extends AppCompatActivity {

    private TextView textViewAccountNumber;
    private TextView textViewBalance;
    private TextView textViewUsername;
    private Button buttonDeposito;
    private Button buttonSaque;
    private Button buttonTransferencia;

    private double saldo = 0.0; // Saldo inicial

    private static final int REQUEST_CODE_DEPOSIT = 1;
    private static final int REQUEST_CODE_WITHDRAW = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        textViewAccountNumber = findViewById(R.id.textViewNumeroConta);
        textViewBalance = findViewById(R.id.textViewSaldo);
        textViewUsername = findViewById(R.id.textViewUsername);
        buttonDeposito = findViewById(R.id.buttonDeposito);
        buttonSaque = findViewById(R.id.buttonSaque);
        buttonTransferencia = findViewById(R.id.buttonTransferencia);

        loadAccountInfo(); // Carrega informações da conta do usuário

        buttonDeposito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, DepositoActivity.class);
                startActivityForResult(intent, REQUEST_CODE_DEPOSIT);
            }
        });

        buttonSaque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, SaqueActivity.class);
                startActivityForResult(intent, REQUEST_CODE_WITHDRAW);
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

    private void loadAccountInfo() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String accountNumber = intent.getStringExtra("accountNumber");
        double balance = intent.getDoubleExtra("balance", 0.0);

        textViewAccountNumber.setText("Número da conta: " + accountNumber);
        textViewBalance.setText("Saldo: R$ " + balance);
        textViewUsername.setText("Usuário: " + username);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_DEPOSIT && resultCode == RESULT_OK) {
            double amountDeposited = data.getDoubleExtra("amountDeposited", 0.0);
            saldo += amountDeposited; // Atualize o saldo com o valor depositado
            updateBalance(); // Atualize o saldo na tela
        } else if (requestCode == REQUEST_CODE_WITHDRAW && resultCode == RESULT_OK) {
            double amountWithdrawn = data.getDoubleExtra("amountWithdrawn", 0.0);
            saldo -= amountWithdrawn; // Atualize o saldo com o valor sacado
            updateBalance(); // Atualize o saldo na tela
        }
    }

    private void updateBalance() {
        textViewBalance.setText("Saldo: R$ " + saldo);
    }
}
