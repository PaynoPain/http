package http;

public class Response {
    public final int statusCode;
    public final String body;

    public Response(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }
}
