package nimusc.vk.core;

public enum VKUrls {
    OAUTH_TOKEN("https://oauth.vk.com/token"),
    AUDIO_SEARCH("https://api.vk.com/method/audio.search"),
    AUDIO_GETBYID("https://api.vk.com/method/audio.getById"),
    AUDIO_GET("https://api.vk.com/method/audio.get"),
    AUDIO_ADD("https://api.vk.com/method/audio.add")


    ;
    public String url;

    VKUrls(String url) {
        this.url = url;
    }
}
