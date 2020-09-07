package nimusc.vk.requests;

import nimusc.core.ParameterValues;
import nimusc.core.VkClient;
import nimusc.core.common.CommonMapParameters;
import nimusc.core.common.OfficialVkClient;
import nimusc.core.request.RequestEntity;

public class SongFinderRequestBuilder {
    private static final VkClient vkClient = OfficialVkClient.get();
    private static final String v = ParameterValues.v;
    private static final String url = "https://api.vk.com/method/audio.search";

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
