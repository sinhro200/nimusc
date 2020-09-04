package core.common;

import core.request.RequestHeaderParams;
import core.request.HttpUrlParameters;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;

public class CommonMapParameters implements RequestHeaderParams, HttpUrlParameters {
    private Map<String,String> map;

    public CommonMapParameters() {
        map = new HashMap<>();
    }

    public void addParam(String key,String value){
        map.put(key,value);
    }

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws CommonException{
        map.forEach(
                (k,v)->{
                    urlBuilder.addQueryParameter(k,v);
                }
        );
    }

    @Override
    public void applyToRequestBuilder(Request.Builder requestBuilder) throws CommonException {
        map.forEach(
                (k,v)->{
                    requestBuilder.header(k,v);
                }
        );
    }
}
