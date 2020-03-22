package dlp.bluelupin.dlp.Models;

/**
 * Created by Neeraj on 8/9/2016.
 */
public class FavoritesData {
    private int id;
    private String FavoritesFlag;
    private String updatedAt;

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFavoritesFlag() {
        return FavoritesFlag;
    }

    public void setFavoritesFlag(String favoritesFlag) {
        FavoritesFlag = favoritesFlag;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    private int serverId; // this represents server id

    private String updated_at;

    private int sequence;

    private String deleted_at;

    private int media_id;

    private String created_at;

    private int thumbnail_media_id;

    private String lang_resource_description;

    private String lang_resource_name;

    private String type;

    private String url;

    private int parent_id;

    private int clientId;

    private String file_path;

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }


    public int getClient_id() {
        return client_id;
    }

    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    private int client_id;


    public int getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(int language_id) {
        this.language_id = language_id;
    }

    private int language_id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String name;
    private String content;


    public int getClientId() {
        return clientId;
    }

    public void setClientId(int client_id) {
        this.clientId = client_id;
    }


    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getThumbnail_media_id() {
        return thumbnail_media_id;
    }

    public void setThumbnail_media_id(int thumbnail_media_id) {
        this.thumbnail_media_id = thumbnail_media_id;
    }

    public String getLang_resource_description() {
        return lang_resource_description;
    }

    public void setLang_resource_description(String lang_resource_description) {
        this.lang_resource_description = lang_resource_description;
    }

    public String getLang_resource_name() {
        return lang_resource_name;
    }

    public void setLang_resource_name(String lang_resource_name) {
        this.lang_resource_name = lang_resource_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    @Override
    public String toString() {
        return "ClassPojo [id = " + id + ", updated_at = " + updated_at + ", sequence = " + sequence + ", deleted_at = " + deleted_at + ", media_id = " + media_id + ", created_at = " + created_at + ", thumbnail_media_id = " + thumbnail_media_id + ", lang_resource_description = " + lang_resource_description + ", lang_resource_name = " + lang_resource_name + ", type = " + type + ", url = " + url + ", parent_id = " + parent_id + ",serverId = " + serverId + ",FavoritesFlag = " + FavoritesFlag + ",updatedAt = " + updatedAt + "]";
    }
}
