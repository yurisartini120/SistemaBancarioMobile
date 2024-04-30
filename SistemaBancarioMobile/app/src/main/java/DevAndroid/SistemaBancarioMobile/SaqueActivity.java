// SaqueActivity.java

package DevAndroid.SistemaBancarioMobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SaqueActivity extends Activity {

    private EditText editTextAmount;
    private Button buttonWithdraw;
    private double saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_saque);

        editTextAmount = findViewById(R.id.editTextAmount);
        buttonWithdraw = findViewById(R.id.buttonWithdraw);

        Intent intent = getIntent();
        saldo = intent.getDoubleExtra("saldo", 0.0);

        buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty()) {
                    double amount = Double.parseDouble(amountString);
                    if (saldo >= amount) {
                        saldo -= amount;
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("amountWithdrawn", amount);
                        resultIntent.putExtra("saldo", saldo);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(SaqueActivity.this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SaqueActivity.this, "Informe o valor do saque", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
