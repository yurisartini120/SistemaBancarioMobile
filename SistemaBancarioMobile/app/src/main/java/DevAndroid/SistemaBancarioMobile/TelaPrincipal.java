package DevAndroid.SistemaBancarioMobile;

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
    private Button buttonDeposit;
    private Button buttonWithdraw;
    private Button buttonTransfer;

    private double saldo = 1000.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_principal);

        textViewAccountNumber = findViewById(R.id.textViewAccountNumber);
        textViewBalance = findViewById(R.id.textViewBalance);
        textViewName = findViewById(R.id.textViewName);
        buttonDeposit = findViewById(R.id.buttonDeposit);
        buttonWithdraw = findViewById(R.id.buttonWithdraw);
        buttonTransfer = findViewById(R.id.buttonTransfer);

        // Simulando dados do cliente
        String accountNumber = "123456789";
        String name = "Fulano de Tal";

        textViewAccountNumber.setText("Número da conta: " + accountNumber);
        textViewName.setText("Nome: " + name);
        textViewBalance.setText("Saldo: R$ " + saldo);

        buttonDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deposit();
            }
        });

        buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdraw();
            }
        });

        buttonTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transfer();
            }
        });
    }

    private void deposit() {
        // Simulando um depósito de R$ 200.00
        double valorDeposito = 200.00;
        saldo += valorDeposito;
        atualizarSaldo();
        Toast.makeText(this, "Depósito de R$ " + valorDeposito + " realizado com sucesso", Toast.LENGTH_SHORT).show();
    }

    private void withdraw() {
        // Simulando um saque de R$ 100.00
        double valorSaque = 100.00;
        if (saldo >= valorSaque) {
            saldo -= valorSaque;
            atualizarSaldo();
            Toast.makeText(this, "Saque de R$ " + valorSaque + " realizado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
        }
    }

    private void transfer() {
        // Simulando uma transferência de R$ 300.00
        double valorTransferencia = 300.00;
        if (saldo >= valorTransferencia) {
            saldo -= valorTransferencia;
            atualizarSaldo();
            Toast.makeText(this, "Transferência de R$ " + valorTransferencia + " realizada com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarSaldo() {
        textViewBalance.setText("Saldo: R$ " + saldo);
    }
}
