package com.inuh.vinproject.api.requests;

import com.inuh.vinproject.api.response.NovelResponse;
import com.inuh.vinproject.api.rest.VinRest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

/**
 * Created by artimus on 18.05.16.
 */
public class CatalogRequest extends RetrofitSpiceRequest<NovelResponse, VinRest> {

    private int mOffset;
    private String mWhereParam;
    private int mPageSize = 10;

    public CatalogRequest(int offset, String whereParam){
        super(NovelResponse.class, VinRest.class);
        mOffset = offset;
        mWhereParam = whereParam;

    }

    @Override
    public NovelResponse loadDataFromNetwork() throws Exception {
        return getService().getNovels(mOffset, mPageSize, mWhereParam);
    }
}
