package com.example.bancodip.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.bancodip.model.ModelBancoDados;

public class ControllerBancoDados {

    private final Context context;
    private SQLiteDatabase database;
    private final ModelBancoDados dbHelper;

    public ControllerBancoDados(Context context) {
        this.context = context;
        dbHelper = new ModelBancoDados(context);
    }

    public ControllerBancoDados open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insertData(String name, String email, double saldo, double chequeEspecial, double chequeEspecialDefi) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ModelBancoDados.COLUNA_TITULAR, name);
        contentValues.put(ModelBancoDados.COLUNA_EMAIL, email);
        contentValues.put(ModelBancoDados.COLUNA_SALDO, saldo);
        contentValues.put(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL_DEFI, chequeEspecialDefi);
        contentValues.put(ModelBancoDados.COLUNA_CHEQUE_ESPECIAL, chequeEspecial);

        long result = -1;

        try {
            result = database.insertOrThrow(ModelBancoDados.NOME_TABELA, null, contentValues);
            Log.d("INSERT_DATA", "Inserção bem-sucedida. ID do novo registro: " + result);
        } catch (SQLException e) {
            Log.e("INSERT_DATA", "Erro ao inserir dados: " + e.getMessage());
        }

        return result;
    }

    // Outros métodos de inserção, atualização e consulta de dados omitidos para brevidade

    public boolean isNumeroContaInDatabase(String numeroConta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {ModelBancoDados.COLUNA_ID};
        String selection = ModelBancoDados.COLUNA_ID + " = ?";
        String[] selectionArgs = {numeroConta};
        String limit = "1"; // Limitar a 1 registro para verificar apenas se existe
        Cursor cursor = db.query(ModelBancoDados.NOME_TABELA, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

    public String getNomeByNumeroConta(String numeroConta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {ModelBancoDados.COLUNA_TITULAR};
        String selection = ModelBancoDados.COLUNA_ID + " = ?";
        String[] selectionArgs = {numeroConta};
        Cursor cursor = db.query(ModelBancoDados.NOME_TABELA, columns, selection, selectionArgs, null, null, null);
        String nome = null;
        if (cursor != null && cursor.moveToFirst()) {
            int nomeIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_TITULAR);
            if (nomeIndex >= 0) {
                nome = cursor.getString(nomeIndex);
            }
            cursor.close();
        }
        return nome;
    }

    public String getEmailByNumeroConta(String numeroConta) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {ModelBancoDados.COLUNA_EMAIL};
        String selection = ModelBancoDados.COLUNA_ID + " = ?";
        String[] selectionArgs = {numeroConta};
        Cursor cursor = db.query(ModelBancoDados.NOME_TABELA, columns, selection, selectionArgs, null, null, null);
        String email = null;
        if (cursor != null && cursor.moveToFirst()) {
            int emailIndex = cursor.getColumnIndex(ModelBancoDados.COLUNA_EMAIL);
            if (emailIndex >= 0) {
                email = cursor.getString(emailIndex);
            }
            cursor.close();
        }
        return email;
    }
}
