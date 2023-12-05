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
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STARTAPP;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.UNITY;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;

import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.appnext.ads.fullscreen.RewardedVideo;

import com.appnext.core.AppnextAdCreativeType;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;
import com.appnext.core.callbacks.OnAdLoaded;
import com.appnext.core.callbacks.OnVideoEnded;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.RewardedVideoAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdCompleteListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdDismissedListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdErrorListener;
import com.marketdigitalcode.adslibrary.helper.AppLovinCustomEventInterstitial;
import com.marketdigitalcode.adslibrary.util.AdsTools;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.wortise.ads.rewarded.RewardedAd;
import com.wortise.ads.rewarded.models.Reward;

public class MdcRewarded {
    private static final String TAG = "MDCRewarded";
    private final Activity activity;
    private com.google.android.gms.ads.rewarded.RewardedAd adMobRewardedAd;
    private com.google.android.gms.ads.rewarded.RewardedAd adManagerRewardedAd;
    private com.facebook.ads.RewardedVideoAd fanRewardedVideoAd;
    private RewardedVideo appNextrewardedVideo;
    private StartAppAd startAppAd;
    private MaxRewardedAd applovinMaxRewardedAd;
    public AppLovinInterstitialAdDialog appLovinInterstitialAdDialog;
    public AppLovinAd appLovinAd;
    private com.wortise.ads.rewarded.RewardedAd wortiseRewardedAd;
    private String adStatus = "";
    private String adsType = "";
    private String backupAdsType = "";
    private String mRewardId = "";


    private boolean legacyGDPR = false;
    public MdcRewarded(Activity activity) {
        this.activity = activity;
    }

    public MdcRewarded build(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
        loadRewardedAd(onComplete, onDismiss);
        return this;
    }

    public MdcRewarded show(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
        showRewardedAd(onComplete, onDismiss, onError);
        return this;
    }

    public MdcRewarded setAdStatus(String adStatus) {
        this.adStatus = adStatus;
        return this;
    }

    public MdcRewarded setAdType(String adsType) {
        this.adsType = adsType;
        return this;
    }

    public MdcRewarded setBackupAdType(String backupAdsType) {
        this.backupAdsType = backupAdsType;
        return this;
    }
    public MdcRewarded setRewardId(String mRewardId) {
        this.mRewardId = mRewardId;
        return this;
    }
    public MdcRewarded setLegacyGDPR(boolean legacyGDPR) {
        this.legacyGDPR = legacyGDPR;
        return this;
    }

