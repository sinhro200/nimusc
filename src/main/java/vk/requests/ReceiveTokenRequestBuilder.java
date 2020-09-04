package vk.requests;

import core.ParameterValues;
import core.VkClient;
import core.common.CommonMapParameters;
import core.common.OfficialVkClient;
import core.request.RequestEntity;
import vk.Utils;

public class ReceiveTokenRequestBuilder {
    private static final VkClient vkClient = OfficialVkClient.get();
    private static final String v = ParameterValues.v;

    public static RequestEntity build() {
        String url = "https://oauth.vk.com/token";
        CommonMapParameters commonMapParameters = new CommonMapParameters();

        String auth_code = "GET_CODE";
        String device_id = Utils.generate_random_string(16, "0123456789abcdef");

        commonMapParameters.addParam("grant_type","password");
        commonMapParameters.addParam("client_id", vkClient.getClientId());
        commonMapParameters.addParam("client_secret", vkClient.getClientSecret());


        commonMapParameters.addParam("v",v);
        commonMapParameters.addParam("lang","en" );
        commonMapParameters.addParam("scope", "all");
        commonMapParameters.addParam("device_id", device_id);
        addTwoFactorPart(commonMapParameters,auth_code);

        return RequestEntity.builder()
                .requestUrl(url)
                .commonUrlParams(commonMapParameters)
                .build();
    }

    private static void addTwoFactorPart(CommonMapParameters commonMapParameters, String code){
        if (code != null){
            commonMapParameters.addParam("2fa_supported","1");
            commonMapParameters.addParam("force_sms","1");
            if (!code.equals("GET_CODE"))
                commonMapParameters.addParam("code",code);
        }
    }
}
