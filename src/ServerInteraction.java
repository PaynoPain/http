import http.GetMethod;
import http.Request;

public class ServerInteraction {
    final String url;
    final Tokens tokens;


    public ServerInteraction(String url, Tokens tokens) {
        this.url = url;
        this.tokens = tokens;
    }
    public String getJSONFromServer(){

        GetMethod request = new GetMethod(5000);

        return request.run(new Request(url, "access_token", tokens.access)).body;
    }
}
