package usage;

import core.Account;
import core.SongInfo;
import core.common.CommonException;
import core.interfaces.linkToSongConverter.LinkConvertingException;
import core.interfaces.songFinder.SongFinderException;
import core.interfaces.tokenReceiver.TokenReceiverException;
import usage.threadSleeper.ThreadSleeper;
import usage.threadSleeper.ThreadSleeperTimeoutException;
import vk.HttpUrlParameters.LinkConvertingHUP;
import vk.Props;
import vk.Utils;
import vk.VK_Service;
import vk.VkService;
import vk.HttpUrlParameters.SongFinderHUP;
import vk.HttpUrlParameters.TokenReceiverHUP;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        //test();

        VK_Service vkService = new VK_Service("+79065841259","1Qkamper");
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

    private static void test(){


        Account account = new Account();
        account.setLogin("+79065841259");
        account.setPassword("1Qkamper");

        VkService vkService = new VkService(account);

        try {
            String token = ThreadSleeper.<String, TokenReceiverException>getData(
                    (stringAtomicReference, tokenReceiverExceptionAtomicReference) -> {
                        vkService.receiveToken(
                                TokenReceiverHUP.builder()
                                    .login(account.getLogin())
                                    .password(account.getPassword())
                                .build(),
                                stringAtomicReference::set,
                                tokenReceiverExceptionAtomicReference::set
                        );
                    },10000);

            account.setAccessToken(token);
        } catch (TokenReceiverException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ThreadSleeperTimeoutException e) {
            e.printStackTrace();
        }

        try {
            List<SongInfo> songs = ThreadSleeper.<List<SongInfo>, SongFinderException>getData(
                    (listAtomicReference, songFinderExceptionAtomicReference) -> {
                        vkService.search(
                                SongFinderHUP.builder()
                                        .query("Дети RAVE - supra")
                                        .build(),
                                listAtomicReference::set,
                                songFinderExceptionAtomicReference::set
                        );
                    },
                    10000
            );

            System.out.println(songs.toArray().toString());
        } catch (SongFinderException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ThreadSleeperTimeoutException e) {
            e.printStackTrace();
        }

    }
}
