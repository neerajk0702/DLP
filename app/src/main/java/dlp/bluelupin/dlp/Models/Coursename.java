package dlp.bluelupin.dlp.Models;

import java.util.List;

public class Coursename {
    private String lang_resource_name;

    private String created_at;

    private String type;

    private String deleted_at;

    private String url;

    private int sequence;
    private List<Names> names;
    private String updated_at;

    private int parent_id;

    private String lang_resource_description;

    private int media_id;

    private int id;
    private int thumbnail_media_id;

    public String getLang_resource_name() {
        return lang_resource_name;
    }

    public void setLang_resource_name(String lang_resource_name) {
        this.lang_resource_name = lang_resource_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public List<Names> getNames() {
        return names;
    }

    public void setNames(List<Names> names) {
        this.names = names;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public String getLang_resource_description() {
        return lang_resource_description;
    }

    public void setLang_resource_description(String lang_resource_description) {
        this.lang_resource_description = lang_resource_description;
    }

    public int getMedia_id() {
        return media_id;
    }

    public void setMedia_id(int media_id) {
        this.media_id = media_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThumbnail_media_id() {
        return thumbnail_media_id;
    }

    public void setThumbnail_media_id(int thumbnail_media_id) {
        this.thumbnail_media_id = thumbnail_media_id;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [lang_resource_name = "+lang_resource_name+", created_at = "+created_at+", type = "+type+", deleted_at = "+deleted_at+", url = "+url+", sequence = "+sequence+", names = "+names+", updated_at = "+updated_at+", parent_id = "+parent_id+", lang_resource_description = "+lang_resource_description+", media_id = "+media_id+", id = "+id+", thumbnail_media_id = "+thumbnail_media_id+"]";
    }

}
