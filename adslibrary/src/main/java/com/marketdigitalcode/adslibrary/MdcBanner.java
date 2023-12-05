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
import static com.marketdigitalcode.adslibrary.util.AdsConstant.UNITY_ADS_BANNER_HEIGHT_MEDIUM;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.UNITY_ADS_BANNER_WIDTH_MEDIUM;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.applovin.sdk.AppLovinSdkUtils;
import com.appnext.banners.BannerAdRequest;
import com.appnext.banners.BannerSize;
import com.appnext.core.AppnextAd;
import com.appnext.core.AppnextAdCreativeType;
import com.appnext.core.AppnextError;
import com.facebook.ads.Ad;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.admanager.AdManagerAdView;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.adunit.adapter.utility.AdInfo;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.LevelPlayBannerListener;
import com.marketdigitalcode.adslibrary.helper.AppLovinCustomEventBanner;
import com.marketdigitalcode.adslibrary.util.AdsTools;
import com.startapp.sdk.ads.banner.Banner;
import com.startapp.sdk.ads.banner.BannerListener;
import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;
import com.wortise.ads.AdError;
import com.wortise.ads.banner.BannerAd;

public class MdcBanner {

    private final Activity activity;
    private AdView adView;
    private com.appnext.banners.BannerView appNextbannerView;
    private AdManagerAdView adManagerAdView;
    private com.facebook.ads.AdView fanAdView;
    private AppLovinAdView appLovinAdView;
    FrameLayout ironSourceBannerView;
    private IronSourceBannerLayout ironSourceBannerLayout;
    private com.wortise.ads.banner.BannerAd wortiseBannerAd;
    FrameLayout wortiseBannerView;

    private String adStatus = "";
    private String adsType = "";
    private String backupAdsType = "";
    private String mBannerId = "";

    private boolean darkTheme = false;
    private boolean legacyGDPR = false;

    public MdcBanner(Activity activity) {
        this.activity = activity;
    }

    public MdcBanner build() {
        loadBannerAd();
        return this;
    }

    public MdcBanner setAdStatus(String adStatus) {
        this.adStatus = adStatus;
        return this;
    }

    public MdcBanner setAdsType(String adsType) {
        this.adsType = adsType;
        return this;
    }

    public MdcBanner setBackupAdsType(String backupAdsType) {
        this.backupAdsType = backupAdsType;
        return this;
    }

    public MdcBanner setBannerId(String bannerId) {
        this.mBannerId = bannerId;
        return this;
    }

    public MdcBanner setDarkTheme(boolean darkTheme) {
        this.darkTheme = darkTheme;
        return this;
    }

    public MdcBanner setLegacyGDPR(boolean legacyGDPR) {
        this.legacyGDPR = legacyGDPR;
        return this;
    }

