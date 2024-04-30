package DevAndroid.SistemaBancarioMobile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.window.SplashScreen;

import DevAndroid.SistemaBancarioMobile.Database.SistemaBancario;

public class SplashActivity extends AppCompatActivity {
    public static final int TIME_OUT_SPLASH = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_splash);

        comutartelaSplash();
    }

    private void comutartelaSplash(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SistemaBancario db = new SistemaBancario(SplashActivity.this);
                Intent telaPrincipal = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(telaPrincipal);
                finish();
            }
        }, TIME_OUT_SPLASH);

    }
}