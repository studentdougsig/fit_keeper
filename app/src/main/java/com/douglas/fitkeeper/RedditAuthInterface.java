package com.douglas.fitkeeper;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Douglas on 1/26/2016.
 */
public interface RedditAuthInterface {
    static final String REDDIT_BASE_URL = "https://www.reddit.com/api/v1/";
    @GET("authorize?" +
            "client_id={id}" +
            "&response_type={type}" +
            "&state={str}" +
            "&redirect_uri={uri}" +
            "&duration={dur}" +
            "&scope={scope}")
    public void redditAuth(
            @Path("id") String id,
            @Path("type") String type,
            @Path("str") String str,
            @Path("redirect_uri") String redir,
            @Path("duration") String dur,
            @Path("scope") String scope
    );
}
