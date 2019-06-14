package com.example.android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ListItemDataSource {

    private SQLiteDatabase database;
    private ListItemDBHelper dbHelper;


    public ListItemDataSource(Context context) {
        dbHelper = new ListItemDBHelper(context);
    }
}
