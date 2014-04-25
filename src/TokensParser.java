import http.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class TokensParser {
    private static final int SUCCESS = 200;

    public Tokens constructFrom(Response response) throws InvalidTokensResponse {
        throwIfCodeIsNotSuccess(response);

        try {
            return parse(response);
        } catch (JSONException e){
            throw new InvalidTokensResponse(e);
        }
    }

    private Tokens parse(Response response) throws JSONException {
        JSONObject json = new JSONObject(response.body);
        return new Tokens(json.getString("access_token"), json.getString("refresh_token"));
    }

    private void throwIfCodeIsNotSuccess(Response response) throws InvalidTokensResponse {
        if(response.statusCode != SUCCESS){
            throw new InvalidTokensResponse("The response code is not 200!");
        }
    }

}
