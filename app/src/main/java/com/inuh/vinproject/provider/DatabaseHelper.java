
package com.inuh.vinproject.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.inuh.vinproject.model.LoadNovel;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "db_vinproject.db";
    private static final int DATABASE_VERSION = 1;

    private LoadNovelDAO mNovelDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, LoadNovel.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, LoadNovel.class, true);
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() {
        super.close();
        mNovelDAO = null;
    }

    public LoadNovelDAO getNovelDAO() throws SQLException{
        if(mNovelDAO == null){
            mNovelDAO  = new LoadNovelDAO(getConnectionSource(), LoadNovel.class);
        }

        return mNovelDAO;
    }


}