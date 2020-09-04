package vk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.Account;
import core.SongInfo;
import core.common.CommonException;
import core.interfaces.linkToSongConverter.LinkConvertingException;
import core.interfaces.tokenReceiver.TokenReceiverException;
import core.request.RequestEntity;
import threadSleeper.ThreadSleeper;
import threadSleeper.ThreadSleeperTimeoutException;
import vk.HttpUrlParameters.LinkConvertingHUP;
import vk.HttpUrlParameters.SongFinderHUP;
import vk.HttpUrlParameters.TokenReceiverHUP;
import vk.requests.LinkConverterRequestBuilder;
import vk.requests.ReceiveTokenRequestBuilder;
import vk.requests.SongFinderRequestBuilder;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.logging.Logger;

import static vk.Utils.getClearUrl;
import static vk.Utils.getMp3FromM3U8;

public class nimusc_vk {
    private RequestEntity receiveTokenRequest = ReceiveTokenRequestBuilder.build();
    private RequestEntity linkConverterRequest = null;
    private RequestEntity songFinderRequest = null;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Account account;
    private long maxTimeout = 10000;

    public nimusc_vk(String login, String password) {
        account = new Account();
        account.setLogin(login);
        account.setPassword(password);
        try {
            account.setAccessToken(
                    Props.getInstance().getCurToken()
            );
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void updateAccountToken() throws InterruptedException, ThreadSleeperTimeoutException, CommonException, IOException {
        String response = ThreadSleeper.<String, CommonException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    receiveTokenRequest.send(
                            TokenReceiverHUP.builder()
                                    .login(account.getLogin())
                                    .password(account.getPassword())
                                    .build(),
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                },maxTimeout);

        JsonNode jsonResponse = objectMapper.readTree(response);

        String error = jsonResponse.get("error").asText();
        String errorDescription = jsonResponse.get("error_description").asText();
        if (error.equals("need_validation")) {
            throw new TokenReceiverException(TokenReceiverException.TEType.TWOFA_REQ, jsonResponse.toString());
        }
        if (error.equals("invalid_client")) {
            throw new TokenReceiverException(
                            TokenReceiverException.TEType.TOKEN_NOT_RECEIVED,errorDescription
                    );
        }
        if (jsonResponse.get("user_id").isEmpty()) {
            throw new TokenReceiverException(TokenReceiverException.TEType.TOKEN_NOT_RECEIVED, jsonResponse.toString());
        }
        String responseAccToken = jsonResponse.get("access_token").asText();

        account.setAccessToken(responseAccToken);
        try {
            Props.getInstance().setCurToken(responseAccToken);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public List<SongInfo> idsToSongInfos(LinkConvertingHUP linkConvertingHUP) throws InterruptedException, CommonException, ThreadSleeperTimeoutException, IOException {
        if (account.getAccessToken() == null || account.getAccessToken().isEmpty())
            updateAccountToken();
        if (linkConverterRequest == null)
            linkConverterRequest = LinkConverterRequestBuilder.build(account.getAccessToken());

        String response = ThreadSleeper.<String, CommonException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    linkConverterRequest.send(
                            linkConvertingHUP,
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                },maxTimeout);


        JsonNode jsonResponse = objectMapper.readTree(response);
        JsonNode jsonSongs = jsonResponse.get("response");
        if (jsonSongs.isEmpty()) {
            throw new LinkConvertingException(LinkConvertingException.LCEType.SONGS_NOT_FOUND,"Song not found");
        }

        List<SongInfo> songInfos = new LinkedList<>();
        for (int i = 0; i < jsonSongs.size(); i++) {
            JsonNode song = jsonSongs.get(i);
            String songUrl = song.get("url").asText();
            String songName = song.get("title").asText();
            String songArtist = song.get("artist").asText();
            int duration = song.get("duration").asInt();
            String clearMp3Url = getMp3FromM3U8(getClearUrl(songUrl));

            songInfos.add(new SongInfo(clearMp3Url,songName,songArtist,duration));
        }

        return songInfos;
    }

    public List<SongInfo> searchSongs(SongFinderHUP songFinderHUP) throws InterruptedException, CommonException, ThreadSleeperTimeoutException, IOException {
        if (account.getAccessToken() == null || account.getAccessToken().isEmpty())
            updateAccountToken();
        if (songFinderRequest == null)
            songFinderRequest = SongFinderRequestBuilder.build(account.getAccessToken());

        String response = ThreadSleeper.<String, CommonException>getData(
                (responseAtomicReference, commonExceptionAtomicReference) -> {
                    songFinderRequest.send(
                            songFinderHUP,
                            responseAtomicReference::set,
                            commonExceptionAtomicReference::set
                    );
                },maxTimeout);


        JsonNode jsonResponse = objectMapper.readTree(response);

        JsonNode jsonSongs = jsonResponse.get("response").get("items");

        List<SongInfo> songInfos = new LinkedList<>();
        for (int i = 0; i < jsonSongs.size(); i++) {
            JsonNode song = jsonSongs.get(i);
            String songUrl = song.get("url").asText();
            String songName = song.get("title").asText();
            String songArtist = song.get("artist").asText();
            int duration = song.get("duration").asInt();
            String clearMp3Url = getMp3FromM3U8(getClearUrl(songUrl));

            songInfos.add(new SongInfo(clearMp3Url,songName,songArtist,duration));
        }

        return songInfos;
    }
}
