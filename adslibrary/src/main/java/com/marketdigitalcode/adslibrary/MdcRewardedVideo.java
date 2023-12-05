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
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinIncentivizedInterstitial;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.MaxReward;
import com.applovin.mediation.MaxRewardedAdListener;
import com.applovin.mediation.ads.MaxRewardedAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdRewardListener;
import com.applovin.sdk.AppLovinAdVideoPlaybackListener;
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
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.model.Placement;
import com.ironsource.mediationsdk.sdk.LevelPlayRewardedVideoListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdCompleteListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdErrorListener;
import com.marketdigitalcode.adslibrary.face.OnRewardedAdLoadedListener;
import com.marketdigitalcode.adslibrary.util.AdsTools;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.ads.UnityAdsShowOptions;
import com.wortise.ads.rewarded.models.Reward;

import java.util.Map;

public class MdcRewardedVideo {
    private static final String TAG = "MdcRewardedVideo";
    private final Activity activity;
    private RewardedAd adMobRewardedAd;
    private RewardedAd adManagerRewardedAd;
    private com.facebook.ads.RewardedVideoAd fanRewardedVideoAd;
    private RewardedVideo appNextrewardedVideo;
    private StartAppAd startAppAd;

    private MaxRewardedAd applovinMaxRewardedAd;
    private AppLovinIncentivizedInterstitial incentivizedInterstitial;
    private com.wortise.ads.rewarded.RewardedAd wortiseRewardedAd;
    private String adStatus = "";
    private String adsType = "";
    private String backupAdsType = "";
    private String mRewardId = "";
    private boolean legacyGDPR = false;
    private boolean showRewardedAdIfLoaded = false;

    public MdcRewardedVideo(Activity activity) {
        this.activity = activity;
    }

    public MdcRewardedVideo build(OnRewardedAdLoadedListener onLoaded, OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
        loadRewardedAd(onLoaded, onComplete, onError);
        return this;
    }

    public MdcRewardedVideo show(OnRewardedAdCompleteListener onRewardedAdCompleteListener, OnRewardedAdErrorListener onRewardedAdErrorListener) {
        showRewardedAd(onRewardedAdCompleteListener, onRewardedAdErrorListener);
        return this;
    }

    public MdcRewardedVideo setAdStatus(String adStatus) {
        this.adStatus = adStatus;
        return this;
    }

    public MdcRewardedVideo setAdsType(String adsType) {
        this.adsType = adsType;
        return this;
    }

    public MdcRewardedVideo setBackupAdsType(String backupAdsType) {
        this.backupAdsType = backupAdsType;
        return this;
    }

    public MdcRewardedVideo setRewardId(String mRewardId) {
        this.mRewardId = mRewardId;
        return this;
    }






    public MdcRewardedVideo setLegacyGDPR(boolean legacyGDPR) {
        this.legacyGDPR = legacyGDPR;
        return this;
    }

