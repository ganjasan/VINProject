package com.inuh.vinproject.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.inuh.vinproject.provider.TableContracts.*;
import static android.provider.BaseColumns._ID;


public class DataProvider extends ContentProvider {

    private static final String TAG = DataProvider.class.getSimpleName();

    private DBHelper mDBHelper;
    private UriMatcher mUriMatcher;

    private static final String CONTENT_TYPE = "vnd.android.cursor.dir";

    private static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item";

    private static final int ITEMS = 1;
    private static final int ITEM_ID = 2;

    @Override
    public boolean onCreate() {

        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //add Catalogs table uri pattern
        mUriMatcher.addURI(TableContracts.AUTHORITY, TableSource.TABLE_NAME, ITEMS );
        mUriMatcher.addURI(TableContracts.AUTHORITY, TableSource.TABLE_NAME + "/#", ITEM_ID);

        this.mDBHelper = new DBHelper(this.getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = mUriMatcher.match(uri);

        if (match != ITEMS && match != ITEM_ID){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String tableName = uri.getPathSegments().get(0);

        if (mUriMatcher.match(uri) == ITEM_ID) {
            long id = Long.parseLong(uri.getPathSegments().get(1));
            selection = appendRowId(selection, id);
        }

        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.query(tableName,projection, selection,
                selectionArgs, null, null, sortOrder);


        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if (mUriMatcher.match(uri) != ITEMS) {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String tableName = uri.getPathSegments().get(0);

        long id = db.insertOrThrow(tableName, null, values);

        Uri newUri = ContentUris.withAppendedId(TableContracts.getContentURI(tableName), id);

        Log.d(TAG, "New content URI: " + newUri.toString());

        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        if(mUriMatcher.match(uri) != ITEMS){
            throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String tableName = uri.getPathSegments().get(0);

        long id = db.update(tableName, values, selection, selectionArgs);
        Uri newUri = ContentUris.withAppendedId(TableContracts.getContentURI(tableName), id);

        Log.d(TAG, "update content URI: " + newUri.toString());

        getContext().getContentResolver().notifyChange(newUri, null);
        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //not response
        return 0;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        switch (mUriMatcher.match(uri)) {
            case ITEMS:
                return CONTENT_TYPE;
            case ITEM_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    private String appendRowId(String selection, long id) {
        return _ID
                + "="
                + id
                + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')'
                : "");
    }
}
