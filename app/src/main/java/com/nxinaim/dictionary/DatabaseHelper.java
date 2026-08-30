package com.nxinaim.dictionary;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
    public DatabaseHelper(Context context) {
        super(context, "dictionary.db", null, 1);
    }



    public Cursor getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        // Pass the actual table name, null for all columns, and null for selection (filter)
        Cursor cursor = db.rawQuery("select * from dictionary", null);
        return cursor;
    }
}
