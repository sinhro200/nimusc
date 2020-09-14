package nimusc.usage;

import nimusc.core.SongInfo;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;
import nimusc.core.interfaces.linkToSongConverter.LinkConvertingNE;
import nimusc.vk.HttpUrlParameters.AudioGetHUP;
import nimusc.vk.HttpUrlParameters.AudioSearchHUP;
import nimusc.vk.VkUtils;
import nimusc.vk.VKServicePublicAudio;
import threadSleeper.ThreadSleeperTimeoutException;

import java.io.IOException;
import java.util.List;

public class Usage {

    private static VKServicePublicAudio vkServicePublicAudio = new VKServicePublicAudio(SecretData.login, SecretData.password);
    public static void run(){
        //test();

        //     dont work, if share = https://vk.com/audio246181510_456239785
        //     work, after add to profile = https://vk.com/audio372155054_456239019
        //     work, if search = https://vk.com/audio474499233_456472576
        //     work = https://vk.com/audio123622163_456240364_b71f8ebf09dc187277

        String link = "https://vk.com/audio474499233_456472576";
        link = "https://vk.com/audio246181510_456239782";

        testConvertingLinkToSong_UsingGetById(link);
    }


    private static void testConvertingLinkToSong_UsingGetById(String link) {
//        String link = "https://vk.com/audio474499233_456472576";
        try{
            String id = VkUtils.convertLinkToId(link);
            String ownerId = id.substring(0,9);
            String audioId = id.substring(10,19);

            List<SongInfo> songInfos;
            songInfos = vkServicePublicAudio.getAudio(
                    AudioGetHUP.builder()
                            .ownerId(ownerId)
                            .albumId(audioId)
                            .build()
            );
            System.out.println(songInfos.toString());
        } catch (NimuscException e) {
            e.printStackTrace();
            NimuscExceptionType NEtype = e.getType();
            if (NEtype instanceof LinkConvertingNE) {
                switch ((LinkConvertingNE) NEtype) {
                    case SONGS_NOT_FOUND:

                        break;
                    case REQUEST_ERR:

                        break;
                    case EMPTY_LINK:

                        break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ThreadSleeperTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void testConvertingLinkToSong_UsingGet(String link) {
//        String link = "https://vk.com/audio474499233_456472576";
        try{
            String id = VkUtils.convertLinkToId(link);
            String ownerId = id.substring(0,9);
            String audioId = id.substring(10,19);

            List<SongInfo> songInfos;
            songInfos = vkServicePublicAudio.getAudio(
                    AudioGetHUP.builder()
                            .albumId(audioId)
                            .ownerId(ownerId)
                            .build()
            );
            System.out.println(songInfos.toString());
        } catch (NimuscException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ThreadSleeperTimeoutException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

   /* private static void testSongAdd(String link) {
//        String link = "https://vk.com/audio474499233_456472576";
        try {
            String id = VkUtils.convertLinkToId(link); //smth like this : 474499233_456472576
            String ownerId = id.substring(0, 9);
            String audioId = id.substring(10, 19);

            vkService. (
                    SongAdderHUP.builder()
                            .audioId(audioId)
                            .ownerId(ownerId)//"372155054")
                            .build()
            );
        } catch (NimuscException e) {
            e.printStackTrace();
        }
    }*/

    private static void testSearchSong(String query){
        try {
            List<SongInfo> songInfos;
            songInfos = vkServicePublicAudio.searchAudios(
                    AudioSearchHUP.builder()
                            .query("Supra")
                            .build()
            );
            System.out.println(songInfos.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NimuscException e) {
            e.printStackTrace();
        } catch (ThreadSleeperTimeoutException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
