import http.PostMethod;
import http.Request;
import http.ResourceRequester;
import http.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login {
    private static final String URL = "https://flatcrew.paynopain.com/oauth/token";
    private static final String CLIENT_ID = "NTM0NjQ4NjlhNGY0Yzgz";
    private static final String CLIENT_SECRET = "b4c4bb62dac450925d7cf3322a9d4e8fde7bc104";
    private static final String GRANT_TYPE = "password";

    private final ResourceRequester requester;
    private final TokensParser parser;
    private final String username;
    private final String password;

    public static Login construct(String username, String password) {
        return new Login(new PostMethod(5000), new TokensParser(), username, password);
    }

    public Login(ResourceRequester requester, TokensParser parser, String username, String password) {
        this.requester = requester;
        this.parser = parser;
        this.username = username;
        this.password = password;
    }

    public Tokens execute() throws IOException {
        try {
            Response response = this.requester.run(new Request(URL, getParams()));
            return this.parser.constructFrom(response);
        } catch (Throwable t) {
            throw new IOException(t);
        }
    }

    private Map<String, String> getParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("grant_type", GRANT_TYPE);
        params.put("username", username);
        params.put("password", password);
        params.put("client_id", CLIENT_ID);
        params.put("client_secret", CLIENT_SECRET);

        return params;
    }
}

