package com.marketdigitalcode.multiads;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.marketdigitalcode.adslibrary.AdsType;
import com.marketdigitalcode.adslibrary.BuildConfig;
import com.marketdigitalcode.adslibrary.MdcBanner;
import com.marketdigitalcode.uti.AppConstant;
import com.marketdigitalcode.uti.AppTools;
import com.marketdigitalcode.uti.SharedPref;

import java.util.Map;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> handleIntentActivityResult;
    private ActivityResultLauncher<String> requestPermissionLauncher;

    public static String[] requiredPermissions = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public static String[] requiredPermissionsTIRAMISU = {
            android.Manifest.permission.READ_MEDIA_IMAGES
    };
    ActivityResultLauncher<String[]> multiplePermissionLauncher;

    AppCompatActivity activity;
    TextView text_hello;
    public static Uri dirDown, dirInstall, rootFile;
    AdsType.Initialize adNetwork;
    MdcBanner mdcBanner;
    SharedPref sharedPref;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
        getAppTheme();
        setContentView(R.layout.activity_second);
        activity = SecondActivity.this;
        text_hello = findViewById(R.id.text_hello);
        loadBannerAd();
        initToolbar();
    }
    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Second Activity");
        }
    }
    private void initAds() {
        adNetwork = new AdsType.Initialize(this)
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        mdcBanner.destroyAndDetachBanner();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void getAppTheme() {
        if (sharedPref.getIsDarkTheme()) {
            setTheme(R.style.AppDarkTheme);
        } else {
            setTheme(R.style.AppTheme);
        }
    }

}