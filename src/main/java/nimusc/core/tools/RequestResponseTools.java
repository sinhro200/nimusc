package nimusc.core.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nimusc.core.common.exception.CommonNE;
import nimusc.core.common.exception.NimuscException;

public class RequestResponseTools {
    public static JsonNode stringResponseToJsonNode(
            ObjectMapper objectMapper,
            String response
    ) throws NimuscException {
        try {
            return objectMapper.readTree(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new NimuscException(CommonNE.ERR_IN_RESPONSE,"Cant convert response to JsonNode");
        }
    }
}
