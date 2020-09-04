package vk;

import core.interfaces.linkToSongConverter.LinkConvertingException;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static String generate_random_string(int length, String characters){
        Random rng = new Random();
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }

    private static final Pattern linkToIdPattern = Pattern.compile("(?<=audio)[0-9]+_[0-9]+((?=_)|$)");
    public static String convertLinkToId(String link) throws LinkConvertingException {
        Matcher m = linkToIdPattern.matcher(link);
        if (m.find())
            return m.group(0);
        throw new LinkConvertingException(
                LinkConvertingException.LCEType.BROKEN_LINK,
                "Can not get id from link. <"+link+">"
        );
    }

    static String getClearUrl(String url) throws LinkConvertingException{
        Pattern pattern = Pattern.compile("https://.+?index\\.m3u8\\?+?");
        Matcher m = pattern.matcher(url);
        if (m.find()){
            return m.group(0);
        } else{
            throw new LinkConvertingException(LinkConvertingException.LCEType.BROKEN_LINK,"Url that matches pattern not found");
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
