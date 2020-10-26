package nimusc.core.authorization;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import nimusc.core.request.RequestHeaderParameters;
import okhttp3.HttpUrl;
import okhttp3.Request;

public interface Authorization extends HttpUrlParameters, RequestHeaderParameters {

    @Override
    void applyToHttpBuilder(HttpUrl.Builder urlBuilder) ;

    @Override
    void applyToRequestBuilder(Request.Builder requestBuilder) ;
}
