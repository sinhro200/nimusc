package nimusc.usage;

import nimusc.core.SongInfo;
import nimusc.core.common.CommonException;
import nimusc.core.interfaces.linkToSongConverter.LinkConvertingException;
import threadSleeper.ThreadSleeperTimeoutException;
import nimusc.vk.HttpUrlParameters.LinkConvertingHUP;
import nimusc.vk.Utils;
import nimusc.vk.Nimusc_vk;
import nimusc.vk.HttpUrlParameters.SongFinderHUP;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //test();

        Nimusc_vk vkService = new Nimusc_vk(SecretData.login, SecretData.password);

        String link = "https://vk.com/audio123622163_456240364_b71f8ebf09dc187277";
        try {
            String id = Utils.convertLinkToId(link);

            List<SongInfo> songInfos;

            songInfos = vkService.idsToSongInfos(
                    LinkConvertingHUP.builder()
                            .ids(Arrays.asList(id))
                    .build()
            );
            System.out.println(songInfos.toString());

            songInfos = vkService.searchSongs(
                    SongFinderHUP.builder()
                    .query("Supra")
                    .build()
            );
            System.out.println(songInfos.toString());
        } catch (LinkConvertingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CommonException e) {
            e.printStackTrace();
        } catch (ThreadSleeperTimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}