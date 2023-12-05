package com.marketdigitalcode.adslibrary.util;



import static com.marketdigitalcode.adslibrary.util.AdsConstant.ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.AD_STATUS_ON;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_DISCOVERY;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FACEBOOK;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_ADMOB;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.FAN_BIDDING_APPLOVIN_MAX;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.GOOGLE_AD_MANAGER;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.NONE;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.STARTAPP;
import static com.marketdigitalcode.adslibrary.util.AdsConstant.WORTISE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.applovin.adview.AppLovinAdView;
import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.mediation.nativeAds.MaxNativeAdViewBinder;
import com.applovin.sdk.AppLovinAd;
import com.applovin.sdk.AppLovinAdLoadListener;
import com.applovin.sdk.AppLovinAdSize;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.marketdigitalcode.adslibrary.R;
import com.marketdigitalcode.adslibrary.helper.AppLovinCustomEventBanner;
import com.marketdigitalcode.adslibrary.view.AdManagerTemplateView;
import com.marketdigitalcode.adslibrary.view.TemplateView;
import com.startapp.sdk.ads.nativead.NativeAdDetails;
import com.startapp.sdk.ads.nativead.NativeAdPreferences;
import com.startapp.sdk.ads.nativead.StartAppNativeAd;
import com.startapp.sdk.adsbase.Ad;
import com.startapp.sdk.adsbase.adlisteners.AdEventListener;
import com.wortise.ads.natives.GoogleNativeAd;

import java.util.ArrayList;
import java.util.List;

public class NativeAdViewHolder extends RecyclerView.ViewHolder {

    private static final String TAG = "AdNetwork";
    LinearLayout nativeAdViewContainer;

    //AdMob
    MediaView mediaView;
    TemplateView admobNativeAd;
    LinearLayout admobNativeBackground;

    //Ad Manager
    MediaView adManagerMediaView;
    AdManagerTemplateView adManagerNativeAd;
    LinearLayout adManagerNativeBackground;

    //FAN
    com.facebook.ads.NativeAd fanNativeAd;
    NativeAdLayout fanNativeAdLayout;

    //StartApp
    View startappNativeAd;
    ImageView startappNativeImage;
    ImageView startappNativeIcon;
    TextView startappNativeTitle;
    TextView startappNativeDescription;
    Button startappNativeButton;
    LinearLayout startappNativeBackground;

    //AppLovin
    FrameLayout applovinNativeAd;
    MaxNativeAdLoader nativeAdLoader;
    MaxAd maxNativeAd;
    LinearLayout appLovinDiscoveryMrecAd;
    private AppLovinAdView appLovinAdView;

    //Wortise
    private GoogleNativeAd mGoogleNativeAd;
    FrameLayout wortiseNativeAd;

