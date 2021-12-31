package com.joebrooks.showmethecoin.infra.model;

public enum ApiUri {
    scheme("https"),
    host("api.upbit.com"),
    ;

    private final String url;

    ApiUri(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return url;
    }

}
