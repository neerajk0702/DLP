package dlp.bluelupin.dlp.Utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

import dlp.bluelupin.dlp.Consts;

/**
 * Created by Neeraj on 2/28/2017.
 */

public class FontManager {

    // usage: yourTextView.setTypeface(FontManager.getTypeface(FontManager.YOURFONT));
    public static Typeface getTypeface(Context context, String font) {
        return Typeface.createFromAsset(context.getAssets(), font);
    }

    private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    public static Typeface getFontTypeface(Context context, String assetPath) {
        String path = getTypefaceBasedOnLanguage(context);
        if (path != null) {
            assetPath = path;
        }
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                            assetPath);

                    cache.put(assetPath, typeface);
                } catch (Exception e) {
                    Log.e(Consts.LOG_TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    //get FontTypeface for Material Design Icons
    public static Typeface getFontTypefaceMaterialDesignIcons(Context context, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface typeface = Typeface.createFromAsset(context.getAssets(),
                            assetPath);

                    cache.put(assetPath, typeface);
                } catch (Exception e) {
                    Log.e(Consts.LOG_TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }

    //get odia language path if odia language selected
    private static String getTypefaceBasedOnLanguage(Context context) {
        String assetPath = null;
        if (Utility.getLanguageCodeFromSharedPreferences(context).equalsIgnoreCase("or")) {
            assetPath = "fonts/ODIA-OT-V2.TTF";
        }
        if (Utility.getLanguageCodeFromSharedPreferences(context).equalsIgnoreCase("kn")) {
            assetPath = "fonts/Nudiweb01e.ttf";
        }
        if (Utility.getLanguageCodeFromSharedPreferences(context).equalsIgnoreCase("ml")) {
            assetPath = "fonts/kartika_0.ttf";
        }
        if (Utility.getLanguageCodeFromSharedPreferences(context).equalsIgnoreCase("te")) {
            assetPath = "fonts/gautami_0.ttf";
        }
        if (Utility.getLanguageCodeFromSharedPreferences(context).equalsIgnoreCase("bn")) {
            assetPath = "fonts/vrinda_0.ttf";
        }
        if (Utility.getLanguageCodeFromSharedPreferences(context).equalsIgnoreCase("gu")) {
            assetPath = "fonts/shruti_0.ttf";
        }
        return assetPath;
    }
}
