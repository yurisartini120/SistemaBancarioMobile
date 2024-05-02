package DevAndroid.SistemaBancarioMobile.View;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import DevAndroid.SistemaBancarioMobile.Controller.SistemaBancario;
import DevAndroid.SistemaBancarioMobile.R;

public class SplashActivity extends AppCompatActivity {
    public static final int TIME_OUT_SPLASH = 5000;
    private SistemaBancario sistemaBancario; // Instância do SistemaBancario

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);

        // Inicializa a instância do SistemaBancario
        sistemaBancario = new SistemaBancario(this);

        comutartelaSplash();
    }

    private void comutartelaSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Abre a MainActivity
                Intent telaPrincipal = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(telaPrincipal);

                // Não feche o banco de dados aqui, apenas finalize a atividade SplashActivity
                finish();
            }
        }, TIME_OUT_SPLASH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Certifique-se de fechar o banco de dados quando a atividade estiver prestes a ser destruída
        if (sistemaBancario != null) {
            sistemaBancario.fecharBancoDados();
        }
    }
}
