package com.inuh.vinproject;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.inuh.vinproject.api.requests.PageRequest;
import com.inuh.vinproject.model.Page;
import com.inuh.vinproject.util.PrefManager;
import com.inuh.vinproject.view.FixViewPager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by artimus on 24.05.16.
 */
public class OfflineReaderActivity extends AppCompatActivity {

    public static final String EXTRA_NOVEL_OBJECTID = "com.inuh.vinproject.offlinereader.extra_novel_objectid";

    private String mNovelObjectId;
    private ArrayList<File> mFileList;
    private int mTotalPageCount;
    private File mDirectory;


    private FixViewPager mViewPager;
    private int mCurrentPageNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNovelObjectId = getIntent().getStringExtra(EXTRA_NOVEL_OBJECTID);
        mCurrentPageNumber = PrefManager.getInstance(this).getNovelsLastPage(mNovelObjectId);

        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        mDirectory = cw.getDir(mNovelObjectId, Context.MODE_PRIVATE);

        File fileList[] = mDirectory.listFiles();
        mFileList = new ArrayList<File>(Arrays.asList(fileList));
        mTotalPageCount = mFileList.size();

        mViewPager = new FixViewPager(this);
        mViewPager.setId(R.id.view_pager);
        mViewPager.setAdapter(new ReaderPageAdapter(getSupportFragmentManager()));

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mCurrentPageNumber = position;
            }
        });

        setContentView(mViewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mCurrentPageNumber < mTotalPageCount) {
            mViewPager.setCurrentItem(mCurrentPageNumber - 1);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        PrefManager.getInstance(this).setNovelsLastPage(mCurrentPageNumber, mNovelObjectId);
    }

    private class ReaderPageAdapter extends FragmentStatePagerAdapter {

        public ReaderPageAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            OfflinePageFragment pf = OfflinePageFragment.getInstance(mFileList.get(position));

            return pf;
        }

        @Override
        public int getCount() {
            return mTotalPageCount;
        }

    }






}
