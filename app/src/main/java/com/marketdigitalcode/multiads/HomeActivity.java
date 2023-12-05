package com.marketdigitalcode.multiads;

import static com.marketdigitalcode.adslibrary.util.AdsConstant.ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_DISCOVERY;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPNEXT;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.GOOGLE_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.IRONSOURCE;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STARTAPP;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STYLE_NEWS;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STYLE_RADIO;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STYLE_VIDEO_LARGE;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STYLE_VIDEO_SMALL;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.UNITY;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;
import static com.marketdigitalcode.uti.AppConstant.STYLE_DEFAULT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.marketdigitalcode.adslibrary.AdsType;
import com.marketdigitalcode.adslibrary.AppOpenAd;
import com.marketdigitalcode.adslibrary.BuildConfig;
import com.marketdigitalcode.adslibrary.MdcBanner;
import com.marketdigitalcode.adslibrary.MdcInterstitial;
import com.marketdigitalcode.adslibrary.MdcNative;
import com.marketdigitalcode.adslibrary.MdcRewarded;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdCompleteListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdDismissedListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdErrorListener;
import com.marketdigitalcode.adslibrary.view.NativeAdView;
import com.marketdigitalcode.uti.AppConstant;
import com.marketdigitalcode.uti.AppTools;
import com.marketdigitalcode.uti.SharedPref;

public class HomeActivity extends AppCompatActivity {
    Toolbar toolbar;
    AdsType.Initialize adNetwork;
    MdcBanner mdcBanner;
    MdcNative mdcNative;
    MdcInterstitial mdcInterstitial;
    MdcRewarded rewardedAd;
    AppCompatActivity activity;
    SwitchMaterial switchMaterial;
    Button btnInterstitial;
    Button btnSelectAds, btnNativeAdStyle,btnRewarded;
    SharedPref sharedPref;
    NativeAdView native_ad;

    AppOpenAd appOpenAdBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        activity = HomeActivity.this;
        appOpenAdBuilder = new AppOpenAd(activity);
        sharedPref = new SharedPref(activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initAds();
        loadOpenAds();
        loadBannerAd();
        loadInterstitialAd();
        loadRewardedAd();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);

        native_ad = new NativeAdView(activity);
        native_ad = findViewById(R.id.native_ad);

