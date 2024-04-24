package DevAndroid.SistemaBancarioMobile;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TransferenciaActivity extends Activity {

    private EditText editTextAmount;
    private EditText editTextRecipient;
    private Button buttonTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_transferencia);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextRecipient = findViewById(R.id.editTextRecipient);
        buttonTransfer = findViewById(R.id.buttonTransfer);

        buttonTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                String recipient = editTextRecipient.getText().toString();
                if (!amountString.isEmpty() && !recipient.isEmpty()) {
                    double amount = Double.parseDouble(amountString);
                    transfer(amount, recipient);
                } else {
                    Toast.makeText(TransferenciaActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void transfer(double amount, String recipient) {
        // Aqui você pode adicionar a lógica para transferir o valor para outra conta
        // Por exemplo, você pode verificar se o destinatário é válido e se há saldo suficiente antes de fazer a transferência
        // Para fins de demonstração, vamos apenas exibir uma mensagem
        Toast.makeText(this, "Transferência de R$" + amount + " para " + recipient + " realizada com sucesso!", Toast.LENGTH_SHORT).show();
        finish();
    }
}
