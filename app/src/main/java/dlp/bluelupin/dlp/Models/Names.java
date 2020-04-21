package dlp.bluelupin.dlp.Models;

public class Names {
    private String updated_at;

    private String name;

    private String created_at;

    private int id;

    private int language_id;

    private String content;

    private int client_id;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [updated_at = "+updated_at+", name = "+name+", created_at = "+created_at+", id = "+id+", language_id = "+language_id+", content = "+content+", client_id = "+client_id+"]";
    }
}
