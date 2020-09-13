package nimusc.core.request;

import nimusc.core.common.exception.NimuscException;
import okhttp3.Request;

public interface RequestHeaderParameters {
    void applyToRequestBuilder(Request.Builder requestBuilder) throws NimuscException;
}
