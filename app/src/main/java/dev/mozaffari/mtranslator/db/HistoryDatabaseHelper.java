package dev.mozaffari.mtranslator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;

import dev.mozaffari.mtranslator.models.Translation;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {
    public static String TAG = "DB_CLASS";

    private static final String DATABASE_NAME = "history.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "HISTORY";

    private static final String KEY_HISTORY_ID = "id";
    private static final String KEY_HISTORY_FROM = "from_code";
    private static final String KEY_HISTORY_TO = "to_code";
    private static final String KEY_HISTORY_ORIGNAL_TEXT = "orignal_text";
    private static final String KEY_HISTORY_TRANSLATED_TEXT = "translated_text";



    private static HistoryDatabaseHelper sInstance;

    public static synchronized HistoryDatabaseHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new HistoryDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private HistoryDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_HISTORY_TABLE = "CREATE TABLE " + TABLE_NAME +
                "(" +
                KEY_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_HISTORY_FROM + " CHAR(2), " +
                KEY_HISTORY_TO + " CHAR(2), " +
                KEY_HISTORY_ORIGNAL_TEXT + " TEXT, " +
                KEY_HISTORY_TRANSLATED_TEXT + " TEXT " +
                ")";
        db.execSQL(CREATE_HISTORY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


    public void addHistory(Translation translation) {

        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {


            ContentValues values = new ContentValues();


            values.put(KEY_HISTORY_FROM, translation.getTranslateFromCode());
            values.put(KEY_HISTORY_TO, translation.getTranslateToCode());
            values.put(KEY_HISTORY_ORIGNAL_TEXT, translation.getOrignalText());
            values.put(KEY_HISTORY_TRANSLATED_TEXT, translation.getTranslatedText());

            // Notice how we haven't specified the primary key. SQLite auto increments the primary key column.
            db.insertOrThrow(TABLE_NAME, null, values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to add History to database");
        } finally {
            db.endTransaction();
        }
    }

    public void removeHistory(Integer id) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();
        try {


            db.delete(TABLE_NAME, KEY_HISTORY_ID + "=?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to remove history from database");
        } finally {
            db.endTransaction();
        }
    }

    public List<Translation> getAllHistory() {
        List<Translation> translations = new ArrayList<>();

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM %s ",
                        TABLE_NAME
                );

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {

                    Translation translation = new Translation();
                    translation.setId(cursor.getInt(cursor.getColumnIndex(KEY_HISTORY_ID)));
                    translation.setTranslateFromCode(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_FROM)));
                    translation.setTranslateToCode(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_TO)));
                    translation.setOrignalText(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_ORIGNAL_TEXT)));
                    translation.setTranslatedText(cursor.getString(cursor.getColumnIndex(KEY_HISTORY_TRANSLATED_TEXT)));


                    translations.add(translation);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return translations;
    }

    public Boolean isPostBookmarked(Integer id) {

        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM bookmarks WHERE id=%s;",
                        String.valueOf(id)
                );

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under low
        // disk space scenarios)
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);

        try {
            if (cursor.moveToFirst()) {

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            cursor.close();
            return false;
        }

    }

    // Delete all posts and users in the database
    public void deleteAllPostsAndUsers() {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            // Order of deletions is important when foreign key relationships exist.
            db.delete(TABLE_NAME, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to delete all posts and users");
        } finally {
            db.endTransaction();
        }
    }
}