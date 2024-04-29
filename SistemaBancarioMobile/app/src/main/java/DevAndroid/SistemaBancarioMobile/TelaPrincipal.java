// TelaPrincipal.java

package DevAndroid.SistemaBancarioMobile;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TelaPrincipal extends AppCompatActivity {

    private TextView textViewAccountNumber;
    private TextView textViewBalance;
    private TextView textViewUsername;
    private Button buttonDeposito;
    private Button buttonSaque;
    private Button buttonTransferencia;

    private double saldo = 0.0; // Saldo inicial

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
                int REQUEST_CODE_DEPOSIT = 0;
                startActivityForResult(intent, REQUEST_CODE_DEPOSIT);
            }
        });

        buttonSaque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TelaPrincipal.this, SaqueActivity.class);
                int REQUEST_CODE_WITHDRAW = 0;
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
}
