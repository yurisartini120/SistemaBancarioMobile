// MainActivity.java
package DevAndroid.SistemaBancarioMobile;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText accountNumberEditText;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountNumberEditText = findViewById(R.id.edit_text_account_number);
        usernameEditText = findViewById(R.id.edit_text_username);
        passwordEditText = findViewById(R.id.edit_text_password);
        loginButton = findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();

            }
        });
    }

    private void login() {
        String accountNumber = accountNumberEditText.getText().toString();
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        // Verifique se os campos estão vazios
        if (accountNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity.this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aqui você pode adicionar a lógica de validação com seu backend
        // Por enquanto, vamos apenas simular um login bem-sucedido se o usuário e a senha forem "admin"
        if (username.equals("admin") && password.equals("admin")) {
            // Login bem-sucedido
            Toast.makeText(MainActivity.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();
            // Abrir a tela principal
            openTelaPrincipal();
        } else {
            // Login falhou
            Toast.makeText(MainActivity.this, "Usuário ou senha incorretos", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para abrir a tela principal
    private void openTelaPrincipal() {
        Intent intent = new Intent(this, TelaPrincipal.class);
        startActivity(intent);
        finish();
    }
}
