package com.marketdigitalcode.multiads;

import static com.marketdigitalcode.adslibrary.util.AdsConstant.ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.AD_STATUS_ON;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.GOOGLE_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.marketdigitalcode.adslibrary.AdsType;
import com.marketdigitalcode.adslibrary.AppOpenAd;
import com.marketdigitalcode.adslibrary.BuildConfig;
import com.marketdigitalcode.adslibrary.face.OnShowAdCompleteListener;
import com.marketdigitalcode.uti.AppConstant;
import com.marketdigitalcode.uti.AppTools;

@SuppressLint("CustomSplashScreen")
public class ScreenActivity extends AppCompatActivity implements OnShowAdCompleteListener {

    public long leftTime = 0;
    SplashScreen splashScreen;
    TimerSplash timerSplash;
    ProgressBar mProgressBar;

    AppCompatActivity activity;

    AdsType.Initialize adNetwork;
    AppOpenAd appOpenAdBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT > 31) {
            splashScreen = androidx.core.splashscreen.SplashScreen.installSplashScreen(this);
        }
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        activity = ScreenActivity.this;
        mProgressBar = findViewById(R.id.view_progressBar);
        initAds();
        loadOpenAds();
        if (!TextUtils.isEmpty(AppConstant.OPEN_ADS_ID)) {
            stopTimer();
            starTimer();
        }
    }


    private void initAds() {
        adNetwork = new AdsType.Initialize(activity)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdsType(AppConstant.ADS_TYPE)
                .setBackupAdsType(AppConstant.BACKUP_ADS_TYPE)
                .setAppId(AppConstant.APP_ID)
                .setDebug(BuildConfig.DEBUG)
                .build();


    }

    private void loadOpenAds() {
        appOpenAdBuilder = new AppOpenAd(this)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdType(AppConstant.ADS_TYPE)
                .setBackupAdType(AppConstant.BACKUP_ADS_TYPE)
                .setOpenAdsId(AppConstant.OPEN_ADS_ID)
                .build(ScreenActivity.this);
    }

    public class TimerSplash extends CountDownTimer {

        public TimerSplash(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            leftTime = millisUntilFinished;
            int progress = (int) (millisUntilFinished / 1000);
            int i = AppConstant.MAX_PROGRESS_SPLASH - progress;
            float f = (((float) i)) / ((float) AppConstant.MAX_PROGRESS_SPLASH);
            int progressInt = Math.round(100 * f);
            mProgressBar.setProgress(progressInt);
        }

        @Override
        public void onFinish() {
            leftTime = 0;
            mProgressBar.setProgress(100);
            setProcess(ScreenActivity.this);
            stopTimer();
        }
    }

    private void setProcess(ScreenActivity screenActivity) {
        if (AppConstant.AD_STATUS.equals(AD_STATUS_ON) && AppConstant.isAppOpen) {
            TimerSplash splashTimer = screenActivity.timerSplash;
            if (splashTimer != null) {
                splashTimer.cancel();
            }
            Application application = screenActivity.getApplication();
            if (!(application instanceof MyApplication)) {
                screenActivity.goToMainApp();
            } else if (screenActivity.isFinishing()) {
                AppTools.setLog("Menyelesaikan SplashActivity");
            } else {
                if (!TextUtils.isEmpty(AppConstant.OPEN_ADS_ID)) {
                    ((MyApplication) getApplication()).showAdIfAvailable(ScreenActivity.this, ScreenActivity.this);
                } else {
                    goToMainApp();
                }
            }
        } else {
            goToMainApp();
        }
    }

    private void goToMainApp() {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(ScreenActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.myanim_in, R.anim.myanim_out);
        }, 200);
    }

    @Override
    public void onShowAdComplete() {
        goToMainApp();
    }

    public void starTimer() {
        timerSplash = new TimerSplash(AppConstant.TIME_SPLASH, AppConstant.COUNT_DOWN_TIMER);
        timerSplash.start();
    }

    public void stopTimer() {
        if (timerSplash != null)
            timerSplash.cancel();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();

    }


    @Override
    public void onDestroy() {
        leftTime = 0;
        stopTimer();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.in_from_bottom, 0);
    }
}