package nimusc.core.request;

import nimusc.core.common.CommonException;
import okhttp3.Request;

public interface RequestHeaderParams {
    void applyToRequestBuilder(Request.Builder requestBuilder) throws CommonException;
}