    public NativeAdViewHolder(View view) {
        super(view);

        nativeAdViewContainer = view.findViewById(R.id.native_ad_view_container);

        //AdMob
        admobNativeAd = view.findViewById(R.id.admob_native_ad_container);
        mediaView = view.findViewById(R.id.media_view);
        admobNativeBackground = view.findViewById(R.id.background);

        //Ad Manager
        adManagerNativeAd = view.findViewById(R.id.google_ad_manager_native_ad_container);
        adManagerMediaView = view.findViewById(R.id.ad_manager_media_view);
        adManagerNativeBackground = view.findViewById(R.id.ad_manager_background);

        //FAN
        fanNativeAdLayout = view.findViewById(R.id.fan_native_ad_container);

        //StartApp
        startappNativeAd = view.findViewById(R.id.startapp_native_ad_container);
        startappNativeImage = view.findViewById(R.id.startapp_native_image);
        startappNativeIcon = view.findViewById(R.id.startapp_native_icon);
        startappNativeTitle = view.findViewById(R.id.startapp_native_title);
        startappNativeDescription = view.findViewById(R.id.startapp_native_description);
        startappNativeButton = view.findViewById(R.id.startapp_native_button);
        startappNativeButton.setOnClickListener(v1 -> itemView.performClick());
        startappNativeBackground = view.findViewById(R.id.startapp_native_background);

        //AppLovin
        applovinNativeAd = view.findViewById(R.id.applovin_native_ad_container);
        appLovinDiscoveryMrecAd = view.findViewById(R.id.applovin_discovery_mrec_ad_container);

        wortiseNativeAd = view.findViewById(R.id.wortise_native_ad_container);

    }
    public void loadNativeAd(Context context, String adStatus,  String adNetwork, String backupAdNetwork, String mNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (adNetwork) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    if (admobNativeAd.getVisibility() != View.VISIBLE) {
                        AdLoader adLoader = new AdLoader.Builder(context, mNativeId)
                                .forNativeAd(NativeAd -> {
                                    if (darkTheme) {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        admobNativeAd.setStyles(styles);
                                        admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        admobNativeAd.setStyles(styles);
                                        admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }
                                    mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    admobNativeAd.setNativeAd(NativeAd);
                                    admobNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                })
                                .withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                        loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                    }
                                })
                                .build();
                        adLoader.loadAd(AdsTools.getAdRequest((Activity) context, legacyGDPR));
                    } else {
                        Log.d(TAG, "AdMob native ads has been loaded");
                    }
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                        AdLoader adLoader = new AdLoader.Builder(context, mNativeId)
                                .forNativeAd(NativeAd -> {
                                    if (darkTheme) {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        adManagerNativeAd.setStyles(styles);
                                        adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        adManagerNativeAd.setStyles(styles);
                                        adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }
                                    adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    adManagerNativeAd.setNativeAd(NativeAd);
                                    adManagerNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                })
                                .withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                        loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                    }
                                })
                                .build();
                        adLoader.loadAd(AdsTools.getGoogleAdManagerRequest());
                    } else {
                        Log.d(TAG, "Ad Manager Native Ad has been loaded");
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                        fanNativeAd = new com.facebook.ads.NativeAd(context, mNativeId);
                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                                loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                // Race condition, load() called again before last ad was displayed
                                fanNativeAdLayout.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                if (fanNativeAd != ad) {
                                    return;
                                }
                                // Inflate Native Ad into Container
                                //inflateAd(nativeAd);
                                fanNativeAd.unregisterView();
                                // Add the Ad view into the ad container.
                                LayoutInflater inflater = LayoutInflater.from(context);
                                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                LinearLayout nativeAdView;

                                switch (nativeAdStyle) {
                                    case AdsConstant.STYLE_NEWS:
                                    case AdsConstant.STYLE_MEDIUM:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_SMALL:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_LARGE:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                        break;
                                    case AdsConstant.STYLE_RADIO:
                                    case AdsConstant.STYLE_SMALL:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                        break;
                                    default:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                        break;
                                }
                                fanNativeAdLayout.addView(nativeAdView);

                                // Add the AdOptionsView
                                LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
                                adChoicesContainer.removeAllViews();
                                adChoicesContainer.addView(adOptionsView, 0);

                                // Create native UI using the ad metadata.
                                TextView nativeAdTitle = nativeAdView.findViewById(R.id.native_ad_title);
                                com.facebook.ads.MediaView nativeAdMedia = nativeAdView.findViewById(R.id.native_ad_media);
                                com.facebook.ads.MediaView nativeAdIcon = nativeAdView.findViewById(R.id.native_ad_icon);
                                TextView nativeAdSocialContext = nativeAdView.findViewById(R.id.native_ad_social_context);
                                TextView nativeAdBody = nativeAdView.findViewById(R.id.native_ad_body);
                                TextView sponsoredLabel = nativeAdView.findViewById(R.id.native_ad_sponsored_label);
                                Button nativeAdCallToAction = nativeAdView.findViewById(R.id.native_ad_call_to_action);
                                LinearLayout fanNativeBackground = nativeAdView.findViewById(R.id.ad_unit);

                                if (darkTheme) {
                                    nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                    nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                    sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                    nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                                // Set the Text.
                                nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                                nativeAdBody.setText(fanNativeAd.getAdBodyText());
                                nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                                nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                                sponsoredLabel.setText(fanNativeAd.getSponsoredTranslation());

                                // Create a list of clickable views
                                List<View> clickableViews = new ArrayList<>();
                                clickableViews.add(nativeAdTitle);
                                clickableViews.add(sponsoredLabel);
                                clickableViews.add(nativeAdIcon);
                                clickableViews.add(nativeAdMedia);
                                clickableViews.add(nativeAdBody);
                                clickableViews.add(nativeAdSocialContext);
                                clickableViews.add(nativeAdCallToAction);

                                // Register the Title and CTA button to listen for clicks.
                                fanNativeAd.registerViewForInteraction(nativeAdView, nativeAdIcon, nativeAdMedia, clickableViews);

                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.NativeAd.NativeLoadAdConfig loadAdConfig = fanNativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
                        fanNativeAd.loadAd(loadAdConfig);
                    } else {
                        Log.d(TAG, "FAN Native Ad has been loaded");
                    }
                    break;

                case STARTAPP:
                    if (startappNativeAd.getVisibility() != View.VISIBLE) {
                        StartAppNativeAd startAppNativeAd = new StartAppNativeAd(context);
                        NativeAdPreferences nativePrefs = new NativeAdPreferences()
                                .setAdsNumber(3)
                                .setAutoBitmapDownload(true)
                                .setPrimaryImageSize(AdsConstant.STARTAPP_IMAGE_MEDIUM);
                        AdEventListener adListener = new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad arg0) {
                                Log.d("STARTAPP_ADS", "ad loaded");
                                startappNativeAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                //noinspection rawtypes
                                ArrayList ads = startAppNativeAd.getNativeAds(); // get NativeAds list

                                // Print all ads details to log
                                for (Object ad : ads) {
                                    Log.d("STARTAPP_ADS", ad.toString());
                                }

                                NativeAdDetails ad = (NativeAdDetails) ads.get(0);
                                if (ad != null) {
                                    startappNativeImage.setImageBitmap(ad.getImageBitmap());
                                    startappNativeIcon.setImageBitmap(ad.getSecondaryImageBitmap());
                                    startappNativeTitle.setText(ad.getTitle());
                                    startappNativeDescription.setText(ad.getDescription());
                                    startappNativeButton.setText(ad.isApp() ? "Install" : "Open");
                                    ad.registerViewForInteraction(itemView);
                                }

                                if (darkTheme) {
                                    startappNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    startappNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                            }

                            @Override
                            public void onFailedToReceiveAd(Ad arg0) {
                                loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                Log.d(TAG, "ad failed");
                            }
                        };
                        //noinspection deprecation
                        startAppNativeAd.loadAd(nativePrefs, adListener);
                    } else {
                        Log.d(TAG, "StartApp native ads has been loaded");
                    }
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (applovinNativeAd.getVisibility() != View.VISIBLE) {
                        nativeAdLoader = new MaxNativeAdLoader(mNativeId, context);
                        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                            @Override
                            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, @NonNull final MaxAd ad) {
                                // Clean up any pre-existing native ad to prevent memory leaks.
                                if (maxNativeAd != null) {
                                    nativeAdLoader.destroy(maxNativeAd);
                                }

                                // Save ad for cleanup.
                                maxNativeAd = ad;

                                // Add ad view to view.
                                applovinNativeAd.removeAllViews();
                                applovinNativeAd.addView(nativeAdView);
                                applovinNativeAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);

                                LinearLayout applovinNativeBackground = nativeAdView.findViewById(R.id.applovin_native_background);
                                if (darkTheme) {
                                    applovinNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    applovinNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                                Log.d(TAG, "Max Native Ad loaded successfully");
                            }

                            @Override
                            public void onNativeAdLoadFailed(@NonNull final String adUnitId, @NonNull final MaxError error) {
                                // We recommend retrying with exponentially higher delays up to a maximum delay
                                loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                Log.d(TAG, "failed to load Max Native Ad with message : " + error.getMessage() + " and error code : " + error.getCode());
                            }

                            @Override
                            public void onNativeAdClicked(@NonNull final MaxAd ad) {
                                // Optional click callback
                            }
                        });
                        if (darkTheme) {
                            nativeAdLoader.loadAd(createNativeAdViewDark(context, nativeAdStyle));
                        } else {
                            nativeAdLoader.loadAd(createNativeAdView(context, nativeAdStyle));
                        }
                    } else {
                        Log.d(TAG, "AppLovin Native ads has been loaded");
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (appLovinDiscoveryMrecAd.getVisibility() != View.VISIBLE) {
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", mNativeId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        AppLovinAdSize adSize = AppLovinAdSize.MREC;
                        this.appLovinAdView = new AppLovinAdView(adSize, context);
                        this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinDiscoveryMrecAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                appLovinDiscoveryMrecAd.setVisibility(View.GONE);
                                nativeAdViewContainer.setVisibility(View.GONE);
                                loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                            }
                        });
                        appLovinDiscoveryMrecAd.addView(this.appLovinAdView);
                        int padding = context.getResources().getDimensionPixelOffset(R.dimen.gnt_default_margin);
                        appLovinDiscoveryMrecAd.setPadding(0, padding, 0, padding);
                        if (darkTheme) {
                            appLovinDiscoveryMrecAd.setBackgroundResource(nativeBackgroundDark);
                        } else {
                            appLovinDiscoveryMrecAd.setBackgroundResource(nativeBackgroundLight);
                        }
                        this.appLovinAdView.loadNextAd();
                    } else {
                        Log.d(TAG, "AppLovin Discovery Mrec Ad has been loaded");
                    }
                    break;

                case WORTISE:
                    if (wortiseNativeAd.getVisibility() != View.VISIBLE) {
                        mGoogleNativeAd = new GoogleNativeAd(context, mNativeId, new GoogleNativeAd.Listener() {
                            @Override
                            public void onNativeFailedToLoad(@NonNull GoogleNativeAd googleNativeAd, @NonNull com.wortise.ads.AdError adError) {
                                loadBackupNativeAd(context, adStatus, backupAdNetwork, mNativeId, darkTheme, legacyGDPR, nativeAdStyle, nativeBackgroundLight, nativeBackgroundDark);
                                Log.d(TAG, "Wortise Native Ad failed loaded");
                            }

                            @Override
                            public void onNativeClicked(@NonNull GoogleNativeAd googleNativeAd) {

                            }



                            @Override
                            public void onNativeImpression(@NonNull GoogleNativeAd googleNativeAd) {

                            }

                            @SuppressLint("InflateParams")
                            @Override
                            public void onNativeLoaded(@NonNull GoogleNativeAd googleNativeAd, @NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                NativeAdView adView;
                                switch (nativeAdStyle) {
                                    case AdsConstant.STYLE_NEWS:
                                    case AdsConstant.STYLE_MEDIUM:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_news_template_view, null);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_SMALL:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_small_template_view, null);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_LARGE:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_large_template_view, null);
                                        break;
                                    case AdsConstant.STYLE_RADIO:
                                    case AdsConstant.STYLE_SMALL:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_radio_template_view, null);
                                        break;
                                    default:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_medium_template_view, null);
                                        break;
                                }
                                populateNativeAdView(context, nativeAd, adView, darkTheme, nativeBackgroundDark, nativeBackgroundLight);
                                wortiseNativeAd.removeAllViews();
                                wortiseNativeAd.addView(adView);
                                wortiseNativeAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                Log.d(TAG, "Wortise Native Ad loaded");
                            }
                        });
                        mGoogleNativeAd.load();
                    } else {
                        Log.d(TAG, "Wortise Native Ad has been loaded");
                    }
                    break;

            }
        }
    }
    public void loadBackupNativeAd(Context context, String adStatus, String backupAdNetwork, String mNativeId, boolean darkTheme, boolean legacyGDPR, String nativeAdStyle, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (adStatus.equals(AD_STATUS_ON)) {
            switch (backupAdNetwork) {
                case ADMOB:
                case FAN_BIDDING_ADMOB:
                    if (admobNativeAd.getVisibility() != View.VISIBLE) {
                        AdLoader adLoader = new AdLoader.Builder(context, mNativeId)
                                .forNativeAd(NativeAd -> {
                                    if (darkTheme) {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        admobNativeAd.setStyles(styles);
                                        admobNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        admobNativeAd.setStyles(styles);
                                        admobNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }
                                    mediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    admobNativeAd.setNativeAd(NativeAd);
                                    admobNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                })
                                .withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                        admobNativeAd.setVisibility(View.GONE);
                                        nativeAdViewContainer.setVisibility(View.GONE);
                                    }
                                })
                                .build();
                        adLoader.loadAd(AdsTools.getAdRequest((Activity) context, legacyGDPR));
                    } else {
                        Log.d(TAG, "AdMob native ads has been loaded");
                    }
                    break;

                case GOOGLE_AD_MANAGER:
                case FAN_BIDDING_AD_MANAGER:
                    if (adManagerNativeAd.getVisibility() != View.VISIBLE) {
                        AdLoader adLoader = new AdLoader.Builder(context, mNativeId)
                                .forNativeAd(NativeAd -> {
                                    if (darkTheme) {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundDark));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        adManagerNativeAd.setStyles(styles);
                                        adManagerNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                    } else {
                                        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, nativeBackgroundLight));
                                        NativeTemplateStyle styles = new NativeTemplateStyle.Builder().withMainBackgroundColor(colorDrawable).build();
                                        adManagerNativeAd.setStyles(styles);
                                        adManagerNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                    }
                                    adManagerMediaView.setImageScaleType(ImageView.ScaleType.CENTER_CROP);
                                    adManagerNativeAd.setNativeAd(NativeAd);
                                    adManagerNativeAd.setVisibility(View.VISIBLE);
                                    nativeAdViewContainer.setVisibility(View.VISIBLE);
                                })
                                .withAdListener(new AdListener() {
                                    @Override
                                    public void onAdFailedToLoad(@NonNull LoadAdError adError) {
                                        adManagerNativeAd.setVisibility(View.GONE);
                                        nativeAdViewContainer.setVisibility(View.GONE);
                                    }
                                })
                                .build();
                        adLoader.loadAd(AdsTools.getGoogleAdManagerRequest());
                    } else {
                        Log.d(TAG, "Ad Manager Native Ad has been loaded");
                    }
                    break;

                case FAN:
                case FACEBOOK:
                    if (fanNativeAdLayout.getVisibility() != View.VISIBLE) {
                        fanNativeAd = new com.facebook.ads.NativeAd(context, mNativeId);
                        NativeAdListener nativeAdListener = new NativeAdListener() {
                            @Override
                            public void onMediaDownloaded(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onError(com.facebook.ads.Ad ad, AdError adError) {

                            }

                            @Override
                            public void onAdLoaded(com.facebook.ads.Ad ad) {
                                // Race condition, load() called again before last ad was displayed
                                fanNativeAdLayout.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                if (fanNativeAd != ad) {
                                    return;
                                }
                                // Inflate Native Ad into Container
                                //inflateAd(nativeAd);
                                fanNativeAd.unregisterView();
                                // Add the Ad view into the ad container.
                                LayoutInflater inflater = LayoutInflater.from(context);
                                // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
                                LinearLayout nativeAdView;

                                switch (nativeAdStyle) {
                                    case AdsConstant.STYLE_NEWS:
                                    case AdsConstant.STYLE_MEDIUM:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_news_template_view, fanNativeAdLayout, false);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_SMALL:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_small_template_view, fanNativeAdLayout, false);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_LARGE:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_video_large_template_view, fanNativeAdLayout, false);
                                        break;
                                    case AdsConstant.STYLE_RADIO:
                                    case AdsConstant.STYLE_SMALL:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_radio_template_view, fanNativeAdLayout, false);
                                        break;
                                    default:
                                        nativeAdView = (LinearLayout) inflater.inflate(R.layout.gnt_fan_medium_template_view, fanNativeAdLayout, false);
                                        break;
                                }
                                fanNativeAdLayout.addView(nativeAdView);

                                // Add the AdOptionsView
                                LinearLayout adChoicesContainer = nativeAdView.findViewById(R.id.ad_choices_container);
                                AdOptionsView adOptionsView = new AdOptionsView(context, fanNativeAd, fanNativeAdLayout);
                                adChoicesContainer.removeAllViews();
                                adChoicesContainer.addView(adOptionsView, 0);

                                // Create native UI using the ad metadata.
                                TextView nativeAdTitle = nativeAdView.findViewById(R.id.native_ad_title);
                                com.facebook.ads.MediaView nativeAdMedia = nativeAdView.findViewById(R.id.native_ad_media);
                                com.facebook.ads.MediaView nativeAdIcon = nativeAdView.findViewById(R.id.native_ad_icon);
                                TextView nativeAdSocialContext = nativeAdView.findViewById(R.id.native_ad_social_context);
                                TextView nativeAdBody = nativeAdView.findViewById(R.id.native_ad_body);
                                TextView sponsoredLabel = nativeAdView.findViewById(R.id.native_ad_sponsored_label);
                                Button nativeAdCallToAction = nativeAdView.findViewById(R.id.native_ad_call_to_action);
                                LinearLayout fanNativeBackground = nativeAdView.findViewById(R.id.ad_unit);

                                if (darkTheme) {
                                    nativeAdTitle.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                    nativeAdSocialContext.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_primary_text_color));
                                    sponsoredLabel.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                    nativeAdBody.setTextColor(ContextCompat.getColor(context, R.color.applovin_dark_secondary_text_color));
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    fanNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                                // Set the Text.
                                nativeAdTitle.setText(fanNativeAd.getAdvertiserName());
                                nativeAdBody.setText(fanNativeAd.getAdBodyText());
                                nativeAdSocialContext.setText(fanNativeAd.getAdSocialContext());
                                nativeAdCallToAction.setVisibility(fanNativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                                nativeAdCallToAction.setText(fanNativeAd.getAdCallToAction());
                                sponsoredLabel.setText(fanNativeAd.getSponsoredTranslation());

                                // Create a list of clickable views
                                List<View> clickableViews = new ArrayList<>();
                                clickableViews.add(nativeAdTitle);
                                clickableViews.add(sponsoredLabel);
                                clickableViews.add(nativeAdIcon);
                                clickableViews.add(nativeAdMedia);
                                clickableViews.add(nativeAdBody);
                                clickableViews.add(nativeAdSocialContext);
                                clickableViews.add(nativeAdCallToAction);

                                // Register the Title and CTA button to listen for clicks.
                                fanNativeAd.registerViewForInteraction(nativeAdView, nativeAdIcon, nativeAdMedia, clickableViews);

                            }

                            @Override
                            public void onAdClicked(com.facebook.ads.Ad ad) {

                            }

                            @Override
                            public void onLoggingImpression(com.facebook.ads.Ad ad) {

                            }
                        };

                        com.facebook.ads.NativeAd.NativeLoadAdConfig loadAdConfig = fanNativeAd.buildLoadAdConfig().withAdListener(nativeAdListener).build();
                        fanNativeAd.loadAd(loadAdConfig);
                    } else {
                        Log.d(TAG, "FAN Native Ad has been loaded");
                    }
                    break;

                case STARTAPP:
                    if (startappNativeAd.getVisibility() != View.VISIBLE) {
                        StartAppNativeAd startAppNativeAd = new StartAppNativeAd(context);
                        NativeAdPreferences nativePrefs = new NativeAdPreferences()
                                .setAdsNumber(3)
                                .setAutoBitmapDownload(true)
                                .setPrimaryImageSize(AdsConstant.STARTAPP_IMAGE_MEDIUM);
                        AdEventListener adListener = new AdEventListener() {
                            @Override
                            public void onReceiveAd(@NonNull Ad arg0) {
                                Log.d("STARTAPP_ADS", "ad loaded");
                                startappNativeAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                //noinspection rawtypes
                                ArrayList ads = startAppNativeAd.getNativeAds(); // get NativeAds list

                                // Print all ads details to log
                                for (Object ad : ads) {
                                    Log.d("STARTAPP_ADS", ad.toString());
                                }

                                NativeAdDetails ad = (NativeAdDetails) ads.get(0);
                                if (ad != null) {
                                    startappNativeImage.setImageBitmap(ad.getImageBitmap());
                                    startappNativeIcon.setImageBitmap(ad.getSecondaryImageBitmap());
                                    startappNativeTitle.setText(ad.getTitle());
                                    startappNativeDescription.setText(ad.getDescription());
                                    startappNativeButton.setText(ad.isApp() ? "Install" : "Open");
                                    ad.registerViewForInteraction(itemView);
                                }

                                if (darkTheme) {
                                    startappNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    startappNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }

                            }

                            @Override
                            public void onFailedToReceiveAd(Ad arg0) {
                                startappNativeAd.setVisibility(View.GONE);
                                nativeAdViewContainer.setVisibility(View.GONE);
                                Log.d(TAG, "ad failed");
                            }
                        };
                        //noinspection deprecation
                        startAppNativeAd.loadAd(nativePrefs, adListener);
                    } else {
                        Log.d(TAG, "StartApp native ads has been loaded");
                    }
                    break;

                case APPLOVIN:
                case APPLOVIN_MAX:
                case FAN_BIDDING_APPLOVIN_MAX:
                    if (applovinNativeAd.getVisibility() != View.VISIBLE) {
                        nativeAdLoader = new MaxNativeAdLoader(mNativeId, context);
                        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
                            @Override
                            public void onNativeAdLoaded(final MaxNativeAdView nativeAdView, @NonNull final MaxAd ad) {
                                // Clean up any pre-existing native ad to prevent memory leaks.
                                if (maxNativeAd != null) {
                                    nativeAdLoader.destroy(maxNativeAd);
                                }

                                // Save ad for cleanup.
                                maxNativeAd = ad;

                                // Add ad view to view.
                                applovinNativeAd.removeAllViews();
                                applovinNativeAd.addView(nativeAdView);
                                applovinNativeAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);

                                LinearLayout applovinNativeBackground = nativeAdView.findViewById(R.id.applovin_native_background);
                                if (darkTheme) {
                                    applovinNativeBackground.setBackgroundResource(nativeBackgroundDark);
                                } else {
                                    applovinNativeBackground.setBackgroundResource(nativeBackgroundLight);
                                }
                            }

                            @Override
                            public void onNativeAdLoadFailed(@NonNull final String adUnitId, @NonNull final MaxError error) {
                                // We recommend retrying with exponentially higher delays up to a maximum delay
                            }

                            @Override
                            public void onNativeAdClicked(@NonNull final MaxAd ad) {
                                // Optional click callback
                            }
                        });
                        if (darkTheme) {
                            nativeAdLoader.loadAd(createNativeAdViewDark(context, nativeAdStyle));
                        } else {
                            nativeAdLoader.loadAd(createNativeAdView(context, nativeAdStyle));
                        }
                    } else {
                        Log.d(TAG, "AppLovin Native ads has been loaded");
                    }
                    break;

                case APPLOVIN_DISCOVERY:
                    if (appLovinDiscoveryMrecAd.getVisibility() != View.VISIBLE) {
                        AdRequest.Builder builder = new AdRequest.Builder();
                        Bundle bannerExtras = new Bundle();
                        bannerExtras.putString("zone_id", mNativeId);
                        builder.addCustomEventExtrasBundle(AppLovinCustomEventBanner.class, bannerExtras);

                        AppLovinAdSize adSize = AppLovinAdSize.MREC;
                        this.appLovinAdView = new AppLovinAdView(adSize, context);
                        this.appLovinAdView.setAdLoadListener(new AppLovinAdLoadListener() {
                            @Override
                            public void adReceived(AppLovinAd ad) {
                                appLovinDiscoveryMrecAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failedToReceiveAd(int errorCode) {
                                appLovinDiscoveryMrecAd.setVisibility(View.GONE);
                                nativeAdViewContainer.setVisibility(View.GONE);
                            }
                        });
                        appLovinDiscoveryMrecAd.addView(this.appLovinAdView);
                        int padding = context.getResources().getDimensionPixelOffset(R.dimen.gnt_default_margin);
                        appLovinDiscoveryMrecAd.setPadding(0, padding, 0, padding);
                        if (darkTheme) {
                            appLovinDiscoveryMrecAd.setBackgroundResource(nativeBackgroundDark);
                        } else {
                            appLovinDiscoveryMrecAd.setBackgroundResource(nativeBackgroundLight);
                        }
                        this.appLovinAdView.loadNextAd();
                    } else {
                        Log.d(TAG, "AppLovin Discovery Mrec Ad has been loaded");
                    }
                    break;

                case WORTISE:
                    if (wortiseNativeAd.getVisibility() != View.VISIBLE) {
                        mGoogleNativeAd = new GoogleNativeAd(context, mNativeId, new GoogleNativeAd.Listener() {
                            @Override
                            public void onNativeFailedToLoad(@NonNull GoogleNativeAd googleNativeAd, @NonNull com.wortise.ads.AdError adError) {
                                Log.d(TAG, "[Backup] Wortise Native Ad failed loaded");
                            }

                            @Override
                            public void onNativeClicked(@NonNull GoogleNativeAd googleNativeAd) {

                            }



                            @Override
                            public void onNativeImpression(@NonNull GoogleNativeAd googleNativeAd) {

                            }

                            @SuppressLint("InflateParams")
                            @Override
                            public void onNativeLoaded(@NonNull GoogleNativeAd googleNativeAd, @NonNull com.google.android.gms.ads.nativead.NativeAd nativeAd) {
                                LayoutInflater inflater = LayoutInflater.from(context);
                                NativeAdView adView;
                                switch (nativeAdStyle) {
                                    case AdsConstant.STYLE_NEWS:
                                    case AdsConstant.STYLE_MEDIUM:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_news_template_view, null);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_SMALL:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_small_template_view, null);
                                        break;
                                    case AdsConstant.STYLE_VIDEO_LARGE:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_video_large_template_view, null);
                                        break;
                                    case AdsConstant.STYLE_RADIO:
                                    case AdsConstant.STYLE_SMALL:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_radio_template_view, null);
                                        break;
                                    default:
                                        adView = (NativeAdView) inflater.inflate(R.layout.gnt_wortise_medium_template_view, null);
                                        break;
                                }
                                populateNativeAdView(context, nativeAd, adView, darkTheme, nativeBackgroundDark, nativeBackgroundLight);
                                wortiseNativeAd.removeAllViews();
                                wortiseNativeAd.addView(adView);
                                wortiseNativeAd.setVisibility(View.VISIBLE);
                                nativeAdViewContainer.setVisibility(View.VISIBLE);
                                Log.d(TAG, "[Backup] Wortise Native Ad loaded");
                            }
                        });
                        mGoogleNativeAd.load();
                    } else {
                        Log.d(TAG, "[Backup] Wortise Native Ad has been loaded");
                    }
                    break;

                case NONE:
                    nativeAdViewContainer.setVisibility(View.GONE);
                    break;

            }
        }
    }


    public void setNativeAdPadding(int left, int top, int right, int bottom) {
        nativeAdViewContainer.setPadding(left, top, right, bottom);
    }

    public void setNativeAdMargin(int left, int top, int right, int bottom) {
        setMargins(nativeAdViewContainer, left, top, right, bottom);
    }

    public void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

    public void setNativeAdBackgroundResource(int drawableBackground) {
        nativeAdViewContainer.setBackgroundResource(drawableBackground);
    }

    public void setNativeAdBackgroundColor(Context context, boolean darkTheme, int nativeBackgroundLight, int nativeBackgroundDark) {
        if (darkTheme) {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundDark));
        } else {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundLight));
        }
    }

    public MaxNativeAdView createNativeAdView(Context context, String nativeStyles) {
        MaxNativeAdViewBinder binder;
        switch (nativeStyles) {
            case AdsConstant.STYLE_NEWS:
            case AdsConstant.STYLE_MEDIUM:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_news_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            case AdsConstant.STYLE_RADIO:
            case AdsConstant.STYLE_SMALL:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_radio_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            case AdsConstant.STYLE_VIDEO_LARGE:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_video_large_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            case AdsConstant.STYLE_VIDEO_SMALL:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_video_small_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            default:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_medium_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
        }
        return new MaxNativeAdView(binder, context);
    }

    public MaxNativeAdView createNativeAdViewDark(Context context, String nativeStyles) {
        MaxNativeAdViewBinder binder;
        switch (nativeStyles) {
            case AdsConstant.STYLE_NEWS:
            case AdsConstant.STYLE_MEDIUM:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_news_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            case AdsConstant.STYLE_RADIO:
            case AdsConstant.STYLE_SMALL:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_radio_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            case AdsConstant.STYLE_VIDEO_LARGE:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_video_large_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            case AdsConstant.STYLE_VIDEO_SMALL:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_video_small_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
            default:
                binder = new MaxNativeAdViewBinder.Builder(R.layout.gnt_applovin_dark_medium_template_view)
                        .setTitleTextViewId(R.id.title_text_view)
                        .setBodyTextViewId(R.id.body_text_view)
                        .setAdvertiserTextViewId(R.id.advertiser_textView)
                        .setIconImageViewId(R.id.icon_image_view)
                        .setMediaContentViewGroupId(R.id.media_view_container)
                        .setOptionsContentViewGroupId(R.id.ad_options_view)
                        .setCallToActionButtonId(R.id.cta_button)
                        .build();
                break;
        }
        return new MaxNativeAdView(binder, context);
    }

    public void populateNativeAdView(Context context, com.google.android.gms.ads.nativead.NativeAd nativeAd, NativeAdView nativeAdView, boolean darkTheme, int nativeBackgroundDark, int nativeBackgroundLight) {

        if (darkTheme) {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundDark));
            nativeAdView.findViewById(R.id.background).setBackgroundResource(nativeBackgroundDark);
        } else {
            nativeAdViewContainer.setBackgroundColor(ContextCompat.getColor(context, nativeBackgroundLight));
            nativeAdView.findViewById(R.id.background).setBackgroundResource(nativeBackgroundLight);
        }

        nativeAdView.setMediaView(nativeAdView.findViewById(R.id.media_view));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.primary));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.cta));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.icon));

        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd.getHeadline());
        nativeAdView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }

        nativeAdView.setNativeAd(nativeAd);
    }

}
