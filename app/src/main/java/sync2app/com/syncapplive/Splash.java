package sync2app.com.syncapplive;

import static sync2app.com.syncapplive.WebActivity.hasPermissions;
import static sync2app.com.syncapplive.constants.AllowOnlyHostUrlInApp;
import static sync2app.com.syncapplive.constants.ChangeBottombarBgColor;
import static sync2app.com.syncapplive.constants.ChangeDrawerHeaderBgColor;
import static sync2app.com.syncapplive.constants.ChangeHeaderTextColor;
import static sync2app.com.syncapplive.constants.ChangeTittleTextColor;
import static sync2app.com.syncapplive.constants.ChangeToolbarBgColor;
import static sync2app.com.syncapplive.constants.EnableWelcomeSlider;
import static sync2app.com.syncapplive.constants.ForceUpdate;
import static sync2app.com.syncapplive.constants.NewVersion;
import static sync2app.com.syncapplive.constants.Notifx_service;
import static sync2app.com.syncapplive.constants.OnesigID;
// import static sync2app.com.syncapplive.constants.ServerUrl;
import static sync2app.com.syncapplive.constants.ShowAdmobInterstitial;
import static sync2app.com.syncapplive.constants.ShowBottomBar;
import static sync2app.com.syncapplive.constants.ShowDrawer;
import static sync2app.com.syncapplive.constants.ShowServerUrlSetUp;
import static sync2app.com.syncapplive.constants.ShowToolbar;
import static sync2app.com.syncapplive.constants.ShowWebBtn;
import static sync2app.com.syncapplive.constants.ToolbarBgColor;
import static sync2app.com.syncapplive.constants.ToolbarTitleText;
import static sync2app.com.syncapplive.constants.ToolbarTitleTextColor;
import static sync2app.com.syncapplive.constants.UpdateAvailable;
import static sync2app.com.syncapplive.constants.UpdateMessage;
import static sync2app.com.syncapplive.constants.UpdateTitle;
import static sync2app.com.syncapplive.constants.UpdateUrl;
import static sync2app.com.syncapplive.constants.Web_button_Img_link;
import static sync2app.com.syncapplive.constants.Web_button_link;
import static sync2app.com.syncapplive.constants.bottomBtn1ImgUrl;
import static sync2app.com.syncapplive.constants.bottomBtn2ImgUrl;
import static sync2app.com.syncapplive.constants.bottomBtn3ImgUrl;
import static sync2app.com.syncapplive.constants.bottomBtn4ImgUrl;
import static sync2app.com.syncapplive.constants.bottomBtn5ImgUrl;
import static sync2app.com.syncapplive.constants.bottomBtn6ImgUrl;
import static sync2app.com.syncapplive.constants.bottomUrl1;
import static sync2app.com.syncapplive.constants.bottomUrl2;
import static sync2app.com.syncapplive.constants.bottomUrl3;
import static sync2app.com.syncapplive.constants.bottomUrl4;
import static sync2app.com.syncapplive.constants.bottomUrl5;
import static sync2app.com.syncapplive.constants.bottomUrl6;
import static sync2app.com.syncapplive.constants.drawerHeaderBgColor;
import static sync2app.com.syncapplive.constants.drawerHeaderImgCommand;
import static sync2app.com.syncapplive.constants.drawerHeaderImgUrl;
import static sync2app.com.syncapplive.constants.drawerHeaderText;
import static sync2app.com.syncapplive.constants.drawerHeaderTextColor;
import static sync2app.com.syncapplive.constants.drawerMenuBtnUrl;
import static sync2app.com.syncapplive.constants.drawerMenuImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem1ImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem1Text;
import static sync2app.com.syncapplive.constants.drawerMenuItem1Url;
import static sync2app.com.syncapplive.constants.drawerMenuItem2ImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem2Text;
import static sync2app.com.syncapplive.constants.drawerMenuItem2Url;
import static sync2app.com.syncapplive.constants.drawerMenuItem3ImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem3Text;
import static sync2app.com.syncapplive.constants.drawerMenuItem3Url;
import static sync2app.com.syncapplive.constants.drawerMenuItem4ImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem4Text;
import static sync2app.com.syncapplive.constants.drawerMenuItem4Url;
import static sync2app.com.syncapplive.constants.drawerMenuItem5ImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem5Text;
import static sync2app.com.syncapplive.constants.drawerMenuItem5Url;
import static sync2app.com.syncapplive.constants.drawerMenuItem6ImgUrl;
import static sync2app.com.syncapplive.constants.drawerMenuItem6Text;
import static sync2app.com.syncapplive.constants.drawerMenuItem6Url;
import static sync2app.com.syncapplive.constants.filterdomain;
import static sync2app.com.syncapplive.constants.jsonUrl;
import static sync2app.com.syncapplive.constants.screen1BgColor;
import static sync2app.com.syncapplive.constants.screen1Desc;
import static sync2app.com.syncapplive.constants.screen1Img;
import static sync2app.com.syncapplive.constants.screen1TextColor;
import static sync2app.com.syncapplive.constants.screen1TitleText;
import static sync2app.com.syncapplive.constants.screen2BgColor;
import static sync2app.com.syncapplive.constants.screen2Desc;
import static sync2app.com.syncapplive.constants.screen2Img;
import static sync2app.com.syncapplive.constants.screen2TextColor;
import static sync2app.com.syncapplive.constants.screen2TitleText;
import static sync2app.com.syncapplive.constants.screen3BgColor;
import static sync2app.com.syncapplive.constants.screen3Desc;
import static sync2app.com.syncapplive.constants.screen3Img;
import static sync2app.com.syncapplive.constants.screen3TextColor;
import static sync2app.com.syncapplive.constants.screen3TitleText;
import static sync2app.com.syncapplive.constants.screen4BgColor;
import static sync2app.com.syncapplive.constants.screen4Desc;
import static sync2app.com.syncapplive.constants.screen4Img;
import static sync2app.com.syncapplive.constants.screen4TextColor;
import static sync2app.com.syncapplive.constants.screen4TitleText;
import static sync2app.com.syncapplive.constants.splashScreenUrl;
import static sync2app.com.syncapplive.constants.splashUrl;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import sync2app.com.syncapplive.additionalSettings.ReSyncActivity;
import sync2app.com.syncapplive.additionalSettings.autostartAppOncrash.Methods;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;
import sync2app.com.syncapplive.databinding.CustomHelperLayoutBinding;
import sync2app.com.syncapplive.databinding.CustomOfflinePopLayoutBinding;


