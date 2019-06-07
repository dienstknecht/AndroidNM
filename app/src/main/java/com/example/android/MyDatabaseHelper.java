package com.example.android;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class MyDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Note_Manager";

    // Table name: Note.
    private static final String TABLE_NOTE = "Note";

    private static final String COLUMN_NOTE_ID ="Note_Id";
    private static final String COLUMN_NOTE_TITLE ="Note_Title";
    private static final String COLUMN_NOTE_CONTENT = "Note_Content";

    public MyDatabaseHelper(Context context)  {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "MyDatabaseHelper.onCreate ... ");
        // Script.
        String script = "CREATE TABLE " + TABLE_NOTE + "("
                + COLUMN_NOTE_ID + " INTEGER PRIMARY KEY," + COLUMN_NOTE_TITLE + " TEXT,"
                + COLUMN_NOTE_CONTENT + " TEXT" + ")";
        // Execute Script.
        db.execSQL(script);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTE);

        // Create tables again
        onCreate(db);
    }


    // If Note table has no data
    // default, Insert 2 records.
    public void createDefaultNotesIfNeed()  {
        int count = this.getItemsCount();
        if(count ==0 ) {
            ListItem item1 = new ListItem("",0);
            ListItem item2 = new ListItem("", 0);
            this.addItem(item1);
            this.addItem(item2);
        }
    }


    public void addItem(ListItem item) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NOTE_TITLE, item.getName());
        values.put(COLUMN_NOTE_CONTENT, item.getDuration());

        // Inserting Row
        db.insert(TABLE_NOTE, null, values);

        // Closing database connection
        db.close();
    }


    public ListItem getItem(int id) {
        Log.i(TAG, "MyDatabaseHelper.getNote ... " + id);

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NOTE, new String[] { COLUMN_NOTE_ID,
                        COLUMN_NOTE_TITLE, COLUMN_NOTE_CONTENT }, COLUMN_NOTE_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        ListItem item = new ListItem(cursor.getString(0),Integer.parseInt(cursor.getString(1)));
        // return note
        return item;
    }


    public List<ListItem> getAllListItems() {
        Log.i(TAG, "MyDatabaseHelper.getAllNotes ... " );

        List<ListItem> itemList = new ArrayList<ListItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NOTE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ListItem item = new ListItem(cursor.getString(0), Integer.parseInt(cursor.getString(0)));
                // Adding note to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }

        // return note list
        return itemList;
    }

    public int getItemsCount() {
        Log.i(TAG, "MyDatabaseHelper.getNotesCount ... " );

        String countQuery = "SELECT  * FROM " + TABLE_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();

        // return count
        return count;
    }


    public void deleteItem(ListItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTE, COLUMN_NOTE_ID + " = ?",
                new String[] { String.valueOf(item.getName()) });
        db.close();
    }

}