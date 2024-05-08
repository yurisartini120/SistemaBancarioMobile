package com.example.bancodip.model;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ModelBancoDados extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BancoWYK.db";
    private static final int DATABASE_VERSION = 1;

    public static final String NOME_TABELA = "BancoWYK";
    public static final String COLUNA_ID = "id";
    public static final String COLUNA_TITULAR = "titular";
    public static final String COLUNA_EMAIL = "email";
    public static final String COLUNA_SALDO= "saldo";
    public static final String COLUNA_CHEQUE_ESPECIAL = "cheque_especial";
    public static final String COLUNA_CHEQUE_ESPECIAL_DEFI = "cheque_especial_defi";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + NOME_TABELA + " (" +
                    COLUNA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUNA_TITULAR + " VARCHAR(80), " +
                    COLUNA_EMAIL + " VARCHAR(80), " +
                    COLUNA_CHEQUE_ESPECIAL + " DECIMAL(10,2), " +
                    COLUNA_CHEQUE_ESPECIAL_DEFI + " DECIMAL(10,2), " +
                    COLUNA_SALDO + " DECIMAL(10,2)" +
                    ");";

    private SQLiteDatabase database;

    public ModelBancoDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NOME_TABELA);
        onCreate(sqLiteDatabase);
    }

    public String getNomeByNumeroConta(String numeroConta) {
        String nome = "";
        try (Cursor cursor = database.query(NOME_TABELA,
                new String[]{COLUNA_TITULAR},
                COLUNA_ID + " = ?",
                new String[]{numeroConta},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int nomeIndex = cursor.getColumnIndex(COLUNA_TITULAR);
                nome = cursor.getString(nomeIndex);
            }
        } catch (Exception e) {
            Log.e("GET_NOME_BY_ID", "Erro ao obter nome por número da conta: " + e.getMessage());
        }
        return nome;
    }

    public String getEmailByNumeroConta(String numeroConta) {
        String email = "";
        try (Cursor cursor = database.query(NOME_TABELA,
                new String[]{COLUNA_EMAIL},
                COLUNA_ID + " = ?",
                new String[]{numeroConta},
                null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {
                int emailIndex = cursor.getColumnIndex(COLUNA_EMAIL);
                email = cursor.getString(emailIndex);
            }
        } catch (Exception e) {
            Log.e("GET_EMAIL_BY_ID", "Erro ao obter email por número da conta: " + e.getMessage());
        }
        return email;
    }

    public boolean isNumeroContaInDatabase(String numeroConta) {
        try (Cursor cursor = database.query(
                NOME_TABELA,
                new String[]{COLUNA_ID},
                COLUNA_ID + " = ?",
                new String[]{numeroConta},
                null, null, null)) {

            return cursor != null && cursor.moveToFirst();
        } catch (Exception e) {
            Log.e("NUMERO_CONTA_IN_DB", "Erro ao verificar número da conta no banco de dados: " + e.getMessage());
            return false;
        }
    }
}
