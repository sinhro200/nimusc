package core;

import lombok.Data;

@Data
public class Account {
    private String login;
    private String password;
    private String accessToken;
}