public class Splash extends AppCompatActivity {

    String ServerUrl;
 //   String Jsonurl = ServerUrl;

    TextView infotext;
    ProgressBar progressBar;
    TextView retryBtn;
    TextView go_settings_Btn;
    TextView gotWifisettings;
    TextView goConnection;
    ImageView imageView34;
    ImageView imagwifi;
    ImageView imageView35;
    ImageView imagwifi2;

    ImageView splash_image;
    ImageView backgroundImage;
    ImageView imageHelper;

    int clickcount = 0;

    Handler handler;
    private ConnectivityReceiver connectivityReceiver;


    @SuppressLint({"NewApi", "MissingInflatedId", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    ServerUrl =   "https://sync2app.com/abc/123/admin_config/appConfig.json";


        SharedPreferences sharedLicenseKeys = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);

        ServerUrl = sharedLicenseKeys.getString(Constants.get_masterDomain , "");


        //add exception
        Methods.addExceptionHandler(this);



        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        } catch ( Exception e ) {
            e.printStackTrace();
        }


        SharedPreferences preferences1 = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences1.getBoolean("darktheme", false)) {
            setTheme(R.style.DarkTheme);
        }
        setContentView(R.layout.activity_splash);
//        preferences1 = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String name = preferences.getString("surl", "");

        if (name.equals("")) {

        } else {
            if (name.startsWith("http://") | name.startsWith("https://") & name.endsWith("json")) {
                ServerUrl = name;
                Log.d("Remote Execution", "Using custom server address");
            } else {
                Log.d("Remote Execution", "Invalid server url" + name);
            }

        }


        //THIS IS THE SPLASH ACTIVITY
