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
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.applovin.adview.AppLovinInterstitialAd;
import com.applovin.adview.AppLovinInterstitialAdDialog;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdDisplayListener;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdk;
import com.appnext.ads.interstitial.Interstitial;
import com.appnext.core.AppnextActivity;
import com.appnext.core.AppnextAdCreativeType;
import com.appnext.core.callbacks.OnAdClicked;
import com.appnext.core.callbacks.OnAdClosed;
import com.appnext.core.callbacks.OnAdError;
import com.appnext.core.callbacks.OnAdLoaded;
import com.appnext.core.callbacks.OnAdOpened;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAd;
import com.google.android.gms.ads.admanager.AdManagerInterstitialAdLoadCallback;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayInterstitialListener;
import com.ironsource.sdk.controller.A;
import com.marketdigitalcode.adslibrary.face.OnInterstitialAdDismissedListener;
import com.marketdigitalcode.adslibrary.face.OnInterstitialAdShowedListener;
import com.marketdigitalcode.adslibrary.helper.AppLovinCustomEventInterstitial;
import com.marketdigitalcode.adslibrary.util.AdsTools;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.unity3d.mediation.IInterstitialAdLoadListener;
import com.unity3d.mediation.IInterstitialAdShowListener;
import com.unity3d.mediation.errors.LoadError;
import com.unity3d.mediation.errors.ShowError;
import com.wortise.ads.interstitial.InterstitialAd;

import java.util.concurrent.TimeUnit;


public class MdcInterstitial {
    private final Activity activity;
    private com.google.android.gms.ads.interstitial.InterstitialAd adMobInterstitialAd;
    private com.appnext.ads.interstitial.Interstitial appNextInterstitialAd;
    private AdManagerInterstitialAd adManagerInterstitialAd;
    private com.facebook.ads.InterstitialAd fanInterstitialAd;
    private StartAppAd startAppAd;
    private com.unity3d.mediation.InterstitialAd unityInterstitialAd;
    private MaxInterstitialAd maxInterstitialAd;
    public AppLovinInterstitialAdDialog appLovinInterstitialAdDialog;
    public AppLovinAd appLovinAd;
    public com.wortise.ads.interstitial.InterstitialAd wortiseInterstitialAd;
    private int retryAttempt;
    private int counter = 1;
    private String adStatus = "";
    private String adsType = "";
    private String backupAdsType = "";
    private String mInterId = "";
    private int interval = 3;
    private boolean legacyGDPR = false;

    public MdcInterstitial(Activity activity) {
        this.activity = activity;
    }

    public MdcInterstitial build() {
        loadInterstitialAd();
        return this;
    }

    public MdcInterstitial build(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
        loadInterstitialAd(onInterstitialAdDismissedListener);
        return this;
    }

    public void show() {
        showInterstitialAd();
    }

    public void show(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
        showInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
    }

    public MdcInterstitial setAdStatus(String adStatus) {
        this.adStatus = adStatus;
        return this;
    }

    public MdcInterstitial setAdsType(String adsType) {
        this.adsType = adsType;
        return this;
    }

    public MdcInterstitial setBackupAdsType(String backupAdsType) {
        this.backupAdsType = backupAdsType;
        return this;
    }

    public MdcInterstitial setInterId(String mInterId) {
        this.mInterId = mInterId;
        return this;
    }

    public MdcInterstitial setInterval(int interval) {
        this.interval = interval;
        return this;
    }

    public MdcInterstitial setLegacyGDPR(boolean legacyGDPR) {
        this.legacyGDPR = legacyGDPR;
        return this;
    }

