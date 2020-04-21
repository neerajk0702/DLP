package dlp.bluelupin.dlp.Models;

public class Content_status {
    private String updated_at;

    private int content_id;

    private int completion_status;

    public String getUpdated_at ()
    {
        return updated_at;
    }

    public void setUpdated_at (String updated_at)
    {
        this.updated_at = updated_at;
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
        return "ClassPojo [updated_at = "+updated_at+", content_id = "+content_id+", completion_status = "+completion_status+"]";
    }
}


