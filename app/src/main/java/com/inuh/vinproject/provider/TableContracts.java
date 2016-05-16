package com.inuh.vinproject.provider;


import android.net.Uri;
import android.os.ResultReceiver;
import android.provider.BaseColumns;

public final class TableContracts {
    public final static String AUTHORITY = "com.inuh.vnreader.provider";
    public final static String SCHEME = "content://";
    public final static String URI_PREFIX = SCHEME + AUTHORITY;

    public final static class TableSource implements ResourceTable{

        public final static String TABLE_NAME = "Source";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);


        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String HREF = "href";

        private TableSource(){

        }
    }

    public static Uri getContentURI(String tableName){
        return Uri.parse(URI_PREFIX +"/"+tableName);
    }

    public final static class TableNovels implements ResourceTable{
        public final static String TABLE_NAME = "Novels";

        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String SOURCE_ID = "sourceId";

        private TableNovels(){

        }
    }

    public final static class TableChapters implements ResourceTable{
        public final static String TABLE_NAME = "Chapter";

        public static final  Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

        public final static String NAME = "name";
        public final static String DESCRIPTION = "description";
        public final static String FIRSTPAGE = "firstPage";
        public final static String NOVELS_ID = "novelsId";
    }
}