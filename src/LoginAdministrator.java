public class LoginAdministrator {
    private final String url;
    private final Tokens tokens;


    LoginAdministrator(String url, Tokens tokens){
        this.url = url;
        this.tokens = tokens;
    }

    LoginAdministrator(String url, String username, String password){
        this.url = url;
        //server
        tokens = new Tokens("", "");
        //guardar tokens en memoria
    }
}
