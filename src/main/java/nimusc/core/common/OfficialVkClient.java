package nimusc.core.common;

import nimusc.core.VkClient;

public class OfficialVkClient implements VkClient {
    private final String userAgent;
    private final String clientSecret;
    private final String clientId;

    private OfficialVkClient(String user_agent, String clientSecret, String clientId) {
        this.userAgent = user_agent;
        this.clientSecret = clientSecret;
        this.clientId = clientId;
    }

    private static final OfficialVkClient officialVkClient = new OfficialVkClient(
            "VKAndroidApp/5.52-4543 (Android 5.1.1; SDK 22; x86_64; unknown Android SDK built for x86_64; en; 320x240)",
            "hHbZxrka2uZ6jB1inYsH",
            "2274003");

    public static OfficialVkClient get(){
        return officialVkClient;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    @Override
    public String getClientId() {
        return clientId;
    }
}
