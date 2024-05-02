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
    SistemaBancario db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);
        comutartelaSplash();
    }

    private void comutartelaSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                db = new SistemaBancario(SplashActivity.this);
                Intent telaPrincipal = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(telaPrincipal);
                finish();
            }
        }, TIME_OUT_SPLASH);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.fecharBancoDados();
            Log.d("SplashActivity", "fecharBancoDados() chamado");
        }
    }
}
