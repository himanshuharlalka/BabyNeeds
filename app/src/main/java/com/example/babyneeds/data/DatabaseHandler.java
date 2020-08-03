package com.example.babyneeds.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.babyneeds.R;
import com.example.babyneeds.model.Item;
import com.example.babyneeds.util.Util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE="CREATE TABLE "+Util.TABLE_NAME+"("+Util.KEY_ID+" INTEGER PRIMARY KEY,"
                +Util.KEY_NAME+" TEXT ,"+Util.KEY_QUANTITY+" INTEGER ,"+Util.KEY_COLOR+" TEXT ,"
                +Util.KEY_SIZE+" TEXT ,"+Util.KEY_DATE+" LONG"+")";
        sqLiteDatabase.execSQL(CREATE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String DROP_TABLE= String.valueOf("DROP TABLE IF EXISTS");
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        onCreate(sqLiteDatabase);

    }
    public void addItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Util.KEY_NAME,item.getItemName());
        values.put(Util.KEY_QUANTITY,item.getItemQuantity());
        values.put(Util.KEY_COLOR,item.getItemColor());
        values.put(Util.KEY_SIZE,item.getItemSize());
        values.put(Util.KEY_DATE,java.lang.System.currentTimeMillis());

        db.insert(Util.TABLE_NAME,null,values);

        db.close();
    }
    public Item getItem(int id){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.query(Util.TABLE_NAME,new String[]{Util.KEY_ID,Util.KEY_NAME,Util.KEY_QUANTITY,Util.KEY_COLOR,Util.KEY_SIZE
        ,Util.KEY_DATE},Util.KEY_ID+"=?", new String[]{String.valueOf(id)},null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        Item item=new Item();

        item.setId(cursor.getInt(0));
        item.setItemName(cursor.getString(cursor.getColumnIndex(Util.KEY_NAME)));
        item.setItemQuantity(cursor.getLong(2));
        item.setItemColor(cursor.getString(3));
        item.setItemSize(cursor.getString(4));
        DateFormat dateFormat=DateFormat.getDateInstance();
        String formattedDate=dateFormat.format(new Date(cursor.getLong(5)).getTime());
        item.setDateItemAdded(formattedDate);
        return item;

    }

    public List<Item> getAllItems(){
        List<Item> itemList=new ArrayList<>();
        SQLiteDatabase db=this.getReadableDatabase();
        String selectAll="SELECT * FROM "+Util.TABLE_NAME;
        Cursor cursor=db.query(Util.TABLE_NAME,new String[]{Util.KEY_ID,Util.KEY_NAME,Util.KEY_QUANTITY,Util.KEY_COLOR,Util.KEY_SIZE
        ,Util.KEY_DATE},null,null,null,null,Util.KEY_DATE+" DESC");
        if(cursor.moveToFirst()) {
            do {
                Item item =new Item();
                item.setId(cursor.getInt(0));
                item.setItemName(cursor.getString(1));
                item.setItemQuantity(cursor.getLong(2));
                item.setItemColor(cursor.getString(3));
                item.setItemSize(cursor.getString(4));
                DateFormat dateFormat=DateFormat.getDateInstance();
                String formattedDate=dateFormat.format(new Date(cursor.getLong(5)).getTime());
                item.setDateItemAdded(formattedDate);
                itemList.add(item);

            }while (cursor.moveToNext());
        }
        return itemList;
    }
    public void deleteItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Util.TABLE_NAME,Util.KEY_ID+"=?",new String[]{String.valueOf(item.getId())});
        db.close();
    }
    public void deleteAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+Util.TABLE_NAME);
    }
    public int updateItem(Item item){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(Util.KEY_NAME,item.getItemName());
        values.put(Util.KEY_QUANTITY,item.getItemQuantity());
        values.put(Util.KEY_COLOR,item.getItemColor());
        values.put(Util.KEY_SIZE,item.getItemSize());
        return db.update(Util.TABLE_NAME,values,Util.KEY_ID+"=?",new String[]{String.valueOf(item.getId())});


    }
    public int getCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        String countQuery="SELECT * FROM "+Util.TABLE_NAME;
        Cursor cursor=db.rawQuery(countQuery,null);
        return cursor.getCount();

    }
}
