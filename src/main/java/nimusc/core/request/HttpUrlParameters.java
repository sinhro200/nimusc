package nimusc.core.request;

import nimusc.core.common.exception.NimuscException;
import okhttp3.HttpUrl;

public interface HttpUrlParameters {
    void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException;
    HttpUrlParameters addParameter(String key,String value);
}
