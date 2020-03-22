package dlp.bluelupin.dlp.Models;

/**
 * Created by Neeraj on 7/29/2016.
 */
public class OtpData {
    private String message;
    private String api_token;

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
