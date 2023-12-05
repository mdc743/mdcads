package com.marketdigitalcode.adslibrary;


import static com.marketdigitalcode.adslibrary.util.AdsConstant.ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.AD_STATUS_ON;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.GOOGLE_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAppOpenAd;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdRequest;
import com.marketdigitalcode.adslibrary.face.OnShowAdCompleteListener;

@SuppressLint("StaticFieldLeak")
public class AppOpenAd {
    public static com.google.android.gms.ads.appopen.AppOpenAd appOpenAd = null;
    public static MaxAppOpenAd maxAppOpenAd = null;
    public static com.wortise.ads.appopen.AppOpenAd wortiseAppOpenAd = null;
    public static boolean isAppOpenAdLoaded = false;

    private static final String TAG = "AppOpenAd";
    private final Activity activity;
    private String adStatus = "";
    private String adsType = "";
    private String backupAdsType = "";
    private String OpenAdsId = "";

    public AppOpenAd(Activity activity) {
        this.activity = activity;
    }

    public AppOpenAd build() {
        loadAppOpenAd();
        return this;
    }

    public AppOpenAd build(OnShowAdCompleteListener onShowAdCompleteListener) {
        loadAppOpenAd(onShowAdCompleteListener);
        return this;
    }

    public AppOpenAd show() {
        showAppOpenAd();
        return this;
    }

    public AppOpenAd show(OnShowAdCompleteListener onShowAdCompleteListener) {
        showAppOpenAd(onShowAdCompleteListener);
        return this;
    }

    public AppOpenAd setAdStatus(String adStatus) {
        this.adStatus = adStatus;
        return this;
    }

    public AppOpenAd setAdType(String adsType) {
        this.adsType = adsType;
        return this;
    }

    public AppOpenAd setBackupAdType(String backupAdsType) {
        this.backupAdsType = backupAdsType;
        return this;
    }

    public AppOpenAd setOpenAdsId(String OpenAdsId) {
        this.OpenAdsId = OpenAdsId;
        return this;
    }

