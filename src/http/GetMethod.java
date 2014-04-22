package http;

public class GetMethod extends HttpClient implements ResourceRequester{
    public GetMethod(int millisecondsToTimeout) {
        super(millisecondsToTimeout);
    }

    @Override
    public Response run(Request request) {
        return get(request.resource, request.parameters);
    }
}
