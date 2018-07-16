package com.joy.todolist3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DbAdapter {
    public static final String KEY_ID = "_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_NAME = "name";
    public static final String KEY_ALARM = "alarm";
    public static final String KEY_BGCOLOR = "bgcolor";
    private static final String TABLE_NAME = "todoList2";
    private DbHelper mDbHelper;
    private SQLiteDatabase mdb;
    private final Context mCtx;
    private ContentValues values;

    public DbAdapter(Context mCtx) {
        this.mCtx = mCtx;
        open();
    }

    public void open() {
        mDbHelper = new DbHelper(mCtx);
        mdb = mDbHelper.getWritableDatabase();
        Log.i("DB_open",mdb.toString());
    }

    public void close(){
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
    public Cursor listContacts(){
        Cursor mCursor = mdb.query(TABLE_NAME, new String[]{KEY_ID, KEY_DATE,KEY_NAME, KEY_ALARM, KEY_BGCOLOR},
                null,null,null,null,null);
        if(mCursor != null){
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public long createContacts(String date, String name, String alarm, String bgcolor ){

            long rowsAffected = -1;
        try{
            values = new ContentValues();
            values.put(KEY_DATE, date);
            values.put(KEY_NAME, name);
            values.put(KEY_ALARM, alarm);
            values.put(KEY_BGCOLOR, bgcolor);

            rowsAffected = mdb.insert(TABLE_NAME,null,values);
            Log.i("DB_Insert_rowsAffected", Long.toString(rowsAffected));
            return rowsAffected;
        } catch (Exception e) {
            Log.i("DB_新增失敗", e.toString());
            Toast.makeText(mCtx,"新增失敗!", Toast.LENGTH_SHORT).show();
            return rowsAffected;
          }
        finally {
            if (rowsAffected == -1)
                Toast.makeText(mCtx,"新增失敗!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mCtx,"新增成功!", Toast.LENGTH_SHORT).show();
        }
    }
    public long updateContacts(int id, String date, String name, String alarm, String bgcolor){
        long rowsAffected = 0;
        try{
             values = new ContentValues();
             values.put(KEY_DATE, date);
             values.put(KEY_NAME, name);
             values.put(KEY_ALARM, alarm);
             values.put(KEY_BGCOLOR, bgcolor);

             rowsAffected = mdb.update(TABLE_NAME, values, "_id=" + id,null);
             Log.i("DB_Update_rowsAffected", Long.toString(rowsAffected));
             return rowsAffected;
        }catch(SQLException e){
              Log.i("DB_修改失敗", e.toString());
              Toast.makeText(mCtx,"修改失敗!", Toast.LENGTH_SHORT).show();
              return -1;
        }finally {
            if (rowsAffected == 1)
                Toast.makeText(mCtx,"修改成功!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(mCtx,"修改失敗!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean deleteContacts(int id){
        String[] args = {Integer.toString(id)};
        long rowsAffected = 0;
        rowsAffected = mdb.delete(TABLE_NAME, "_id= ?",args);
        Log.i("DB_Delete_rowsAffected", Long.toString(rowsAffected));
        if (rowsAffected == 1)
            Toast.makeText(mCtx,"刪除成功!", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(mCtx,"刪除失敗!", Toast.LENGTH_SHORT).show();
        return true;
    }

    public Cursor queryById(int item_id){
//        Log.i("DB_DbAdapter_queryByName",item_id);
        Cursor mCursor = mdb.query(TABLE_NAME, new String[] {KEY_ID, KEY_DATE,KEY_NAME, KEY_ALARM, KEY_BGCOLOR},
                KEY_ID + "=" + item_id, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }
}
