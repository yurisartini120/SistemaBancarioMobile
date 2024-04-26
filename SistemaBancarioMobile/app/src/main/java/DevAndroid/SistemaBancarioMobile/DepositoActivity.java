package DevAndroid.SistemaBancarioMobile;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DevAndroid.SistemaBancarioMobile.MobileDB;
import DevAndroid.SistemaBancarioMobile.R;

public class DepositoActivity extends Activity {

    private EditText editTextAmount;
    private Button buttonDeposit;
    private SQLiteDatabase database;
    private MobileDB mobileDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_deposito);

        mobileDB = new MobileDB(this);
        database = mobileDB.getWritableDatabase();

        editTextAmount = findViewById(R.id.editTextAmount);
        buttonDeposit = findViewById(R.id.buttonDeposit);

        buttonDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty()) {
                    double amount = Double.parseDouble(amountString);
                    deposit(amount);
                } else {
                    Toast.makeText(DepositoActivity.this, "Informe o valor do depósito", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void deposit(double amount) {
        ContentValues values = new ContentValues();
        values.put(MobileDB.COLUMN_BALANCE, amount);

        // Atualiza o saldo na tabela de contas
        database.update(MobileDB.TABLE_ACCOUNTS, values, null, null);

        // Para fins de demonstração, vamos apenas enviar o valor depositado de volta
        Intent resultIntent = new Intent();
        resultIntent.putExtra("amountDeposited", amount);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
