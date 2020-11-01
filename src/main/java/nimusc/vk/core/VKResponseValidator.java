package nimusc.vk.core;

import com.fasterxml.jackson.databind.JsonNode;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.exceptions.LinkConvertingNE;
import nimusc.core.exceptions.AuthorizationNE;

public class VKResponseValidator {
    /*
    [jsonResponse]:{
        [responseJsonNode] response:{
            "asd":"dsa",
            ...
        },
        [errorJsonNode] error:{
            ...
        }
    }
     */
    private JsonNode responseJsonNode;
    private JsonNode errorJsonNode;
    private JsonNode jsonResponse;
    public void validate(JsonNode jsonResponse,VKUrls requestUrl)throws NimuscException {
        this.jsonResponse = jsonResponse;
        responseJsonNode = jsonResponse.get("response");
        errorJsonNode = jsonResponse.get("error");

        switch (requestUrl){
            case OAUTH_TOKEN:
                validateOAuth();
                break;
            case AUDIO_GET:
                validateAudioGet();
                break;
            case AUDIO_GETBYID:
                validateAudioGetById();
                break;
            case AUDIO_ADD:
                validateAudioAdd();
                break;
            case AUDIO_SEARCH:
                validateAudioSearch();
                break;
            case USERS_GET:
                validateUsersGet();
                break;
        }
    }

    private void validateUsersGet() throws NimuscException {
        //ToDO
        if (errorJsonNode != null && !errorJsonNode.isEmpty())
            throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,errorJsonNode.get("error_msg").asText());

        if (responseJsonNode==null || responseJsonNode.isEmpty()) {
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Response data is null or empty");
        }
    }

    private void validateAudioSearch() throws NimuscException{
        //ToDO
    }

    private  void validateAudioAdd() throws NimuscException {
        //ToDO
        if (errorJsonNode != null && !errorJsonNode.isEmpty())
            throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,errorJsonNode.get("error_msg").asText());

        if (responseJsonNode==null || responseJsonNode.isEmpty()) {
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Response data is null or empty");
        }
    }

    private void validateAudioGetById() throws NimuscException {
        //ToDO
        try {
            if (errorJsonNode.get("error_code").asText().equals("100") &&
                    errorJsonNode.get("error_msg").asText().contains("audios is invalid"))
                throw new NimuscException(LinkConvertingNE.SONGS_NOT_FOUND);
        }catch (NullPointerException ignore){}

        if (errorJsonNode != null && !errorJsonNode.isEmpty())
            throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,errorJsonNode.get("error_msg").asText());

        if (responseJsonNode==null || responseJsonNode.isEmpty()) {
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Response data is null or empty");
        }
    }

    private void validateAudioGet() throws NimuscException  {
        //ToDO
        if (responseJsonNode.get("count").asInt() <= 0)
            throw new NimuscException(LinkConvertingNE.SONGS_NOT_FOUND);
        if (errorJsonNode != null && !errorJsonNode.isEmpty())
            throw new NimuscException(CommonNE.ERR_WHILE_SENDING_REQUEST,errorJsonNode.get("error_msg").asText());

        if (responseJsonNode==null || responseJsonNode.isEmpty()) {
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Response data is null or empty");
        }

    }

    private void validateOAuth() throws NimuscException {
        if (errorJsonNode == null)
            return ;
        String error = errorJsonNode.asText();
        String errorDescription = jsonResponse.get("error_description").asText();
        if (error.equals("need_validation")) {
            throw new NimuscException(AuthorizationNE.TWOFA_REQ, jsonResponse.toString());
        }
        if (error.equals("invalid_client")) {
            throw new NimuscException(
                    AuthorizationNE.TOKEN_NOT_RECEIVED, errorDescription
            );
        }
        if (jsonResponse.get("user_id").isEmpty()) {
            throw new NimuscException(AuthorizationNE.TOKEN_NOT_RECEIVED, jsonResponse.toString());
        }
    }
}
