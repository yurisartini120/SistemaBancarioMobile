package DevAndroid.SistemaBancarioMobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.edit_text_usuario);
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login", "Login button clicked");
                String username = usernameEditText.getText().toString().trim();

                // Verificar se o campo de usuário está vazio
                if (username.isEmpty()) {
                    // Exibir mensagem de erro se o campo estiver vazio
                    usernameEditText.setError("Por favor, preencha o nome de usuário");
                    return;
                }

                // Verificar se o nome de usuário está correto
                if (username.equals("yuri")) {
                    // Abrir a tela principal e passar os dados do usuário
                    Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                    intent.putExtra("username", username);
                    intent.putExtra("accountNumber", "123456");
                    intent.putExtra("balance", 1000.00);
                    startActivity(intent);
                } else {
                    // Exibir mensagem de erro se o nome de usuário estiver incorreto
                    usernameEditText.setError("Nome de usuário incorreto");
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a tela de registro
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
