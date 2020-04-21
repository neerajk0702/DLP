package dlp.bluelupin.dlp.Models;

public class Invitations {
    private String created_at;
    private String updated_at;
    private int created_by;
    private String signedup_date;
    private int id;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCreated_by() {
        return created_by;
    }

    public void setCreated_by(int created_by) {
        this.created_by = created_by;
    }

    public String getSignedup_date() {
        return signedup_date;
    }

    public void setSignedup_date(String signedup_date) {
        this.signedup_date = signedup_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public String toString()
    {
        return "ClassPojo [signedup_date = "+signedup_date+", updated_at = "+updated_at+", phone = "+phone+", created_at = "+created_at+", id = "+id+", created_by = "+created_by+"]";
    }
}