//        rootlayout = findViewById(R.id.splash);
//        if (preferences.getBoolean("darktheme", false)) {
//            rootlayout.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));
//        }



        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor22 = sharedBiometric.edit();
        editor22.remove(Constants.img_Let_offline_load_Listner);
        editor22.apply();


        ImageView view = findViewById(R.id.splash_image);
        infotext = findViewById(R.id.splash_sub);
        progressBar = findViewById(R.id.splash_progress);
        retryBtn = findViewById(R.id.retryntn);
        go_settings_Btn = findViewById(R.id.go_settings_Btn);
        gotWifisettings = findViewById(R.id.gotWifisettings);
        goConnection = findViewById(R.id.goConnection);


        imageView34 = findViewById(R.id.imageView34);
        imagwifi = findViewById(R.id.imagwifi);
        imageView35 = findViewById(R.id.imageView35);
        imagwifi2 = findViewById(R.id.imagwifi2);
        splash_image = findViewById(R.id.splash_image);
        imageHelper = findViewById(R.id.imageHelper);

        String get_imgToggleImageBackground = sharedBiometric.getString(Constants.imgToggleImageBackground, "");
        String get_imageUseBranding = sharedBiometric.getString(Constants.imageUseBranding, "");
        if (get_imgToggleImageBackground.equals(Constants.imgToggleImageBackground) && get_imageUseBranding.equals(Constants.imageUseBranding) ){
            loadBackGroundImage();
        }

        if (get_imageUseBranding.equals(Constants.imageUseBranding)) {
            loadImage();
        }


        handler = new Handler(Looper.getMainLooper());
        imageHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToolHelpPiopUp();

                //   Toast.makeText(Splash.this, "Please wait", Toast.LENGTH_SHORT).show();
            }
        });


        //   retryBtn.setTooltipText("Clicking on this three times will redirect you to the settings page or reconnect if the app Internet comes back.");
        //  go_settings_Btn.setTooltipText("This will direct you to the settings page.");
        //  gotWifisettings.setTooltipText("Check your WiFi connection by clicking on this button.");
        //   goConnection.setTooltipText("This means that there is no network. You don't have an internet connection");


        int deepBlue = getResources().getColor(R.color.white);
        int deepRed = getResources().getColor(R.color.red);

        // Create ObjectAnimator for text color change
        ObjectAnimator colorAnimator = ObjectAnimator.ofInt(goConnection, "textColor", deepBlue, deepRed);


        colorAnimator.setEvaluator(new ArgbEvaluator());
        colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator.setDuration(900); // Adjust the duration as needed

        colorAnimator.start();


        // Create ObjectAnimator for color change
        ObjectAnimator colorAnimator22 = ObjectAnimator.ofInt(imagwifi2, "colorFilter", deepBlue, deepRed);


        colorAnimator22.setEvaluator(new ArgbEvaluator());
        colorAnimator22.setRepeatCount(ValueAnimator.INFINITE);
        colorAnimator22.setRepeatMode(ValueAnimator.REVERSE);
        colorAnimator22.setDuration(900); // Adjust the duration as needed
        colorAnimator22.start();


        go_settings_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                finish();
                Toast.makeText(Splash.this, "Please wait", Toast.LENGTH_SHORT).show();
            }
        });


        gotWifisettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);
                Toast.makeText(Splash.this, "Please wait", Toast.LENGTH_SHORT).show();
            }
        });


        String splashLoadStatus = preferences.getString("splashStarted", null);
        String filename = "splash.png";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File splashImg = new File(path + File.separator + filename);


        if (hasPermissions(Splash.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (splashImg.length() == 0) {

                Log.d("Remote Execution", "Invalid splash img");
            } else {
                try {
                    if (splashLoadStatus.equals("finished")) {
                        Drawable drw = Drawable.createFromPath(splashImg.getAbsolutePath());
                        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
                        view.setImageDrawable(drw);
                    }
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }

        }


//        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_fadein);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setDuration(1300);
//
//
//
//        view.startAnimation(animation);
/*        int SPLASH_TIME_OUT = 1300;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ApiCall(Splash.this, Jsonurl);
                progressBar.setVisibility(View.VISIBLE);

            }
        }, SPLASH_TIME_OUT);
        */


    }


    private void loadImage() {
        splash_image = findViewById(R.id.splash_image);
        SharedPreferences sharedP = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);
        String getFolderClo = sharedP.getString(Constants.getFolderClo, "").toString();
        String getFolderSubpath = sharedP.getString(Constants.getFolderSubpath, "").toString();

        String pathFolder = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Constants.App + "/" + "Config";
        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + Constants.Syn2AppLive + "/" + pathFolder;

        String fileTypes = "app_logo.png";
        File file = new File(folder, fileTypes);

        if (file.exists()) {
            Glide.with(this).load(file).centerCrop().into(splash_image);
        }
    }

    private void loadBackGroundImage() {


        backgroundImage = findViewById(R.id.backgroundImage);
        SharedPreferences sharedP = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);
        String getFolderClo = sharedP.getString(Constants.getFolderClo, "").toString();
        String getFolderSubpath = sharedP.getString(Constants.getFolderSubpath, "").toString();

        String pathFolder = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Constants.App + "/" + "Config";
        String folder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/" + Constants.Syn2AppLive + "/" + pathFolder;

        String fileTypes =  "app_background.png";
        File file = new File(folder, fileTypes);

        if (file.exists()) {
            Glide.with(this).load(file).centerCrop().into(backgroundImage);
        }
    }




    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    private static int clearCacheFolder(final File dir) {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory()) {
            try {
                for (File child : dir.listFiles()) {
                    // First delete subdirectories recursively
                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child);
                    }

                    if (child.delete()) {
                        deletedFiles++;
                    }
                }
            } catch ( Exception e ) {
                // Log the exception
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }


    public void ApiCall(Context context, String url) {

        infotext.setText(R.string.connecting);
        progressBar.setVisibility(View.VISIBLE);


        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        infotext.setText(R.string.initializing);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject remoteJson = jsonObject.getJSONObject("remoteConfig");

                            String homeurl = remoteJson.getString("homeUrl");

                            //BOTTOM BAR
                            ShowBottomBar = remoteJson.getBoolean("ShowBottomBar");
                            ChangeBottombarBgColor = remoteJson.getBoolean("ChangeBottomBarBgColor");
                            constants.bottomBarBgColor = remoteJson.getString("bottomBarBackgroundColor");

                            //Bottom Menu Actions
                            bottomUrl1 = remoteJson.getString("bottom1");
                            bottomUrl2 = remoteJson.getString("bottom2");
                            bottomUrl3 = remoteJson.getString("bottom3");
                            bottomUrl4 = remoteJson.getString("bottom4");
                            bottomUrl5 = remoteJson.getString("bottom5");
                            bottomUrl6 = remoteJson.getString("bottom6");

                            //Bottom Menu icons
                            bottomBtn1ImgUrl = remoteJson.getString("bottom1_img_url");
                            bottomBtn2ImgUrl = remoteJson.getString("bottom2_img_url");
                            bottomBtn3ImgUrl = remoteJson.getString("bottom3_img_url");
                            bottomBtn4ImgUrl = remoteJson.getString("bottom4_img_url");
                            bottomBtn5ImgUrl = remoteJson.getString("bottom5_img_url");
                            bottomBtn6ImgUrl = remoteJson.getString("bottom6_img_url");

                            //DRAWER MENU
                            ChangeDrawerHeaderBgColor = remoteJson.getBoolean("ChangeDrawerHeaderColor");
                            ChangeHeaderTextColor = remoteJson.getBoolean("ChangeDrawerHeaderTextColor");

                            ShowDrawer = remoteJson.getBoolean("ShowDrawerMenu");
                            drawerMenuBtnUrl = remoteJson.getString("DrawerMenuUrl");
                            drawerMenuImgUrl = remoteJson.getString("DrawerMenuImgUrl");

                            drawerMenuItem1ImgUrl = remoteJson.getString("DrawerMenuImg1Url");
                            drawerMenuItem2ImgUrl = remoteJson.getString("DrawerMenuImg2Url");
                            drawerMenuItem3ImgUrl = remoteJson.getString("DrawerMenuImg3Url");
                            drawerMenuItem4ImgUrl = remoteJson.getString("DrawerMenuImg4Url");
                            drawerMenuItem5ImgUrl = remoteJson.getString("DrawerMenuImg5Url");
                            drawerMenuItem6ImgUrl = remoteJson.getString("DrawerMenuImg6Url");

                            drawerMenuItem1Url = remoteJson.getString("DrawerMenuItem1Url");
                            drawerMenuItem2Url = remoteJson.getString("DrawerMenuItem2Url");
                            drawerMenuItem3Url = remoteJson.getString("DrawerMenuItem3Url");
                            drawerMenuItem4Url = remoteJson.getString("DrawerMenuItem4Url");
                            drawerMenuItem5Url = remoteJson.getString("DrawerMenuItem5Url");
                            drawerMenuItem6Url = remoteJson.getString("DrawerMenuItem6Url");


                            drawerMenuItem1Text = remoteJson.getString("DrawerMenuItem1Title");
                            drawerMenuItem2Text = remoteJson.getString("DrawerMenuItem2Title");
                            drawerMenuItem3Text = remoteJson.getString("DrawerMenuItem3Title");
                            drawerMenuItem4Text = remoteJson.getString("DrawerMenuItem4Title");
                            drawerMenuItem5Text = remoteJson.getString("DrawerMenuItem5Title");
                            drawerMenuItem6Text = remoteJson.getString("DrawerMenuItem6Title");

                            drawerHeaderImgUrl = remoteJson.getString("DrawerHeaderImgUrl");
                            drawerHeaderText = remoteJson.getString("DrawerHeaderText");
                            drawerHeaderImgCommand = remoteJson.getString("DrawerHeaderImgCommand");
                            drawerHeaderBgColor = remoteJson.getString("DrawerHeaderBgColor");
                            drawerHeaderTextColor = remoteJson.getString("DrawerHeaderTextColor");


                            //TOOLBAR
                            ShowToolbar = remoteJson.getBoolean("ShowToolbar");
                            ToolbarTitleText = remoteJson.getString("ToolbarTitleText");
                            ToolbarTitleTextColor = remoteJson.getString("ToolbarTitleTextColor");
                            ToolbarBgColor = remoteJson.getString("ToolbarBgColor");

                            ChangeToolbarBgColor = remoteJson.getBoolean("ChangeToolbarBgColor");
                            ChangeTittleTextColor = remoteJson.getBoolean("ChangeToolbarTitleTextColor");


                            //FLOATING BUTTON
                            Web_button_link = remoteJson.getString("webBtnUrl");
                            Web_button_Img_link = remoteJson.getString("webBtnImgUrl");
                            ShowWebBtn = remoteJson.getBoolean("ShowWebBtn");


                            //ADS
                            constants.ShowAdmobBanner = remoteJson.getBoolean("admobBanner");
                            ShowAdmobInterstitial = remoteJson.getBoolean("admobInter");

                            //Notifications
                            OnesigID = remoteJson.getString("onesigID");
                            splashUrl = remoteJson.getString("splashUrl");
                            Notifx_service = remoteJson.getBoolean("NotifXService");

                            //MORE
                            ShowServerUrlSetUp = remoteJson.getBoolean("AllowChangingServerUrl");
                            AllowOnlyHostUrlInApp = remoteJson.getBoolean("allowOnlyHostUrl");


                            //App Update
                            UpdateAvailable = remoteJson.getBoolean("UpdateAvailable");
                            ForceUpdate = remoteJson.getBoolean("ForceUpdate");
                            UpdateTitle = remoteJson.getString("Updatetitle");
                            UpdateMessage = remoteJson.getString("UpdateMsg");
                            UpdateUrl = remoteJson.getString("UpdateUrl");
                            NewVersion = remoteJson.getString("NewVersion");

//                            WELCOME SCREEN
                            EnableWelcomeSlider = remoteJson.getBoolean("AllowWelcomeSlider");

                            //screen title texts
                            screen1TitleText = remoteJson.getString("Screen1Title");
                            screen2TitleText = remoteJson.getString("Screen2Title");
                            screen3TitleText = remoteJson.getString("Screen3Title");
                            screen4TitleText = remoteJson.getString("Screen4Title");

                            //screen desc texts
                            screen1Desc = remoteJson.getString("screen1Desc");
                            screen2Desc = remoteJson.getString("screen2Desc");
                            screen3Desc = remoteJson.getString("screen3Desc");
                            screen4Desc = remoteJson.getString("screen4Desc");

                            //screen BG colors
                            screen1BgColor = remoteJson.getString("Screen1bgColor");
                            screen2BgColor = remoteJson.getString("Screen2bgColor");
                            screen3BgColor = remoteJson.getString("Screen3bgColor");
                            screen4BgColor = remoteJson.getString("Screen4bgColor");

                            //screen Text colors
                            screen1TextColor = remoteJson.getString("Screen1TxtColor");
                            screen2TextColor = remoteJson.getString("Screen2TxtColor");
                            screen3TextColor = remoteJson.getString("Screen3TxtColor");
                            screen4TextColor = remoteJson.getString("Screen4TxtColor");

                            //screen Text colors
                            screen1Img = remoteJson.getString("Screen1ImgUrl");
                            screen2Img = remoteJson.getString("Screen2ImgUrl");
                            screen3Img = remoteJson.getString("Screen3ImgUrl");
                            screen4Img = remoteJson.getString("Screen4ImgUrl");


                            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);

                            if (URLUtil.isValidUrl(homeurl)) {
                                jsonUrl = homeurl;

                                try {
                                    URI uri = new URI(homeurl);
                                    String domain = uri.getHost();
                                    filterdomain = domain;
                                    Log.d("WebActivity", "shouldOverrideUrlLoading: " + domain);

                                } catch ( URISyntaxException e ) {
                                    e.printStackTrace();
                                }

                                if (EnableWelcomeSlider) {
                                    Intent myactivity = new Intent(Splash.this, WelcomeSlider.class);
                                    startActivity(myactivity);
                                    finish();
                                } else {


                                    String getTvMode = sharedBiometric.getString(Constants.CALL_RE_SYNC_MANGER, "");

                                    if (getTvMode.equals(Constants.CALL_RE_SYNC_MANGER)) {
                                        Intent myactivity = new Intent(Splash.this, ReSyncActivity.class);
                                        myactivity.putExtra("url", jsonUrl);
                                        startActivity(myactivity);
                                        finish();
                                    } else {
                                        Intent myactivity = new Intent(Splash.this, WebActivity.class);
                                        myactivity.putExtra("url", jsonUrl);
                                        startActivity(myactivity);
                                        finish();
                                        Toast.makeText(context, "Webby", Toast.LENGTH_SHORT).show();

                                    }

                                }


                            } else {
                                infotext.setText(R.string.invalide_remote_data);
                                progressBar.setVisibility(View.GONE);
                                if (retryBtn.getVisibility() == View.GONE) {
                                    retryBtn.setVisibility(View.VISIBLE);
                                }

                                if (go_settings_Btn.getVisibility() == View.GONE) {
                                    go_settings_Btn.setVisibility(View.VISIBLE);
                                }

                                if (gotWifisettings.getVisibility() == View.GONE) {
                                    gotWifisettings.setVisibility(View.VISIBLE);
                                }

                                if (goConnection.getVisibility() == View.GONE) {
                                    goConnection.setVisibility(View.VISIBLE);
                                }


                                if (imageView34.getVisibility() == View.GONE) {
                                    imageView34.setVisibility(View.VISIBLE);
                                }
                                if (imagwifi.getVisibility() == View.GONE) {
                                    imagwifi.setVisibility(View.VISIBLE);
                                }

                                if (imageView35.getVisibility() == View.GONE) {
                                    imageView35.setVisibility(View.VISIBLE);
                                }
                                if (imagwifi2.getVisibility() == View.GONE) {
                                    imagwifi2.setVisibility(View.VISIBLE);
                                }
                                if (imageHelper.getVisibility() == View.GONE) {
                                    imageHelper.setVisibility(View.VISIBLE);
                                }


                            }


                        } catch (
                                JSONException e ) {
                            e.printStackTrace();
                            infotext.setText(e.getMessage());
                        }

                    }


                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
                infotext.setText("Error occurred! =" + error.toString());
                progressBar.setVisibility(View.GONE);

                if (retryBtn.getVisibility() == View.GONE) {
                    retryBtn.setVisibility(View.VISIBLE);
                }

                if (go_settings_Btn.getVisibility() == View.GONE) {
                    go_settings_Btn.setVisibility(View.VISIBLE);
                }

                if (goConnection.getVisibility() == View.GONE) {
                    goConnection.setVisibility(View.VISIBLE);
                }

                if (gotWifisettings.getVisibility() == View.GONE) {
                    gotWifisettings.setVisibility(View.VISIBLE);
                }


                if (imageView34.getVisibility() == View.GONE) {
                    imageView34.setVisibility(View.VISIBLE);
                }
                if (imagwifi.getVisibility() == View.GONE) {
                    imagwifi.setVisibility(View.VISIBLE);
                }

                if (imageView35.getVisibility() == View.GONE) {
                    imageView35.setVisibility(View.VISIBLE);
                }

                if (imagwifi2.getVisibility() == View.GONE) {
                    imagwifi2.setVisibility(View.VISIBLE);
                }


                if (imageHelper.getVisibility() == View.GONE) {
                    imageHelper.setVisibility(View.VISIBLE);
                }


            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }

    public void retryCall(View view) {
        clickcount++;
        if (clickcount == 3) {

            Intent myactivity = new Intent(Splash.this, SettingsActivity.class);
            startActivity(myactivity);
            finish();
            ShowServerUrlSetUp = true;
        } else {
            ApiCall(Splash.this, ServerUrl);
            if (retryBtn.getVisibility() == View.VISIBLE) {
                retryBtn.setVisibility(View.GONE);
            }

            if (go_settings_Btn.getVisibility() == View.VISIBLE) {
                go_settings_Btn.setVisibility(View.GONE);
            }

            if (gotWifisettings.getVisibility() == View.VISIBLE) {
                gotWifisettings.setVisibility(View.GONE);
            }

            if (goConnection.getVisibility() == View.VISIBLE) {
                goConnection.setVisibility(View.GONE);
            }


            if (imageView34.getVisibility() == View.VISIBLE) {
                imageView34.setVisibility(View.GONE);
            }
            if (imagwifi.getVisibility() == View.VISIBLE) {
                imagwifi.setVisibility(View.GONE);
            }

            if (imageView35.getVisibility() == View.VISIBLE) {
                imageView35.setVisibility(View.GONE);


            }
            if (imagwifi2.getVisibility() == View.VISIBLE) {
                imagwifi2.setVisibility(View.GONE);
            }


            if (imageHelper.getVisibility() == View.VISIBLE) {
                imageHelper.setVisibility(View.GONE);
            }
        }
    }

    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    private void showToolHelpPiopUp() {

        CustomHelperLayoutBinding binding = CustomHelperLayoutBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        final AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setCancelable(true);


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        // TextView textDescription = binding.textDescription;

        //  textDescription.setText(message);


        alertDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
        connectivityReceiver = new ConnectivityReceiver();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(connectivityReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //  unregisterReceiver(connectivityReceiver);


    }

    public class ConnectivityReceiver extends BroadcastReceiver {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

                    try {

                        int SPLASH_TIME_OUT = 1300;

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    ApiCall(Splash.this, ServerUrl);
                                    progressBar.setVisibility(View.VISIBLE);


                                    retryBtn.setVisibility(View.GONE);
                                    go_settings_Btn.setVisibility(View.GONE);
                                    gotWifisettings.setVisibility(View.GONE);
                                    goConnection.setVisibility(View.GONE);
                                    imageView34.setVisibility(View.GONE);
                                    imagwifi.setVisibility(View.GONE);
                                    imageView35.setVisibility(View.GONE);
                                    imagwifi2.setVisibility(View.GONE);

                                    splash_image.setVisibility(View.VISIBLE);
                                    imageHelper.setVisibility(View.GONE);


                                } catch ( Exception e ) {
                                }

                            }
                        }, SPLASH_TIME_OUT);


                    } catch ( Exception ignored ) {
                    }


                } else {

                    // No internet Connection
                    try {
                        infotext.setText("No Internet Connection");
                        progressBar.setVisibility(View.GONE);


                        retryBtn.setVisibility(View.VISIBLE);
                        go_settings_Btn.setVisibility(View.VISIBLE);
                        gotWifisettings.setVisibility(View.VISIBLE);
                        goConnection.setVisibility(View.VISIBLE);
                        imageView34.setVisibility(View.VISIBLE);
                        imagwifi.setVisibility(View.VISIBLE);
                        imageView35.setVisibility(View.VISIBLE);
                        imagwifi2.setVisibility(View.VISIBLE);

                        splash_image.setVisibility(View.VISIBLE);
                        imageHelper.setVisibility(View.VISIBLE);


                    } catch ( Exception e ) {
                    }

                }

                // No internet Connection


            } catch ( Exception ignored ) {
            }
        }

    }


}

