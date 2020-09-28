package nimusc.vk;

import nimusc.core.common.exception.NimuscException;
import nimusc.core.exceptions.LinkConvertingNE;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VkUtils {
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
    public static String convertLinkToId(String link) throws NimuscException {
        Matcher m = linkToIdPattern.matcher(link);
        if (m.find())
            return m.group(0);
        throw new NimuscException(
                LinkConvertingNE.BROKEN_LINK,
                "Can not get id from link. <"+link+">"
        );
    }

}
