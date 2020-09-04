package core.request;

import core.common.CommonException;
import okhttp3.Request;

public interface RequestHeaderParams {
    void applyToRequestBuilder(Request.Builder requestBuilder) throws CommonException;
}
