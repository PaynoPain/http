package http;

public class PostMethod extends HttpClient implements ResourceRequester {
    public PostMethod(int millisecondsToTimeout) {
        super(millisecondsToTimeout);
    }

    @Override
    public Response run(Request request) {
        return post(request.resource, request.parameters);
    }
}
