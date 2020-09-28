package nimusc.vk.core;

import com.fasterxml.jackson.databind.JsonNode;
import nimusc.core.SongInfo;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.exceptions.LinkConvertingNE;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VkSongInfosCollector {
    public List<SongInfo> collect(JsonNode successResponse) throws NimuscException {
        JsonNode response = getResponse(successResponse);

        List<SongInfo> songInfos = new LinkedList<>();
        for (int i = 0; i < response.size(); i++) {
            JsonNode song = response.get(i);
            String songUrl = song.get("url").asText();
            String songName = song.get("title").asText();
            String songArtist = song.get("artist").asText();
            int duration = song.get("duration").asInt();
            String clearMp3Url = getMp3FromM3U8(getClearUrl(songUrl));

            songInfos.add(new SongInfo(clearMp3Url,songName,songArtist,duration));
        }

        return songInfos;
    }

    private JsonNode getResponse(JsonNode successResponse){
        if (successResponse.get("response").isNull())
            return successResponse;
        return successResponse.get("response");
    }

    static String getClearUrl(String url) throws NimuscException {
        Pattern pattern = Pattern.compile("https://.+?index\\.m3u8\\?+?");
        Matcher m = pattern.matcher(url);
        if (m.find()){
            return m.group(0);
        } else{
            throw new NimuscException(LinkConvertingNE.BROKEN_LINK,"Url that matches pattern not found");
        }
    }

    static String getMp3FromM3U8(String clearUrl){
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
}