    public void loadBannerAd() {
        AdsTools.setLogString(adsType + " Banner Ad unit Id : " + mBannerId);
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    FrameLayout adContainerView = activity.findViewById(R.id.v_banner_admob_container);
                    adContainerView.post(() -> {
                        adView = new AdView(activity);
                        adView.setAdUnitId(mBannerId);
                        adContainerView.removeAllViews();
                        adContainerView.addView(adView);
                        adView.setAdSize(AdsTools.getAdSize(activity));
                        adView.loadAd(AdsTools.getAdRequest(activity, legacyGDPR));
                        adView.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                                adContainerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                // Code to be executed when an ad request fails.
                                adContainerView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when an ad opens an overlay that
                                // covers the screen.
                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when the user is about to return
                                // to the app after tapping on an ad.
                            }
                        });
                    });

                    break;
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    FrameLayout googleAdContainerView = activity.findViewById(R.id.v_banner_google_ad_container);
                    googleAdContainerView.post(() -> {
                        adManagerAdView = new AdManagerAdView(activity);
                        adManagerAdView.setAdUnitId(mBannerId);
                        googleAdContainerView.removeAllViews();
                        googleAdContainerView.addView(adManagerAdView);
                        adManagerAdView.setAdSize(AdsTools.getAdSize(activity));
                        adManagerAdView.loadAd(AdsTools.getGoogleAdManagerRequest());
                        adManagerAdView.setAdListener(new AdListener() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                googleAdContainerView.setVisibility(View.GONE);
                                loadBackupBannerAd();
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                googleAdContainerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();
                            }
                        });
                    });
                    break;
                case FAN:
                case FACEBOOK:
                    fanAdView = new com.facebook.ads.AdView(activity, mBannerId, AdSize.BANNER_HEIGHT_50);
                    RelativeLayout fanAdViewContainer = activity.findViewById(R.id.v_banner_fan_container);
                    fanAdViewContainer.addView(fanAdView);
                    com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                        @Override
                        public void onError(Ad ad, com.facebook.ads.AdError adError) {
                            fanAdViewContainer.setVisibility(View.GONE);
                            loadBackupBannerAd();
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            fanAdViewContainer.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdClicked(Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {

                        }
                    };
                    com.facebook.ads.AdView.AdViewLoadConfig loadAdConfig = fanAdView.buildLoadAdConfig().withAdListener(adListener).build();
                    fanAdView.loadAd(loadAdConfig);
                    break;
                case STARTAPP:
                    RelativeLayout startAppAdView = activity.findViewById(R.id.v_banner_startapp_container);
                    Banner banner = new Banner(activity, new BannerListener() {
                        @Override
                        public void onReceiveAd(View banner) {
                            startAppAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailedToReceiveAd(View banner) {
                            startAppAdView.setVisibility(View.GONE);
                            loadBackupBannerAd();
                        }

                        @Override
                        public void onImpression(View view) {

                        }

                        @Override
                        public void onClick(View banner) {
                        }
                    });
                    startAppAdView.addView(banner);
                    break;
                case UNITY:
                    RelativeLayout unityAdView = activity.findViewById(R.id.v_banner_unity_container);
                    BannerView bottomBanner = new BannerView(activity, mBannerId, new UnityBannerSize(UNITY_ADS_BANNER_WIDTH_MEDIUM, UNITY_ADS_BANNER_HEIGHT_MEDIUM));

                    bottomBanner.setListener(new BannerView.IListener() {
                        @Override
                        public void onBannerLoaded(BannerView bannerAdView) {
                            unityAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onBannerShown(BannerView bannerAdView) {

                        }

                        @Override
                        public void onBannerClick(BannerView bannerAdView) {

                        }

                        @Override
                        public void onBannerFailedToLoad(BannerView bannerAdView, BannerErrorInfo errorInfo) {
                            unityAdView.setVisibility(View.GONE);
                            loadBackupBannerAd();
                        }

                        @Override
                        public void onBannerLeftApplication(BannerView bannerView) {

                        }
                    });
                    unityAdView.addView(bottomBanner);
                    bottomBanner.load();
                    break;
                case APPNEXT:
                    RelativeLayout relView = activity.findViewById(R.id.v_banner_app_next_container);

                    com.appnext.banners.BannerView bannerView = new com.appnext.banners.BannerView(activity);
                    appNextbannerView = bannerView;
                    bannerView.setPlacementId(mBannerId);

                    bannerView.setBannerSize(BannerSize.BANNER);
                    relView.addView(bannerView);
                    bannerView.setBannerListener(new com.appnext.banners.BannerListener() {
                        @Override
                        public void onAdLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            relView.setVisibility(View.VISIBLE);

                        }

                        @Override
                        public void onAdClicked() {

                        }

                        @Override
                        public void onError(AppnextError appnextError) {

                            loadBackupBannerAd();
                        }
                    });
                    bannerView.loadAd(new BannerAdRequest());
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    RelativeLayout appLovinAdView = activity.findViewById(R.id.v_banner_applovin_container);
                    MaxAdView maxAdView = new MaxAdView(mBannerId, activity);
                    maxAdView.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdCollapsed(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            appLovinAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            appLovinAdView.setVisibility(View.GONE);
                            loadBackupBannerAd();
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {

                        }
                    });
                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);
                    maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                    if (darkTheme) {
                        maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_dark));
                    } else {
                        maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_light));
                    }
                    appLovinAdView.addView(maxAdView);
                    maxAdView.loadAd();
                    break;
                case APPLOVIN_DISCOVERY:
                    RelativeLayout appLovinDiscoveryAdView = activity.findViewById(R.id.v_banner_applovin_dc_container);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle bannerExtras = new Bundle();
                    bannerExtras.putString("zone_id", mBannerId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                    boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                    AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                    this.appLovinAdView = new AppLovinAdView(adSize, activity);
                    this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinDiscoveryAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            appLovinDiscoveryAdView.setVisibility(View.GONE);
                            loadBackupBannerAd();
                        }
                    });
                    appLovinDiscoveryAdView.addView(this.appLovinAdView);
                    this.appLovinAdView.loadNextAd();
                    break;
                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    ironSourceBannerView = activity.findViewById(R.id.v_banner_iron_src_container);
                    ISBannerSize size = ISBannerSize.BANNER;
                    ironSourceBannerLayout = IronSource.createBanner(activity, size);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    ironSourceBannerView.addView(ironSourceBannerLayout, 0, layoutParams);
                    if (ironSourceBannerLayout != null) {
                        ironSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                            @Override
                            public void onAdLoaded(AdInfo adInfo) {
                                ironSourceBannerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                                loadBackupBannerAd();
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdLeftApplication(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdScreenPresented(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdScreenDismissed(AdInfo adInfo) {

                            }
                        });
                        IronSource.loadBanner(ironSourceBannerLayout, mBannerId);
                    }
                    break;
                case WORTISE:
                    wortiseBannerAd = new com.wortise.ads.banner.BannerAd(activity);
                    wortiseBannerAd.setAdSize(AdsTools.getWortiseAdSize(activity));
                    wortiseBannerAd.setAdUnitId(mBannerId);
                    wortiseBannerView = activity.findViewById(R.id.v_banner_wr_ts_container);
                    wortiseBannerView.addView(wortiseBannerAd);
                    wortiseBannerAd.loadAd();
                    wortiseBannerAd.setListener(new com.wortise.ads.banner.BannerAd.Listener() {
                        @Override
                        public void onBannerImpression(@NonNull BannerAd bannerAd) {

                        }

                        @Override
                        public void onBannerFailedToLoad(@NonNull BannerAd bannerAd, @NonNull AdError adError) {
                            wortiseBannerView.setVisibility(View.GONE);
                            loadBackupBannerAd();
                        }

                        @Override
                        public void onBannerClicked(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {

                        }

                        @Override
                        public void onBannerLoaded(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                            wortiseBannerView.setVisibility(View.VISIBLE);

                        }
                    });
                    break;
            }
        }
    }

    public void loadBackupBannerAd() {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdsType) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    FrameLayout adContainerView = activity.findViewById(R.id.v_banner_admob_container);
                    adContainerView.post(() -> {
                        adView = new AdView(activity);
                        adView.setAdUnitId(mBannerId);
                        adContainerView.removeAllViews();
                        adContainerView.addView(adView);
                        adView.setAdSize(AdsTools.getAdSize(activity));
                        adView.loadAd(AdsTools.getAdRequest(activity, legacyGDPR));
                        adView.setAdListener(new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                // Code to be executed when an ad finishes loading.
                                adContainerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                // Code to be executed when an ad request fails.
                                adContainerView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdOpened() {
                                // Code to be executed when an ad opens an overlay that
                                // covers the screen.
                            }

                            @Override
                            public void onAdClicked() {
                                // Code to be executed when the user clicks on an ad.
                            }

                            @Override
                            public void onAdClosed() {
                                // Code to be executed when the user is about to return
                                // to the app after tapping on an ad.
                            }
                        });
                    });

                    break;
                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    FrameLayout googleAdContainerView = activity.findViewById(R.id.v_banner_google_ad_container);
                    googleAdContainerView.post(() -> {
                        adManagerAdView = new AdManagerAdView(activity);
                        adManagerAdView.setAdUnitId(mBannerId);
                        googleAdContainerView.removeAllViews();
                        googleAdContainerView.addView(adManagerAdView);
                        adManagerAdView.setAdSize(AdsTools.getAdSize(activity));
                        adManagerAdView.loadAd(AdsTools.getGoogleAdManagerRequest());
                        adManagerAdView.setAdListener(new AdListener() {
                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                googleAdContainerView.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAdImpression() {
                                super.onAdImpression();
                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                googleAdContainerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdOpened() {
                                super.onAdOpened();
                            }
                        });
                    });
                    break;

                case FAN:
                case FACEBOOK:
                    fanAdView = new com.facebook.ads.AdView(activity, mBannerId, AdSize.BANNER_HEIGHT_50);
                    RelativeLayout fanAdViewContainer = activity.findViewById(R.id.v_banner_fan_container);
                    fanAdViewContainer.addView(fanAdView);
                    com.facebook.ads.AdListener adListener = new com.facebook.ads.AdListener() {
                        @Override
                        public void onError(Ad ad, com.facebook.ads.AdError adError) {
                            fanAdViewContainer.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdLoaded(Ad ad) {
                            fanAdViewContainer.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdClicked(Ad ad) {

                        }

                        @Override
                        public void onLoggingImpression(Ad ad) {

                        }
                    };
                    com.facebook.ads.AdView.AdViewLoadConfig loadAdConfig = fanAdView.buildLoadAdConfig().withAdListener(adListener).build();
                    fanAdView.loadAd(loadAdConfig);
                    break;
                case STARTAPP:
                    RelativeLayout startAppAdView = activity.findViewById(R.id.v_banner_startapp_container);
                    Banner banner = new Banner(activity, new BannerListener() {
                        @Override
                        public void onReceiveAd(View banner) {
                            startAppAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFailedToReceiveAd(View banner) {
                            startAppAdView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onImpression(View view) {

                        }

                        @Override
                        public void onClick(View banner) {
                        }
                    });
                    startAppAdView.addView(banner);
                    break;
                case UNITY:
                    RelativeLayout unityAdView = activity.findViewById(R.id.v_banner_unity_container);
                    BannerView bottomBanner = new BannerView(activity, mBannerId, new UnityBannerSize(UNITY_ADS_BANNER_WIDTH_MEDIUM, UNITY_ADS_BANNER_HEIGHT_MEDIUM));
                    bottomBanner.setListener(new BannerView.IListener() {
                        @Override
                        public void onBannerLoaded(BannerView bannerView) {
                            unityAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onBannerShown(BannerView bannerAdView) {

                        }

                        @Override
                        public void onBannerClick(BannerView bannerView) {

                        }

                        @Override
                        public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
                            unityAdView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBannerLeftApplication(BannerView bannerView) {

                        }
                    });
                    unityAdView.addView(bottomBanner);
                    bottomBanner.load();
                    break;
                case APPNEXT:
                    RelativeLayout relView = activity.findViewById(R.id.v_banner_app_next_container);

                    com.appnext.banners.BannerView bannerView = new com.appnext.banners.BannerView(activity);
                    appNextbannerView = bannerView;
                    bannerView.setPlacementId(mBannerId);

                    bannerView.setBannerSize(BannerSize.BANNER);
                    relView.addView(bannerView);
                    bannerView.setBannerListener(new com.appnext.banners.BannerListener() {
                        @Override
                        public void onAdLoaded(String s, AppnextAdCreativeType appnextAdCreativeType) {
                            relView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdClicked() {

                        }

                        @Override
                        public void onError(AppnextError appnextError) {
                            relView.setVisibility(View.GONE);
                        }
                    });
                    bannerView.loadAd(new BannerAdRequest());
                    break;
                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    RelativeLayout appLovinAdView = activity.findViewById(R.id.v_banner_applovin_container);
                    MaxAdView maxAdView = new MaxAdView(mBannerId, activity);
                    maxAdView.setListener(new MaxAdViewAdListener() {
                        @Override
                        public void onAdExpanded(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdCollapsed(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoaded(@NonNull MaxAd ad) {
                            appLovinAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAdDisplayed(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdHidden(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdClicked(@NonNull MaxAd ad) {

                        }

                        @Override
                        public void onAdLoadFailed(@NonNull String adUnitId, @NonNull MaxError error) {
                            appLovinAdView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAdDisplayFailed(@NonNull MaxAd ad, @NonNull MaxError error) {

                        }
                    });

                    int width = ViewGroup.LayoutParams.MATCH_PARENT;
                    int heightPx = activity.getResources().getDimensionPixelSize(R.dimen.applovin_banner_height);
                    maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, heightPx));
                    if (darkTheme) {
                        maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_dark));
                    } else {
                        maxAdView.setBackgroundColor(activity.getResources().getColor(R.color.color_light));
                    }
                    appLovinAdView.addView(maxAdView);
                    maxAdView.loadAd();
                    break;
                case APPLOVIN_DISCOVERY:
                    RelativeLayout appLovinDiscoveryAdView = activity.findViewById(R.id.v_banner_applovin_dc_container);
                    AdRequest.Builder builder = new AdRequest.Builder();
                    Bundle bannerExtras = new Bundle();
                    bannerExtras.putString("zone_id", mBannerId);
                    builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                    boolean isTablet2 = AppLovinSdkUtils.isTablet(activity);
                    AppLovinAdSize adSize = isTablet2 ? AppLovinAdSize.LEADER : AppLovinAdSize.BANNER;
                    this.appLovinAdView = new AppLovinAdView(adSize, activity);
                    this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                        @Override
                        public void adReceived(AppLovinAd ad) {
                            appLovinDiscoveryAdView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void failedToReceiveAd(int errorCode) {
                            appLovinDiscoveryAdView.setVisibility(View.GONE);
                        }
                    });
                    appLovinDiscoveryAdView.addView(this.appLovinAdView);
                    this.appLovinAdView.loadNextAd();
                    break;
                case IRONSOURCE:
                case FAN_BIDDING_IRONSOURCE:
                    ironSourceBannerView = activity.findViewById(R.id.v_banner_iron_src_container);
                    ISBannerSize size = ISBannerSize.BANNER;
                    ironSourceBannerLayout = IronSource.createBanner(activity, size);
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    ironSourceBannerView.addView(ironSourceBannerLayout, 0, layoutParams);
                    if (ironSourceBannerLayout != null) {
                        ironSourceBannerLayout.setLevelPlayBannerListener(new LevelPlayBannerListener() {
                            @Override
                            public void onAdLoaded(AdInfo adInfo) {
                                ironSourceBannerView.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onAdLoadFailed(IronSourceError ironSourceError) {
                            }

                            @Override
                            public void onAdClicked(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdLeftApplication(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdScreenPresented(AdInfo adInfo) {

                            }

                            @Override
                            public void onAdScreenDismissed(AdInfo adInfo) {

                            }
                        });
                        IronSource.loadBanner(ironSourceBannerLayout, mBannerId);
                    }
                    break;
                case WORTISE:
                    wortiseBannerAd = new com.wortise.ads.banner.BannerAd(activity);
                    wortiseBannerAd.setAdSize(AdsTools.getWortiseAdSize(activity));
                    wortiseBannerAd.setAdUnitId(mBannerId);
                    wortiseBannerView = activity.findViewById(R.id.v_banner_wr_ts_container);
                    wortiseBannerView.addView(wortiseBannerAd);
                    wortiseBannerAd.loadAd();
                    wortiseBannerAd.setListener(new com.wortise.ads.banner.BannerAd.Listener() {
                        @Override
                        public void onBannerImpression(@NonNull BannerAd bannerAd) {

                        }

                        @Override
                        public void onBannerFailedToLoad(@NonNull BannerAd bannerAd, @NonNull AdError adError) {
                            wortiseBannerView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBannerClicked(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {

                        }



                        @Override
                        public void onBannerLoaded(@NonNull com.wortise.ads.banner.BannerAd bannerAd) {
                            wortiseBannerView.setVisibility(View.VISIBLE);
                        }
                    });
                    break;

            }
        }
    }

    public void destroyAndDetachBanner() {
        if (adStatus.equals(AD_STATUS_ON)) {
            if (adsType.equals(IRONSOURCE) || backupAdsType.equals(IRONSOURCE)) {
                if (ironSourceBannerView != null) {
                    IronSource.destroyBanner(ironSourceBannerLayout);
                    ironSourceBannerView.removeView(ironSourceBannerLayout);
                }
            }
            if (adsType.equals(APPNEXT)) {
                if (appNextbannerView != null) {
                    appNextbannerView.destroy();
                }
            }
        }
    }

}
