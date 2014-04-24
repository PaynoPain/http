import http.PostMethod;
import http.Request;
import http.ResourceRequester;
import http.Response;

public class Test {

    private static final String TokenUrl = "https://flatcrew.paynopain.com/oauth/token";
    private static final String CLIENT_ID = "NTM0NjQ4NjlhNGY0Yzgz";
    private static final String CLIENT_SECRET = "b4c4bb62dac450925d7cf3322a9d4e8fde7bc104";
    private static final String USERNAME = "paco@paquito.pa";
    private static final String PASSWORD = "paco";
    private static final String GRANT_TYPE = "password";
    public PostMethod requester;
    private String getAccessToken() {

        requester = new PostMethod(5000);
        /*ResourceRequester requester = new ResourceRequester() {
            @Override
            public Response run(Request request) {
                return new Response(200, "Simulado!");
            }
        };*/

        String body= requester.run(new Request(TokenUrl,
                "grant_type", GRANT_TYPE,
                "username", USERNAME,
                "password", PASSWORD,
                "client_id", CLIENT_ID,
                "client_secret", CLIENT_SECRET)).body;
        int code= requester.run(new Request(TokenUrl,
                "grant_type", GRANT_TYPE,
                "username", USERNAME,
                "password", PASSWORD,
                "client_id", CLIENT_ID,
                "client_secret", CLIENT_SECRET)).statusCode;
        return code + " > " + body;
    }
    public static void main(String[] args){
        Test access = new Test();
        System.out.print(access.getAccessToken());
    }
}
