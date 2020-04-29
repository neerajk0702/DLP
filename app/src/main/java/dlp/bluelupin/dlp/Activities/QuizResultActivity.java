package dlp.bluelupin.dlp.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import java.util.List;

import dlp.bluelupin.dlp.Database.DbHelper;
import dlp.bluelupin.dlp.MainActivity;
import dlp.bluelupin.dlp.Models.QuizAnswer;
import dlp.bluelupin.dlp.R;
import dlp.bluelupin.dlp.Utilities.FontManager;
import dlp.bluelupin.dlp.Utilities.Utility;

public class QuizResultActivity extends AppCompatActivity implements View.OnClickListener  {
    private TextView trophyIcon, score_text, restart_icon, restart_text, quit_text, quit_icon;
    private Context context;
    Typeface materialdesignicons_font;
    private Handler handler = new Handler();
    TextView out,leftArrow,title;
    MainActivity rootActivity;
    private int quizId, contentId;
    private int totalQuestion;
    private List<QuizAnswer> correctAnsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_right);
        setContentView(R.layout.fragment_quiz_result);
        context=QuizResultActivity.this;
        if (Utility.isTablet(this)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        init();
    }
    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        leftArrow = toolbar.findViewById(R.id.leftArrow);
        title =  toolbar.findViewById(R.id.title);
        materialdesignicons_font = FontManager.getFontTypefaceMaterialDesignIcons(context, "fonts/materialdesignicons-webfont.otf");
        Typeface VodafoneExB = FontManager.getFontTypeface(context, "fonts/VodafoneExB.TTF");
        Typeface VodafoneRg = FontManager.getFontTypeface(context, "fonts/VodafoneRg.ttf");
        Typeface VodafoneRgBd = FontManager.getFontTypeface(context, "fonts/VodafoneRgBd.ttf");
        Typeface VodafoneLt = FontManager.getFontTypeface(context, "fonts/VodafoneLt.ttf");
        leftArrow.setTypeface(materialdesignicons_font);
        leftArrow.setOnClickListener(this);
        leftArrow.setText(Html.fromHtml("&#xf04d;"));
        title.setTypeface(VodafoneExB);
//        rootActivity = (MainActivity) getActivity();
//        rootActivity.setShowQuestionIconOption(false);

        quizId = getIntent().getExtras().getInt("quizId");
        contentId = getIntent().getExtras().getInt("contentId");
        totalQuestion = getIntent().getExtras().getInt("totalQuestion");

        trophyIcon = (TextView) findViewById(R.id.trophyIcon);
        score_text = (TextView) findViewById(R.id.score_text);
        out = (TextView) findViewById(R.id.out);
        restart_icon = (TextView) findViewById(R.id.restart_icon);
        restart_text = (TextView) findViewById(R.id.restart_text);
        quit_text = (TextView) findViewById(R.id.quit_text);
        quit_icon = (TextView) findViewById(R.id.quit_icon);
        quit_text.setTypeface(VodafoneExB);
        restart_text.setTypeface(VodafoneExB);
        score_text.setTypeface(VodafoneLt);
        out.setTypeface(VodafoneRg);
        trophyIcon.setTypeface(materialdesignicons_font);
        trophyIcon.setText(Html.fromHtml("&#xf53a;"));
        quit_icon.setTypeface(materialdesignicons_font);
        quit_icon.setText(Html.fromHtml("&#xf054;"));
        restart_icon.setTypeface(materialdesignicons_font);
        restart_icon.setText(Html.fromHtml("&#xf459;"));
        LinearLayout restart_layout = (LinearLayout) findViewById(R.id.restart_layout);
        restart_layout.setOnClickListener(this);
        LinearLayout exitLayout = (LinearLayout) findViewById(R.id.exitLayout);
        exitLayout.setOnClickListener(this);
        DbHelper dbhelper = new DbHelper(context);
        correctAnsList = dbhelper.getAllQuizAnswerEntity(quizId, contentId);

        Resources res = getResources();
        Drawable drawable = res.getDrawable(R.drawable.progress_bar_circle);
        final ProgressBar mProgress = (ProgressBar) findViewById(R.id.circularProgressbar);
        mProgress.setProgress(0);   // Main Progress
        mProgress.setSecondaryProgress(totalQuestion); // Secondary Progress
        mProgress.setMax(totalQuestion); // Maximum Progress
        mProgress.setProgressDrawable(drawable);
        out = (TextView) findViewById(R.id.out);
        mProgress.setProgress(correctAnsList.size());
        out.setText(correctAnsList.size() + " OUT OF " + totalQuestion);

       /* new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (correctAnsList.size() < totalQuestion) {
                    handler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            mProgress.setProgress(correctAnsList.size());
                            out.setText(correctAnsList.size() + " OUT OF " + totalQuestion);

                        }
                    });
                    try {
                        // Sleep for 200 milliseconds.
                        // Just to display the progress slowly
                        Thread.sleep(20); //thread will take approx 3 seconds to finish
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
*/

    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.restart_layout:
                Intent mIntent = new Intent(this, QuizQuestionActivity.class);
                mIntent.putExtra("quizId", quizId);
                mIntent.putExtra("contentId", contentId);
                startActivity(mIntent);
                finish();
                break;
            case R.id.exitLayout:
                Intent intent = new Intent();
                intent.putExtra("QuizeResult", 1);
                setResult(RESULT_OK, intent);
                finish();
                break;
            case R.id.leftArrow:
                Intent Leftintent = new Intent();
                Leftintent.putExtra("QuizeResult", 1);
                setResult(RESULT_OK, Leftintent);
                finish();
                break;
        }
    }
}
