package com.anton.dietpro;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 04.11.16.
 */

public class DietDB extends SQLiteOpenHelper {

    final String LOG_TAG = "myLog";

    public DietDB(Context context)
    {
        super(context, "DietDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(LOG_TAG, "--- onCreate database ---");
        // создаем таблицу с полями
        db.execSQL("create table diet ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "email text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void init()
    {
        Log.d(LOG_TAG,"---Insert to table---");
        // создаем объект для данных
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        String name = "first name";
        String email = "first email";
        cv.put("name", name);
        cv.put("email", email);
        // вставляем запись и получаем ее ID
        long rowID = db.insert("diet", null, cv);
        Log.d(LOG_TAG,"---Insert "+rowID+" row ---");

        cv.clear();
        name = "second name";
        email = "second email";
        cv.put("name", name);
        cv.put("email", email);
        // вставляем запись и получаем ее ID
        rowID = db.insert("diet", null, cv);
        Log.d(LOG_TAG,"---Insert "+rowID+" row ---");

        Log.d(LOG_TAG, "--- Rows in mytable: ---");
        // делаем запрос всех данных из таблицы mytable, получаем Cursor
        Cursor c = db.query("diet", null, null, null, null, null, null);

        // ставим позицию курсора на первую строку выборки
        // если в выборке нет строк, вернется false
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("id");
            int nameColIndex = c.getColumnIndex("name");
            int emailColIndex = c.getColumnIndex("email");

            do {
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", email = " + c.getString(emailColIndex));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else
            Log.d(LOG_TAG, "0 rows");
        c.close();
        this.close();
    }
}
