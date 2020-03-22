package dlp.bluelupin.dlp.Models;

/**
 * Created by harsh on 13-04-2018.
 */

public class ApplicationVersionResponse {

    private App_version app_version;

    public App_version getApp_version() {
        return app_version;
    }

    public void setApp_version(App_version android) {
        this.app_version = android;
    }

    @Override
    public String toString() {
        return "ClassPojo [android = " + app_version + "]";
    }
}




