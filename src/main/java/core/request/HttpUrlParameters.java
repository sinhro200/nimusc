package core.request;

import core.common.CommonException;
import okhttp3.HttpUrl;

public interface HttpUrlParameters {
    void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws CommonException;
}
