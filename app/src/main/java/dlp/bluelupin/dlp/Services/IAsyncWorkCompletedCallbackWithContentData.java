package dlp.bluelupin.dlp.Services;

import dlp.bluelupin.dlp.Models.ContentData;

public interface IAsyncWorkCompletedCallbackWithContentData {
    public void onDone(ContentData workName, boolean isComplete);
}
