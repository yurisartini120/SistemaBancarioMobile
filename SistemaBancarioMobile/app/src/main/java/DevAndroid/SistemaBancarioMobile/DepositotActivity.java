package DevAndroid.SistemaBancarioMobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DepositotActivity extends Activity {

    private EditText editTextAmount;
    private Button buttonDeposit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_deposito);

        editTextAmount = findViewById(R.id.editTextAmount);
        buttonDeposit = findViewById(R.id.buttonDeposit);

        buttonDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty()) {
                    double amount = Double.parseDouble(amountString);
                    deposit(amount);
                } else {
                    Toast.makeText(DepositotActivity.this, "Informe o valor do depósito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deposit(double amount) {
        // Aqui você pode adicionar a lógica para depositar o valor na conta
        // Por exemplo, você pode atualizar o saldo da conta
        // Para fins de demonstração, vamos apenas exibir uma mensagem
        Toast.makeText(this, "Depósito de R$" + amount + " realizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