    public void destroyOpenAd() {
        AppOpenAd.isAppOpenAdLoaded = false;
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (appOpenAd != null) {
                        appOpenAd = null;
                    }
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                    if (maxAppOpenAd != null) {
                        maxAppOpenAd = null;
                    }
                    break;

                case WORTISE:
                    if (wortiseAppOpenAd != null) {
                        wortiseAppOpenAd = null;
                    }
                    break;
                default:
                    //do nothing
                    break;
            }
        }
    }

    //main ads
    public void loadAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    AdRequest adRequest = new AdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            showAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            loadBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + adsType + "] " + "[on start] failed to load app open ad: " + loadAdError.getMessage());
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            showAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            loadBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + adsType + "] " + "[on start] failed to load app open ad: " + loadAdError.getMessage());
                        }
                    });
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                    if (!OpenAdsId.equals("")) {
                        maxAppOpenAd = new MaxAppOpenAd(OpenAdsId, activity);
                        maxAppOpenAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(@NonNull MaxAd ad) {
                                showAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad loaded");
                            }

                            @Override
                            public void onAdDisplayed(@NonNull MaxAd ad) {
                            }

                            @Override
                            public void onAdHidden(@NonNull MaxAd ad) {
                                maxAppOpenAd = null;
                                showAppOpenAd(onShowAdCompleteListener);
                            }

                            @Override
                            public void onAdClicked(@NonNull MaxAd ad) {
                            }

                            @Override
                            public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                                maxAppOpenAd = null;
                                loadBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adsType + "] " + "[on start] failed to load app open ad: " + error.getMessage());
                            }

                            @Override
                            public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                                maxAppOpenAd = null;
                                loadBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + adsType + "] " + "[on start] failed to display app open ad: " + error.getMessage());
                            }
                        });
                        maxAppOpenAd.loadAd();
                    } else {
                        loadBackupAppOpenAd(onShowAdCompleteListener);
                    }
                    break;

                case WORTISE:
                    wortiseAppOpenAd = new com.wortise.ads.appopen.AppOpenAd(activity, OpenAdsId);
                    wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                        @Override
                        public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                            wortiseAppOpenAd = null;
                            loadBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + adsType + "] " + "[on start] failed to load app open ad: " + adError);


                        }

                        @Override
                        public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                        }

                        @Override
                        public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            showAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad loaded");
                        }

                        @Override
                        public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }
                    });

                    wortiseAppOpenAd.loadAd();
                    break;

                default:
                    onShowAdCompleteListener.onShowAdComplete();
                    break;
            }
        } else {
            onShowAdCompleteListener.onShowAdComplete();
        }
    }

    public void showAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
        switch (adsType) {
            case ADMOB:
            case FAN_BIDDING_ADMOB:
            case GOOGLE_AD_MANAGER:
            case FAN_BIDDING_AD_MANAGER:
                if (appOpenAd != null) {
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "[on start] close app open ad");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "[on start] failed to show app open ad: " + adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "[" + adsType + "] " + "[on start] show app open ad");
                        }
                    });
                    appOpenAd.show(activity);
                } else {
                    onShowAdCompleteListener.onShowAdComplete();
                }
                break;

            case APPLOVIN:
            case APPLOVIN_MAX:
                if (maxAppOpenAd != null) {
                    maxAppOpenAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                            Log.d(TAG, "[" + adsType + "] " + "[on start] show app open ad");
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "[on start] close app open ad");
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad load failed: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad display failed: " + error.getMessage());
                        }
                    });
                    maxAppOpenAd.showAd();
                } else {
                    onShowAdCompleteListener.onShowAdComplete();
                }
                break;

            case WORTISE:
                if (wortiseAppOpenAd != null) {
                    if (wortiseAppOpenAd.isAvailable()) {
                        wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                            @Override
                            public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + adsType + "] " + "[on start] close app open ad");
                            }

                            @Override
                            public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + adsType + "] " + "[on start] app open ad load failed: " + adError);
                            }

                            @Override
                            public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                            }

                            @Override
                            public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                Log.d(TAG, "[" + adsType + "] " + "[on start] show app open ad");
                            }
                        });

                        wortiseAppOpenAd.showAd(activity);
                    } else {
                        onShowAdCompleteListener.onShowAdComplete();
                    }
                } else {
                    onShowAdCompleteListener.onShowAdComplete();
                }
                break;

            default:
                onShowAdCompleteListener.onShowAdComplete();
                break;
        }
    }

    public void loadAppOpenAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    AdRequest adRequest = new AdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] failed to load app open ad : " + loadAdError.getMessage());
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] failed to load app open ad : " + loadAdError.getMessage());
                        }
                    });
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                    maxAppOpenAd = new MaxAppOpenAd(OpenAdsId, activity);
                    maxAppOpenAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad loaded");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] failed to load app open ad: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] failed to display app open ad: " + error.getMessage());
                        }
                    });
                    maxAppOpenAd.loadAd();
                    break;

                case WORTISE:
                    wortiseAppOpenAd = new com.wortise.ads.appopen.AppOpenAd(activity, OpenAdsId);
                    wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                        @Override
                        public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            wortiseAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                        }

                        @Override
                        public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                            wortiseAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] failed to load app open ad: " + adError);

                        }

                        @Override
                        public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad loaded");
                        }

                        @Override
                        public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }
                    });

                    wortiseAppOpenAd.loadAd();
                    break;

                default:
                    //do nothing
                    break;
            }
        }
    }

    public void showAppOpenAd() {
        switch (adsType) {
            case ADMOB:
            case FAN_BIDDING_ADMOB:
            case GOOGLE_AD_MANAGER:
            case FAN_BIDDING_AD_MANAGER:
                if (appOpenAd != null) {
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;
                            loadAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] close app open ad");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            loadAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] failed to show app open ad: " + adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] show app open ad");
                        }
                    });
                    appOpenAd.show(activity);
                } else {
                    showBackupAppOpenAd();
                }
                break;

            case APPLOVIN:
            case APPLOVIN_MAX:
                if (maxAppOpenAd != null) {
                    maxAppOpenAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] show app open ad");
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxAppOpenAd = null;
                            loadAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] close app open ad");
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            loadAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad load failed: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            loadAppOpenAd();
                            Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad display failed: " + error.getMessage());
                        }
                    });
                    maxAppOpenAd.showAd();
                } else {
                    showBackupAppOpenAd();
                }
                break;

            case WORTISE:
                if (wortiseAppOpenAd != null) {
                    if (wortiseAppOpenAd.isAvailable()) {
                        wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                            @Override
                            public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                wortiseAppOpenAd = null;
                                loadAppOpenAd();
                                Log.d(TAG, "[" + adsType + "] " + "[on resume] close app open ad");
                            }

                            @Override
                            public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                                wortiseAppOpenAd = null;
                                loadAppOpenAd();
                                Log.d(TAG, "[" + adsType + "] " + "[on resume] app open ad load failed: " + adError);
                            }

                            @Override
                            public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                            }

                            @Override
                            public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                Log.d(TAG, "[" + adsType + "] " + "[on resume] show app open ad");
                            }
                        });

                        wortiseAppOpenAd.showAd(activity);
                    } else {
                        showBackupAppOpenAd();
                    }
                } else {
                    showBackupAppOpenAd();
                }
                break;

            default:
                //do nothing
                break;
        }
    }

    //backup ads
    public void loadBackupAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    AdRequest adRequest = new AdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            showBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            showBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] failed to load app open ad: " + loadAdError.getMessage());
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            showBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            showBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] failed to load app open ad: " + loadAdError.getMessage());
                        }
                    });
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                    if (!OpenAdsId.equals("0")) {
                        maxAppOpenAd = new MaxAppOpenAd(OpenAdsId, activity);
                        maxAppOpenAd.setListener(new MaxAdListener() {
                            @Override
                            public void onAdLoaded(@NonNull MaxAd ad) {
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad loaded");
                            }

                            @Override
                            public void onAdDisplayed(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdHidden(@NonNull MaxAd ad) {
                                maxAppOpenAd = null;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                            }

                            @Override
                            public void onAdClicked(@NonNull MaxAd ad) {

                            }

                            @Override
                            public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                                maxAppOpenAd = null;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] failed to load app open ad: " + error.getMessage());
                            }

                            @Override
                            public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                                maxAppOpenAd = null;
                                showBackupAppOpenAd(onShowAdCompleteListener);
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] failed to display app open ad: " + error.getMessage());
                            }
                        });
                        maxAppOpenAd.loadAd();
                    } else {
                        showBackupAppOpenAd(onShowAdCompleteListener);
                    }
                    break;

                case WORTISE:
                    wortiseAppOpenAd = new com.wortise.ads.appopen.AppOpenAd(activity, OpenAdsId);
                    wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                        @Override
                        public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            wortiseAppOpenAd = null;
                            showBackupAppOpenAd(onShowAdCompleteListener);
                        }

                        @Override
                        public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                            wortiseAppOpenAd = null;
                            showBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] failed to load app open ad: " + adError);
                        }

                        @Override
                        public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            showBackupAppOpenAd(onShowAdCompleteListener);
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad loaded");

                        }

                        @Override
                        public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }
                    });

                    wortiseAppOpenAd.loadAd();
                    break;

                default:
                    onShowAdCompleteListener.onShowAdComplete();
                    break;
            }
        } else {
            onShowAdCompleteListener.onShowAdComplete();
        }
    }

    public void showBackupAppOpenAd(OnShowAdCompleteListener onShowAdCompleteListener) {
        switch (backupAdsType) {
            case ADMOB:
            case FAN_BIDDING_ADMOB:
            case GOOGLE_AD_MANAGER:
            case FAN_BIDDING_AD_MANAGER:
                if (appOpenAd != null) {
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] close app open ad");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] failed to show app open ad: " + adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] show app open ad");
                        }
                    });
                    appOpenAd.show(activity);
                } else {
                    onShowAdCompleteListener.onShowAdComplete();
                }
                break;

            case APPLOVIN:
            case APPLOVIN_MAX:
                if (maxAppOpenAd != null) {
                    maxAppOpenAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] show app open ad");
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] close app open ad");
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad load failed: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            onShowAdCompleteListener.onShowAdComplete();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad display failed: " + error.getMessage());
                        }
                    });
                    maxAppOpenAd.showAd();
                } else {
                    onShowAdCompleteListener.onShowAdComplete();
                }
                break;

            case WORTISE:
                if (wortiseAppOpenAd != null) {
                    if (wortiseAppOpenAd.isAvailable()) {
                        wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                            @Override
                            public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] close app open ad");
                            }

                            @Override
                            public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                                onShowAdCompleteListener.onShowAdComplete();
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] app open ad load failed: " + adError);
                            }

                            @Override
                            public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                            }

                            @Override
                            public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on start] [backup] show app open ad");
                            }
                        });

                        wortiseAppOpenAd.showAd(activity);
                    } else {
                        onShowAdCompleteListener.onShowAdComplete();
                    }
                } else {
                    onShowAdCompleteListener.onShowAdComplete();
                }
                break;

            default:
                onShowAdCompleteListener.onShowAdComplete();
                break;
        }
    }

    public void loadBackupAppOpenAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    AdRequest adRequest = new AdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] failed to load app open ad : " + loadAdError.getMessage());
                        }
                    });
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    @SuppressLint("VisibleForTests") AdManagerAdRequest adManagerAdRequest = new AdManagerAdRequest.Builder().build();
                    com.google.android.gms.ads.appopen.AppOpenAd.load(activity, OpenAdsId, adManagerAdRequest, new com.google.android.gms.ads.appopen.AppOpenAd.AppOpenAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull com.google.android.gms.ads.appopen.AppOpenAd ad) {
                            appOpenAd = ad;
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad loaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            appOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] failed to load app open ad : " + loadAdError.getMessage());
                        }
                    });
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                    maxAppOpenAd = new MaxAppOpenAd(OpenAdsId, activity);
                    maxAppOpenAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad loaded");
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] failed to load app open ad: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] failed to display app open ad: " + error.getMessage());
                        }
                    });
                    maxAppOpenAd.loadAd();
                    break;

                case WORTISE:
                    wortiseAppOpenAd = new com.wortise.ads.appopen.AppOpenAd(activity, OpenAdsId);
                    wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                        @Override
                        public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            wortiseAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                        }

                        @Override
                        public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                            wortiseAppOpenAd = null;
                            isAppOpenAdLoaded = false;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] failed to load app open ad: " + adError);
                        }

                        @Override
                        public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                        }

                        @Override
                        public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }

                        @Override
                        public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                            isAppOpenAdLoaded = true;
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad loaded");
                        }

                        @Override
                        public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                        }
                    });

                    wortiseAppOpenAd.loadAd();
                    break;

                default:
                    //do nothing
                    break;
            }
        }
    }

    public void showBackupAppOpenAd() {
        switch (backupAdsType) {
            case ADMOB:
            case FAN_BIDDING_ADMOB:
            case GOOGLE_AD_MANAGER:
            case FAN_BIDDING_AD_MANAGER:
                if (appOpenAd != null) {
                    appOpenAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            appOpenAd = null;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] close app open ad");
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                            appOpenAd = null;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] failed to show app open ad: " + adError.getMessage());
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] show app open ad");
                        }
                    });
                    appOpenAd.show(activity);
                }
                break;

            case APPLOVIN:
            case APPLOVIN_MAX:
                if (maxAppOpenAd != null) {
                    maxAppOpenAd.setListener(new MaxAdListener() {
                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] show app open ad");
                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {
                            maxAppOpenAd = null;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] close app open ad");
                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad load failed: " + error.getMessage());
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {
                            maxAppOpenAd = null;
                            loadBackupAppOpenAd();
                            Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad display failed: " + error.getMessage());
                        }
                    });
                    maxAppOpenAd.showAd();
                }
                break;

            case WORTISE:
                if (wortiseAppOpenAd != null) {
                    if (wortiseAppOpenAd.isAvailable()) {
                        wortiseAppOpenAd.setListener(new com.wortise.ads.appopen.AppOpenAd.Listener() {
                            @Override
                            public void onAppOpenClicked(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenDismissed(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                wortiseAppOpenAd = null;
                                loadAppOpenAd();
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] close app open ad");
                            }

                            @Override
                            public void onAppOpenFailedToLoad(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {
                                wortiseAppOpenAd = null;
                                loadAppOpenAd();
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] app open ad load failed: " + adError);
                            }

                            @Override
                            public void onAppOpenFailedToShow(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd, @NonNull com.wortise.ads.AdError adError) {

                            }

                            @Override
                            public void onAppOpenImpression(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenLoaded(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {

                            }

                            @Override
                            public void onAppOpenShown(@NonNull com.wortise.ads.appopen.AppOpenAd appOpenAd) {
                                Log.d(TAG, "[" + backupAdsType + "] " + "[on resume] [backup] show app open ad");
                            }
                        });

                        wortiseAppOpenAd.showAd(activity);
                    } else {
                        showBackupAppOpenAd();
                    }
                } else {
                    showBackupAppOpenAd();
                }
                break;

            default:
                //do nothing
                break;
        }
    }


}
