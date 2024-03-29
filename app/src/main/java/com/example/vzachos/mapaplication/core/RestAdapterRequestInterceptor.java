package com.example.vzachos.mapaplication.core;


import retrofit.RequestInterceptor;

public class RestAdapterRequestInterceptor implements RequestInterceptor {

    private UserAgentProvider userAgentProvider;

    public RestAdapterRequestInterceptor(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    @Override
    public void intercept(RequestFacade request) {

        // Add header to set content type of JSON
        request.addHeader("Content-Type", "application/json");

        // Add auth info for PARSE, normally this is where you'd add your auth info for this request (if needed).
        request.addHeader(Constants.Http.HEADER_PARSE_REST_API_KEY, Constants.Http.PARSE_REST_API_KEY);
        request.addHeader(Constants.Http.HEADER_PARSE_APP_ID, Constants.Http.PARSE_APP_ID);

        // Add the user agent to the request.
        request.addHeader("User-Agent", userAgentProvider.get());
    }
}
