package dlp.bluelupin.dlp.Models;

public class Certificates {
    private String updated_at;

    private Coursename coursename;

    private int user_id;

    private int content_id;

    private String created_at;

    private int id;

    private String serial_no;

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Coursename getCoursename() {
        return coursename;
    }

    public void setCoursename(Coursename coursename) {
        this.coursename = coursename;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getContent_id() {
        return content_id;
    }

    public void setContent_id(int content_id) {
        this.content_id = content_id;
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

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [updated_at = "+updated_at+", coursename = "+coursename+", user_id = "+user_id+", content_id = "+content_id+", created_at = "+created_at+", id = "+id+", serial_no = "+serial_no+"]";
    }
}
