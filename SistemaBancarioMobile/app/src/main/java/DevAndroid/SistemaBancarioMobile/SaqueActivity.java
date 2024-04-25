package DevAndroid.SistemaBancarioMobile;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import DevAndroid.SistemaBancarioMobile.MobileDB;
import DevAndroid.SistemaBancarioMobile.R;

public class SaqueActivity extends Activity {

    private EditText editTextAmount;
    private Button buttonWithdraw;
    private SQLiteDatabase database;
    private MobileDB mobileDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela_saque);

        mobileDB = new MobileDB(this);
        database = mobileDB.getWritableDatabase();

        editTextAmount = findViewById(R.id.editTextAmount);
        buttonWithdraw = findViewById(R.id.buttonWithdraw);

        buttonWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty()) {
                    double amount = Double.parseDouble(amountString);
                    withdraw(amount);
                } else {
                    Toast.makeText(SaqueActivity.this, "Informe o valor do saque", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void withdraw(double amount) {
        // Verifica se há saldo suficiente
        Cursor cursor = database.query(
                MobileDB.TABLE_ACCOUNTS,
                new String[]{MobileDB.COLUMN_BALANCE},
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            double saldo = cursor.getDouble(cursor.getColumnIndexOrThrow(MobileDB.COLUMN_BALANCE));
            if (saldo >= amount) {
                saldo -= amount;
                ContentValues values = new ContentValues();
                values.put(MobileDB.COLUMN_BALANCE, saldo);

                // Atualiza o saldo na tabela de contas
                database.update(MobileDB.TABLE_ACCOUNTS, values, null, null);

                // Exibe uma mensagem de sucesso
                Toast.makeText(this, "Saque de R$" + amount + " realizado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent resultIntent = new Intent();
                resultIntent.putExtra("amountWithdrawn", amount);
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Exibe uma mensagem de saldo insuficiente
                Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        }
    }
}
