package core.common;

import okhttp3.HttpUrl;

import java.util.HashMap;
import java.util.Map;

public class CommonMapParameters {
    private Map<String,String> map;

    public CommonMapParameters() {
        map = new HashMap<>();
    }

    public void addParam(String key,String value){
        map.put(key,value);
    }

    public void applyToHttpBuilder(HttpUrl.Builder urlBuilder){
        map.forEach(
                (k,v)->{
                    urlBuilder.addQueryParameter(k,v);
                }
        );
    }
}
