package nimusc.vk.core;

import nimusc.core.ParameterValues;
import nimusc.core.VkClient;
import nimusc.core.common.CommonMapParameters;
import nimusc.core.common.OfficialVkClient;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.request.HttpUrlParameters;
import nimusc.core.request.RequestEntity;
import nimusc.core.request.RequestHeaderParameters;
import nimusc.core.request.RequestSender;

import java.util.function.Consumer;

public class VKRequest implements RequestSender {
    public final static VkClient vkClient = OfficialVkClient.get();
    public final static String v = ParameterValues.v;
    private RequestEntity requestEntity;

    private VKRequest( RequestEntity requestEntity) {
        this.requestEntity = requestEntity;
    }

    public static VKRequest buildDefault(String url, String accessToken){
        return build(
                url,
                CommonMapParameters.createEmpty()
                        .addParam("access_token",accessToken)
                        .addParam("v",v),
                CommonMapParameters.createEmpty()
                        .addParam("User-Agent",vkClient.getUserAgent())
        );
    }

    public static VKRequest build(String url,HttpUrlParameters httpUrlParameters, RequestHeaderParameters requestHeaderParameters){
        return new VKRequest(
                RequestEntity.builder()
                        .requestUrl(url)
                        .commonUrlParams(httpUrlParameters)
                        .commonRequestHeaderParams(requestHeaderParameters)
                        .build()
        );
    }

    @Override
    public void send(HttpUrlParameters userParams, Consumer<String> onResponse, Consumer<NimuscException> onError) {
        requestEntity.send(userParams,onResponse,onError);
    }
}