package remotex.com.remotewebview;

import static remotex.com.remotewebview.WebActivity.hasPermissions;
import static remotex.com.remotewebview.constants.AllowOnlyHostUrlInApp;
import static remotex.com.remotewebview.constants.ChangeBottombarBgColor;
import static remotex.com.remotewebview.constants.ChangeDrawerHeaderBgColor;
import static remotex.com.remotewebview.constants.ChangeHeaderTextColor;
import static remotex.com.remotewebview.constants.ChangeTittleTextColor;
import static remotex.com.remotewebview.constants.ChangeToolbarBgColor;
import static remotex.com.remotewebview.constants.EnableWelcomeSlider;
import static remotex.com.remotewebview.constants.ForceUpdate;
import static remotex.com.remotewebview.constants.NewVersion;
import static remotex.com.remotewebview.constants.Notifx_service;
import static remotex.com.remotewebview.constants.OnesigID;
import static remotex.com.remotewebview.constants.ServerUrl;
import static remotex.com.remotewebview.constants.ShowAdmobInterstitial;
import static remotex.com.remotewebview.constants.ShowBottomBar;
import static remotex.com.remotewebview.constants.ShowDrawer;
import static remotex.com.remotewebview.constants.ShowServerUrlSetUp;
import static remotex.com.remotewebview.constants.ShowToolbar;
import static remotex.com.remotewebview.constants.ShowWebBtn;
import static remotex.com.remotewebview.constants.ToolbarBgColor;
import static remotex.com.remotewebview.constants.ToolbarTitleText;
import static remotex.com.remotewebview.constants.ToolbarTitleTextColor;
import static remotex.com.remotewebview.constants.UpdateAvailable;
import static remotex.com.remotewebview.constants.UpdateMessage;
import static remotex.com.remotewebview.constants.UpdateTitle;
import static remotex.com.remotewebview.constants.UpdateUrl;
import static remotex.com.remotewebview.constants.Web_button_Img_link;
import static remotex.com.remotewebview.constants.Web_button_link;
import static remotex.com.remotewebview.constants.bottomBtn1ImgUrl;
import static remotex.com.remotewebview.constants.bottomBtn2ImgUrl;
import static remotex.com.remotewebview.constants.bottomBtn3ImgUrl;
import static remotex.com.remotewebview.constants.bottomBtn4ImgUrl;
import static remotex.com.remotewebview.constants.bottomBtn5ImgUrl;
import static remotex.com.remotewebview.constants.bottomBtn6ImgUrl;
import static remotex.com.remotewebview.constants.bottomUrl1;
import static remotex.com.remotewebview.constants.bottomUrl2;
import static remotex.com.remotewebview.constants.bottomUrl3;
import static remotex.com.remotewebview.constants.bottomUrl4;
import static remotex.com.remotewebview.constants.bottomUrl5;
import static remotex.com.remotewebview.constants.bottomUrl6;
import static remotex.com.remotewebview.constants.drawerHeaderBgColor;
import static remotex.com.remotewebview.constants.drawerHeaderImgCommand;
import static remotex.com.remotewebview.constants.drawerHeaderImgUrl;
import static remotex.com.remotewebview.constants.drawerHeaderText;
import static remotex.com.remotewebview.constants.drawerHeaderTextColor;
import static remotex.com.remotewebview.constants.drawerMenuBtnUrl;
import static remotex.com.remotewebview.constants.drawerMenuImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem1ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem1Text;
import static remotex.com.remotewebview.constants.drawerMenuItem1Url;
import static remotex.com.remotewebview.constants.drawerMenuItem2ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem2Text;
import static remotex.com.remotewebview.constants.drawerMenuItem2Url;
import static remotex.com.remotewebview.constants.drawerMenuItem3ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem3Text;
import static remotex.com.remotewebview.constants.drawerMenuItem3Url;
import static remotex.com.remotewebview.constants.drawerMenuItem4ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem4Text;
import static remotex.com.remotewebview.constants.drawerMenuItem4Url;
import static remotex.com.remotewebview.constants.drawerMenuItem5ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem5Text;
import static remotex.com.remotewebview.constants.drawerMenuItem5Url;
import static remotex.com.remotewebview.constants.drawerMenuItem6ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem6Text;
import static remotex.com.remotewebview.constants.drawerMenuItem6Url;
import static remotex.com.remotewebview.constants.drawerMenuItem7ImgUrl;
import static remotex.com.remotewebview.constants.drawerMenuItem7Text;
import static remotex.com.remotewebview.constants.drawerMenuItem7Url;
import static remotex.com.remotewebview.constants.filterdomain;
import static remotex.com.remotewebview.constants.jsonUrl;
import static remotex.com.remotewebview.constants.screen1BgColor;
import static remotex.com.remotewebview.constants.screen1Desc;
import static remotex.com.remotewebview.constants.screen1Img;
import static remotex.com.remotewebview.constants.screen1TextColor;
import static remotex.com.remotewebview.constants.screen1TitleText;
import static remotex.com.remotewebview.constants.screen2BgColor;
import static remotex.com.remotewebview.constants.screen2Desc;
import static remotex.com.remotewebview.constants.screen2Img;
import static remotex.com.remotewebview.constants.screen2TextColor;
import static remotex.com.remotewebview.constants.screen2TitleText;
import static remotex.com.remotewebview.constants.screen3BgColor;
import static remotex.com.remotewebview.constants.screen3Desc;
import static remotex.com.remotewebview.constants.screen3Img;
import static remotex.com.remotewebview.constants.screen3TextColor;
import static remotex.com.remotewebview.constants.screen3TitleText;
import static remotex.com.remotewebview.constants.screen4BgColor;
import static remotex.com.remotewebview.constants.screen4Desc;
import static remotex.com.remotewebview.constants.screen4Img;
import static remotex.com.remotewebview.constants.screen4TextColor;
import static remotex.com.remotewebview.constants.screen4TitleText;
import static remotex.com.remotewebview.constants.splashScreenUrl;
import static remotex.com.remotewebview.constants.splashUrl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import remotex.com.remotewebview.additionalSettings.utils.Constants;


