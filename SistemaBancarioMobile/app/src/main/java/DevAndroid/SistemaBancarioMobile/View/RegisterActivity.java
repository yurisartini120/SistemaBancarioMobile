package DevAndroid.SistemaBancarioMobile.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashSet;
import java.util.Set;

import DevAndroid.SistemaBancarioMobile.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private Button registerButton;
    private Set<String> accountNumbers = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        fullNameEditText = findViewById(R.id.edit_text_full_name);
        emailEditText = findViewById(R.id.edit_text_email);

        registerButton = findViewById(R.id.button_register);

        // Simulando uma lista de números de conta já existentes
        accountNumbers.add("123456");
        accountNumbers.add("789012");
        accountNumbers.add("345678");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (fullName.isEmpty() || email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar se o número de conta já existe
        String accountNumber = generateAccountNumber();
        if (accountNumber == null) {
            Toast.makeText(RegisterActivity.this, "Erro ao gerar número de conta", Toast.LENGTH_SHORT).show();
            return;
        }

        // Exibir uma caixa de diálogo com o número de conta
        showAccountNumberDialog(accountNumber);
    }

    private String generateAccountNumber() {
        // Gera um número de conta único
        String accountNumber = String.valueOf((int) (Math.random() * 900000) + 100000);
        if (accountNumbers.contains(accountNumber)) {
            // Se o número já existe, tenta novamente até encontrar um número único
            accountNumber = generateAccountNumber();
        } else {
            // Adiciona o novo número à lista de números de conta
            accountNumbers.add(accountNumber);
        }
        return accountNumber;
    }

    private void showAccountNumberDialog(final String accountNumber) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Registro bem-sucedido");
        builder.setMessage("Seu número de conta é: " + accountNumber + "\n\nDeseja copiar o número ou voltar para a tela de login?");

        builder.setPositiveButton("Copiar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Copiar o número de conta para a área de transferência
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("Número de Conta", accountNumber);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(RegisterActivity.this, "Número de conta copiado", Toast.LENGTH_SHORT).show();
                goToLogin();
            }
        });

        builder.setNegativeButton("Voltar para Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToLogin();
            }
        });

        builder.setCancelable(false); // Impede que o diálogo seja cancelado clicando fora dele

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void goToLogin() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Finaliza a atividade de registro
    }
}
