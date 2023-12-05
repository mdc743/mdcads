package com.marketdigitalcode.adslibrary;

import static com.marketdigitalcode.adslibrary.util.AdsConstant.ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.AD_STATUS_ON;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_DISCOVERY;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPNEXT;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FACEBOOK;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_IRONSOURCE;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.GOOGLE_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.IRONSOURCE;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.NONE;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STARTAPP;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.UNITY;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import android.app.Activity;

import com.applovin.sdk.AppLovinMediationProvider;
import com.applovin.sdk.AppLovinSdk;
import com.appnext.base.Appnext;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.ironsource.mediationsdk.IronSource;
import com.marketdigitalcode.adslibrary.helper.AudienceNetworkInitializeHelper;
import com.marketdigitalcode.adslibrary.util.AdsTools;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.unity3d.mediation.IInitializationListener;
import com.unity3d.mediation.InitializationConfiguration;
import com.unity3d.mediation.UnityMediation;
import com.unity3d.mediation.errors.SdkInitializationError;
import com.wortise.ads.WortiseSdk;

import java.util.Map;

public class AdsType {
    public static class Initialize {
        Activity activity;
        private String adStatus = "";
        private String adsType = "";
        private String backupAdsType = "";
        private String appId = "";
        private boolean debug = true;

        public Initialize(Activity activity) {
            this.activity = activity;
        }

        public Initialize build() {
            initAds();
            initBackupAds();
            return this;
        }
        public Initialize setAdStatus(String adStatus) {
            this.adStatus = adStatus;
            return this;
        }

        public Initialize setAdsType(String adsType) {
            this.adsType = adsType;
            return this;
        }

        public Initialize setBackupAdsType(String backupAdsType) {
            this.backupAdsType = backupAdsType;
            return this;
        }
        public Initialize setAppId(String AppId) {
            this.appId = AppId;
            return this;
        }

        public Initialize setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }
        public void initAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (adsType) {
                    case ADMOB:
                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_ADMOB:
                    case FAN_BIDDING_AD_MANAGER:
                        MobileAds.initialize(activity, initializationStatus -> {
                            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                            for (String adapterClass : statusMap.keySet()) {
                                AdapterStatus adapterStatus = statusMap.get(adapterClass);
                                assert adapterStatus != null;
                            }
                        });
                        AudienceNetworkInitializeHelper.initializeAd(activity, debug);
                        break;
                    case FAN:
                    case FACEBOOK:
                        AudienceNetworkInitializeHelper.initializeAd(activity, debug);
                        break;
                    case STARTAPP:
                        StartAppSDK.init(activity, appId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case UNITY:
                        InitializationConfiguration configuration = InitializationConfiguration.builder()
                                .setGameId(appId)
                                .setInitializationListener(new IInitializationListener() {
                                    @Override
                                    public void onInitializationComplete() {
                                        AdsTools.setLogString("Unity Mediation is successfully initialized. with ID : " + appId);
                                    }

                                    @Override
                                    public void onInitializationFailed(SdkInitializationError errorCode, String msg) {
                                        AdsTools.setLogString("Unity Mediation Failed to Initialize : " +msg);
                                    }
                                }).build();
                        UnityMediation.initialize(configuration);
                        break;
                    case APPNEXT:
                        Appnext.init(activity);
                        break;
                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;

                    case APPLOVIN_DISCOVERY:
                        AppLovinSdk.initializeSdk(activity);
                        break;


                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        String advertisingId = IronSource.getAdvertiserId(activity);
                        IronSource.setUserId(advertisingId);
                        IronSource.init(activity, appId, () -> {
                            AdsTools.setLogString("[" + adsType + "] initialize complete");
                        });
                        break;

                    case WORTISE:
                        WortiseSdk.initialize(activity, appId);
                        break;
                }
                AdsTools.setLogString("[" + adsType + "] is selected as Primary Ads");
            }
        }
        public void initBackupAds() {
            if (adStatus.equals(AD_STATUS_ON)) {
                switch (backupAdsType) {
                    case ADMOB:
                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_ADMOB:
                    case FAN_BIDDING_AD_MANAGER:
                        MobileAds.initialize(activity, initializationStatus -> {
                            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
                            for (String adapterClass : statusMap.keySet()) {
                                AdapterStatus adapterStatus = statusMap.get(adapterClass);
                                assert adapterStatus != null;
                            }
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;
                    case FAN:
                    case FACEBOOK:
                        AudienceNetworkInitializeHelper.initializeAd(activity, debug);
                        break;
                    case STARTAPP:
                        StartAppSDK.init(activity, appId, false);
                        StartAppSDK.setTestAdsEnabled(debug);
                        StartAppAd.disableSplash();
                        StartAppSDK.setUserConsent(activity, "pas", System.currentTimeMillis(), true);
                        break;
                    case UNITY:
                        InitializationConfiguration configuration = InitializationConfiguration.builder()
                                .setGameId(appId)
                                .setInitializationListener(new IInitializationListener() {
                                    @Override
                                    public void onInitializationComplete() {
                                        AdsTools.setLogString("Unity Mediation is successfully initialized. with ID : " + appId);

                                    }

                                    @Override
                                    public void onInitializationFailed(SdkInitializationError errorCode, String msg) {
                                        AdsTools.setLogString("Unity Mediation Failed to Initialize : " + msg);
                                    }
                                }).build();
                        UnityMediation.initialize(configuration);
                        break;
                    case APPNEXT:
                        Appnext.init(activity);
                        break;
                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        AppLovinSdk.getInstance(activity).setMediationProvider(AppLovinMediationProvider.MAX);
                        AppLovinSdk.getInstance(activity).initializeSdk(config -> {
                        });
                        AudienceNetworkInitializeHelper.initialize(activity);
                        break;

                    case APPLOVIN_DISCOVERY:
                        AppLovinSdk.initializeSdk(activity);
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        String advertisingId = IronSource.getAdvertiserId(activity);
                        IronSource.setUserId(advertisingId);
                        IronSource.init(activity, appId, () -> {
                            AdsTools.setLogString("[" + adsType + "] initialize complete");
                        });
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.REWARDED_VIDEO);
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.INTERSTITIAL);
//                        IronSource.init(activity, ironSourceAppKey, IronSource.AD_UNIT.BANNER);
                        break;

                    case WORTISE:
                        WortiseSdk.initialize(activity, appId);
                        break;

                    case NONE:
                        //do nothing
                        break;
                }
                AdsTools.setLogString("[" + adsType + "] is selected as Primary Ads");
            }
        }
    }
}
