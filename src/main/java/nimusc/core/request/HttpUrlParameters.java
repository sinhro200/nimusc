package nimusc.core.request;

import nimusc.core.common.CommonException;
import okhttp3.HttpUrl;

public interface HttpUrlParameters {
    void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws CommonException;
}
