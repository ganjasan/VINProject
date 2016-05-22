package com.inuh.vinproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Toast;

import com.inuh.vinproject.api.RestService;
import com.inuh.vinproject.api.requests.PageRequest;
import com.inuh.vinproject.api.response.PageResponse;
import com.inuh.vinproject.model.Page;
import com.inuh.vinproject.util.OkPicasso;
import com.inuh.vinproject.util.TouchImageView;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.Collection;

import uk.co.senab.photoview.PhotoView;


/**
 * Created by artimus on 19.05.16.
 */
public class PageFragment extends Fragment {

    private static final String CURRENT_PAGE = "com.inuh.vinproject.pagefragment.current_page";
    private static final String CURRENT_NOVEL_OBJECTID = "com.inuh.vinproject.pagefragment.current_novel_objectid";
    private static final String CURRENT_PAGE_NUMBER = "com.inuh.vinproject.pagefragment.current_page_number";

    //private WebView mWebView;
    //private TouchImageView mImageView;
    private PhotoView mImageView;


    private Page    mCurrentPage;
    private int     mCurrentPageNumber;
    private String  mNovelsObjectId;

    private SpiceManager mSpiceManager = new SpiceManager(RestService.class);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_page,container,false);

        mImageView = (PhotoView)v.findViewById(R.id.page_image);

        return v;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments().containsKey(CURRENT_PAGE)) {
            mCurrentPage = (Page) getArguments().getSerializable(CURRENT_PAGE);
            //TODO не забыть перезагрузить страницы
            String imgHref = "http://acomics.ru" + mCurrentPage.getImgHref();
            OkPicasso.getPicassoInstance(getActivity())
                    .load(imgHref)
                    .into(mImageView);
        }else{
            mCurrentPageNumber = getArguments().getInt(CURRENT_PAGE_NUMBER);
            mNovelsObjectId = getArguments().getString(CURRENT_NOVEL_OBJECTID);
            mSpiceManager.start(getActivity());
            mSpiceManager.execute(new PageRequest(mCurrentPageNumber, 1, getWhereClause()), new PageRequestListener());
        }

    }

    private class PageRequestListener implements RequestListener<PageResponse> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(PageResponse pageResponse) {
            Collection<Page> pages = pageResponse.getData();
            if (pages.iterator().hasNext()){
                mCurrentPage = pages.iterator().next();
                String imgHref = "http://acomics.ru" + mCurrentPage.getImgHref();
                OkPicasso.getPicassoInstance(getActivity())
                        .load(imgHref)
                        .into(mImageView);
            }
        }
    }

    public static PageFragment getInstance(Page page){
        Bundle bundle = new Bundle();
        PageFragment pf = new PageFragment();
        bundle.putSerializable(CURRENT_PAGE, page);
        pf.setArguments(bundle);
        return pf;
    }

    public static PageFragment getInstance(String novelsObjectId, int pageNumber){
        Bundle bundle = new Bundle();
        PageFragment pf = new PageFragment();
        bundle.putString(CURRENT_NOVEL_OBJECTID, novelsObjectId);
        bundle.putInt(CURRENT_PAGE_NUMBER, pageNumber);
        pf.setArguments(bundle);
        return pf;
    }

    private String getWhereClause(){
        String selectNovelWhereClause = "Novels[pages].objectId='" + mNovelsObjectId +"'";

        return selectNovelWhereClause;
    }

}