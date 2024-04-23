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
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameEditText = findViewById(R.id.edit_text_usuario);
        passwordEditText = findViewById(R.id.edit_text_senha);
        loginButton = findViewById(R.id.button_login);
        registerButton = findViewById(R.id.button_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login", "Login button clicked");
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Verificar se os campos de login e senha estão vazios
                if (username.isEmpty() || password.isEmpty()) {
                    // Exibir mensagem de erro se os campos estiverem vazios
                    usernameEditText.setError("Por favor, preencha o nome de usuário");
                    passwordEditText.setError("Por favor, preencha a senha");
                    return;
                }

                // Verificar se o nome de usuário e a senha estão corretos
                if (username.equals("yuri") && password.equals("123")) {
                    // Abrir a tela principal
                    Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                    startActivity(intent);
                } else {
                    // Exibir mensagem de erro se o nome de usuário ou senha estiverem incorretos
                    usernameEditText.setError("Nome de usuário ou senha incorretos");
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
