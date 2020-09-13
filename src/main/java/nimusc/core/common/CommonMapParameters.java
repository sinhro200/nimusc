package nimusc.core.common;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.RequestHeaderParameters;
import nimusc.core.request.HttpUrlParameters;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.util.HashMap;
import java.util.Map;

public class CommonMapParameters implements RequestHeaderParameters, HttpUrlParameters {
    private Map<String,String> map;

    public CommonMapParameters() {
        map = new HashMap<>();
    }

    public static CommonMapParameters createEmpty(){
        return new CommonMapParameters();
    }

    public CommonMapParameters addParam(String key,String value){
        map.put(key,value);
        return this;
    }

    @Override
    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) throws NimuscException {
        map.forEach(
                (k,v)->{
                    urlBuilder.addQueryParameter(k,v);
                }
        );
    }

    @Override
    public void applyToRequestBuilder(Request.Builder requestBuilder) throws NimuscException {
        map.forEach(
                (k,v)->{
                    requestBuilder.header(k,v);
                }
        );
    }
}
