package DevAndroid.SistemaBancarioMobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaqueActivity extends Activity {

    private EditText editTextAmount;
    private Button buttonWithdraw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_saque);

        editTextAmount = findViewById(R.id.editTextAmount);
        buttonWithdraw = findViewById(R.id.buttonWithdraw);

        buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty()) {
                    double amount = Double.parseDouble(amountString);
                    withdraw(amount);
                } else {
                    Toast.makeText(SaqueActivity.this, "Informe o valor do saque", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void withdraw(double amount) {
        // Aqui você pode adicionar a lógica para sacar o valor da conta
        // Por exemplo, você pode verificar se há saldo suficiente antes de fazer o saque
        // Para fins de demonstração, vamos apenas exibir uma mensagem
        Toast.makeText(this, "Saque de R$" + amount + " realizado com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
