package com.marketdigitalcode.multiads;

import static com.marketdigitalcode.adslibrary.util.AdsConstant.ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.AD_STATUS_ON;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.GOOGLE_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.multidex.MultiDex;


import com.marketdigitalcode.adslibrary.AppOpenAdAppLovin;
import com.marketdigitalcode.adslibrary.AppOpenAdManager;
import com.marketdigitalcode.adslibrary.AppOpenAdMob;
import com.marketdigitalcode.adslibrary.AppOpenAdWortise;
import com.marketdigitalcode.adslibrary.face.OnShowAdCompleteListener;
import com.marketdigitalcode.uti.AppConstant;

public class MyApplication extends Application {
    private AppOpenAdMob appOpenAdMob;
    private AppOpenAdManager appOpenAdManager;
    private AppOpenAdAppLovin appOpenAdAppLovin;
    private AppOpenAdWortise appOpenAdWortise;
    Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(lifecycleObserver);
        appOpenAdMob = new AppOpenAdMob();
        appOpenAdManager = new AppOpenAdManager();
        appOpenAdAppLovin = new AppOpenAdAppLovin();
        appOpenAdWortise = new AppOpenAdWortise();
    }




    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
    LifecycleObserver lifecycleObserver = new DefaultLifecycleObserver() {
        @Override
        public void onStart(@NonNull LifecycleOwner owner) {
            DefaultLifecycleObserver.super.onStart(owner);
            if (AppConstant.isAppOpen) {
                if (AppConstant.AD_STATUS.equals(AD_STATUS_ON)) {
                    switch (AppConstant.ADS_TYPE) {
                        case ADMOB:
                            if (!TextUtils.isEmpty(AppConstant.OPEN_ADS_ID)) {
                                if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                    appOpenAdMob.showAdIfAvailable(currentActivity, AppConstant.ADMOB_APP_OPEN_AD_ID);
                                }
                            }

                            break;
                        case GOOGLE_AD_MANAGER:
                            if (!TextUtils.isEmpty(AppConstant.OPEN_ADS_ID)) {
                                if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                    appOpenAdManager.showAdIfAvailable(currentActivity, AppConstant.GOOGLE_AD_MANAGER_APP_OPEN_AD_ID);
                                }
                            }

                            break;
                        case APPLOVIN:
                        case APPLOVIN_MAX:
                            if (!TextUtils.isEmpty(AppConstant.OPEN_ADS_ID)) {
                                if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                    appOpenAdAppLovin.showAdIfAvailable(currentActivity, AppConstant.APPLOVIN_APP_OPEN_AP_ID);
                                }
                            }

                            break;

                        case WORTISE:
                            if (!TextUtils.isEmpty(AppConstant.OPEN_ADS_ID)) {
                                if (!currentActivity.getIntent().hasExtra("unique_id")) {
                                    appOpenAdWortise.showAdIfAvailable(currentActivity, AppConstant.WORTISE_APP_OPEN_AD_ID);
                                }
                            }

                            break;
                    }
                }
            }
        }
    };
    ActivityLifecycleCallbacks activityLifecycleCallbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            if (AppConstant.AD_STATUS.equals(AD_STATUS_ON)) {
                if (AppConstant.isAppOpen) {
                    switch (AppConstant.ADS_TYPE) {
                        case ADMOB:
                            if (!appOpenAdMob.isShowingAd) {
                                currentActivity = activity;
                            }
                            break;
                        case GOOGLE_AD_MANAGER:
                            if (!appOpenAdManager.isShowingAd) {
                                currentActivity = activity;
                            }
                            break;
                        case APPLOVIN:
                        case APPLOVIN_MAX:
                            if (!appOpenAdAppLovin.isShowingAd) {
                                currentActivity = activity;
                            }
                            break;
                        case WORTISE:
                            if (!appOpenAdWortise.isShowingAd) {
                                currentActivity = activity;
                            }
                            break;
                    }
                }

            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {

        }
    };
    public void showAdIfAvailable(Activity activity, OnShowAdCompleteListener onShowAdCompleteListener) {
        if (AppConstant.AD_STATUS.equals(AD_STATUS_ON)) {
            if (AppConstant.isAppOpen) {
                switch (AppConstant.ADS_TYPE) {
                    case ADMOB:
                        appOpenAdMob.showAdIfAvailable(activity, AppConstant.OPEN_ADS_ID, onShowAdCompleteListener);
                        break;
                    case GOOGLE_AD_MANAGER:
                        appOpenAdManager.showAdIfAvailable(activity, AppConstant.OPEN_ADS_ID, onShowAdCompleteListener);
                        break;
                    case APPLOVIN:
                    case APPLOVIN_MAX:
                        appOpenAdAppLovin.showAdIfAvailable(activity,AppConstant.OPEN_ADS_ID, onShowAdCompleteListener);
                        break;
                    case WORTISE:
                        appOpenAdWortise.showAdIfAvailable(activity, AppConstant.OPEN_ADS_ID, onShowAdCompleteListener);
                        break;
                }
            }

        }
    }

}
