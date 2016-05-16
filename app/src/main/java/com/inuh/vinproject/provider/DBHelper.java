package com.inuh.vinproject.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.inuh.vinproject.provider.TableContracts.*;
/**
 * Created by artimus on 16.05.16.
 */
public class DBHelper extends SQLiteOpenHelper {

    public final String TAG  = getClass().getSimpleName();

    private final static String DB_NAME = "vnr.db";
    private final static int version = 1;

    public DBHelper(Context context){
        super(context, DB_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //CREATE SOURCE TABLE
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + TableSource.TABLE_NAME + "(");
        sqlBuilder.append(TableSource._ID + " INTEGER, ");
        sqlBuilder.append(TableSource._CREATED + " INTEGER, ");
        sqlBuilder.append(TableSource._UPDATED + " INTERGER, ");
        sqlBuilder.append(TableSource._RESULT + " TEXT, ");
        sqlBuilder.append(TableSource._STATUS + " TEXT, ");
        sqlBuilder.append(TableSource._OBJECTID + " TEXT, ");
        sqlBuilder.append(TableSource._ACTIVE + " INTEGER, ");
        sqlBuilder.append(TableSource.NAME + " TEXT, ");
        sqlBuilder.append(TableSource.DESCRIPTION + " TEXT,");
        sqlBuilder.append(TableSource.HREF + " TEXT ");
        sqlBuilder.append(");");
        String sql = sqlBuilder.toString();
        Log.i(TAG, "Creating DB table with string: '" + sql + "'");
        db.execSQL(sql);

        //CREATE NOVELS TABLE
        sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE " + TableNovels.TABLE_NAME + "(");
        sqlBuilder.append(TableNovels._ID + " INTEGER, ");
        sqlBuilder.append(TableNovels._CREATED + " INTEGER, ");
        sqlBuilder.append(TableNovels._UPDATED + " INTEGER, ");
        sqlBuilder.append(TableNovels.NAME + " TEXt, ");
        sqlBuilder.append(TableNovels.DESCRIPTION + " TEXT ");
        sqlBuilder.append(")");
        sql = sqlBuilder.toString();
        Log.i(TAG, "Creating DB table with string: '" + sql + "'");
        db.execSQL(sql);

        //CREATE CHAPTERS TABLE
        sqlBuilder.append("CREATE TABLE " + TableChapters.TABLE_NAME + "(");
        sqlBuilder.append(TableChapters._ID + " INTEGER, ");
        sqlBuilder.append(TableChapters._CREATED + " INTEGER, ");
        sqlBuilder.append(TableChapters._UPDATED + " INTEGER, ");
        sqlBuilder.append(TableChapters.NAME + " TEXt, ");
        sqlBuilder.append(TableChapters.DESCRIPTION + " TEXT, ");
        sqlBuilder.append(TableChapters.NOVELS_ID + " TEXT, ");
        sqlBuilder.append(TableChapters.FIRSTPAGE + " TEXT ");
        sqlBuilder.append(")");
        sql = sqlBuilder.toString();
        Log.i(TAG, "Creating DB table with string: '" + sql + "'");
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}