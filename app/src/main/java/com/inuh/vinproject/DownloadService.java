package com.inuh.vinproject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.inuh.vinproject.model.LoadNovel;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.provider.HelperFactory;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;


public class DownloadService extends IntentService {
    public static final String EXTRA_NOVEL_NAME = "com.inuh.vinproject.downloadservice.extra_novel_name";
    public static final String EXTRA_NOVEL_OBJECTID = "com.inuh.vinproject.downloadservice.extra_novel_objectID";
    public static final String EXTRA_BITMAP = "com.inuh.vinptoject.downloadservice.extra_drawable";

    private static final String TAG = "DownloadService";


    public DownloadService(){
        super(TAG);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, DownloadService.class);

    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String novelName = (String) intent.getStringExtra(EXTRA_NOVEL_NAME);
        String novelObjectId = (String) intent.getStringExtra(EXTRA_NOVEL_OBJECTID);
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra(EXTRA_BITMAP);

        Novel novel = new Novel();
        novel.setName(novelName);
        novel.setObjectId(novelObjectId);

        try {
            saveNovelsToDB(novel, bitmap);
        }catch (SQLException sqlex){
            //TODO отменить транзакцию сообщить, что все плохо
        }
    }

    private void saveNovelsToDB(Novel novel, Bitmap bitmap) throws SQLException{

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100 ,stream);
        byte[] imgData = stream.toByteArray();

        LoadNovel loadNovel = new LoadNovel(novel);
        loadNovel.setImgBytes(imgData);

        HelperFactory.getHelper().getNovelDAO().create(loadNovel);
    }
}
