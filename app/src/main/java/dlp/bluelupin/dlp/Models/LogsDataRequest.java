package dlp.bluelupin.dlp.Models;

public class LogsDataRequest {
    private String Status;

    private String EmailID;

    private String Mobile_Make;

    private String Action_Type;

    private String MediaUrl;

    private String Mobile_Model;

    private String Initial_Language_Selected;

    private String Latitude;

    private String OS_Version;

    private String Application_Version;

    private String Longitude;

    private String Name;

    private String Course_Selected;

    private String Role;

    private String Date_Time;

    private String Phone;

    private String logEventName;

    private String Language_Name_Code;

    private String Unique_Identifier;

    private String Current_Language_Selected;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getEmailID() {
        return EmailID;
    }

    public void setEmailID(String EmailID) {
        this.EmailID = EmailID;
    }

    public String getMobile_Make() {
        return Mobile_Make;
    }

    public void setMobile_Make(String Mobile_Make) {
        this.Mobile_Make = Mobile_Make;
    }

    public String getAction_Type() {
        return Action_Type;
    }

    public void setAction_Type(String Action_Type) {
        this.Action_Type = Action_Type;
    }

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String MediaUrl) {
        this.MediaUrl = MediaUrl;
    }

    public String getMobile_Model() {
        return Mobile_Model;
    }

    public void setMobile_Model(String Mobile_Model) {
        this.Mobile_Model = Mobile_Model;
    }

    public String getInitial_Language_Selected() {
        return Initial_Language_Selected;
    }

    public void setInitial_Language_Selected(String Initial_Language_Selected) {
        this.Initial_Language_Selected = Initial_Language_Selected;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String Latitude) {
        this.Latitude = Latitude;
    }

    public String getOS_Version() {
        return OS_Version;
    }

    public void setOS_Version(String OS_Version) {
        this.OS_Version = OS_Version;
    }

    public String getApplication_Version() {
        return Application_Version;
    }

    public void setApplication_Version(String Application_Version) {
        this.Application_Version = Application_Version;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String Longitude) {
        this.Longitude = Longitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getCourse_Selected() {
        return Course_Selected;
    }

    public void setCourse_Selected(String Course_Selected) {
        this.Course_Selected = Course_Selected;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String Role) {
        this.Role = Role;
    }

    public String getDate_Time() {
        return Date_Time;
    }

    public void setDate_Time(String Date_Time) {
        this.Date_Time = Date_Time;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getLogEventName() {
        return logEventName;
    }

    public void setLogEventName(String logEventName) {
        this.logEventName = logEventName;
    }

    public String getLanguage_Name_Code() {
        return Language_Name_Code;
    }

    public void setLanguage_Name_Code(String Language_Name_Code) {
        this.Language_Name_Code = Language_Name_Code;
    }

    public String getUnique_Identifier() {
        return Unique_Identifier;
    }

    public void setUnique_Identifier(String Unique_Identifier) {
        this.Unique_Identifier = Unique_Identifier;
    }

    public String getCurrent_Language_Selected() {
        return Current_Language_Selected;
    }

    public void setCurrent_Language_Selected(String Current_Language_Selected) {
        this.Current_Language_Selected = Current_Language_Selected;
    }

    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    private String ContentId;

    public String getContentId() {
        return ContentId;
    }

    public void setContentId(String contentId) {
        ContentId = contentId;
    }

    @Override
    public String toString() {
        return "ClassPojo [Status = " + Status + ", EmailID = " + EmailID + ", Mobile_Make = " + Mobile_Make + ", Action_Type = " + Action_Type + ", MediaUrl = " + MediaUrl + ", Mobile_Model = " + Mobile_Model + ", Initial_Language_Selected = " + Initial_Language_Selected + ", Latitude = " + Latitude + ", OS_Version = " + OS_Version + ", Application_Version = " + Application_Version + ", Longitude = " + Longitude + ", Name = " + Name + ", Course_Selected = " + Course_Selected + ", Role = " + Role + ", Date_Time = " + Date_Time + ", Phone = " + Phone + ", logEventName = " + logEventName + ", Language_Name_Code = " + Language_Name_Code + ", Unique_Identifier = " + Unique_Identifier + ", Current_Language_Selected = " + Current_Language_Selected + ", message = " + message + ",ContentId="+ContentId+"]";
    }
}
