package com.inuh.vinproject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.api.requests.PageRequest;
import com.inuh.vinproject.api.response.PageResponse;
import com.inuh.vinproject.api.rest.VinRest;
import com.inuh.vinproject.model.LoadNovel;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.model.Page;
import com.inuh.vinproject.provider.HelperFactory;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.datatype.Duration;

import retrofit.RestAdapter;


public class DownloadService extends IntentService {
    public static final String EXTRA_NOVEL_NAME = "com.inuh.vinproject.downloadservice.extra_novel_name";
    public static final String EXTRA_NOVEL_OBJECTID = "com.inuh.vinproject.downloadservice.extra_novel_objectID";
    public static final String EXTRA_NOVEL_PAGE_TOTAL = "com.inuh.vinproject.downloadservice.extra_novel_page_total";
    public static final String EXTRA_BITMAP = "com.inuh.vinptoject.downloadservice.extra_drawable";

    private static final String TAG = "DownloadService";

    private int notificationId = 1;

    private String mNovelObjectId;
    private String mNovelName;
    private int    mTotalPageCount;

    private File mDirectory;

    private NotificationManager  mNotifyManager;
    private NotificationCompat.Builder mBuilder;


    public DownloadService(){
        super(TAG);
    }

    public static Intent newIntent(Context context){
        return new Intent(context, DownloadService.class);

    }

    @Override
    protected void onHandleIntent(Intent intent) {


        String novelName = (String) intent.getStringExtra(EXTRA_NOVEL_NAME);
        mNovelObjectId = (String) intent.getStringExtra(EXTRA_NOVEL_OBJECTID);
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra(EXTRA_BITMAP);
        mTotalPageCount = intent.getIntExtra(EXTRA_NOVEL_PAGE_TOTAL, 0);

        setNotification();

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        mDirectory = cw.getDir(mNovelObjectId, Context.MODE_PRIVATE);

        Novel novel = new Novel();
        novel.setName(novelName);
        novel.setObjectId(mNovelObjectId);

        try {
            saveImages(mNovelObjectId);
            saveNovelsToDB(novel, bitmap);

            mBuilder.setContentText("Download complete")
                    .setProgress(0,0,false);
            mNotifyManager.notify(notificationId, mBuilder.build());

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Download fail", Toast.LENGTH_LONG);
            mBuilder.setContentText("Download fail")
                    .setProgress(0,0,false);
            mNotifyManager.notify(notificationId, mBuilder.build());
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

    private void saveImages(String novelObjectId) throws IOException{

        RestAdapter restAdpter = new RestAdapter.Builder()
                .setEndpoint("https://api.backendless.com/")
                .build();
        VinRest service = restAdpter.create(VinRest.class);

        int offset = 0;
        try {
            while (offset < mTotalPageCount) {
                PageResponse response = service.getPages(0, 50, getWhereClause(), "number");
                for (Page page:response.getData()) {
                    String imgHref = "http://" + page.getImgHref();
                    String destinationFileName = mNovelObjectId + page.getNumber() + ".png";
                    File destinationFile = new File(mDirectory, destinationFileName);

                    saveImage(imgHref, destinationFile);
                    offset++;

                    mBuilder.setProgress(mTotalPageCount, offset, false);
                    mNotifyManager.notify(notificationId, mBuilder.build());
                }
            }
        }
        catch (Exception e){
            throw e;
        }



    }

    private void saveImage(String imgHref, File destinationFile) throws IOException{

        InputStream is = null;
        OutputStream os = null;
        try {
            URL url = new URL(imgHref);
            is = url.openStream();
            os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }
        }catch (IOException ex){
            throw  ex;
        }finally {
            is.close();
            os.close();
        }


    }

    private String getWhereClause(){
      return  "Novels[pages].objectId='" + mNovelObjectId +"'";
    }

    private void setNotification(){
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentText("Novel Download")
                .setContentTitle("Title")
                .setSmallIcon(android.R.drawable.ic_notification_clear_all);
    }
}
