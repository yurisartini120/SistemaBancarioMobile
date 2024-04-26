package DevAndroid.SistemaBancarioMobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MobileDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MobideDB.db";
    private static final int DATABASE_VERSION = 1;

    // Tabela de contas bancárias
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_ACCOUNT_NUMBER = "account_number";
    public static final String COLUMN_BALANCE = "balance";
    public static final String COLUMN_USERNAME = "username";

    // Comando SQL para criar a tabela de contas
    private static final String SQL_CREATE_TABLE_ACCOUNTS =
            "CREATE TABLE " + TABLE_ACCOUNTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ACCOUNT_NUMBER + " TEXT NOT NULL, " +
                    COLUMN_BALANCE + " REAL NOT NULL, " +
                    COLUMN_USERNAME + " TEXT NOT NULL" +
                    ");";

    public MobileDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Cria a tabela de contas
        db.execSQL(SQL_CREATE_TABLE_ACCOUNTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Aqui você pode realizar atualizações do esquema do banco de dados
        // Neste exemplo, simplesmente excluímos a tabela e a criamos novamente
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
        onCreate(db);
    }
}
