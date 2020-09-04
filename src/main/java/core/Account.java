package core;

import lombok.Data;
import vk.Props;

import java.io.IOException;
import java.net.URISyntaxException;

@Data
public class Account {
    private String login;
    private String password;
    private String accessToken;
}
