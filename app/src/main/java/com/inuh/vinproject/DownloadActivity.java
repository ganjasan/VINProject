package com.inuh.vinproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.model.LoadNovel;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.provider.HelperFactory;
import com.inuh.vinproject.util.PrefManager;
import com.inuh.vinproject.view.ExpandedSearchView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DownloadActivity extends AppCompatActivity {

    private ArrayList<LoadNovel> mLoadNovels;

    RecyclerView mRecyclerView;
    LoadNovelsAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        mLoadNovels = getLoadNovels();
        mRecyclerView = (RecyclerView)findViewById(R.id.dowloaded_list);
        mAdapter = new LoadNovelsAdapter(mLoadNovels);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(PrefManager.PREF_CHANGE_NOTIFICATION);
        registerReceiver(mOnPreferenceChange, filter);
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mOnPreferenceChange);
    }

    private ArrayList<LoadNovel> getLoadNovels(){
        ArrayList<LoadNovel> loadNovels = new ArrayList<LoadNovel>();
        try {
            loadNovels = (ArrayList)HelperFactory.getHelper().getNovelDAO().getAllLoadNovels();
        }catch (SQLException sqlex){
            Toast.makeText(this,"data base error", Toast.LENGTH_SHORT);
        }
        return loadNovels;
    }

    private class LoadNovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LoadNovel mNovel;
        private TextView mNameTextView;
        private ImageView mImageView;

        private ImageButton mDeleteButton;

        public LoadNovelHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView)itemView.findViewById(R.id.load_novel_name);
            mImageView = (ImageView)itemView.findViewById(R.id.load_novel_image);
            mDeleteButton = (ImageButton) itemView.findViewById(R.id.delete_button);
            mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        deleteNovel(mNovel);
                    }catch (Exception e){
                        Toast.makeText(DownloadActivity.this, "Delete fail", Toast.LENGTH_LONG);
                    }
                }
            });
        }

        public void bindHolder(LoadNovel novel){
            mNovel = novel;
            mNameTextView.setText(novel.getName());

            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(mNovel.getImgBytes()));
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(DownloadActivity.this, OfflineReaderActivity.class);
            intent.putExtra(OfflineReaderActivity.EXTRA_NOVEL_OBJECTID, mNovel.getObjectId());
            startActivity(intent);
        }
    }

    private BroadcastReceiver mOnPreferenceChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mLoadNovels = getLoadNovels();
            mAdapter.notifyDataSetChanged();

        }
    };

    private class LoadNovelsAdapter extends RecyclerView.Adapter<LoadNovelHolder>{

        private ArrayList<LoadNovel> novelList;

        public LoadNovelsAdapter(List<LoadNovel> novels){
            novelList = (ArrayList<LoadNovel>)novels;
        }

        @Override
        public void onBindViewHolder(LoadNovelHolder holder, int position) {
            LoadNovel novel = novelList.get(position);
            holder.bindHolder(novel);
        }

        @Override
        public LoadNovelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
            View view = layoutInflater.inflate(R.layout.downloaded_novel_item, parent, false);
            return new LoadNovelHolder(view);
        }

        @Override
        public int getItemCount() {
            return  novelList.size();
        }
    }

    private void deleteNovel(LoadNovel novel) throws SQLException {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File dir = cw.getDir(novel.getObjectId(), Context.MODE_PRIVATE);

        if(dir.exists()){
            String deleteCmd = "rm -r " + dir.getAbsolutePath();
            Runtime runtime = Runtime.getRuntime();
            try {
                runtime.exec(deleteCmd);
            }catch (IOException ioe){
                Toast.makeText(this, "Delete fail", Toast.LENGTH_LONG);
            }
        }

        HelperFactory.getHelper().getNovelDAO().delete(novel);

        PrefManager.getInstance(this).deleteDownloadedNovle(novel.getObjectId());
        int pos = mLoadNovels.indexOf(novel);
        mAdapter.notifyItemRemoved(pos);
        mLoadNovels.remove(novel);

    }



}
