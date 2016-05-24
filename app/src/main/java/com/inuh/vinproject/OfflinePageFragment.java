package com.inuh.vinproject;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.inuh.vinproject.model.Page;
import com.inuh.vinproject.util.OkPicasso;

import java.io.File;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by artimus on 24.05.16.
 */
public class OfflinePageFragment extends Fragment {

    private static final String CURRENT_PAGE_FILE = "com.inuh.vinproject.offlinepagefragment.current_page_file";

    private PhotoView mImageView;

    private File mFile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page, container, false);

        mImageView = (PhotoView)v.findViewById(R.id.page_image);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }

        return v;

    }

    public static OfflinePageFragment getInstance(File file){
        Bundle bundle = new Bundle();
        OfflinePageFragment pf = new OfflinePageFragment();
        bundle.putSerializable(CURRENT_PAGE_FILE, file);
        pf.setArguments(bundle);
        return pf;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFile = (File)getArguments().getSerializable(CURRENT_PAGE_FILE);
        OkPicasso.getPicassoInstance(getActivity())
                .load(mFile)
                .into(mImageView);
    }
}
