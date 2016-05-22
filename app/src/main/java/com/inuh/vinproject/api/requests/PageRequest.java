package com.inuh.vinproject.api.requests;

import com.inuh.vinproject.api.response.PageResponse;
import com.inuh.vinproject.api.rest.VinRest;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

/**
 * Created by artimus on 19.05.16.
 */
public class PageRequest extends RetrofitSpiceRequest<PageResponse, VinRest> {

    private int offset;
    private int pageSize = 10;
    private String whereClause;
    private String sortBy = "number asc";

    public PageRequest(int offset, String whereClause){
        super(PageResponse.class, VinRest.class);
        this.offset = offset;
        this.whereClause = whereClause;
    }

    public PageRequest(int offset, int pageSize, String whereClause){
        super(PageResponse.class, VinRest.class);
        this.offset = offset;
        this.whereClause = whereClause;
        this.pageSize = pageSize;
    }


    @Override
    public PageResponse loadDataFromNetwork() throws Exception {
        return getService().getPages(offset,pageSize,whereClause,sortBy);
    }
}