    public MdcRewardedVideo showRewardedAdIfLoaded(boolean showRewardedAdIfLoaded) {
        this.showRewardedAdIfLoaded = showRewardedAdIfLoaded;
        return this;
    }
    public void loadRewardedAd(OnRewardedAdLoadedListener onLoaded, OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    RewardedAd.load(activity, mRewardId, AdsTools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adMobRewardedAd = null;
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            adMobRewardedAd = ad;
                            adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adMobRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adMobRewardedAd = null;
                                }
                            });
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    RewardedAd.load(activity, mRewardId, AdsTools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adManagerRewardedAd = null;
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            adManagerRewardedAd = ad;
                            adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adManagerRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adManagerRewardedAd = null;
                                }
                            });
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
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
                                    Log.d(TAG, "[" + adsType + "] " + "rewarded ad closed");
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    loadRewardedBackupAd(onLoaded, onComplete, onError);
                                    Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + mRewardId + ", try to load backup ad: " + backupAdsType);
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    if (showRewardedAdIfLoaded) {
                                        showRewardedAd(onComplete, onError);
                                    } else {
                                        onLoaded.onRewardedAdLoaded();
                                    }
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
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable com.startapp.sdk.adsbase.Ad ad) {
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad, try to load backup ad: " + backupAdsType);

                        }
                    });
                    break;

                case UNITY:
                    UnityAds.load(mRewardId, new IUnityAdsLoadListener() {
                        @Override
                        public void onUnityAdsAdLoaded(String placementId) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                        }

                        @Override
                        public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                            Log.e(TAG, "[" + adsType + "] " + "rewarded ad failed to load ad for " + placementId + " with error: [" + error + "] " + message);
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
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
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                        }
                    });
                    appNextrewardedVideo.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad, try to load backup ad: " + backupAdsType);

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
                        public void onUserRewarded(MaxAd maxAd, MaxReward maxReward) {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                        }

                        @Override
                        public void onRewardedVideoStarted(MaxAd maxAd) {

                        }

                        @Override
                        public void onRewardedVideoCompleted(MaxAd maxAd) {

                        }

                        @Override
                        public void onAdLoaded(MaxAd maxAd) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdDisplayed(MaxAd maxAd) {

                        }

                        @Override
                        public void onAdHidden(MaxAd maxAd) {

                        }

                        @Override
                        public void onAdClicked(MaxAd maxAd) {

                        }

                        @Override
                        public void onAdLoadFailed(String s, MaxError maxError) {
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdDisplayFailed(MaxAd maxAd, MaxError maxError) {
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + maxError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    applovinMaxRewardedAd.loadAd();
                    break;

                case APPLOVIN_DISCOVERY:
                    incentivizedInterstitial = AppLovinIncentivizedInterstitial.create(mRewardId, AppLovinSdk.getInstance(activity));
                    incentivizedInterstitial.preload(new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd appLovinAd) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad loaded");
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + errorCode + ", try to load backup ad: " + backupAdsType);
                        }
                    });
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayRewardedVideoListener(new LevelPlayRewardedVideoListener() {
                        @Override
                        public void onAdAvailable(AdInfo adInfo) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
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
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
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

                        }
                    });
                    if (showRewardedAdIfLoaded) {
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo(mRewardId);
                        }
                    }
                    break;

                case WORTISE:
                    wortiseRewardedAd = new com.wortise.ads.rewarded.RewardedAd(activity, mRewardId);
                    wortiseRewardedAd.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                        @Override
                        public void onRewardedImpression(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                        }

                        @Override
                        public void onRewardedFailedToShow(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onRewardedFailedToLoad(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {
                            loadRewardedBackupAd(onLoaded, onComplete, onError);
                            Log.d(TAG, "[" + adsType + "] " + "failed to load rewarded ad: " + adError + ", try to load backup ad: " + backupAdsType);

                        }

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
                            Log.d(TAG, "[" + adsType + "] " + "rewarded ad dismissed");
                        }



                        @Override
                        public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
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
    public void loadRewardedBackupAd(OnRewardedAdLoadedListener onLoaded, OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    RewardedAd.load(activity, mRewardId, AdsTools.getAdRequest(activity, legacyGDPR), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adMobRewardedAd = null;
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            adMobRewardedAd = ad;
                            adMobRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adMobRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adMobRewardedAd = null;
                                }
                            });
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    RewardedAd.load(activity, mRewardId, AdsTools.getGoogleAdManagerRequest(), new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            Log.d(TAG, loadAdError.toString());
                            adManagerRewardedAd = null;
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + loadAdError.getMessage() + ", try to load backup ad: " + backupAdsType);
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd ad) {
                            adManagerRewardedAd = ad;
                            adManagerRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    adManagerRewardedAd = null;
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                    adManagerRewardedAd = null;
                                }
                            });
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
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
                                    Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad closed");
                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + adError.getErrorMessage() + ", try to load backup ad: " + backupAdsType);
                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    if (showRewardedAdIfLoaded) {
                                        showRewardedBackupAd(onComplete, onError);
                                    } else {
                                        onLoaded.onRewardedAdLoaded();
                                    }
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
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
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
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
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
                    appNextrewardedVideo.setOnVideoEndedCallback(new OnVideoEnded() {
                        @Override
                        public void videoEnded() {
                            onComplete.onRewardedAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                        }
                    });
                    appNextrewardedVideo.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");

                        }
                    });
                    appNextrewardedVideo.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad, try to load backup ad: " + backupAdsType);

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
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad complete");
                        }

                        @Override
                        public void onRewardedVideoStarted(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onRewardedVideoCompleted(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onAdLoaded(@NonNull MaxAd maxAd) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd maxAd) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd maxAd) {

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
                    incentivizedInterstitial = AppLovinIncentivizedInterstitial.create(mRewardId, AppLovinSdk.getInstance(activity));
                    incentivizedInterstitial.preload(new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd appLovinAd) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedBackupAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad loaded");
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "failed to load rewarded ad: " + errorCode + ", try to load backup ad: " + backupAdsType);
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

                        }
                    });
                    if (showRewardedAdIfLoaded) {
                        if (IronSource.isRewardedVideoAvailable()) {
                            IronSource.showRewardedVideo(mRewardId);
                        }
                    }
                    break;

                case WORTISE:
                    wortiseRewardedAd = new com.wortise.ads.rewarded.RewardedAd(activity, mRewardId);
                    wortiseRewardedAd.setListener(new com.wortise.ads.rewarded.RewardedAd.Listener() {
                        @Override
                        public void onRewardedImpression(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {

                        }

                        @Override
                        public void onRewardedFailedToShow(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onRewardedFailedToLoad(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd, @NonNull com.wortise.ads.AdError adError) {
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
                            Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad dismissed");
                        }


                        @Override
                        public void onRewardedLoaded(@NonNull com.wortise.ads.rewarded.RewardedAd rewardedAd) {
                            if (showRewardedAdIfLoaded) {
                                showRewardedAd(onComplete, onError);
                            } else {
                                onLoaded.onRewardedAdLoaded();
                            }
                            Log.d(TAG, "[" + backupAdsType + "] [backup]" + "rewarded ad loaded");
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

    public void showRewardedAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
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
                        showRewardedBackupAd(onComplete, onError);
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
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanRewardedVideoAd != null && fanRewardedVideoAd.isAdLoaded()) {
                        fanRewardedVideoAd.show();
                    } else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                case STARTAPP:
                    if (startAppAd != null) {
                        startAppAd.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                case UNITY:
                    UnityAds.show(activity, mRewardId, new UnityAdsShowOptions(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String placementId, UnityAds.UnityAdsShowError error, String message) {
                            showRewardedBackupAd(onComplete, onError);
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
                        }
                    });
                    break;
                case APPNEXT:
                    if (appNextrewardedVideo.isAdLoaded()){
                        appNextrewardedVideo.showAd();
                    }else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (applovinMaxRewardedAd != null && applovinMaxRewardedAd.isReady()) {
                        applovinMaxRewardedAd.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (incentivizedInterstitial != null) {
                        incentivizedInterstitial.show(activity, new AppLovinAdRewardListener() {
                            @Override
                            public void userRewardVerified(AppLovinAd ad, Map<String, String> response) {
                                onComplete.onRewardedAdComplete();
                                Log.d(TAG, "[" + adsType + "] " + "rewarded ad complete");
                            }

                            @Override
                            public void userOverQuota(AppLovinAd ad, Map<String, String> response) {

                            }

                            @Override
                            public void userRewardRejected(AppLovinAd ad, Map<String, String> response) {

                            }

                            @Override
                            public void validationRequestFailed(AppLovinAd ad, int errorCode) {

                            }


                        }, null, new AppLovinAdDisplayListener() {
                            @Override
                            public void adDisplayed(AppLovinAd appLovinAd) {
                                Log.d(TAG, "[" + adsType + "] " + "rewarded ad displayed");
                            }

                            @Override
                            public void adHidden(AppLovinAd appLovinAd) {
                                Log.d(TAG, "[" + adsType + "] " + "rewarded ad dismissed");
                            }
                        });
                    } else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    if (IronSource.isRewardedVideoAvailable()) {
                        IronSource.showRewardedVideo(mRewardId);
                    } else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                case WORTISE:
                    if (wortiseRewardedAd != null && wortiseRewardedAd.isAvailable()) {
                        wortiseRewardedAd.showAd();
                    } else {
                        showRewardedBackupAd(onComplete, onError);
                    }
                    break;

                default:
                    onError.onRewardedAdError();
                    break;
            }
        }

    }

    public void showRewardedBackupAd(OnRewardedAdCompleteListener onComplete, OnRewardedAdErrorListener onError) {
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
                    if (incentivizedInterstitial != null) {
                        incentivizedInterstitial.show(activity, new AppLovinAdRewardListener() {
                            @Override
                            public void userRewardVerified(AppLovinAd ad, Map<String, String> response) {
                                onComplete.onRewardedAdComplete();
                            }

                            @Override
                            public void userOverQuota(AppLovinAd ad, Map<String, String> response) {

                            }

                            @Override
                            public void userRewardRejected(AppLovinAd ad, Map<String, String> response) {

                            }

                            @Override
                            public void validationRequestFailed(AppLovinAd ad, int errorCode) {

                            }


                        }, new AppLovinAdVideoPlaybackListener() {
                            @Override
                            public void videoPlaybackBegan(AppLovinAd appLovinAd) {

                            }

                            @Override
                            public void videoPlaybackEnded(AppLovinAd appLovinAd, double v, boolean b) {

                            }
                        }, new AppLovinAdDisplayListener() {
                            @Override
                            public void adDisplayed(AppLovinAd appLovinAd) {

                            }

                            @Override
                            public void adHidden(AppLovinAd appLovinAd) {
                                Log.d(TAG, "[" + backupAdsType + "] [backup] " + "rewarded ad dismissed");
                            }
                        });
                    } else {
                        onError.onRewardedAdError();
                    }
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    if (IronSource.isRewardedVideoAvailable()) {
                        IronSource.showRewardedVideo(mRewardId);
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
