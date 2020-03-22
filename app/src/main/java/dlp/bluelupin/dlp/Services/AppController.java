package dlp.bluelupin.dlp.Services;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import dlp.bluelupin.dlp.Utilities.FNObjectUtil;

/**
 * Created by Neeraj on 08/25/2019.
 */
public class AppController extends MultiDexApplication {

    public static final String TAG = AppController.class
            .getSimpleName();
    private static AppController mInstance;
    private String deviceType;
    private String deviceModel;
    private String deviceName;
    private String packageName;
    private Number versionCode;
    private String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Crashlytics.log("SamVaad App Crash Report");
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public PackageInfo packageInfo() {
        PackageManager manager = this.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e2) {
            // FNExceptionUtil.logException(e2, getApplicationContext());
        }
        return info;
    }

    public String packageName() {
        if (this.packageName == null) {
            PackageInfo info = this.packageInfo();
            this.packageName = info != null ? info.packageName : "com.android.foundation";
        }
        return this.packageName;
    }

    public Number versionCode() {
        if (this.versionCode == null) {
            PackageInfo info = this.packageInfo();
            this.versionCode = info != null ? info.versionCode : null;
        }
        return this.versionCode;
    }

    public String versionName() {
        if (this.versionName == null) {
            PackageInfo info = this.packageInfo();
            this.versionName = info != null ? info.versionName : "";
        }
        return this.versionName;
    }
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }


    public String deviceName() {
        if (FNObjectUtil.isEmptyStr(deviceName)) {
            deviceName = Build.MANUFACTURER;
            if (FNObjectUtil.isEmptyStr(deviceName)) {
                deviceName = Build.BRAND;
            }
        }
        return deviceName;
    }
    public String versionNameString() {
        String versionName = this.versionName();
        return FNObjectUtil.isNonEmptyStr(versionName) ? "v " + versionName : "";
    }

    public String deviceType(Context context) {
        if (FNObjectUtil.isEmptyStr(deviceType)) {
            deviceType = isTablet(context) ? "ANDROIDTAB" : "ANDROID";
        }
        return deviceType;
    }
    public String deviceInfo() {
        StringBuilder deviceInfo = new StringBuilder();
        deviceInfo.append("************ DEVICE INFORMATION " +
                "***********\nAndroid version: ").append(Build.VERSION.SDK_INT).append("\nDevice: " +
                "").append(deviceModel()).append("\nApp version: ").append(this.versionCode()).append("\n");
        return deviceInfo.toString();
    }
    public String deviceModel() {
        if (FNObjectUtil.isEmptyStr(deviceModel)) {
            deviceModel = Build.MODEL;
            if (!deviceModel.startsWith(deviceName())) {
                deviceModel = deviceModel.replaceAll(deviceName(), "").trim();
            }
        }
        return deviceModel;
    }
    public String OSInfo() {
        return String.valueOf(Build.VERSION.SDK_INT);
    }
}