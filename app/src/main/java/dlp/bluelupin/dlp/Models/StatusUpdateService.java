package dlp.bluelupin.dlp.Models;

public class StatusUpdateService {
    private String api_token;
    private int content_id;
    private int completion_status;
    private String updated_at;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getApi_token() {
        return api_token;
    }

    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
    }

    public int getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(int completion_status) {
        this.completion_status = completion_status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [api_token = "+api_token+", content_id = "+content_id+", completion_status = "+completion_status+", updated_at = "+updated_at+"]";
    }
}
