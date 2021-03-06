package com.inuh.vinproject;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.api.requests.CatalogRequest;
import com.inuh.vinproject.api.response.NovelResponse;
import com.inuh.vinproject.model.Novel;
import com.inuh.vinproject.util.PrefManager;
import com.inuh.vinproject.view.EndlessRecyclerViewScrollListener;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class SearchActivity extends AppCompatActivity {

    private ArrayList<Novel> mSearchList;
    private int totalCount;
    private String mSearchString;

    SpiceManager mSpiceManager = new SpiceManager(RestService.class);

    RecyclerView mRecyclerView;
    NovelsAdapter mAdapter;
    LinearLayoutManager mLinearLayoutManager;

    private Timer mTimer= new Timer();
    private final long DELAY = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mRecyclerView = (RecyclerView)findViewById(R.id.search_list);
        mSearchList = new ArrayList<Novel>();
        mAdapter = new NovelsAdapter(mSearchList);
        mRecyclerView.setAdapter(mAdapter);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLinearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (totalItemsCount < totalCount) {
                    CatalogRequest request = new CatalogRequest(totalItemsCount, getWhereClause(), getSortClause());
                    mSpiceManager.execute(request, new NovelRequestListener());
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mSpiceManager.start(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSpiceManager.shouldStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_appbar_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);


        MenuItem searchItem = menu.findItem(R.id.menu_searchview);
        final SearchView searchView = (SearchView)MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setSubmitButtonEnabled(false);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //отправить текст запроса

                mSearchString =  newText;
                mTimer.cancel();
                if (mSearchString.equals("")){
                    mSearchList.clear();
                    mAdapter.notifyDataSetChanged();

                }else {
                    mTimer = new Timer();
                    mTimer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    mSearchList.clear();
                                    if(!mSearchString.equals("")) {
                                        CatalogRequest request = new CatalogRequest(0, getWhereClause(), getSortClause());
                                        mSpiceManager.execute(request, new NovelRequestListener());
                                    }

                                }
                            },
                            DELAY
                    );
                }

                return true;
            }
        });

        return true;
    }

    public final class NovelRequestListener implements RequestListener<NovelResponse> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(SearchActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(NovelResponse response) {
            totalCount = response.getTotalObjects();
            Collection<Novel> novelsList = response.getData();
            mSearchList.addAll(novelsList);
            mAdapter.notifyDataSetChanged();
        }
    }

    private class NovelHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Novel       mNovel;
        private TextView    mNameTextView;
        private ImageView   mImageView;

        private ImageButton mFavoriteButton;
        private ImageButton mDownloadButton;

        public NovelHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);
            mNameTextView = (TextView)itemView.findViewById(R.id.novel_name);
            mImageView = (ImageView)itemView.findViewById(R.id.novel_image);

            mFavoriteButton = (ImageButton)itemView.findViewById(R.id.favorite_button);
            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefManager.getInstance(SearchActivity.this).setNovelsFavorite(mNovel.getObjectId());
                    mFavoriteButton.setVisibility(View.INVISIBLE);
                    mDownloadButton.setVisibility(View.VISIBLE);
                }
            });

            mDownloadButton = (ImageButton)itemView.findViewById(R.id.download_button);
            mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PrefManager.getInstance(SearchActivity.this).setDownloadedNovel(mNovel.getObjectId());
                    mDownloadButton.setVisibility(View.INVISIBLE);

                    Intent intent = DownloadService.newIntent(SearchActivity.this);

                    intent.putExtra(DownloadService.EXTRA_NOVEL_NAME, mNovel.getName());
                    intent.putExtra(DownloadService.EXTRA_NOVEL_OBJECTID, mNovel.getObjectId());
                    intent.putExtra(DownloadService.EXTRA_NOVEL_PAGE_TOTAL, mNovel.getPageTotal());

                    Bitmap bitmap = ((BitmapDrawable)mImageView.getDrawable()).getBitmap();
                    intent.putExtra(DownloadService.EXTRA_BITMAP, bitmap);
                    startService(intent);

                }
            });

        }

        public void bindHolder(Novel novel){
            mNovel = novel;
            mNameTextView.setText(novel.getName());
            Picasso.with(SearchActivity.this)
                    .load(novel.getImgHref())
                    .placeholder(android.R.drawable.ic_dialog_info)
                    .into(mImageView);

            mDownloadButton.setVisibility(View.INVISIBLE);
            mFavoriteButton.setVisibility(View.VISIBLE);
            if(PrefManager.getInstance(SearchActivity.this).isNovelsFavorite(mNovel.getObjectId())){
                if (PrefManager.getInstance(SearchActivity.this).isNovelDownloaded(mNovel.getObjectId()))
                {
                    mFavoriteButton.setVisibility(View.INVISIBLE);
                    mDownloadButton.setVisibility(View.INVISIBLE);
                }else{
                    mFavoriteButton.setVisibility(View.INVISIBLE);
                    mDownloadButton.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SearchActivity.this, ReaderActivity.class);
            intent.putExtra(ReaderActivity.EXTRA_NOVELS_OBJECTID, mNovel.getObjectId());
            intent.putExtra(ReaderActivity.EXTRA_TOTAL_PAGE_COUNT, mNovel.getPageTotal());
            startActivity(intent);
        }
    }

    private class NovelsAdapter extends RecyclerView.Adapter<NovelHolder>{
        private ArrayList<Novel> novelList;

        public NovelsAdapter(List<Novel> novels){
            novelList = (ArrayList<Novel>)novels;
        }

        @Override
        public void onBindViewHolder(NovelHolder holder, int position) {
            Novel novel = novelList.get(position);
            holder.bindHolder(novel);
        }

        @Override
        public NovelHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getBaseContext());
            View view = layoutInflater.inflate(R.layout.novel_item_layout, parent, false);
            return new NovelHolder(view);
        }

        @Override
        public int getItemCount() {
            return  novelList.size();
        }
    }


    private String getWhereClause(){
        String whereClause = "name LIKE '%" + mSearchString +"%'";

        return whereClause;

    }

    private String getSortClause(){
        String sortClause = PrefManager.getInstance(this).getSortFilter() + " asc";
        return sortClause;
    }
}
