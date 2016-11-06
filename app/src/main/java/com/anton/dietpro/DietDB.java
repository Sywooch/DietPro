package com.anton.dietpro;

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
    static final String TABLE = "diet";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_LENGTH = "length";
    public SQLiteDatabase database;
    private Context myContext;

    public DietDB(Context context)
    {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
        // создаем таблицу с полями
        db.execSQL("create table diet ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "length integer unsigned,"
                + "discription text"
                + ");");
        initDB(db);
        */
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // db.execSQL("DROP TABLE IF EXISTS diet");
        //onCreate(db);
    }


    public void initDB(SQLiteDatabase db)
    {
        //инициализация БД
    }

    public void create_db(){
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

}
