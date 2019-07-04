package com.example.promisealarmfinal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.promisealarmfinal.data.Alarm;
import com.example.promisealarmfinal.data.User;

import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static MySQLiteHelper instance;

    public static final String DATABASE_NAME = "promise_alarm";
    public static final int DATABASE_VERSION = 3;

    public static final String TABLE_ALARM = "alarm";
    public static final String TABLE_ALARMUSERS = "alarm_users";

    public static final String KEY_ALARM_ID = "id";
    public static final String KEY_ALARM_TITLE = "title";
    public static final String KEY_ALARM_DESCRIPTION = "description";
    public static final String KEY_ALARM_CREATOR = "creator";
    public static final String KEY_ALARM_ALARM_DATE_TIME = "alarm_date_time";
    public static final String KEY_ALARM_END_MINUTE = "end_minute";
    public static final String KEY_ALARM_CREATED_DATE_TIME = "created_date_time";
    public static final String KEY_ALARM_MODIFIED_COUNT = "modified_count";

    public static final String KEY_ALARMUSERS_ALARM_ID = "alarm_id";
    public static final String KEY_ALARMUSERS_USER_ID = "user_id";
    public static final String KEY_ALARMUSERS_USER_NAME = "user_name";

    public static MySQLiteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new MySQLiteHelper(context.getApplicationContext());
        }

        return instance;
    }

    private MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ALARM_TABLE = "CREATE TABLE " + TABLE_ALARM +
                "(" + KEY_ALARM_ID + " INTEGER PRIMARY KEY," +
                KEY_ALARM_TITLE + " TEXT," +
                KEY_ALARM_DESCRIPTION + " TEXT," +
                KEY_ALARM_CREATOR + " TEXT," +
                KEY_ALARM_ALARM_DATE_TIME + " TEXT," +
                KEY_ALARM_END_MINUTE + " INTEGER," +
                KEY_ALARM_CREATED_DATE_TIME + " TEXT," +
                KEY_ALARM_MODIFIED_COUNT + " INTEGER" + ")";

        String CREATE_ALARMUSERS_TABLE = "CREATE TABLE " + TABLE_ALARMUSERS +
                "(" + KEY_ALARMUSERS_ALARM_ID + " INTEGER REFERENCES " + TABLE_ALARM + "," +
                KEY_ALARMUSERS_USER_ID + " TEXT," +
                KEY_ALARMUSERS_USER_NAME + " TEXT" + ")";

        db.execSQL(CREATE_ALARM_TABLE);
        db.execSQL(CREATE_ALARMUSERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARMUSERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
            onCreate(db);
        }
    }

    public void insertAlarm(Alarm alarm) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ALARM_ID, alarm.getId());
            values.put(KEY_ALARM_TITLE, alarm.getTitle());
            values.put(KEY_ALARM_DESCRIPTION, alarm.getDescription());
            values.put(KEY_ALARM_CREATOR, alarm.getCreator());
            values.put(KEY_ALARM_ALARM_DATE_TIME, alarm.getAlarmDateTimeInString());
            values.put(KEY_ALARM_END_MINUTE, alarm.getEndMinute());
            values.put(KEY_ALARM_CREATED_DATE_TIME, alarm.getCreatedDateTimeInString());
            values.put(KEY_ALARM_MODIFIED_COUNT, alarm.getModifiedCount());

            db.insertOrThrow(TABLE_ALARM, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Alarm> selectAlarm() {
        String SELECT_ALARM_QUERY =
                String.format("SELECT * FROM %s", TABLE_ALARM);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALARM_QUERY, null);

        ArrayList<Alarm> alarms = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    Alarm alarm = new Alarm();
                    alarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_ID)));
                    alarm.setTitle(cursor.getString(cursor.getColumnIndex(KEY_ALARM_TITLE)));
                    alarm.setDescription(cursor.getString(cursor.getColumnIndex(KEY_ALARM_DESCRIPTION)));
                    alarm.setCreator(cursor.getString(cursor.getColumnIndex(KEY_ALARM_CREATOR)));
                    alarm.setAlarmDateTimeOfString(cursor.getString(cursor.getColumnIndex(KEY_ALARM_ALARM_DATE_TIME)));
                    alarm.setEndMinute(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_END_MINUTE)));
                    alarm.setCreatedDateTimeOfString(cursor.getString(cursor.getColumnIndex(KEY_ALARM_CREATED_DATE_TIME)));
                    alarm.setModifiedCount(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_MODIFIED_COUNT)));

                    alarms.add(alarm);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return alarms;
    }

    public Alarm selectAlarmById(int id) {
        String SELECT_ALARM_QUERY =
                String.format("SELECT * FROM %s WHERE %s = %s", TABLE_ALARM, KEY_ALARM_ID, id);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_ALARM_QUERY, null);

        try {
            if (cursor.moveToFirst()) {
                Alarm alarm = new Alarm();
                alarm.setId(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_ID)));
                alarm.setTitle(cursor.getString(cursor.getColumnIndex(KEY_ALARM_TITLE)));
                alarm.setDescription(cursor.getString(cursor.getColumnIndex(KEY_ALARM_DESCRIPTION)));
                alarm.setCreator(cursor.getString(cursor.getColumnIndex(KEY_ALARM_CREATOR)));
                alarm.setAlarmDateTimeOfString(cursor.getString(cursor.getColumnIndex(KEY_ALARM_ALARM_DATE_TIME)));
                alarm.setEndMinute(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_END_MINUTE)));
                alarm.setCreatedDateTimeOfString(cursor.getString(cursor.getColumnIndex(KEY_ALARM_CREATED_DATE_TIME)));
                alarm.setModifiedCount(cursor.getInt(cursor.getColumnIndex(KEY_ALARM_MODIFIED_COUNT)));

                return alarm;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }

    public void insertAlarmUser(int alarmId, String userId, String userName) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();
            values.put(KEY_ALARMUSERS_ALARM_ID, alarmId);
            values.put(KEY_ALARMUSERS_USER_ID, userId);
            values.put(KEY_ALARMUSERS_USER_NAME, userName);

            db.insertOrThrow(TABLE_ALARMUSERS, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<User> selectAlarmUserById(int alarmId) {
        String query = String.format("select * from %s where %s = %s",
                TABLE_ALARMUSERS, KEY_ALARMUSERS_ALARM_ID, alarmId);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<User> users = new ArrayList<>();

        Log.d("db", cursor.getCount() + "");

        try {
            if (cursor.moveToFirst()) {
                do {
                    User user = new User();
                    user.setId(cursor.getString(cursor.getColumnIndex(KEY_ALARMUSERS_USER_ID)));
                    user.setName(cursor.getString(cursor.getColumnIndex(KEY_ALARMUSERS_USER_NAME)));

                    Log.d("db", user.getId());
                    Log.d("db", user.getName());

                    users.add(user);
                } while (cursor.moveToNext());
            }

            return users;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return null;
    }
}
