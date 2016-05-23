package com.inuh.vinproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inuh.vinproject.model.LoadNovel;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.provider.HelperFactory;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
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

    private ArrayList<LoadNovel> getLoadNovels(){
        ArrayList<LoadNovel> loadNovels = new ArrayList<LoadNovel>();
        try {
            loadNovels = (ArrayList)HelperFactory.getHelper().getNovelDAO().getAllLoadNovels();
        }catch (SQLException sqlex){
            //TODO сообзить об ощибке
        }
        return loadNovels;
    }

    private class LoadNovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private LoadNovel mNovel;
        private TextView mNameTextView;
        private ImageView mImageView;

        public LoadNovelHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView)itemView.findViewById(R.id.load_novel_name);
            mImageView = (ImageView)itemView.findViewById(R.id.load_novel_image);
        }

        public void bindHolder(LoadNovel novel){
            mNovel = novel;
            mNameTextView.setText(novel.getName());

            Bitmap bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(mNovel.getImgBytes()));
            mImageView.setImageBitmap(bitmap);
        }

        @Override
        public void onClick(View v) {
            //запустить LoadReader
        }
    }

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



}
