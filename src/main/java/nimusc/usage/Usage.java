package nimusc.usage;

import nimusc.core.SongInfo;
import nimusc.core.authorization.Authorization;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;
import nimusc.core.common.exception.NimuscExceptionType;
import nimusc.core.exceptions.LinkConvertingNE;
import nimusc.vk.HttpUrlParameters.AudioGetHUP;
import nimusc.vk.HttpUrlParameters.AudioSearchHUP;
import nimusc.vk.HttpUrlParameters.GetUserInfoHUP;
import nimusc.vk.VKAudioService;
import nimusc.vk.VkAuthorizationService;
import nimusc.vk.VkUserDataService;
import nimusc.vk.VkUtils;

import java.io.IOException;
import java.util.List;

public class Usage {

    private static VKAudioService vkAudioService = new VKAudioService();
    private static VkAuthorizationService vkAuthorizationService = new VkAuthorizationService(SecretData.login, SecretData.password);

    public static void run() {
        //test();

        //     dont work, if share = https://vk.com/audio246181510_456239785
        //     work, after add to profile = https://vk.com/audio372155054_456239019
        //     work, if search = https://vk.com/audio474499233_456472576
        //     work = https://vk.com/audio123622163_456240364_b71f8ebf09dc187277

        String link = "https://vk.com/audio474499233_456472576";
        link = "https://vk.com/audio246181510_456239782";
        link = "https://vk.com/audio123622163_456240364_b71f8ebf09dc187277";

        testConvertingLinkToSong_UsingGetById(link);
    }


    private static void testConvertingLinkToSong_UsingGetById(String link) {
//        String link = "https://vk.com/audio474499233_456472576";
        try {
            String id = VkUtils.convertLinkToId(link);
            String ownerId = id.substring(0, 9);
            String audioId = id.substring(10, 19);

            vkAuthorizationService.authorize();

            List<SongInfo> songInfos;
            songInfos = vkAudioService.getAudio(
                    vkAuthorizationService.authFromCommonAccount(),
                    AudioGetHUP.builder()
                            .ownerId(ownerId)
                            .albumId(audioId)
                            .build()
            );
            System.out.println(songInfos.toString());
        } catch (NimuscException e) {
            e.printStackTrace();
            NimuscExceptionType nimuscExcType = e.getType();
            System.out.println("[" + nimuscExcType.getMessage() + "]");
            if (nimuscExcType instanceof CommonNE) {
                switch (((CommonNE) nimuscExcType)) {
                    case ERR_IN_RESPONSE:

                        break;
                    case WRONG_HTTP_URL_PARAMETERS:

                        break;

                    case ERR_WHILE_SENDING_REQUEST:

                        break;
                }
            }
            if (nimuscExcType instanceof LinkConvertingNE) {
                switch ((LinkConvertingNE) nimuscExcType) {
                    case SONGS_NOT_FOUND:
                        System.out.println("Песни не найдены. Возможно это песня с закрытого профиля.");
                        break;
                }
            }
        }

    }

    private static void testConvertingLinkToSong_UsingGet(String link) {
//        String link = "https://vk.com/audio474499233_456472576";
        try {
            String id = VkUtils.convertLinkToId(link);
            String ownerId = id.substring(0, 9);
            String audioId = id.substring(10, 19);

            List<SongInfo> songInfos;
            songInfos = vkAudioService.getAudio(
                    vkAuthorizationService.authFromCommonAccount(),
                    AudioGetHUP.builder()
                            .albumId(audioId)
                            .ownerId(ownerId)
                            .build()
            );
            System.out.println(songInfos.toString());
        } catch (NimuscException e) {
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

    private static void testSearchSong(String query) {
        try {
            List<SongInfo> songInfos;
            songInfos = vkAudioService.searchAudios(
                    vkAuthorizationService.authFromCommonAccount(),
                    AudioSearchHUP.builder()
                            .query("Supra")
                            .build()
            );
            System.out.println(songInfos.toString());
        } catch (NimuscException e) {
            e.printStackTrace();
        }
    }

    public static void testGetUserData() {
        try {
            VkAuthorizationService.VkResponseUserAuthorizationData userData =
                    new VkAuthorizationService(SecretData.login, SecretData.password)
                            .authorize();

            Authorization authorization =
                    VkAuthorizationService.authUsingUserAccessToken(userData.access_token);

            VkUserDataService vkUserDataService = new VkUserDataService();
            VkUserDataService.UserData ud = vkUserDataService.getUserInfo(
                    GetUserInfoHUP.builder()
                            .user_id(userData.user_id)
                            .build(),
                    authorization
            );
            System.out.println(ud.first_name + " " + ud.last_name);
            System.out.println("Test succeeded");
        } catch (NimuscException e) {
            e.printStackTrace();
        }
    }
}
