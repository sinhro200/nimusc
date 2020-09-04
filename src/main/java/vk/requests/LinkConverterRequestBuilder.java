package vk.requests;

import core.ParameterValues;
import core.VkClient;
import core.common.CommonMapParameters;
import core.common.OfficialVkClient;
import core.request.RequestEntity;

public class LinkConverterRequestBuilder {
    private static final VkClient vkClient = OfficialVkClient.get();
    private static final String v = ParameterValues.v;
    private static final String url = "https://api.vk.com/method/audio.getById";

    public static RequestEntity build(String accessToken){
        CommonMapParameters  urlParams = new CommonMapParameters();
        urlParams.addParam(
                "access_token",accessToken
        );



        urlParams.addParam(
                "v",v
        );

        CommonMapParameters headerParams = new CommonMapParameters();
        headerParams.addParam("User-Agent",vkClient.getUserAgent());


        return RequestEntity.builder()
                .requestUrl(url)
                .commonUrlParams(urlParams)
                .commonRequestHeaderParams(headerParams)
                .build();
    }
}
