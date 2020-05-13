package com.nexttech.sathethakun;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "register.db";
    private static final String TABLE_NAME = "registeruser";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

         sqLiteDatabase.execSQL("CREATE TABLE registeruser (ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE TEXT,STATUS TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public long addDrag(String dragname,String dragtype,String dragdescription,String dragsolution){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",dragname);
        contentValues.put("type",dragtype);
        contentValues.put("description",dragdescription);
        contentValues.put("solution",dragsolution);
        long status=db.insert("DRUGLIST",null,contentValues);
        db.close();
        return status;
    }
    public Cursor getDragData(){
        SQLiteDatabase database=this.getWritableDatabase();

        return database.rawQuery("SELECT * FROM DRUGLIST",null);

    }

}
