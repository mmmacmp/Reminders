package com.example.reminders;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;


public class RemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //TODO implement the function createReminder() which take the name as the content of the reminder and boolean important...note that the id will be created for you automatically
    public void createReminder(String name, boolean important) {
    SQLiteDatabase db = mDbHelper.getWritableDatabase();
    int imp;
    if  (important)
        imp=1;
    else
        imp=0;
    String queryStr="INSERT INTO "+TABLE_NAME+" ("+COL_CONTENT+","+COL_IMPORTANT+") VALUES (\""+name+"\","+imp+");";
    db.execSQL(queryStr);
    }
    //TODO overloaded to take a reminder
    public long createReminder(Reminder reminder) {
        Boolean imp;
        if (reminder.getImportant()==1)
            imp=Boolean.TRUE;
        else
            imp=Boolean.FALSE;
        createReminder(reminder.getContent(), imp);
        return 0;
    }

    //TODO implement the function fetchReminderById() to get a certain reminder given its id
    public Reminder fetchReminderById(int id) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String queryStr= "SELECT * FROM "+TABLE_NAME+" WHERE "+COL_ID+" = "+id+";";
        Cursor remind= db.rawQuery(queryStr,null);
        remind.moveToFirst();
        Reminder a=new Reminder(remind.getInt(remind.getColumnIndex(RemindersDbAdapter.COL_ID)),remind.getString(remind.getColumnIndex(RemindersDbAdapter.COL_CONTENT)),remind.getInt(remind.getColumnIndex(RemindersDbAdapter.COL_IMPORTANT)));
        return a;
    }


    //TODO implement the function fetchAllReminders() which get all reminders
    public Cursor fetchAllReminders() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String queryStr= "SELECT * FROM "+TABLE_NAME+";";
        Cursor allReminders= db.rawQuery(queryStr,null);
        return allReminders;
    }

    //TODO implement the function updateReminder() to update a certain reminder
    public void updateReminder(Reminder reminder) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String queryStr=  "UPDATE "+TABLE_NAME+" SET "+COL_CONTENT+" = \""+reminder.getContent()+"\" ,"+COL_IMPORTANT+" = "+reminder.getImportant()+" WHERE "+COL_ID+" = "+reminder.getId()+";";
        db.execSQL(queryStr);
    }
    //TODO implement the function deleteReminderById() to delete a certain reminder given its id
    public void deleteReminderById(int nId) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String queryStr= "DELETE FROM "+TABLE_NAME+" WHERE "+COL_ID+" = "+nId+";";
        db.execSQL(queryStr);
    }

    //TODO implement the function deleteAllReminders() to delete all reminders
    public void deleteAllReminders() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String queryStr= "DELETE FROM "+TABLE_NAME+";";
        db.execSQL(queryStr);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
