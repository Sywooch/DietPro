package com.anton.dietpro.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 04.11.16.
 */

public class DietDB extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.anton.dietpro/databases/";
    private static String DB_NAME = "dietpro";
    private static final int SCHEMA = 1; // версия базы данных
    public static final String TABLE_DIET = "diet";
    public static final String TABLE_PRODUCT = "product";

    public SQLiteDatabase database;
    private Context myContext;


    public DietDB(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void create_db(){
        //TODO добавить проверку версии БД
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            File file = new File(DB_PATH + DB_NAME);
            if (!file.exists()) {
                this.getReadableDatabase();
                //получаем локальную бд как поток
                myInput = myContext.getAssets().open(DB_NAME);
                // Путь к новой бд
                String outFileName = DB_PATH + DB_NAME;

                // Открываем пустую бд
                myOutput = new FileOutputStream(outFileName);

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }

                myOutput.flush();
                myOutput.close();
                myInput.close();
            }
        }
        catch(IOException ex){

        }
    }
    public void open() throws SQLException {
        String path = DB_PATH + DB_NAME;
        database = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        super.close();
    }

    public static DietDB openDB(Context context) {
        DietDB dbHelper = new DietDB(context);
        dbHelper.create_db();
        dbHelper.open();
        return dbHelper;
    }
}
