package com.inuh.vinproject.api;

import com.inuh.vinproject.api.response.SourceResponse;

import retrofit.http.GET;
import retrofit.http.Headers;

/**
 * Created by artimus on 16.05.16.
 */
public interface VIN {
    @Headers({
            "application-id: 7444C9F0-61B7-8E3B-FFF7-5D156E2E3400",
            "secret-key: 2CF64E2E-F2E4-29EE-FF09-62A49BFD8000"
    })
    @GET("/v1/data/Source")
    SourceResponse getSources();
}
