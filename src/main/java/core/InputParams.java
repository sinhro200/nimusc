package core;

import okhttp3.HttpUrl;

public interface InputParams {
    boolean applyToHttpBuilder(HttpUrl.Builder urlBuilder) ;
}
