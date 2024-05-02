package DevAndroid.SistemaBancarioMobile.Controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SistemaBancario extends SQLiteOpenHelper {
    private static final String TAG = "SistemaBancario";

    private static final String DB_NAME = "SistemaBancario_db";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;

    public SistemaBancario(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        db = getWritableDatabase();
        Log.d(TAG, "Banco de dados aberto");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlTabela1 =
                "CREATE TABLE IF NOT EXISTS Cliente(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "Nome TEXT, " +
                        "idade INT, " +
                        "email TEXT)" ;
        String sqlTabela2 =
                "CREATE TABLE IF NOT EXISTS ContaCorrente(id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "saldo REAL (10, 2), " +
                        "cheque_especial REAL (10, 2)) ";
        db.execSQL(sqlTabela1);
        db.execSQL(sqlTabela2);
        Log.d(TAG, "Tabelas criadas");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void salvarObjeto(String tabela, ContentValues dados) {
        db.insert(tabela, null, dados);
    }

    public void fecharBancoDados() {
        if (db != null && db.isOpen()) {
            db.close();
            Log.d(TAG, "Banco de dados fechado");
        }
    }
}
