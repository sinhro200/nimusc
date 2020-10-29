package nimusc.vk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nimusc.core.Account;
import nimusc.core.authorization.Authorization;
import nimusc.core.common.CommonMapParameters;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.vk.HttpUrlParameters.TokenReceiverHUP;
import nimusc.vk.core.VKRequest;
import nimusc.vk.core.VKResponseValidator;
import nimusc.vk.core.VKUrls;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;
import java.net.URISyntaxException;

import static nimusc.core.tools.RequestResponseTools.stringResponseToJsonNode;

public class VkAuthorizationService {
    private final Account account;
    private final VKRequest oauthToken;
    public final static String ACCESS_TOKEN_KEY = "access_token";
    private final VKResponseValidator vkResponseValidator;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public VkAuthorizationService(String commonLogin, String commonPassword) {
        vkResponseValidator = new VKResponseValidator();
        account = new Account();
        account.setLogin(commonLogin);
        account.setPassword(commonPassword);
        try {
            account.setAccessToken(
                    Props.getInstance().getCurToken()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        oauthToken = createOAuthTokenRequest();
    }

    public VkResponseUserAuthorizationData authorize() throws NimuscException {
        String response = oauthToken.doRequestSync(
                TokenReceiverHUP.builder()
                        .login(account.getLogin())
                        .password(account.getPassword())
                        .build(),
                null);

        JsonNode jsonResponse = stringResponseToJsonNode(objectMapper, response);

        vkResponseValidator.validate(jsonResponse, VKUrls.OAUTH_TOKEN);

        try {
            VkResponseUserAuthorizationData vkResponseUserAuthorizationData =
                    objectMapper.treeToValue(
                            jsonResponse, VkResponseUserAuthorizationData.class
                    );
            String accessToken = vkResponseUserAuthorizationData.access_token;
            account.setAccessToken(accessToken);
            try {
                Props.getInstance().setCurToken(accessToken);
            } catch (IOException | URISyntaxException e) {
                e.printStackTrace();
            }
            return vkResponseUserAuthorizationData;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE, "cant convert response to vkAuth PoJo");
        }
    }

    private VKRequest createOAuthTokenRequest() {
        String auth_code = "GET_CODE";
        String device_id = VkUtils.generate_random_string(16, "0123456789abcdef");
        CommonMapParameters httpUrlParameters =
                CommonMapParameters.createEmpty()
                        .addParam("grant_type", "password")
                        .addParam("client_id", VKRequest.vkClient.getClientId())
                        .addParam("client_secret", VKRequest.vkClient.getClientSecret())
                        .addParam("v", VKRequest.v)
                        .addParam("lang", "en")
                        .addParam("scope", "all")
                        .addParam("device_id", device_id);

//        if (auth_code != null){
        httpUrlParameters.addParam("2fa_supported", "1");
        httpUrlParameters.addParam("force_sms", "1");
        if (!auth_code.equals("GET_CODE"))
            httpUrlParameters.addParam("code", auth_code);
//        }

        return VKRequest.build(
                VKUrls.OAUTH_TOKEN.url,
                httpUrlParameters,
                CommonMapParameters.createEmpty()
        );
    }

    public static Authorization authUsingUserAccessToken(String accessToken) {
        return new VKAuthorization(accessToken);
    }

    public Authorization authFromCommonAccount()
            throws NimuscException {
        if (account.getAccessToken() == null)
            authorize();
        return new VKAuthorization(account.getAccessToken());
    }

    static class VKAuthorization implements Authorization {

        private String accessToken;

        public VKAuthorization(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public void applyToHttpBuilder(HttpUrl.Builder urlBuilder) {
            urlBuilder.addQueryParameter(ACCESS_TOKEN_KEY, accessToken);
        }

        @Override
        public void applyToRequestBuilder(Request.Builder requestBuilder) {
            requestBuilder.header("Authorization", "Bearer " + accessToken);
        }
    }

    public static class VkResponseUserAuthorizationData {
        public String access_token;
        public String expires_in;
        public String user_id;
        public String webview_refresh_token;
        public String webview_access_token;
        public String webview_access_token_expires_in;
    }
}
