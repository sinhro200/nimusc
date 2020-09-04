package vk;

import com.fasterxml.jackson.databind.ObjectMapper;
import core.Account;
import core.request.HttpUrlParameters;
import core.SongInfo;
import core.VkClient;
import core.common.OfficialVkClient;
import core.interfaces.linkToSongConverter.LinkConvertingException;
import core.interfaces.linkToSongConverter.LinkToSongConverter;
import core.interfaces.songFinder.SongFinder;
import core.interfaces.songFinder.SongFinderException;
import core.interfaces.tokenReceiver.TokenReceiver;
import core.interfaces.tokenReceiver.TokenReceiverException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.java.Log;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
public class VkService implements LinkToSongConverter, SongFinder, TokenReceiver {
    private final String getSongByIdUrl = "https://api.vk.com/method/audio.getById";
    private final String searchMusicUrl = "https://api.vk.com/method/audio.search";
    private final String tokenReceiverUrl = "https://oauth.vk.com/token";

    private final String v = "5.116";

    private final VkClient vkClient;
    private final OkHttpClient httpClient;

    @Getter
    @Setter
    private Account account;

    public VkService(Account account) {
        this.account = account;
        vkClient = OfficialVkClient.get();
        httpClient = new OkHttpClient();
    }

    @Override
    public void convert(HttpUrlParameters userParams, Consumer<SongInfo> onSongReady, Consumer<LinkConvertingException> onError) {
        if (userParams == null) {
            onError.accept(new LinkConvertingException(LinkConvertingException.LCEType.EMPTY_LINK,"Input params is null"));
            return;
        }

        if (account.getAccessToken() == null || account.getAccessToken().isEmpty() ) {
            onError.accept(new LinkConvertingException(LinkConvertingException.LCEType.ACCESS_TOKEN_EMPTYORNULL));
        }

        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(getSongByIdUrl).newBuilder();
        urlBuilder
                .addQueryParameter("access_token",account.getAccessToken());
//        if (!userParams.applyToHttpBuilder(urlBuilder)){
//            onError.accept(new LinkConvertingException(LinkConvertingException.LCEType.WRONG_INPUT_DATA));
//            return;
//        }

        urlBuilder
                .addQueryParameter("v",v)
                .build();

        log.info("_Trying to execute 'get' to : "+urlBuilder.toString());

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .header("User-Agent",vkClient.getUserAgent())
                .build();
        //perform request
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String responseJSON = response.body().string();
                ObjectMapper om = new ObjectMapper();
                HashMap<String, ArrayList<LinkedHashMap<Object,Object>>> result =
                        om.readValue(responseJSON, HashMap.class);
                ArrayList<LinkedHashMap<Object,Object>> responseList = result.get("response");
                if (responseList.isEmpty()) {
                    onError.accept(new LinkConvertingException(LinkConvertingException.LCEType.SONGS_NOT_FOUND,"Song not found"));
                    return;
                }

                List<SongInfo> songInfos = new LinkedList<>();
                for (int i = 0; i < responseList.size(); i++) {
                    Map<Object,Object> responseMap = responseList.get(i);
                    String musicUrl = (String) responseMap.get("url");
                    log.info("Got dirty song url : " + musicUrl);
                    String clUrl = null;
                    try {
                        clUrl = getMp3FromM3U8(getClearUrl(musicUrl));
                    } catch (LinkConvertingException e) {
                        onError.accept(e);
                    }
                    log.info("Produced clear song url : " + clUrl);

                    songInfos.add(new SongInfo(clUrl,null,null,123));
                }
                onSongReady.accept(songInfos.get(0));
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                onError.accept(new LinkConvertingException(LinkConvertingException.LCEType.REQUEST_ERR));
            }
        });
    }

    private static String getClearUrl(String url) throws LinkConvertingException{
        Pattern pattern = Pattern.compile("https://.+?index\\.m3u8\\?+?");
        Matcher m = pattern.matcher(url);
        if (m.find()){
            return m.group(0);
        } else{
            throw new LinkConvertingException(LinkConvertingException.LCEType.BROKEN_LINK,"Url that matches pattern not found");
        }
    }

    private static String getMp3FromM3U8(String clearUrl){
        if (!clearUrl.contains("index.m3u8?"))
            return clearUrl;


        if (clearUrl.contains("/audios/")) {
            //для скачивания всей музыки со страницы как я понял
            // тогда способ битый, т.к. ссыль выглядит 'audios123123' а не 'audios/123123'
            Pattern pattern = Pattern.compile("^(.+?)/[^/]+?/audios/([^/]+)/.+$");
            Matcher m = pattern.matcher(clearUrl);
            if (m.find()) {
                return m.group(1)+"/audios/"+m.group(2)+".mp3";
            }
        }
        else{
            Pattern pattern = Pattern.compile("^(.+?)\\/(p[0-9]+)\\/[^/]+?\\/([^/]+)\\/.+$");
            Matcher m = pattern.matcher(clearUrl);
            if (m.find()) {
                return m.group(1)+"/"+m.group(2)+"/"+m.group(3)+".mp3";
            }
        }
        return null;
    }

    @Override
    public void search(HttpUrlParameters userParams, Consumer<List<SongInfo>> onSongReady, Consumer<SongFinderException> onError) {
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(searchMusicUrl).newBuilder();
        urlBuilder
                .addQueryParameter("access_token",account.getAccessToken());


//        if (!userParams.applyToHttpBuilder(urlBuilder)){
//            onError.accept(new SongFinderException(SongFinderException.SFEType.WRONG_INPUT_DATA));
//            return;
//        }

        urlBuilder.addQueryParameter("v",v)
                .build();

        log.info("_Trying to execute 'get' to : "+urlBuilder.toString());

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .header("User-Agent",vkClient.getUserAgent()) /******/
                .build();
        //perform request
        Call call = httpClient.newCall(request);

        call.enqueue(
                new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();                        /******/
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                                    /******/
                        Response r = response;
                        ResponseBody rb = r.body();
                        String rbjson = rb.string();
                        ObjectMapper om = new ObjectMapper();
                        HashMap<String, Object> result =
                                om.readValue(rbjson, HashMap.class);
                        ArrayList<LinkedHashMap<Object,Object>> responseList = (ArrayList<LinkedHashMap<Object, Object>>) result.get("response");

                        if (responseList.isEmpty()) {
                            onError.accept(new SongFinderException(SongFinderException.SFEType.SONG_NOT_FOUND,"Song not found"));
                            return;
                        }


                        log.info("response :" + response);
                        log.info("response body :" + response.body().string());
                    }
                }
        );
    }

    @Override
    public void receiveToken(HttpUrlParameters userParams,
                             Consumer<String> onAccessTokenReady,
                             Consumer<TokenReceiverException> onError) {
        String auth_code = "GET_CODE";
        String device_id = Utils.generate_random_string(16, "0123456789abcdef");
        HttpUrl.Builder urlBuilder
                = HttpUrl.parse(tokenReceiverUrl).newBuilder();
        urlBuilder
                .addQueryParameter("grant_type", "password")
                .addQueryParameter("client_id", vkClient.getClientId())
                .addQueryParameter("client_secret", vkClient.getClientSecret());
//        if (!userParams.applyToHttpBuilder(urlBuilder)){
//            onError.accept(new TokenReceiverException(TokenReceiverException.TEType.WRONG_INPUT_DATA));
//            return;
//        }
        urlBuilder
                .addQueryParameter("v",v )
                .addQueryParameter("lang","en" )
                .addQueryParameter("scope", "all")
                .addQueryParameter("device_id", device_id);
        addTwoFactorPart(urlBuilder,auth_code);
        urlBuilder.build();

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();
        //perform request
        Call call = httpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                call.request().body();
                onError.accept(new TokenReceiverException(
                        TokenReceiverException.TEType.REQUEST_ERR,
                        "_Error while executing request."
                ));
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResponseBody rb = response.body();

                String responseJson = rb.string();
                Map<String,String> result =
                        new ObjectMapper().readValue(responseJson, HashMap.class);
                String error = result.getOrDefault("error","");
                String errorDescription = result.getOrDefault("error_description","");
                if (error.equals("need_validation")) {
                    onError.accept(new TokenReceiverException(TokenReceiverException.TEType.TWOFA_REQ, result.toString()));
                    return;
                }
                if (error.equals("invalid_client")) {
                    onError.accept(
                            new TokenReceiverException(
                                    TokenReceiverException.TEType.TOKEN_NOT_RECEIVED,errorDescription
                            )
                    );
                    return;
                }
                if (!result.containsKey("user_id")) {
                    onError.accept(new TokenReceiverException(TokenReceiverException.TEType.TOKEN_NOT_RECEIVED, result.toString()));
                    return;
                }
                String responseToken = result.get("access_token");
                if (!error.isEmpty() || responseToken.isEmpty()){
                    onError.accept(new TokenReceiverException(TokenReceiverException.TEType.TOKEN_NOT_RECEIVED, errorDescription));
                }else{
                    onAccessTokenReady.accept(responseToken);
                }


            }
        });
    }
    private static void addTwoFactorPart(HttpUrl.Builder builder,String code){
        if (code != null){
            builder.addQueryParameter("2fa_supported","1");
            builder.addQueryParameter("force_sms","1");
            if (!code.equals("GET_CODE"))
                builder.addQueryParameter("code",code);
        }
    }
}
