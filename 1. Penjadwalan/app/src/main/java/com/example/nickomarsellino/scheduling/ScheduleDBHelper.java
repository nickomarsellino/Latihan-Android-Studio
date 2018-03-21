package com.example.nickomarsellino.scheduling;

/**
 * Created by nicko marsellino on 3/18/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;


public class ScheduleDBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "todolist.db";
    private static final int DATABASE_VERSION = 1 ;


    //Tabel Si Schedule
    public static final String TABLE_SCHEDULE_NAME = "Schedule";
    public static final String COLUMN_SCHEDULE_ID = "_id";
    public static final String COLUMN_SCHEDULE_TITLE = "title";
    public static final String COLUMN_SCHEDULE_CONTENT = "content";
    public static final String COLUMN_SCHEDULE_DATE = "date";



    //Tabel si Image
    public static final String TABLE_IMAGE_NAME= "Image";
    public static final String COLUMN_IMAGE_ID = "_id";
    public static final String COLUMN_IMAGE_PATH = "path";




    public ScheduleDBHelper(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Ini Untuk Schedule Table
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_SCHEDULE_NAME + " (" +
                COLUMN_SCHEDULE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SCHEDULE_TITLE + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_CONTENT + " TEXT NOT NULL, " +
                COLUMN_SCHEDULE_DATE+ " TEXT NOT NULL);"
        );

        //Ini Untuk Image Table
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_IMAGE_NAME + " (" +
                COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_PATH+ " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //Ini Untuk Schedule
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE_NAME);

        //Ini untuk Image
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGE_NAME);
        this.onCreate(sqLiteDatabase);
    }

    public void saveNewSchedule(Schedule schedule) {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SCHEDULE_TITLE, schedule.getTitle());
        values.put(COLUMN_SCHEDULE_CONTENT, schedule.getContent());
        values.put(COLUMN_SCHEDULE_DATE, schedule.getDate());

        // insert
        sqLiteDatabase.insert(TABLE_SCHEDULE_NAME,null, values);
        sqLiteDatabase.close();
    }

    public void saveNewScheduleImage(ScheduleImage scheduleImage){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE_PATH, scheduleImage.getImage());

        // insert
        sqLiteDatabase.insert(TABLE_IMAGE_NAME,null, values);
        sqLiteDatabase.close();
    }


    public List<Schedule> schedulesList() {
        String query;

        query = "SELECT  * FROM " + TABLE_SCHEDULE_NAME;

        List<Schedule> ScheduleLinkedList = new LinkedList<>();
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        Schedule schedule;


        if (cursor.moveToFirst()) {
            do {
                schedule= new Schedule();

                schedule.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_SCHEDULE_ID)));
                schedule.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE)));
                schedule.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CONTENT)));
                schedule.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_DATE)));
                ScheduleLinkedList.add(schedule);
            } while (cursor.moveToNext());
        }

        return ScheduleLinkedList;
    }






    /**Query untuk nampilin detail sesuai yang dipilih**/
    public Schedule getSchedule(long id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_SCHEDULE_NAME + " WHERE _id="+ id;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        Schedule receivedSchedule = new Schedule();
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();

            receivedSchedule.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_TITLE)));
            receivedSchedule.setContent(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_CONTENT)));
            receivedSchedule.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_SCHEDULE_DATE)));
        }


        return receivedSchedule;
    }


    public void deleteSchedule(long id, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DELETE FROM "+TABLE_SCHEDULE_NAME+" WHERE _id='"+id+"'");
        Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();

    }


}
