package nimusc.vk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nimusc.core.Account;
import nimusc.core.SongInfo;
import nimusc.core.common.CommonMapParameters;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.vk.HttpUrlParameters.AudioGetByIdHUP;
import nimusc.vk.HttpUrlParameters.AudioGetHUP;
import nimusc.vk.HttpUrlParameters.AudioSearchHUP;
import nimusc.vk.HttpUrlParameters.TokenReceiverHUP;
import nimusc.vk.core.VKRequest;
import nimusc.vk.core.VKResponseValidator;
import nimusc.vk.core.VKUrls;
import nimusc.vk.core.VkSongInfosCollector;
import threadSleeper.ThreadSleeper;
import threadSleeper.ThreadSleeperTimeoutException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class VKServicePublicAudio {
    private VKRequest oauthToken;
    private VKRequest audioSearch;
    private VKRequest audioAdd;
    private VKRequest audioGet;
    private VKRequest audioGetById;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final VKResponseValidator vkResponseValidator;
    private final VkSongInfosCollector vkSongInfosCollector;
    private final Account account;
    private long requestTimeoutMillis = 10000L;

    public VKServicePublicAudio(String login, String password) {
        vkResponseValidator = new VKResponseValidator();
        vkSongInfosCollector = new VkSongInfosCollector();
        account = new Account();
        account.setLogin(login);
        account.setPassword(password);
        try {
            account.setAccessToken(
                    Props.getInstance().getCurToken()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        oauthToken = createOAuthTokenRequest();
    }

    public List<SongInfo> getAudio(AudioGetHUP audioGetHUP) throws InterruptedException, NimuscException, ThreadSleeperTimeoutException, IOException {
        checkAccountAccessToken();

        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    audioGet.send(
                            audioGetHUP,
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_GET);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public List<SongInfo> searchAudios(AudioSearchHUP audioSearchHUP) throws InterruptedException, NimuscException, ThreadSleeperTimeoutException, IOException {
        checkAccountAccessToken();

        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    audioSearch.send(
                            audioSearchHUP,
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_SEARCH);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public List<SongInfo> getAudioById(AudioGetByIdHUP audioGetByIdHUP) throws InterruptedException, NimuscException, ThreadSleeperTimeoutException, IOException {
        checkAccountAccessToken();

        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    audioGetById.send(
                            audioGetByIdHUP,
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_GETBYID);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public void updateAccountToken() throws InterruptedException, ThreadSleeperTimeoutException, NimuscException {
        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    oauthToken.send(
                            TokenReceiverHUP.builder()
                                    .login(account.getLogin())
                                    .password(account.getPassword())
                                    .build(),
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.OAUTH_TOKEN);

        String responseAccToken = jsonResponse.get("access_token").asText();

        account.setAccessToken(responseAccToken);
        try {
            Props.getInstance().setCurToken(responseAccToken);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void updateAllRequests(String accessToken){
        audioSearch = VKRequest.buildDefault(
                VKUrls.AUDIO_SEARCH.url,
                accessToken
                );
        audioAdd = VKRequest.buildDefault(
                VKUrls.AUDIO_ADD.url,
                accessToken
        );
        audioGet = VKRequest.buildDefault(
                VKUrls.AUDIO_GET.url,
                accessToken
        );
        audioGetById = VKRequest.buildDefault(
                VKUrls.AUDIO_GETBYID.url,
                accessToken
        );
    }

    private void checkAccountAccessToken() throws InterruptedException, ThreadSleeperTimeoutException, NimuscException {
        if (account.getAccessToken() == null || account.getAccessToken().isEmpty()) {
            updateAccountToken();
            updateAllRequests(account.getAccessToken());
        }
    }

    private VKRequest createOAuthTokenRequest(){
        String auth_code = "GET_CODE";
        String device_id = VkUtils.generate_random_string(16, "0123456789abcdef");
        CommonMapParameters httpUrlParameters =
                CommonMapParameters.createEmpty()
                    .addParam("grant_type","password")
                    .addParam("client_id", VKRequest.vkClient.getClientId())
                    .addParam("client_secret", VKRequest.vkClient.getClientSecret())
                    .addParam("v",VKRequest.v)
                    .addParam("lang","en" )
                    .addParam("scope", "all")
                    .addParam("device_id", device_id);

//        if (auth_code != null){
            httpUrlParameters.addParam("2fa_supported","1");
            httpUrlParameters.addParam("force_sms","1");
            if (!auth_code.equals("GET_CODE"))
                httpUrlParameters.addParam("code",auth_code);
//        }

        return VKRequest.build(
                VKUrls.OAUTH_TOKEN.url,
                httpUrlParameters,
                CommonMapParameters.createEmpty()
                );
    }

    private JsonNode stringResponseToJsonNode(String response) throws NimuscException {
        try {
            return objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Cant convert response to JsonNode");
        }
    }

    public long getRequestTimeoutMillis() {
        return requestTimeoutMillis;
    }

    public void setRequestTimeoutMillis(long requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
    }
}
