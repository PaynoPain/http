import http.Response;
import org.json.JSONException;
import org.json.JSONObject;


public class TokensGetter {
    public Tokens getAccessToken(Response responseFromMock) throws JSONException, BadRequestException {
        throwIfCodeIsNotSuccess(responseFromMock);
        JSONObject json = new JSONObject(responseFromMock.body);
        return new Tokens(getValuesFromJSON(json, "access_token"), getValuesFromJSON(json, "refresh_token"));

    }

    private String getValuesFromJSON(JSONObject json, String keyValue) throws JSONException {
        return json.get(keyValue).toString();
    }

    private void throwIfCodeIsNotSuccess(Response responseFromMock) throws BadRequestException {
        if(!codeIsSuccess(responseFromMock)){
            throw new BadRequestException("The client credentials are invalid");
        }
    }
    private boolean codeIsSuccess(Response responseFromMock) {
        return responseFromMock.statusCode == 200;
    }

}
