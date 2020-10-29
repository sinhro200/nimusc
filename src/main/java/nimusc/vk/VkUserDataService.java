package nimusc.vk;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nimusc.core.authorization.Authorization;
import nimusc.core.common.exception.NimuscException;
import nimusc.vk.HttpUrlParameters.GetUserInfoHUP;
import nimusc.vk.core.VKRequest;
import nimusc.vk.core.VKResponseValidator;
import nimusc.vk.core.VKUrls;
import nimusc.vk.core.VkSongInfosCollector;

import static nimusc.core.tools.RequestResponseTools.stringResponseToJsonNode;

public class VkUserDataService {

    private VKRequest userDataRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private VKResponseValidator vkResponseValidator;

    public VkUserDataService() {
        vkResponseValidator = new VKResponseValidator();
        userDataRequest = VKRequest.buildDefault(
                VKUrls.USERS_GET.url
        );
    }

    public UserData getUserInfo(GetUserInfoHUP getUserInfoHUP, Authorization authorization) throws NimuscException {
        String response = userDataRequest.doRequestSync(
                getUserInfoHUP,
                authorization
        );

        JsonNode jsonResponse = stringResponseToJsonNode(
                objectMapper, response);

        vkResponseValidator.validate(jsonResponse, VKUrls.USERS_GET);

        JsonNode respArr = jsonResponse.get("response");
        JsonNode resp = respArr.get(0);
        if (resp != null && !resp.isEmpty()) {
            JsonNode fname_obj = resp.get("first_name");
            JsonNode lname_obj = resp.get("last_name");
            String fname = fname_obj.textValue();
            String lname = lname_obj.textValue();
            return new UserData(fname, lname);
        }
        return null;
    }

    public static class UserData {
        public String first_name;
        public String last_name;

        public UserData(String first_name, String last_name) {
            this.first_name = first_name;
            this.last_name = last_name;
        }
    }
}
