package dlp.bluelupin.dlp.Services;

import dlp.bluelupin.dlp.Models.ContentData;

/**
 * Created by subod on 25-Jul-16.
 */
public interface IAsyncWorkCompletedCallback {
    public void onDone(String workName, boolean isComplete);
}
