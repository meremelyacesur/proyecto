package com.example.proyecto_login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{

    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table productos(ID INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, kcal real)");
        db.execSQL("create table registro_consumo(ID INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, kcalcontado real, fecha real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVerison) {
        db.execSQL("drop table if exists productos");
        db.execSQL("create table productos(ID INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, kcal real)");
    }


}