        setNativeAdStyle(native_ad);
        btnInterstitial = findViewById(R.id.btn_interstitial);
        btnInterstitial.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), SecondActivity.class));
            showInterstitialAd();
            destroyBannerAd();
        });

        btnSelectAds = findViewById(R.id.btn_select_ads);
        btnSelectAds.setOnClickListener(v -> showAdChooser());
        btnNativeAdStyle = findViewById(R.id.btn_native_ad_style);
        btnNativeAdStyle.setOnClickListener(v -> changeNativeAdStyle());
        btnRewarded = findViewById(R.id.btn_rewarded);
        btnRewarded.setOnClickListener(view -> showRewardedAd());
        switchAppTheme();
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

    private void loadBannerAd() {
        mdcBanner = new MdcBanner(activity)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdsType(AppConstant.ADS_TYPE)
                .setBackupAdsType(AppConstant.BACKUP_ADS_TYPE)
                .setLegacyGDPR(false)
                .setBannerId(AppConstant.BANNER_ID)
                .setDarkTheme(false)
                .build();
    }


    private void loadNativeAdView(View view) {
        mdcNative = new MdcNative(this)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdsType(AppConstant.ADS_TYPE)
                .setBackupAdsType(AppConstant.BACKUP_ADS_TYPE)
                .setNativeId(AppConstant.NATIVE_ID)
                .setNativeAdStyle(AppConstant.NATIVE_STYLE)
                .setNativeAdBackgroundColor(R.color.colorNativeBackgroundLight, R.color.colorNativeBackgroundDark)
                .setDarkTheme(sharedPref.getIsDarkTheme())
                .setView(view)
                .build();
        mdcNative.setPadding(0, 0, 0, 0);
    }

    private void loadInterstitialAd() {
        mdcInterstitial = new MdcInterstitial(activity)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdsType(AppConstant.ADS_TYPE)
                .setBackupAdsType(AppConstant.BACKUP_ADS_TYPE)
                .setInterId(AppConstant.INTER_ID)
                .setInterval(AppConstant.INTERSTITIAL_AD_INTERVAL)
                .build(() -> {
                    AppTools.setLog("onAdDismissed");
                });
    }
    private void loadRewardedAd() {
        rewardedAd = new MdcRewarded(activity)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdType(AppConstant.ADS_TYPE)
                .setBackupAdType(AppConstant.BACKUP_ADS_TYPE)
                .setRewardId(AppConstant.REWARD_ID)
                .build(new OnRewardedAdCompleteListener() {
                    @Override
                    public void onRewardedAdComplete() {
                        Toast.makeText(getApplicationContext(), "Rewarded complete", Toast.LENGTH_SHORT).show();
                    }
                }, new OnRewardedAdDismissedListener() {
                    @Override
                    public void onRewardedAdDismissed() {

                    }
                });
    }
    private void showRewardedAd() {
        rewardedAd.show(new OnRewardedAdCompleteListener() {
            @Override
            public void onRewardedAdComplete() {
                Toast.makeText(getApplicationContext(), "Rewarded complete", Toast.LENGTH_SHORT).show();
            }
        }, new OnRewardedAdDismissedListener() {
            @Override
            public void onRewardedAdDismissed() {

            }
        }, new OnRewardedAdErrorListener() {
            @Override
            public void onRewardedAdError() {
                Toast.makeText(getApplicationContext(), "Rewarded error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadOpenAds() {
        appOpenAdBuilder = new AppOpenAd(this)
                .setAdStatus(AppConstant.AD_STATUS)
                .setAdType(AppConstant.ADS_TYPE)
                .setBackupAdType(AppConstant.BACKUP_ADS_TYPE)
                .setOpenAdsId(AppConstant.OPEN_ADS_ID)
                .build();
    }
    LifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onStart(@NonNull LifecycleOwner owner) {
            DefaultLifecycleObserver.super.onStart(owner);
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (AppOpenAd.isAppOpenAdLoaded) {
                    appOpenAdBuilder.show();
                }
            }, 100);
        }
    };
    private void showInterstitialAd() {
        mdcInterstitial.show(() -> {
            AppTools.setLog("onAdShowed");
        }, () -> {
            AppTools.setLog("onAdDismissed");
        });

    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        destroyBannerAd();
    }


    private void destroyBannerAd() {
        mdcBanner.destroyAndDetachBanner();
    }

    @Override
    public void onResume() {
        super.onResume();

        mdcBanner.loadBannerAd();
        mdcNative.loadNativeAd();

    }

    private void switchAppTheme() {
        switchMaterial = findViewById(R.id.switch_theme);
        switchMaterial.setChecked(sharedPref.getIsDarkTheme());
        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            sharedPref.setIsDarkTheme(isChecked);
            recreate();
        });
    }

    private void setNativeAdStyle(NativeAdView nativeAd) {
        switch (AppConstant.NATIVE_STYLE) {
            case "news":
                nativeAd.setNews();
                loadNativeAdView(nativeAd);
                break;
            case "radio":
                nativeAd.setRadio();
                loadNativeAdView(nativeAd);
                break;
            case "video_small":
                nativeAd.setVideoSmall();
                loadNativeAdView(nativeAd);
                break;
            case "video_large":
                nativeAd.setVideoLarge();
                loadNativeAdView(nativeAd);
                break;
            default:
                nativeAd.setMedium();
                loadNativeAdView(nativeAd);
                break;
        }
    }

    private void changeNativeAdStyle() {
        final String[] styles = {"Default", "News", "Radio", "Video Small", "Video Large"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Native Style");
        builder.setItems(styles, (dialog, which) -> {
            String selectedItem = styles[which];
            switch (selectedItem) {
                case "Default":
                    AppConstant.NATIVE_STYLE = STYLE_DEFAULT;
                    break;
                case "News":
                    AppConstant.NATIVE_STYLE = STYLE_NEWS;
                    break;
                case "Radio":
                    AppConstant.NATIVE_STYLE = STYLE_RADIO;
                    break;
                case "Video Small":
                    AppConstant.NATIVE_STYLE = STYLE_VIDEO_SMALL;
                    break;
                case "Video Large":
                    AppConstant.NATIVE_STYLE = STYLE_VIDEO_LARGE;
                    break;
                default:
                    AppConstant.NATIVE_STYLE = STYLE_DEFAULT;
                    break;
            }
            recreate();
        });
        builder.show();
    }

    private void showAdChooser() {
        final String[] ads = {"AdMob", "Google Ad Manager", "Start.io", "AppLovin MAX", "AppLovin Discovery", "Unity Ads", "Appnext Ads", "ironSource", "FAN (Waterfall)", "Wortise"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Ad");
        builder.setItems(ads, (dialog, which) -> {
            String selectedItem = ads[which];
            switch (selectedItem) {
                case "Google Ad Manager":
                    AppConstant.ADS_TYPE = GOOGLE_AD_MANAGER;
                    AppConstant.BACKUP_ADS_TYPE = GOOGLE_AD_MANAGER;
                    AppConstant.APP_ID = null;
                    AppConstant.OPEN_ADS_ID = AppConstant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID;
                    AppConstant.BANNER_ID = AppConstant.GOOGLE_AD_MANAGER_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.GOOGLE_AD_MANAGER_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.GOOGLE_AD_MANAGER_REWARDED_ID;
                    AppConstant.NATIVE_ID = AppConstant.GOOGLE_AD_MANAGER_NATIVE_ID;
                    break;
                case "Start.io":
                    AppConstant.ADS_TYPE = STARTAPP;
                    AppConstant.BACKUP_ADS_TYPE = STARTAPP;
                    AppConstant.APP_ID = AppConstant.STARTAPP_APP_ID;
                    AppConstant.OPEN_ADS_ID = null;
                    AppConstant.BANNER_ID = null;
                    AppConstant.INTER_ID = null;
                    AppConstant.REWARD_ID = null;
                    AppConstant.NATIVE_ID = null;
                    break;
                case "AppLovin MAX":
                    AppConstant.ADS_TYPE = APPLOVIN_MAX;
                    AppConstant.BACKUP_ADS_TYPE = APPLOVIN_MAX;
                    AppConstant.APP_ID = AppConstant.APPLOVIN_SDK_KEY;
                    AppConstant.OPEN_ADS_ID = AppConstant.APPLOVIN_APP_OPEN_AP_ID;
                    AppConstant.BANNER_ID = AppConstant.APPLOVIN_MAX_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.APPLOVIN_MAX_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.APPLOVIN_MAX_INTERSTITIAL_ID;
                    AppConstant.NATIVE_ID = AppConstant.APPLOVIN_MAX_NATIVE_ID;
                    break;
                case "AppLovin Discovery":
                    AppConstant.ADS_TYPE = APPLOVIN_DISCOVERY;
                    AppConstant.BACKUP_ADS_TYPE = APPLOVIN_DISCOVERY;
                    AppConstant.APP_ID = null;
                    AppConstant.OPEN_ADS_ID = null;
                    AppConstant.BANNER_ID = AppConstant.APPLOVIN_BANNER_ZONE_ID;
                    AppConstant.INTER_ID = AppConstant.APPLOVIN_INTERSTITIAL_ZONE_ID;
                    AppConstant.REWARD_ID = AppConstant.APPLOVIN_DISC_REWARDED_ZONE_ID;
                    AppConstant.NATIVE_ID = null;
                    break;
                case "Unity Ads":
                    AppConstant.ADS_TYPE = UNITY;
                    AppConstant.BACKUP_ADS_TYPE = UNITY;
                    AppConstant.APP_ID = AppConstant.UNITY_GAME_ID;
                    AppConstant.OPEN_ADS_ID = null;
                    AppConstant.BANNER_ID = AppConstant.UNITY_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.UNITY_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.UNITY_REWARDED_ID;
                    AppConstant.NATIVE_ID = null;
                    break;
                case "Appnext Ads":
                    AppConstant.ADS_TYPE = APPNEXT;
                    AppConstant.BACKUP_ADS_TYPE = APPNEXT;
                    AppConstant.APP_ID = AppConstant.APP_NEXT_APP_ID;
                    AppConstant.OPEN_ADS_ID = null;
                    AppConstant.BANNER_ID = AppConstant.APP_NEXT_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.APP_NEXT_INTER_ID;
                    AppConstant.REWARD_ID = AppConstant.APP_NEXT_REWARD_ID;
                    AppConstant.NATIVE_ID = null;
                    break;
                case "ironSource":
                    AppConstant.ADS_TYPE = IRONSOURCE;
                    AppConstant.BACKUP_ADS_TYPE = IRONSOURCE;
                    AppConstant.APP_ID = AppConstant.IRONSOURCE_APP_KEY;
                    AppConstant.OPEN_ADS_ID = null;
                    AppConstant.BANNER_ID = AppConstant.IRONSOURCE_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.IRONSOURCE_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.IRONSOURCE_REWARDED_ID;
                    AppConstant.NATIVE_ID = null;
                    break;
                case "FAN (Waterfall)":
                    AppConstant.ADS_TYPE = FAN;
                    AppConstant.BACKUP_ADS_TYPE = FAN;
                    AppConstant.APP_ID = AppConstant.IRONSOURCE_APP_KEY;
                    AppConstant.OPEN_ADS_ID = null;
                    AppConstant.BANNER_ID = AppConstant.FAN_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.FAN_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.FAN_REWARDED_ID;
                    AppConstant.NATIVE_ID = AppConstant.FAN_NATIVE_ID;
                    break;
                case "Wortise":
                    AppConstant.ADS_TYPE = WORTISE;
                    AppConstant.BACKUP_ADS_TYPE = WORTISE;
                    AppConstant.APP_ID = AppConstant.WORTISE_APP_ID;
                    AppConstant.OPEN_ADS_ID = AppConstant.WORTISE_APP_OPEN_AD_ID;
                    AppConstant.BANNER_ID = AppConstant.WORTISE_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.WORTISE_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.WORTISE_REWARDED_ID;
                    AppConstant.NATIVE_ID = AppConstant.WORTISE_NATIVE_ID;
                    break;
                default:
                    AppConstant.ADS_TYPE = ADMOB;
                    AppConstant.BACKUP_ADS_TYPE = ADMOB;
                    AppConstant.APP_ID = AppConstant.ADMOB_APP_ID;
                    AppConstant.OPEN_ADS_ID = AppConstant.ADMOB_APP_OPEN_AD_ID;
                    AppConstant.BANNER_ID = AppConstant.ADMOB_BANNER_ID;
                    AppConstant.INTER_ID = AppConstant.ADMOB_INTERSTITIAL_ID;
                    AppConstant.REWARD_ID = AppConstant.ADMOB_REWARDED_ID;
                    AppConstant.NATIVE_ID = AppConstant.ADMOB_NATIVE_ID;
                    break;
            }
            recreate();
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyBannerAd();
        destroyAppOpenAd();
    }
    private void destroyAppOpenAd() {
        appOpenAdBuilder.destroyOpenAd();
        ProcessLifecycleOwner.get().getLifecycle().removeObserver(lifecycleObserver);
    }
}