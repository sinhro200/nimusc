package nimusc.vk;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nimusc.core.Account;
import nimusc.core.SongInfo;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.vk.HttpUrlParameters.AudioGetByIdHUP;
import nimusc.vk.HttpUrlParameters.AudioGetHUP;
import nimusc.vk.HttpUrlParameters.AudioSearchHUP;
import nimusc.vk.core.VKRequest;
import nimusc.vk.core.VKResponseValidator;
import nimusc.vk.core.VKUrls;
import nimusc.vk.core.VkSongInfosCollector;
import threadSleeper.ThreadSleeper;
import threadSleeper.ThreadSleeperTimeoutException;

import java.io.IOException;
import java.util.List;

import static nimusc.vk.core.VKRequest.ACCESS_TOKEN_KEY;

public class VKServicePrivateAudio {

    private VKRequest audioGet;
    private VKRequest audioSearch;
    private VKRequest audioAdd;
    private VKRequest audioGetById;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final VKResponseValidator vkResponseValidator;
    private final VkSongInfosCollector vkSongInfosCollector;
    private long requestTimeoutMillis = 10000L;

    public VKServicePrivateAudio() {
        vkResponseValidator = new VKResponseValidator();
        vkSongInfosCollector = new VkSongInfosCollector();
        createAllRequests();
    }

    private void createAllRequests(){
        audioGet = VKRequest.buildDynamicAccTok(
                VKUrls.AUDIO_GET.url
        );
        audioSearch = VKRequest.buildDynamicAccTok(
                VKUrls.AUDIO_SEARCH.url
        );
        audioAdd = VKRequest.buildDynamicAccTok(
                VKUrls.AUDIO_ADD.url
        );
        audioGetById = VKRequest.buildDynamicAccTok(
                VKUrls.AUDIO_GETBYID.url
        );

    }


    public List<SongInfo> getAudio(String privateAccessToken, AudioGetHUP audioGetHUP) throws InterruptedException, NimuscException, ThreadSleeperTimeoutException, IOException {
        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    audioGet.send(
                            audioGetHUP
                                    .addParameter(ACCESS_TOKEN_KEY,privateAccessToken),
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_GET);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public List<SongInfo> searchAudios(String privateAccessToken,AudioSearchHUP audioSearchHUP) throws InterruptedException, NimuscException, ThreadSleeperTimeoutException, IOException {

        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    audioSearch.send(
                            audioSearchHUP
                                    .addParameter(ACCESS_TOKEN_KEY,privateAccessToken),
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_SEARCH);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public List<SongInfo> getAudioById(String privateAccessToken,AudioGetByIdHUP audioGetByIdHUP) throws InterruptedException, NimuscException, ThreadSleeperTimeoutException, IOException {

        String response = ThreadSleeper.<String, NimuscException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    audioGetById.send(
                            audioGetByIdHUP
                                    .addParameter(ACCESS_TOKEN_KEY,privateAccessToken),
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                }, requestTimeoutMillis);

        JsonNode jsonResponse = stringResponseToJsonNode(response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_GETBYID);

        return vkSongInfosCollector.collect(jsonResponse);
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
