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
    private int mPageSize = 50;
    private String mSourtClause;

    public CatalogRequest(int offset, String whereParam, String sortClause){
        super(NovelResponse.class, VinRest.class);
        mOffset = offset;
        mWhereParam = whereParam;
        mSourtClause = sortClause;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mOffset;
        result = prime * result + mPageSize;
        result = prime * result + mWhereParam.hashCode();
        result = prime * result + mSourtClause.hashCode();

        return result;
    }

    @Override
    public NovelResponse loadDataFromNetwork() throws Exception {
        return getService().getNovels(mOffset, mPageSize, mWhereParam, mSourtClause);
    }
}