    public void loadInterstitialAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, mInterId, AdsTools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                            adMobInterstitialAd = interstitialAd;
                            adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    loadInterstitialAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    AdsTools.setLogString("The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    adMobInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                            AdsTools.setLogString("onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            AdsTools.setLogString(loadAdError.getMessage());
                            adMobInterstitialAd = null;
                            loadBackupInterstitialAd();
                            AdsTools.setLogString("Failed load AdMob Interstitial Ad");
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    AdManagerInterstitialAd.load(activity, mInterId, AdsTools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            super.onAdLoaded(adManagerInterstitialAd);
                            adManagerInterstitialAd = interstitialAd;
                            adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    loadInterstitialAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    adManagerInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            adManagerInterstitialAd = null;
                            loadBackupInterstitialAd();
                            AdsTools.setLogString("Failed load Ad Manager Interstitial Ad");
                        }
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, mInterId);
                    com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                            fanInterstitialAd.loadAd();
                        }

                        @Override
                        public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                            loadBackupInterstitialAd();
                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {
                            AdsTools.setLogString("FAN Interstitial is loaded");
                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }
                    };

                    com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                    fanInterstitialAd.loadAd(loadAdConfig);
                    break;

                case STARTAPP:
                    startAppAd = new StartAppAd(activity);
                    startAppAd.loadAd(new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull Ad ad) {
                            AdsTools.setLogString("Startapp Interstitial Ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable Ad ad) {
                            AdsTools.setLogString("Failed to load Startapp Interstitial Ad");
                            loadBackupInterstitialAd();
                        }
                    });
                    break;

                case UNITY:
                    unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, mInterId);
                    final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                        @Override
                        public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("unity interstitial ad loaded");
                        }

                        @Override
                        public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                            AdsTools.setLogString("Unity Ads failed to load ad : " + mInterId + " : error : " + s);
                            loadBackupInterstitialAd();
                        }

                    };
                    unityInterstitialAd.load(unityAdLoadListener);
                    break;
                case APPNEXT:
                    appNextInterstitialAd = new Interstitial(activity, mInterId);
                    appNextInterstitialAd.loadAd();
                    appNextInterstitialAd.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            AdsTools.setLogString("Appnext Interstitial Ad loaded");
                        }
                    });
                    appNextInterstitialAd.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            AdsTools.setLogString("Appnext Interstitial Failed "+s);
                            loadBackupInterstitialAd();
                        }
                    });
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    maxInterstitialAd = new MaxInterstitialAd(mInterId, activity);
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            retryAttempt = 0;
                            AdsTools.setLogString("AppLovin Interstitial Ad loaded...");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxInterstitialAd.loadAd();
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            retryAttempt++;
                            long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                            new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                            loadBackupInterstitialAd();
                            AdsTools.setLogString("failed to load AppLovin Interstitial");
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxInterstitialAd.loadAd();
                        }
                    });

                    // Load the first ad
                    maxInterstitialAd.loadAd();
                    break;

                case APPLOVIN_DISCOVERY:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle interstitialExtras = new Bundle();
                    interstitialExtras.putString("zone_id", mInterId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                    AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinAd = ad;
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            loadBackupInterstitialAd();
                        }
                    });
                    appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                        @Override
                        public void onAdReady(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdReady");
                        }

                        @Override
                        public void onAdLoadFailed(IronSourceError ironSourceError) {
                            AdsTools.setLogString("onInterstitialAdLoadFailed" + " " + ironSourceError);
                            loadBackupInterstitialAd();
                        }

                        @Override
                        public void onAdOpened(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdOpened");
                        }

                        @Override
                        public void onAdShowSucceeded(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowSucceeded");
                        }

                        @Override
                        public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowFailed" + " " + ironSourceError);
                            loadBackupInterstitialAd();
                        }

                        @Override
                        public void onAdClicked(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClicked");
                        }

                        @Override
                        public void onAdClosed(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClosed");
                            loadInterstitialAd();
                        }
                    });
                    IronSource.loadInterstitial();
                    break;

                case WORTISE:
                    wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, mInterId);
                    wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                        @Override
                        public void onInterstitialImpression(@NonNull InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialFailedToShow(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onInterstitialFailedToLoad(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                            loadBackupInterstitialAd();
                            AdsTools.setLogString("[Wortise] Failed to load Interstitial Ad");
                        }

                        @Override
                        public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            loadInterstitialAd();
                            AdsTools.setLogString("[Wortise] Interstitial Ad dismissed");
                        }


                        @Override
                        public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("[Wortise] Interstitial Ad loaded");
                        }

                        @Override
                        public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }
                    });
                    wortiseInterstitialAd.loadAd();
                    break;
            }
        }
    }
    public void loadInterstitialAd(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, mInterId, AdsTools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                            adMobInterstitialAd = interstitialAd;
                            adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    loadInterstitialAd(onInterstitialAdDismissedListener);
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    AdsTools.setLogString("The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    adMobInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                            AdsTools.setLogString("onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            AdsTools.setLogString(loadAdError.getMessage());
                            adMobInterstitialAd = null;
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            AdsTools.setLogString("Failed load AdMob Interstitial Ad");
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    AdManagerInterstitialAd.load(activity, mInterId, AdsTools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            super.onAdLoaded(adManagerInterstitialAd);
                            adManagerInterstitialAd = interstitialAd;
                            adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    loadInterstitialAd(onInterstitialAdDismissedListener);
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    adManagerInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            adManagerInterstitialAd = null;
                            loadBackupInterstitialAd();
                            AdsTools.setLogString("Failed load Ad Manager Interstitial Ad");
                        }
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, mInterId);
                    com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                            fanInterstitialAd.loadAd();
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                        }

                        @Override
                        public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {
                            AdsTools.setLogString("FAN Interstitial is loaded");
                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }
                    };

                    com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                    fanInterstitialAd.loadAd(loadAdConfig);
                    break;

                case STARTAPP:
                    startAppAd = new StartAppAd(activity);
                    startAppAd.loadAd(new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull Ad ad) {
                            AdsTools.setLogString("Startapp Interstitial Ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable Ad ad) {
                            AdsTools.setLogString("Failed to load Startapp Interstitial Ad");
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                        }
                    });
                    break;

                case UNITY:
                    unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, mInterId);
                    final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                        @Override
                        public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("unity interstitial ad loaded");
                        }


                        @Override
                        public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                            AdsTools.setLogString("Unity Ads failed to load ad : " + mInterId + " : error : " + s);
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                        }

                    };
                    unityInterstitialAd.load(unityAdLoadListener);
                    break;
                case APPNEXT:

                    appNextInterstitialAd = new Interstitial(activity, mInterId);
                    appNextInterstitialAd.loadAd();
                    appNextInterstitialAd.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            AdsTools.setLogString("Appenxt Interstitial Ad loaded");
                        }
                    });
                    appNextInterstitialAd.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            AdsTools.setLogString("Failed to load Appenxt Interstitial Ad "+s);
                        }
                    });
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    maxInterstitialAd = new MaxInterstitialAd(mInterId, activity);
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            retryAttempt = 0;
                            AdsTools.setLogString("AppLovin Interstitial Ad loaded...");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxInterstitialAd.loadAd();
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();

                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            retryAttempt++;
                            long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                            new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            AdsTools.setLogString("failed to load AppLovin Interstitial");
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxInterstitialAd.loadAd();
                        }
                    });

                    // Load the first ad
                    maxInterstitialAd.loadAd();
                    break;

                case APPLOVIN_DISCOVERY:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle interstitialExtras = new Bundle();
                    interstitialExtras.putString("zone_id", mInterId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                    AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinAd = ad;
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                        }
                    });
                    appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                        @Override
                        public void onAdReady(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdReady");
                        }

                        @Override
                        public void onAdLoadFailed(IronSourceError ironSourceError) {
                            AdsTools.setLogString("onInterstitialAdLoadFailed" + " " + ironSourceError);
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                        }

                        @Override
                        public void onAdOpened(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdOpened");
                        }

                        @Override
                        public void onAdShowSucceeded(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowSucceeded");
                        }

                        @Override
                        public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowFailed" + " " + ironSourceError);
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                        }

                        @Override
                        public void onAdClicked(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClicked");
                        }

                        @Override
                        public void onAdClosed(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClosed");
                            loadInterstitialAd(onInterstitialAdDismissedListener);
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                        }
                    });
                    IronSource.loadInterstitial();
                    break;

                case WORTISE:
                    wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, mInterId);
                    wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                        @Override
                        public void onInterstitialImpression(@NonNull InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialFailedToShow(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onInterstitialFailedToLoad(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                            loadBackupInterstitialAd(onInterstitialAdDismissedListener);
                            AdsTools.setLogString("[Wortise] Failed to load Interstitial Ad");
                        }

                        @Override
                        public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            loadInterstitialAd();
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            AdsTools.setLogString("[Wortise] Interstitial Ad dismissed");
                        }


                        @Override
                        public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("[Wortise] Interstitial Ad loaded");
                        }

                        @Override
                        public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }
                    });
                    wortiseInterstitialAd.loadAd();
                    break;
            }
        }
    }
    public void showInterstitialAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (counter == interval) {
                switch (adsType) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobInterstitialAd != null) {
                            adMobInterstitialAd.show(activity);
                            AdsTools.setLogString("admob interstitial not null");
                        } else {
                            showBackupInterstitialAd();
                            AdsTools.setLogString("admob interstitial null");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerInterstitialAd != null) {
                            adManagerInterstitialAd.show(activity);
                            AdsTools.setLogString("ad manager interstitial not null");
                        } else {
                            showBackupInterstitialAd();
                            AdsTools.setLogString("ad manager interstitial null");
                        }
                        break;

                    case FAN:
                    case FACEBOOK:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                            AdsTools.setLogString("fan interstitial not null");
                        } else {
                            showBackupInterstitialAd();
                            AdsTools.setLogString("fan interstitial null");
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd();
                            AdsTools.setLogString("startapp interstitial not null [counter] : " + counter);
                        } else {
                            showBackupInterstitialAd();
                            AdsTools.setLogString("startapp interstitial null");
                        }
                        break;

                    case UNITY:
                        final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                            @Override
                            public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                AdsTools.setLogString("unity ads show failure");
                                showBackupInterstitialAd();
                            }
                        };
                        unityInterstitialAd.show(showListener);
                        break;
                    case APPNEXT:

                        if (appNextInterstitialAd.isAdLoaded()) {
                            appNextInterstitialAd.showAd();
                            AdsTools.setLogString("Appnext interstitial not null [counter] : " + counter);
                        } else {
                            showBackupInterstitialAd();
                            AdsTools.setLogString("Appnext interstitial null");
                        }
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            AdsTools.setLogString("ready : " + counter);
                            maxInterstitialAd.showAd();
                            AdsTools.setLogString("show ad");
                        } else {
                            showBackupInterstitialAd();
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(mInterId);
                        } else {
                            showBackupInterstitialAd();
                        }
                        break;

                    case WORTISE:
                        if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                            wortiseInterstitialAd.showAd();
                        } else {
                            showBackupInterstitialAd();
                        }
                        break;
                }
                counter = 1;
            } else {
                counter++;
            }
            AdsTools.setLogString("Current counter : " + counter);
        }
    }
    public void showInterstitialAd(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (counter == interval) {
                switch (adsType) {
                    case ADMOB:
                    case FAN_BIDDING_ADMOB:
                        if (adMobInterstitialAd != null) {
                            adMobInterstitialAd.show(activity);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                            AdsTools.setLogString("admob interstitial not null");
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            AdsTools.setLogString("admob interstitial null");
                        }
                        break;

                    case GOOGLE_AD_MANAGER:
                    case FAN_BIDDING_AD_MANAGER:
                        if (adManagerInterstitialAd != null) {
                            adManagerInterstitialAd.show(activity);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                            AdsTools.setLogString("ad manager interstitial not null");
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            AdsTools.setLogString("ad manager interstitial null");
                        }
                        break;

                    case FAN:
                    case FACEBOOK:
                        if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                            fanInterstitialAd.show();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                            AdsTools.setLogString("fan interstitial not null");
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            AdsTools.setLogString("fan interstitial null");
                        }
                        break;

                    case STARTAPP:
                        if (startAppAd != null) {
                            startAppAd.showAd(new AdDisplayListener() {
                                @Override
                                public void adHidden(Ad ad) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void adDisplayed(Ad ad) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void adClicked(Ad ad) {

                                }

                                @Override
                                public void adNotDisplayed(Ad ad) {

                                }
                            });
                            AdsTools.setLogString("startapp interstitial not null [counter] : " + counter);
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            AdsTools.setLogString("startapp interstitial null");
                        }
                        break;

                    case UNITY:
                        final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                            @Override
                            public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            }

                            @Override
                            public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                            }

                            @Override
                            public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                                AdsTools.setLogString("unity ads show failure");
                                showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            }
                        };
                        unityInterstitialAd.show(showListener);
                        break;
                    case APPNEXT:
                        if (appNextInterstitialAd.isAdLoaded()) {
                            appNextInterstitialAd.showAd();
                            appNextInterstitialAd.setOnAdClosedCallback(new OnAdClosed() {
                                @Override
                                public void onAdClosed() {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }
                            });
                            appNextInterstitialAd.setOnAdOpenedCallback(new OnAdOpened() {
                                @Override
                                public void adOpened() {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }
                            });
                            AdsTools.setLogString("appnext interstitial not null [counter] : " + counter);
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                            AdsTools.setLogString("appnext interstitial null");
                        }
                        break;

                    case APPLOVIN:
                    case APPLOVIN_MAX:
                    case FAN_BIDDING_APPLOVIN_MAX:
                        if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                            AdsTools.setLogString("ready : " + counter);
                            maxInterstitialAd.showAd();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                            AdsTools.setLogString("show ad");
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                        }
                        break;

                    case APPLOVIN_DISCOVERY:
                        if (appLovinInterstitialAdDialog != null) {
                            appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                                @Override
                                public void adDisplayed(AppLovinAd appLovinAd) {
                                    onInterstitialAdShowedListener.onInterstitialAdShowed();
                                }

                                @Override
                                public void adHidden(AppLovinAd appLovinAd) {
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }
                            });
                            appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                        }
                        break;

                    case IRONSOURCE:
                    case FAN_BIDDING_IRONSOURCE:
                        if (IronSource.isInterstitialReady()) {
                            IronSource.showInterstitial(mInterId);
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                        }
                        break;

                    case WORTISE:
                        if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                            wortiseInterstitialAd.showAd();
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        } else {
                            showBackupInterstitialAd(onInterstitialAdShowedListener, onInterstitialAdDismissedListener);
                        }
                        break;
                }
                counter = 1;
            } else {
                counter++;
            }
            AdsTools.setLogString("Current counter : " + counter);
        } else {
            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
        }
    }


    public void loadBackupInterstitialAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, mInterId, AdsTools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                            adMobInterstitialAd = interstitialAd;
                            adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    loadInterstitialAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    AdsTools.setLogString("The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    adMobInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                            AdsTools.setLogString("onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            AdsTools.setLogString(loadAdError.getMessage());
                            adMobInterstitialAd = null;
                            AdsTools.setLogString("Failed load AdMob Interstitial Ad");
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    AdManagerInterstitialAd.load(activity, mInterId, AdsTools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            super.onAdLoaded(adManagerInterstitialAd);
                            adManagerInterstitialAd = interstitialAd;
                            adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    loadInterstitialAd();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    adManagerInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            adManagerInterstitialAd = null;
                            AdsTools.setLogString("Failed load Ad Manager Interstitial Ad");
                        }
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, mInterId);
                    com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                            fanInterstitialAd.loadAd();
                        }

                        @Override
                        public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {
                            AdsTools.setLogString("FAN Interstitial is loaded");
                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }
                    };

                    com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                    fanInterstitialAd.loadAd(loadAdConfig);
                    break;

                case STARTAPP:
                    startAppAd = new StartAppAd(activity);
                    startAppAd.loadAd(new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull Ad ad) {
                            AdsTools.setLogString("Startapp Interstitial Ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable Ad ad) {
                            AdsTools.setLogString("Failed to load Startapp Interstitial Ad");
                        }
                    });
                    AdsTools.setLogString("load StartApp as backup Ad");
                    break;

                case UNITY:
                    unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, mInterId);
                    final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                        @Override
                        public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("unity interstitial ad loaded");
                        }

                        @Override
                        public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                            AdsTools.setLogString("Unity Ads failed to load ad : " + mInterId + " : error : " + s);
                        }

                    };
                    unityInterstitialAd.load(unityAdLoadListener);
                    break;
                case APPNEXT:
                    appNextInterstitialAd = new Interstitial(activity, mInterId);
                    appNextInterstitialAd.loadAd();
                    appNextInterstitialAd.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            AdsTools.setLogString("Appnext Interstitial Ad loaded");
                        }
                    });
                    appNextInterstitialAd.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            AdsTools.setLogString("Appnext Interstitial Failed "+s);
                        }
                    });
                    AdsTools.setLogString("load Appnext as backup Ad");
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    maxInterstitialAd = new MaxInterstitialAd(mInterId, activity);
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            retryAttempt = 0;
                            AdsTools.setLogString("AppLovin Interstitial Ad loaded...");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxInterstitialAd.loadAd();
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            retryAttempt++;
                            long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                            new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                            AdsTools.setLogString("failed to load AppLovin Interstitial");
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxInterstitialAd.loadAd();
                        }
                    });

                    // Load the first ad
                    maxInterstitialAd.loadAd();
                    break;

                case APPLOVIN_DISCOVERY:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle interstitialExtras = new Bundle();
                    interstitialExtras.putString("zone_id", mInterId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                    AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinAd = ad;
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                        }
                    });
                    appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                    break;


                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                        @Override
                        public void onAdReady(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdReady");
                        }

                        @Override
                        public void onAdLoadFailed(IronSourceError ironSourceError) {
                            AdsTools.setLogString("onInterstitialAdLoadFailed" + " " + ironSourceError);
                        }

                        @Override
                        public void onAdOpened(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdOpened");
                        }

                        @Override
                        public void onAdShowSucceeded(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowSucceeded");
                        }

                        @Override
                        public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowFailed" + " " + ironSourceError);
                        }

                        @Override
                        public void onAdClicked(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClicked");
                        }

                        @Override
                        public void onAdClosed(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClosed");
                            loadInterstitialAd();
                        }
                    });
                    IronSource.loadInterstitial();
                    break;

                case WORTISE:
                    wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, mInterId);
                    wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                        @Override
                        public void onInterstitialImpression(@NonNull InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialFailedToShow(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onInterstitialFailedToLoad(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                            AdsTools.setLogString("[Wortise] [Backup] Failed to load Interstitial Ad");
                        }

                        @Override
                        public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            loadInterstitialAd();
                            AdsTools.setLogString("[Wortise] [Backup] Interstitial Ad dismissed");
                        }


                        @Override
                        public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("[Wortise] [Backup] Interstitial Ad loaded");
                        }

                        @Override
                        public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }
                    });
                    wortiseInterstitialAd.loadAd();
                    break;

                case NONE:
                    //do nothing
                    break;
            }
        }
    }
    public void showBackupInterstitialAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            AdsTools.setLogString("Show Backup Interstitial Ad [" + backupAdsType.toUpperCase() + "]");
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    if (adMobInterstitialAd != null) {
                        adMobInterstitialAd.show(activity);
                    }
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (adManagerInterstitialAd != null) {
                        adManagerInterstitialAd.show(activity);
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                        fanInterstitialAd.show();
                    }
                    break;

                case STARTAPP:
                    if (startAppAd != null) {
                        startAppAd.showAd();
                    }
                    break;

                case UNITY:
                    final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                        @Override
                        public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                            AdsTools.setLogString("unity ads show failure");
                        }
                    };
                    unityInterstitialAd.show(showListener);
                    break;
                case APPNEXT:
                    if (appNextInterstitialAd.isAdLoaded()) {
                        appNextInterstitialAd.showAd();
                    }
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                        maxInterstitialAd.showAd();
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (appLovinInterstitialAdDialog != null) {
                        appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                    }
                    break;

                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    if (IronSource.isInterstitialReady()) {
                        IronSource.showInterstitial(mInterId);
                    }
                    break;

                case WORTISE:
                    if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                        wortiseInterstitialAd.showAd();
                    }
                    break;

                case NONE:
                    //do nothing
                    break;
            }
        }
    }
    public void loadBackupInterstitialAd(OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    com.google.android.gms.ads.interstitial.InterstitialAd.load(activity, mInterId, AdsTools.getAdRequest(activity, legacyGDPR), new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                            adMobInterstitialAd = interstitialAd;
                            adMobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    loadInterstitialAd(onInterstitialAdDismissedListener);
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull com.google.android.gms.ads.AdError adError) {
                                    AdsTools.setLogString("The ad failed to show.");
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    adMobInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                            AdsTools.setLogString("onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            AdsTools.setLogString(loadAdError.getMessage());
                            adMobInterstitialAd = null;
                            AdsTools.setLogString("Failed load AdMob Interstitial Ad");
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    AdManagerInterstitialAd.load(activity, mInterId, AdsTools.getGoogleAdManagerRequest(), new AdManagerInterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull AdManagerInterstitialAd interstitialAd) {
                            super.onAdLoaded(adManagerInterstitialAd);
                            adManagerInterstitialAd = interstitialAd;
                            adManagerInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdClicked() {
                                    super.onAdClicked();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    super.onAdDismissedFullScreenContent();
                                    loadInterstitialAd(onInterstitialAdDismissedListener);
                                    onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                                    super.onAdFailedToShowFullScreenContent(adError);
                                }

                                @Override
                                public void onAdImpression() {
                                    super.onAdImpression();
                                }

                                @Override
                                public void onAdShowedFullScreenContent() {
                                    super.onAdShowedFullScreenContent();
                                    adManagerInterstitialAd = null;
                                    AdsTools.setLogString("The ad was shown.");
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            super.onAdFailedToLoad(loadAdError);
                            adManagerInterstitialAd = null;
                            AdsTools.setLogString("Failed load Ad Manager Interstitial Ad");
                        }
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanInterstitialAd = new com.facebook.ads.InterstitialAd(activity, mInterId);
                    com.facebook.ads.InterstitialAdListener adListener = new InterstitialAdListener() {
                        @Override
                        public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                            fanInterstitialAd.loadAd();
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                        }

                        @Override
                        public void onError(com.facebook.ads.Ad ad, com.facebook.ads.AdError adError) {

                        }

                        @Override
                        public void onAdLoaded(com.facebook.ads.Ad ad) {
                            AdsTools.setLogString("FAN Interstitial is loaded");
                        }

                        @Override
                        public void onAdClicked(com.facebook.ads.Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(com.facebook.ads.Ad ad) {

                        }
                    };

                    com.facebook.ads.InterstitialAd.InterstitialLoadAdConfig loadAdConfig = fanInterstitialAd.buildLoadAdConfig().withAdListener(adListener).build();
                    fanInterstitialAd.loadAd(loadAdConfig);
                    break;

                case STARTAPP:
                    startAppAd = new StartAppAd(activity);
                    startAppAd.loadAd(new AdEventListener() {
                        @Override
                        public void onReceiveAd(@NonNull Ad ad) {
                            AdsTools.setLogString("Startapp Interstitial Ad loaded");
                        }

                        @Override
                        public void onFailedToReceiveAd(@Nullable Ad ad) {
                            AdsTools.setLogString("Failed to load Startapp Interstitial Ad");
                        }
                    });
                    AdsTools.setLogString("load StartApp as backup Ad");
                    break;

                case UNITY:
                    unityInterstitialAd = new com.unity3d.mediation.InterstitialAd(activity, mInterId);
                    final IInterstitialAdLoadListener unityAdLoadListener = new IInterstitialAdLoadListener() {
                        @Override
                        public void onInterstitialLoaded(com.unity3d.mediation.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("unity interstitial ad loaded");
                        }

                        @Override
                        public void onInterstitialFailedLoad(com.unity3d.mediation.InterstitialAd interstitialAd, LoadError loadError, String s) {
                            AdsTools.setLogString("Unity Ads failed to load ad : " + mInterId + " : error : " + s);
                        }

                    };
                    unityInterstitialAd.load(unityAdLoadListener);
                    break;
                case APPNEXT:
                    appNextInterstitialAd = new Interstitial(activity, mInterId);
                    appNextInterstitialAd.loadAd();
                    appNextInterstitialAd.setOnAdLoadedCallback(new OnAdLoaded() {
                        @Override
                        public void adLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            AdsTools.setLogString("Appnext Interstitial Ad loaded");
                        }
                    });
                    appNextInterstitialAd.setOnAdErrorCallback(new OnAdError() {
                        @Override
                        public void adError(String s) {
                            AdsTools.setLogString("Appnext Interstitial Failed "+s);
                        }
                    });
                    AdsTools.setLogString("load Appnext as backup Ad");
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    maxInterstitialAd = new MaxInterstitialAd(mInterId, activity);
                    maxInterstitialAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            retryAttempt = 0;
                            AdsTools.setLogString("AppLovin Interstitial Ad loaded...");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxInterstitialAd.loadAd();
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            retryAttempt++;
                            long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));
                            new Handler().postDelayed(() -> maxInterstitialAd.loadAd(), delayMillis);
                            AdsTools.setLogString("failed to load AppLovin Interstitial");
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxInterstitialAd.loadAd();
                        }
                    });

                    // Load the first ad
                    maxInterstitialAd.loadAd();
                    break;

                case APPLOVIN_DISCOVERY:
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle interstitialExtras = new Bundle();
                    interstitialExtras.putString("zone_id", mInterId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventInterstitial.class, interstitialExtras);
                    AppLovinSdk.getInstance(activity).getAdService().loadNextAd(AppLovinAdSize.INTERSTITIAL, new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinAd = ad;
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                        }
                    });
                    appLovinInterstitialAdDialog = AppLovinInterstitialAd.create(AppLovinSdk.getInstance(activity), activity);
                    break;


                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    IronSource.setLevelPlayInterstitialListener(new LevelPlayInterstitialListener() {
                        @Override
                        public void onAdReady(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdReady");
                        }

                        @Override
                        public void onAdLoadFailed(IronSourceError ironSourceError) {
                            AdsTools.setLogString("onInterstitialAdLoadFailed" + " " + ironSourceError);
                        }

                        @Override
                        public void onAdOpened(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdOpened");
                        }

                        @Override
                        public void onAdShowSucceeded(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowSucceeded");
                        }

                        @Override
                        public void onAdShowFailed(IronSourceError ironSourceError, AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdShowFailed" + " " + ironSourceError);
                        }

                        @Override
                        public void onAdClicked(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClicked");
                        }

                        @Override
                        public void onAdClosed(AdInfo adInfo) {
                            AdsTools.setLogString("onInterstitialAdClosed");
                            loadInterstitialAd(onInterstitialAdDismissedListener);
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                        }
                    });
                    IronSource.loadInterstitial();
                    break;

                case WORTISE:
                    wortiseInterstitialAd = new com.wortise.ads.interstitial.InterstitialAd(activity, mInterId);
                    wortiseInterstitialAd.setListener(new com.wortise.ads.interstitial.InterstitialAd.Listener() {
                        @Override
                        public void onInterstitialImpression(@NonNull InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialFailedToShow(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onInterstitialFailedToLoad(@NonNull InterstitialAd interstitialAd, @NonNull com.wortise.ads.AdError adError) {
                            AdsTools.setLogString("[Wortise] [Backup] Failed to load Interstitial Ad");
                        }

                        @Override
                        public void onInterstitialClicked(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialDismissed(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            loadInterstitialAd(onInterstitialAdDismissedListener);
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            AdsTools.setLogString("[Wortise] [Backup] Interstitial Ad dismissed");
                        }



                        @Override
                        public void onInterstitialLoaded(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {
                            AdsTools.setLogString("[Wortise] [Backup] Interstitial Ad loaded");
                        }

                        @Override
                        public void onInterstitialShown(@NonNull com.wortise.ads.interstitial.InterstitialAd interstitialAd) {

                        }
                    });
                    wortiseInterstitialAd.loadAd();
                    break;

                case NONE:
                    //do nothing
                    break;
            }
        }
    }

    public void showBackupInterstitialAd(OnInterstitialAdShowedListener onInterstitialAdShowedListener, OnInterstitialAdDismissedListener onInterstitialAdDismissedListener) {
        if (adStatus.equals(AD_STATUS_ON)) {
            AdsTools.setLogString("Show Backup Interstitial Ad [" + backupAdsType.toUpperCase() + "]");
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    if (adMobInterstitialAd != null) {
                        adMobInterstitialAd.show(activity);
                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                    }
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (adManagerInterstitialAd != null) {
                        adManagerInterstitialAd.show(activity);
                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanInterstitialAd != null && fanInterstitialAd.isAdLoaded()) {
                        fanInterstitialAd.show();
                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                    }
                    break;

                case STARTAPP:
                    if (startAppAd != null) {
                        startAppAd.showAd(new AdDisplayListener() {
                            @Override
                            public void adHidden(Ad ad) {
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }

                            @Override
                            public void adDisplayed(Ad ad) {
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            }

                            @Override
                            public void adClicked(Ad ad) {

                            }

                            @Override
                            public void adNotDisplayed(Ad ad) {

                            }
                        });
                    }
                    break;

                case UNITY:
                    final IInterstitialAdShowListener showListener = new IInterstitialAdShowListener() {
                        @Override
                        public void onInterstitialShowed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                            onInterstitialAdShowedListener.onInterstitialAdShowed();
                        }

                        @Override
                        public void onInterstitialClicked(com.unity3d.mediation.InterstitialAd interstitialAd) {

                        }

                        @Override
                        public void onInterstitialClosed(com.unity3d.mediation.InterstitialAd interstitialAd) {
                            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                        }

                        @Override
                        public void onInterstitialFailedShow(com.unity3d.mediation.InterstitialAd interstitialAd, ShowError showError, String s) {
                            AdsTools.setLogString("unity ads show failure");
                        }
                    };
                    unityInterstitialAd.show(showListener);
                    break;
                case APPNEXT:
                    if (appNextInterstitialAd.isAdLoaded()) {
                        appNextInterstitialAd.showAd();
                        appNextInterstitialAd.setOnAdClosedCallback(new OnAdClosed() {
                            @Override
                            public void onAdClosed() {
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }
                        });
                        appNextInterstitialAd.setOnAdOpenedCallback(new OnAdOpened() {
                            @Override
                            public void adOpened() {
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            }
                        });
                    }
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (maxInterstitialAd != null && maxInterstitialAd.isReady()) {
                        maxInterstitialAd.showAd();
                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (appLovinInterstitialAdDialog != null) {
                        appLovinInterstitialAdDialog.setAdDisplayListener(new AppLovinAdDisplayListener() {
                            @Override
                            public void adDisplayed(AppLovinAd appLovinAd) {
                                onInterstitialAdShowedListener.onInterstitialAdShowed();
                            }

                            @Override
                            public void adHidden(AppLovinAd appLovinAd) {
                                onInterstitialAdDismissedListener.onInterstitialAdDismissed();
                            }
                        });
                        appLovinInterstitialAdDialog.showAndRender(appLovinAd);
                    }
                    break;
                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    if (IronSource.isInterstitialReady()) {
                        IronSource.showInterstitial(mInterId);
                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                    }
                    break;

                case WORTISE:
                    if (wortiseInterstitialAd != null && wortiseInterstitialAd.isAvailable()) {
                        wortiseInterstitialAd.showAd();
                        onInterstitialAdShowedListener.onInterstitialAdShowed();
                    }
                    break;

                case NONE:
                    //do nothing
                    break;
            }
        } else {
            onInterstitialAdDismissedListener.onInterstitialAdDismissed();
        }
    }


}
