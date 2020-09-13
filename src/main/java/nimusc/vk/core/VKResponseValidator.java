package nimusc.vk.core;

import com.fasterxml.jackson.databind.JsonNode;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.interfaces.linkToSongConverter.LinkConvertingNE;
import nimusc.core.interfaces.tokenReceiver.TokenReceiverNE;

public class VKResponseValidator {
    public void validate(JsonNode jsonResponse,VKUrls requestUrl)throws NimuscException {
        JsonNode response = jsonResponse.get("response");
        if (response==null || response.isEmpty()) {
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Response is null or empty");
        }

        switch (requestUrl){
            case OAUTH_TOKEN:
                validateOAuth(jsonResponse);
                break;
            case AUDIO_GET:
                validateAudioGet(jsonResponse);
                break;
            case AUDIO_GETBYID:
                validateAudioGetById(jsonResponse);
                break;
            case AUDIO_ADD:
                validateAudioAdd(jsonResponse);
                break;
            case AUDIO_SEARCH:
                validateAudioSearch(jsonResponse);
                break;
        }
    }

    private void validateAudioSearch(JsonNode jsonResponse) throws NimuscException{
        //ToDO

    }

    private void validateAudioAdd(JsonNode jsonResponse) throws NimuscException {
        //ToDO
        if (jsonResponse.get("error") != null && !jsonResponse.get("error").isEmpty())
            throw new NimuscException(LinkConvertingNE.REQUEST_ERR,jsonResponse.get("error").get("error_msg").asText());
    }

    private void validateAudioGetById(JsonNode jsonResponse) throws NimuscException {
        //ToDO
        if (jsonResponse.get("error") != null && !jsonResponse.get("error").isEmpty())
            throw new NimuscException(LinkConvertingNE.REQUEST_ERR,jsonResponse.get("error").get("error_msg").asText());

    }

    private void validateAudioGet(JsonNode jsonResponse) throws NimuscException  {
        //ToDO
        if (jsonResponse.get("error") != null && !jsonResponse.get("error").isEmpty())
            throw new NimuscException(LinkConvertingNE.REQUEST_ERR,jsonResponse.get("error").get("error_msg").asText());
    }

    private void validateOAuth(JsonNode jsonResponse) throws NimuscException {
        String error = jsonResponse.get("error").asText();
        String errorDescription = jsonResponse.get("error_description").asText();
        if (error.equals("need_validation")) {
            throw new NimuscException(TokenReceiverNE.TWOFA_REQ, jsonResponse.toString());
        }
        if (error.equals("invalid_client")) {
            throw new NimuscException(
                    TokenReceiverNE.TOKEN_NOT_RECEIVED, errorDescription
            );
        }
        if (jsonResponse.get("user_id").isEmpty()) {
            throw new NimuscException(TokenReceiverNE.TOKEN_NOT_RECEIVED, jsonResponse.toString());
        }
    }
}