    public void loadRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    com.google.android.gms.ads.rewarded.RewardedAd.load(activity, mRewardId, AdsTools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                            adMobRewardedAd = ad;
                            adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adMobRewardedAd = null;
                                    loadRewardedAd(onComplete, onDismiss);
                                    onDismiss.onRewardedAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adMobRewardedAd = null;
                                }
                            });
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adMobRewardedAd = null;
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    com.google.android.gms.ads.rewarded.RewardedAd.load(activity, mRewardId, AdsTools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                            adManagerRewardedAd = ad;
                            adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adManagerRewardedAd = null;
                                    loadRewardedAd(onComplete, onDismiss);
                                    onDismiss.onRewardedAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adManagerRewardedAd = null;
                                }
                            });
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adManagerRewardedAd = null;
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanRewardedVideoAd = new com.facebook.ads.RewardedVideoAd(activity, mRewardId);
                    fanRewardedVideoAd.loadAd(fanRewardedVideoAd.buildLoadAdConfig()
                            .withAdListener(new RewardedVideoAdListener() {
                                @Override
                                public void onRewardedVideoCompleted() {
                                    onComplete.onRewardedAdComplete();
                                    Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                                }

                                @Override
                                public void onRewardedVideoClosed() {
                                    loadRewardedAd(onComplete, onDismiss);
                                    onDismiss.onRewardedAdDismissed();
                                    Log.d(TAG, "[" + adsType + "] " + "rewarded ad closed");
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    loadRewardedBackupAd(onComplete, onDismiss);
                                    Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + mRewardId + ", try to load backup ad: " + backupAdsType);
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                                }

                                @Override
                                public void onAdClicked(Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {

                                }
                            })
                            .build());
                    break;

                case STARTAPP:
                    startAppAd = new StartAppAd(activity);
                    startAppAd.setVideoListener(() -> {
                        onComplete.onRewardedAdComplete();
                        Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                    });
                    startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad, try to load backup ad: " + backupAdsType);

                        }
                    });
                    break;

                case UNITY:
                    UnityAds.load(mRewardId, new IUnityAdsLoadListener() {
                        @Override
                        public void onUnityAdsAdLoaded(String placementId) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                        }

                        @Override
                        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                            Log.e(TAG, "[" + adsType + "] " + "rewarded ad failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                            loadRewardedBackupAd(onComplete, onDismiss);
                        }
                    });
                    break;

                case APPNEXT:
                    appNextrewardedVideo = new RewardedVideo(activity,mRewardId);
                    appNextrewardedVideo.loadAd();
                    appNextrewardedVideo.setMode(RewardedVideo.VIDEO_MODE_NORMAL);
                    appNextrewardedVideo.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }
                    });
                    appNextrewardedVideo.setOnAdClosedCallback(new OnAdClosed() {
                        @Override
                        public void onAdClosed() {
                            loadRewardedAd(onComplete, onDismiss);
                            onDismiss.onRewardedAdDismissed();
                        }
                    });
                    appNextrewardedVideo.setOnVideoEndedCallback(new OnVideoEnded() {
                        @Override
                        public void videoEnded() {
                            onComplete.onRewardedAdComplete();
                        }
                    });
                    appNextrewardedVideo.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + s + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    appNextrewardedVideo.setRewardedServerSidePostback("TransactionId", "UserId", "TypeCurrency", "Amount", "CustomParameter");

                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    applovinMaxRewardedAd = MaxRewardedAd.getInstance(mRewardId, activity);
                    applovinMaxRewardedAd.loadAd();
                    applovinMaxRewardedAd.setListener(new MaxRewardedAdListener() {
                        @Override
                        public void onUserRewarded(@NonNull MaxAd maxAd, @NonNull MaxReward maxReward) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                        }

                        @Override
                        public void onRewardedVideoStarted(@NonNull MaxAd maxAd) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded video started");
                        }

                        @Override
                        public void onRewardedVideoCompleted(@NonNull MaxAd maxAd) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "rewarded video complete");
                        }

                        @Override
                        public void onAdLoaded(@NonNull MaxAd maxAd) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd maxAd) {
                            loadRewardedAd(onComplete, onDismiss);
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad hidden");
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case APPLOVIN_DISCOVERY:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle interstitialExtras = new Bundle();
                    interstitialExtras.putString("zone_id", mRewardId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                    AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinAd = ad;
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + errorCode + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                    appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                        @Override
                        public void adDisplayed(AppLovinAd appLovinAd) {

                        }

                        @Override
                        public void adHidden(AppLovinAd appLovinAd) {
                            loadRewardedAd(onComplete, onDismiss);
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "ad hidden");
                        }
                    });
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                        @Override
                        public void onAdAvailable(AdInfo adInfo) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad is ready");
                        }

                        @Override
                        public void onAdUnavailable() {

                        }

                        @Override
                        public void onAdOpened(AdInfo adInfo) {

                        }

                        @Override
                        public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + ironSourceError.getErrorMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdClicked(Placement placement, AdInfo adInfo) {

                        }

                        @Override
                        public void onAdRewarded(Placement placement, AdInfo adInfo) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                        }

                        @Override
                        public void onAdClosed(AdInfo adInfo) {
                            loadRewardedAd(onComplete, onDismiss);
                        }
                    });
                    break;

                case WORTISE:
                    wortiseRewardedAd = new com.wortise.ads.rewarded.RewardedAd(activity, mRewardId);
                    wortiseRewardedAd.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                        @Override
                        public void onRewardedClicked(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                        }

                        @Override
                        public void onRewardedCompleted(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull Reward reward) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                        }

                        @Override
                        public void onRewardedDismissed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                            loadRewardedAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad dismissed");
                        }


                        @Override
                        public void onRewardedFailedToShow(@NonNull RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onRewardedImpression(@NonNull RewardedAd rewardedAd) {

                        }

                        @Override
                        public void onRewardedFailedToLoad(@NonNull RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {
                            loadRewardedBackupAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + adError + ", try to load backup ad: " + backupAdsType);

                        }

                        @Override
                        public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onRewardedShown(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                        }
                    });
                    wortiseRewardedAd.loadAd();
                    break;
            }
        }
    }

    public void loadRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    com.google.android.gms.ads.rewarded.RewardedAd.load(activity, mRewardId, AdsTools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                            adMobRewardedAd = ad;
                            adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adMobRewardedAd = null;
                                    loadRewardedAd(onComplete, onDismiss);
                                    onDismiss.onRewardedAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adMobRewardedAd = null;
                                }
                            });
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adMobRewardedAd = null;
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    com.google.android.gms.ads.rewarded.RewardedAd.load(activity, mRewardId, AdsTools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.rewarded.RewardedAd ad) {
                            adManagerRewardedAd = ad;
                            adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adManagerRewardedAd = null;
                                    loadRewardedAd(onComplete, onDismiss);
                                    onDismiss.onRewardedAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adManagerRewardedAd = null;
                                }
                            });
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adManagerRewardedAd = null;
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanRewardedVideoAd = new com.facebook.ads.RewardedVideoAd(activity, mRewardId);
                    fanRewardedVideoAd.loadAd(fanRewardedVideoAd.buildLoadAdConfig()
                            .withAdListener(new RewardedVideoAdListener() {
                                @Override
                                public void onRewardedVideoCompleted() {
                                    onComplete.onRewardedAdComplete();
                                    Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                                }

                                @Override
                                public void onRewardedVideoClosed() {
                                    loadRewardedAd(onComplete, onDismiss);
                                    onDismiss.onRewardedAdDismissed();
                                    Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad closed");
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + mRewardId + ", try to load backup ad: " + backupAdsType);
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                                }

                                @Override
                                public void onAdClicked(Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {

                                }
                            })
                            .build());
                    break;

                case STARTAPP:
                    startAppAd = new StartAppAd(activity);
                    startAppAd.setVideoListener(() -> {
                        onComplete.onRewardedAdComplete();
                        Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                    });
                    startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO, new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull com.startapp.sdk.adsbase.Ad ad) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad, try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case UNITY:
                    UnityAds.load(mRewardId, new IUnityAdsLoadListener() {
                        @Override
                        public void onUnityAdsAdLoaded(String placementId) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                        }

                        @Override
                        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                            Log.e(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                        }
                    });
                    break;
                case APPNEXT:
                    appNextrewardedVideo = new RewardedVideo(activity,mRewardId);
                    appNextrewardedVideo.loadAd();
                    appNextrewardedVideo.setMode(RewardedVideo.VIDEO_MODE_NORMAL);
                    appNextrewardedVideo.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }
                    });
                    appNextrewardedVideo.setOnAdClosedCallback(new OnAdClosed() {
                        @Override
                        public void onAdClosed() {
                            loadRewardedAd(onComplete, onDismiss);
                            onDismiss.onRewardedAdDismissed();
                        }
                    });
                    appNextrewardedVideo.setOnVideoEndedCallback(new OnVideoEnded() {
                        @Override
                        public void videoEnded() {
                            onComplete.onRewardedAdComplete();
                        }
                    });
                    appNextrewardedVideo.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + s + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    appNextrewardedVideo.setRewardedServerSidePostback("TransactionId", "UserId", "TypeCurrency", "Amount", "CustomParameter");

                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    applovinMaxRewardedAd = MaxRewardedAd.getInstance(mRewardId, activity);
                    applovinMaxRewardedAd.setListener(new MaxRewardedAdListener() {
                        @Override
                        public void onUserRewarded(@NonNull MaxAd maxAd, @NonNull MaxReward maxReward) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "user earn rewards");
                        }

                        @Override
                        public void onRewardedVideoStarted(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onRewardedVideoCompleted(@NonNull MaxAd maxAd) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded video complete");
                        }

                        @Override
                        public void onAdLoaded(@NonNull MaxAd maxAd) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd maxAd) {
                            loadRewardedAd(onComplete, onDismiss);
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] [backup]" + "rewarded ad hidden");
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String s, @NonNull MaxError maxError) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd maxAd, @NonNull MaxError maxError) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    applovinMaxRewardedAd.loadAd();
                    break;

                case APPLOVIN_DISCOVERY:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle interstitialExtras = new Bundle();
                    interstitialExtras.putString("zone_id", mRewardId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                    AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinAd = ad;
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + errorCode + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                    appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                        @Override
                        public void adDisplayed(AppLovinAd appLovinAd) {

                        }

                        @Override
                        public void adHidden(AppLovinAd appLovinAd) {
                            loadRewardedAd(onComplete, onDismiss);
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "ad hidden");
                        }
                    });
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                        @Override
                        public void onAdAvailable(AdInfo adInfo) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad is ready");
                        }

                        @Override
                        public void onAdUnavailable() {

                        }

                        @Override
                        public void onAdOpened(AdInfo adInfo) {

                        }

                        @Override
                        public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + ironSourceError.getErrorMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdClicked(Placement placement, AdInfo adInfo) {

                        }

                        @Override
                        public void onAdRewarded(Placement placement, AdInfo adInfo) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                        }

                        @Override
                        public void onAdClosed(AdInfo adInfo) {
                            loadRewardedAd(onComplete, onDismiss);
                        }
                    });
                    break;

                case WORTISE:
                    wortiseRewardedAd = new com.wortise.ads.rewarded.RewardedAd(activity, mRewardId);
                    wortiseRewardedAd.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                        @Override
                        public void onRewardedImpression(@NonNull RewardedAd rewardedAd) {

                        }

                        @Override
                        public void onRewardedFailedToShow(@NonNull RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onRewardedFailedToLoad(@NonNull RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + adError + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onRewardedClicked(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                        }

                        @Override
                        public void onRewardedCompleted(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull Reward reward) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                        }

                        @Override
                        public void onRewardedDismissed(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                            loadRewardedAd(onComplete, onDismiss);
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad dismissed");
                        }


                        @Override
                        public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onRewardedShown(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                        }
                    });
                    wortiseRewardedAd.loadAd();
                    break;
            }
        }
    }

    public void showRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    if (adMobRewardedAd != null) {
                        adMobRewardedAd.show(activity, rewardItem -> {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "The user earned the reward.");
                        });
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (adManagerRewardedAd != null) {
                        adManagerRewardedAd.show(activity, rewardItem -> {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "The user earned the reward.");
                        });
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                        fanRewardedVideoAd.show();
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case STARTAPP:
                    if (startAppAd != null) {
                        startAppAd.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case UNITY:
                    UnityAds.show(activity, mRewardId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                            showRewardedBackupAd(onComplete, onDismiss, onError);
                        }

                        @Override
                        public void onUnityAdsShowStart(String placementId) {

                        }

                        @Override
                        public void onUnityAdsShowClick(String placementId) {

                        }

                        @Override
                        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            onComplete.onRewardedAdComplete();
                            loadRewardedAd(onComplete, onDismiss);
                        }
                    });
                    break;
                case APPNEXT:
                    if (appNextrewardedVideo.isAdLoaded()) {
                        appNextrewardedVideo.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (applovinMaxRewardedAd != null && applovinMaxRewardedAd.isReady()) {
                        applovinMaxRewardedAd.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (appLovinInterstitialAdDialog != null) {
                        appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    if (IronSource.isRewardedVideoAvailable()) {
                        IronSource.showRewardedVideo(mRewardId);
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                case WORTISE:
                    if (wortiseRewardedAd != null && wortiseRewardedAd.isAvailable()) {
                        wortiseRewardedAd.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onDismiss, onError);
                    }
                    break;

                default:
                    onError.onRewardedAdError();
                    break;
            }
        }

    }

    public void showRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdDismissedListener onDismiss, OnRewardedAdErrorListener onError) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    if (adMobRewardedAd != null) {
                        adMobRewardedAd.show(activity, rewardItem -> {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "The user earned the reward.");
                        });
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (adManagerRewardedAd != null) {
                        adManagerRewardedAd.show(activity, rewardItem -> {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "The user earned the reward.");
                        });
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                        fanRewardedVideoAd.show();
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case STARTAPP:
                    if (startAppAd != null) {
                        startAppAd.showAd();
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case UNITY:
                    UnityAds.show(activity, mRewardId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                            onError.onRewardedAdError();
                        }

                        @Override
                        public void onUnityAdsShowStart(String placementId) {

                        }

                        @Override
                        public void onUnityAdsShowClick(String placementId) {

                        }

                        @Override
                        public void onUnityAdsShowComplete(String placementId, UnityAds.UnityAdsShowCompletionState state) {
                            onComplete.onRewardedAdComplete();
                            loadRewardedAd(onComplete, onDismiss);
                        }
                    });
                    break;
                case APPNEXT:
                    if (appNextrewardedVideo.isAdLoaded()) {
                        appNextrewardedVideo.showAd();
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (applovinMaxRewardedAd != null && applovinMaxRewardedAd.isReady()) {
                        applovinMaxRewardedAd.showAd();
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (appLovinInterstitialAdDialog != null) {
                        appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    if (IronSource.isRewardedVideoAvailable()) {
                        IronSource.showRewardedVideo(mRewardId);
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case WORTISE:
                    if (wortiseRewardedAd != null && wortiseRewardedAd.isAvailable()) {
                        wortiseRewardedAd.showAd();
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                default:
                    onError.onRewardedAdError();
                    break;
            }
        }

    }

    public void destroyRewardedAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case FAN:
                case FACEBOOK:
                    if (fanRewardedVideoAd != null) {
                        fanRewardedVideoAd.destroy();
                        fanRewardedVideoAd = null;
                    }
                    break;
            }

            switch (backupAdsType) {
                case FAN:
                case FACEBOOK:
                    if (fanRewardedVideoAd != null) {
                        fanRewardedVideoAd.destroy();
                        fanRewardedVideoAd = null;
                    }
                    break;
            }
        }
    }

}
