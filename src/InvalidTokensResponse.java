public class InvalidTokensResponse extends Exception {
    InvalidTokensResponse(String message) {
        super(message);
    }
    InvalidTokensResponse(Throwable cause){
        super(cause);
    }
}