public class Splash extends AppCompatActivity {


    String Jsonurl = ServerUrl;

    TextView infotext;
    ProgressBar progressBar;
    Button retryBtn;
    ImageView splash_image;



    int clickcount = 0;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        ImageView view = findViewById(R.id.splash_image);
        infotext = findViewById(R.id.splash_sub);
        progressBar = findViewById(R.id.splash_progress);
        retryBtn = findViewById(R.id.retryntn);
        splash_image = findViewById(R.id.splash_image);



        Glide.with(getApplicationContext()).load(splashScreenUrl).fitCenter().into(splash_image);

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

        @SuppressLint("CommitPrefEdits")
        SharedPreferences simpleSavedPassword = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);

        String CheckForPassword = simpleSavedPassword.getString(Constants.onCreatePasswordSaved, "");

        if (CheckForPassword.isEmpty()) {
            SharedPreferences.Editor editor = simpleSavedPassword.edit();
            editor.putString(Constants.onCreatePasswordSaved, "onCreatePasswordSaved");
            editor.putString(Constants.simpleSavedPassword, "1234");
            editor.apply();

        }
        //   public static String ServerUrl = "https://zidsworld.com/appz/remotex/appConfig.json";

//        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_fadein);
//        animation.setInterpolator(new LinearInterpolator());
//        animation.setRepeatCount(Animation.INFINITE);
//        animation.setDuration(1300);
//
//
//
//        view.startAnimation(animation);
        int SPLASH_TIME_OUT = 1300;


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ApiCall(Splash.this, Jsonurl);
                progressBar.setVisibility(View.VISIBLE);

            }
        }, SPLASH_TIME_OUT);
    }





    public void ApiCall(Context context, String url) {

        infotext.setText(R.string.connecting);
        progressBar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
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
                            drawerMenuItem7ImgUrl = remoteJson.getString("DrawerMenuImg7Url"); // i added this

                            drawerMenuItem1Url = remoteJson.getString("DrawerMenuItem1Url");
                            drawerMenuItem2Url = remoteJson.getString("DrawerMenuItem2Url");
                            drawerMenuItem3Url = remoteJson.getString("DrawerMenuItem3Url");
                            drawerMenuItem4Url = remoteJson.getString("DrawerMenuItem4Url");
                            drawerMenuItem5Url = remoteJson.getString("DrawerMenuItem5Url");
                            drawerMenuItem6Url = remoteJson.getString("DrawerMenuItem6Url");
                            drawerMenuItem7Url = remoteJson.getString("DrawerMenuItem7Url");


                            drawerMenuItem1Text = remoteJson.getString("DrawerMenuItem1Title");
                            drawerMenuItem2Text = remoteJson.getString("DrawerMenuItem2Title");
                            drawerMenuItem3Text = remoteJson.getString("DrawerMenuItem3Title");
                            drawerMenuItem4Text = remoteJson.getString("DrawerMenuItem4Title");
                            drawerMenuItem5Text = remoteJson.getString("DrawerMenuItem5Title");
                            drawerMenuItem6Text = remoteJson.getString("DrawerMenuItem6Title");
                            drawerMenuItem7Text = remoteJson.getString("DrawerMenuItem7Title");

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


                            if (URLUtil.isValidUrl(homeurl)) {
                                jsonUrl = homeurl;

                                try {
                                    URI uri = new URI(homeurl);
                                    String domain = uri.getHost();
                                    filterdomain = domain;
                                } catch ( URISyntaxException e ) {
                                    e.printStackTrace();
                                }

                                if (EnableWelcomeSlider) {
                                    Intent myactivity = new Intent(Splash.this, WelcomeSlider.class);
                                    startActivity(myactivity);
                                    finish();
                                } else {
                                    Intent myactivity = new Intent(Splash.this, WebActivity.class);
                                    myactivity.putExtra("url", jsonUrl);
                                    startActivity(myactivity);
                                    finish();
                                }


                            } else {
                                infotext.setText(R.string.invalide_remote_data);
                                progressBar.setVisibility(View.GONE);
                                if (retryBtn.getVisibility() == View.GONE) {
                                    retryBtn.setVisibility(View.VISIBLE);
                                }
                            }


                        } catch ( JSONException e ) {
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
            ShowServerUrlSetUp = true;
        } else {
            ApiCall(Splash.this, Jsonurl);
            if (retryBtn.getVisibility() == View.VISIBLE) {
                retryBtn.setVisibility(View.GONE);
            }
        }
    }

}

