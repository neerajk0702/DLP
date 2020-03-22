package dlp.bluelupin.dlp.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

import dlp.bluelupin.dlp.Consts;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Utilities.Utility;

public class AudioActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener, VideoControllerView.MediaPlayerControl {


    SurfaceView audioSurface;
    MediaPlayer player;
    VideoControllerView controller;
    private String audioUrl;
    ProgressDialog pDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);

        if (Utility.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        String msg = getString(R.string.buffring);
        pDialog = new ProgressDialog(AudioActivity.this);
        pDialog.setMessage(msg);
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            audioUrl = extras.getString("audioUrl");
        }
        audioSurface = (SurfaceView) findViewById(R.id.audioSurface);
        SurfaceHolder videoHolder = audioSurface.getHolder();
        videoHolder.addCallback(this);

        player = new MediaPlayer();
        controller = new VideoControllerView(this);

        try {
            player.setAudioStreamType(AudioManager.STREAM_MUSIC);
            if (audioUrl != null) {
                player.setDataSource(this, Uri.parse(audioUrl));
            }
            player.setOnPreparedListener(this);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
        Utility.setLangRecreate(AudioActivity.this, Utility.getLanguageCodeFromSharedPreferences(AudioActivity.this));
        player.reset();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controller.show();
        return false;
    }

    // Implement SurfaceHolder.Callback
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        player.setDisplay(holder);
        player.prepareAsync();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    // End SurfaceHolder.Callback

    // Implement MediaPlayer.OnPreparedListener
    @Override
    public void onPrepared(MediaPlayer mp) {
        controller.setMediaPlayer(this);
        controller.setAnchorView((FrameLayout) findViewById(R.id.audioSurfaceContainer));
        player.start();
        pDialog.dismiss();
    }
    // End MediaPlayer.OnPreparedListener

    // Implement VideoMediaController.MediaPlayerControl
    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        try {
            return player.getCurrentPosition();
        } catch (Exception e) {
            Log.e(Consts.LOG_TAG, "error: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public int getDuration() {
        try {
            return player.getDuration();
        } catch (Exception e) {
            Log.e(Consts.LOG_TAG, "error: " + e.getMessage(), e);
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        try {
            return player.isPlaying();
        } catch (Exception e) {
            Log.e(Consts.LOG_TAG, "error: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void pause() {
        player.pause();
    }

    @Override
    public void seekTo(int i) {
        player.seekTo(i);
    }

    @Override
    public void start() {
        player.start();
    }

    @Override
    public boolean isFullScreen() {
        return false;
    }

    @Override
    public void toggleFullScreen() {

    }
    // End VideoMediaController.MediaPlayerControl
}
