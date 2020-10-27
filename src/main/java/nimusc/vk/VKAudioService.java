package nimusc.vk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nimusc.core.SongInfo;
import nimusc.core.authorization.Authorization;
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

import static nimusc.core.tools.RequestResponseTools.stringResponseToJsonNode;

public class VKAudioService {

    private VKRequest audioGet;
    private VKRequest audioSearch;
    private VKRequest audioAdd;
    private VKRequest audioGetById;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private VKResponseValidator vkResponseValidator;


    private final VkSongInfosCollector vkSongInfosCollector;

    public VKAudioService() {
        vkResponseValidator = new VKResponseValidator();
        vkSongInfosCollector = new VkSongInfosCollector();
        createAllRequests();
    }

    private void createAllRequests(){
        audioGet = VKRequest.buildDefault(
                VKUrls.AUDIO_GET.url
        );
        audioSearch = VKRequest.buildDefault(
                VKUrls.AUDIO_SEARCH.url
        );
        audioAdd = VKRequest.buildDefault(
                VKUrls.AUDIO_ADD.url
        );
        audioGetById = VKRequest.buildDefault(
                VKUrls.AUDIO_GETBYID.url
        );
    }


    public List<SongInfo> getAudio(
            Authorization authorization,
            AudioGetHUP audioGetHUP
    ) throws NimuscException{

        String response = audioGet.doRequestSync(audioGetHUP, authorization);

        JsonNode jsonResponse = stringResponseToJsonNode(
                objectMapper,response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_GET);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public List<SongInfo> searchAudios(Authorization authorization,AudioSearchHUP audioSearchHUP)
            throws NimuscException{

        String response = audioSearch.doRequestSync(audioSearchHUP, authorization);

        JsonNode jsonResponse = stringResponseToJsonNode(
                objectMapper,response);

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_SEARCH);

        return vkSongInfosCollector.collect(jsonResponse);
    }

    public List<SongInfo> getAudioById(Authorization authorization,AudioGetByIdHUP audioGetByIdHUP)
            throws NimuscException{

        String response = audioGetById.doRequestSync(audioGetByIdHUP, authorization);

        JsonNode jsonResponse = stringResponseToJsonNode(
                objectMapper,
                response
        );

        vkResponseValidator.validate(jsonResponse,VKUrls.AUDIO_GETBYID);

        return vkSongInfosCollector.collect(jsonResponse);
    }



//    private JsonNode stringResponseToJsonNode(String response) throws NimuscException {
//        try {
//            return objectMapper.readTree(response);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Cant convert response to JsonNode");
//        }
//    }
}
