package nimusc.usage;

import nimusc.core.SongInfo;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;
import nimusc.core.interfaces.linkToSongConverter.LinkConvertingNE;
import nimusc.vk.HttpUrlParameters.AudioGetHUP;
import nimusc.vk.core.VKService;
import threadSleeper.ThreadSleeperTimeoutException;
import nimusc.vk.VkUtils;
import nimusc.vk.HttpUrlParameters.AudioSearchHUP;
import usage.SecretData;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        Usage.run();
    }


}
