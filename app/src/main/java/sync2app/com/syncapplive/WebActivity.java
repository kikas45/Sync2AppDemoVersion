package sync2app.com.syncapplive;


import static sync2app.com.syncapplive.AdvancedControls.showToast;
import static sync2app.com.syncapplive.constants.AllowOnlyHostUrlInApp;
import static sync2app.com.syncapplive.constants.ChangeBottombarBgColor;
import static sync2app.com.syncapplive.constants.ChangeDrawerHeaderBgColor;
import static sync2app.com.syncapplive.constants.ChangeHeaderTextColor;
import static sync2app.com.syncapplive.constants.ChangeTittleTextColor;
import static sync2app.com.syncapplive.constants.ChangeToolbarBgColor;
import static sync2app.com.syncapplive.constants.CurrVersion;
import static sync2app.com.syncapplive.constants.ForceUpdate;
import static sync2app.com.syncapplive.constants.NewVersion;
import static sync2app.com.syncapplive.constants.NotifAvailable;
import static sync2app.com.syncapplive.constants.NotifLinkExternal;
import static sync2app.com.syncapplive.constants.NotifSound;
import static sync2app.com.syncapplive.constants.Notif_ID;
import static sync2app.com.syncapplive.constants.Notif_Img_url;
import static sync2app.com.syncapplive.constants.Notif_Shown;
import static sync2app.com.syncapplive.constants.Notif_button_action;
import static sync2app.com.syncapplive.constants.Notif_desc;
import static sync2app.com.syncapplive.constants.Notif_title;
import static sync2app.com.syncapplive.constants.Notifx_service;
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
import static sync2app.com.syncapplive.constants.bottomBarBgColor;
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
import static sync2app.com.syncapplive.constants.isAppOpen;
import static sync2app.com.syncapplive.constants.jsonUrl;
import static sync2app.com.syncapplive.constants.splashUrl;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;


import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import sync2app.com.syncapplive.QrPages.QRSanActivity;
import sync2app.com.syncapplive.additionalSettings.myService.MyDownloadMangerClass;
import sync2app.com.syncapplive.additionalSettings.myService.SyncInterval;
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils;
import sync2app.com.syncapplive.databinding.CustomNotificationLayoutBinding;
import sync2app.com.syncapplive.databinding.CustomOfflinePopLayoutBinding;
import sync2app.com.syncapplive.glidetovectoryou.GlideToVectorYou;
import sync2app.com.syncapplive.glidetovectoryou.GlideToVectorYouListener;


public class WebActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    public static final int FILECHOOSER_RESULTCODE = 5173;
    private static final String TAG = WebActivity.class.getSimpleName();
    private final static int FCR = 1;
    public ValueCallback<Uri> mUploadMessage;
    //Adjusting Rating bar popup timeframe
    private final static int DAYS_UNTIL_PROMPT = 0;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 5;//Min number of app launches
    public static boolean openblobPdfafterDownload = true;

    public static boolean ChangeListener = false;
    public static boolean storagecamrequest = false;
    public LinearLayout errorlayout;
    public Context mContext;

    // private ConnectivityReceiver connectivityReceiver;

    private CountDownTimer countdownTimer;
    //  private CountDownTimer countdownTimer222;
    private long remainingTime = 0;


    private ConnectivityReceiver connectivityReceiver;

    private Handler myHandler;

    protected ObservableWebView webView;
    RelativeLayout drawer_menu;
    public View.OnClickListener imgClk = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @SuppressLint("NonConstantResourceId")
        @Override
        public void onClick(View v) {
            final AlphaAnimation buttonClick = new AlphaAnimation(0.1F, 0.4F);
            v.startAnimation(buttonClick);
            switch (v.getId()) {
                case R.id.bottomtoolbar_btn_1:
//                    List valid = Arrays.asList("UP", "DOWN", "RIGHT", "LEFT", "up", "down", "right", "left");

                    HandleRemoteCommand(bottomUrl1);
                    break;

                case R.id.bottomtoolbar_btn_2:
                    HandleRemoteCommand(bottomUrl2);
                    break;

                case R.id.bottomtoolbar_btn_3:
                    HandleRemoteCommand(bottomUrl3);
                    break;

                case R.id.bottomtoolbar_btn_4:
                    HandleRemoteCommand(bottomUrl4);
                    break;

                case R.id.bottomtoolbar_btn_5:
                    HandleRemoteCommand(bottomUrl5);
                    break;

                case R.id.bottomtoolbar_btn_6:
                    HandleRemoteCommand(bottomUrl6);
                    break;

                case R.id.drawer_menu_Btn:
                    HandleRemoteCommand(drawerMenuBtnUrl);
                    break;


                case R.id.drawer_item_1:
                    HandleRemoteCommand(drawerMenuItem1Url);
                    ShowHideViews(drawer_menu);
                    break;

                case R.id.drawer_item_2:
                    HandleRemoteCommand(drawerMenuItem2Url);
                    ShowHideViews(drawer_menu);
                    break;

                case R.id.drawer_item_3:
                    HandleRemoteCommand(drawerMenuItem3Url);
                    ShowHideViews(drawer_menu);
                    break;

                case R.id.drawer_item_4:
                    HandleRemoteCommand(drawerMenuItem4Url);
                    ShowHideViews(drawer_menu);
                    break;

                case R.id.drawer_item_5:
                    HandleRemoteCommand(drawerMenuItem5Url);
                    ShowHideViews(drawer_menu);
                    break;

                case R.id.drawer_item_6:
                    HandleRemoteCommand(drawerMenuItem6Url);
                    ShowHideViews(drawer_menu);
                    break;

                case R.id.drawer_headerImg:
                    HandleRemoteCommand(drawerHeaderImgCommand);
                    ShowHideViews(drawer_menu);
                    break;
            }
        }
    };
    LinearLayout urllayout;
//    Toolbar toolbar;

    LinearLayout bottomToolBar;
    ProgressBar tbarprogress;
    ProgressBar HorizontalProgressBar;
    ProgressDialog progressDialog;
    FrameLayout horizontalProgressFramelayout;
    Intent UrlIntent;
    Uri data;
    float dX;
    float dY;
    int lastAction;
    String currentDownloadFileName;
    String currentDownloadFileMimeType;

    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,

    };


    //Progress
    boolean ShowHorizontalProgress = false; //shows a horizontal progressbar
    boolean ShowToolbarProgress = false; // show rotating progressbar on toolbar (unstable)
    boolean ShowProgressDialogue = false; // the main progress dialogue
    boolean ShowSimpleProgressBar = true; //  a simple progressbar
    boolean ShowNativeLoadView = false; // a progress style like native loading, it may have bugs
    boolean EnableSwipeRefresh = false; // pull to refresh
    //Ads
    boolean ShowBannerAds = constants.ShowAdmobBanner; // if set true, this will show admob banner ads
    boolean ShowInterstitialAd = constants.ShowAdmobInterstitial;


    //Menus, Toolbar etc
    boolean ShowOptionMenu = false; //Top right Menu
    boolean ShowToolbar = constants.ShowToolbar; /*Header toolbar. If this set false, option menu and drawer menus will
    also be hidden,
    but drawer menu still will be accessible by swiping right*/
    boolean ShowDrawer = constants.ShowDrawer; // hide or show side navigation drawer, this will disable side navigation drawer menu
    boolean ShowBottomBar = constants.ShowBottomBar;//show bottom navigation
    boolean ShowHideBottomBarOnScroll = false;//hide or show bottom navigation onscroll
    //Functions
    boolean UseInappDownloader = false; //(has bugs since android 10) if true, all downloads will process inside app.
    // if false, it will use system download manager
    boolean AllowRating = false;  //Show a rating popup to your customers after opening app several times
    boolean ClearCacheOnExit = false; // Clear all cache upon exiting app
    boolean AskToExit = false; //Shows App Exit Dialogue
    boolean BlockAds = false; //Value overrides in Settings, Experimental, if true, this should disable some ads in web pages. This has bugs, be careful
    boolean AllowGPSLocationAccess = false;
    boolean RequestRunTimePermissions = false;
    boolean LoadLastWebPageOnAccidentalExit;
    boolean OpenFileAfterDownload = true;
    boolean AutoHideToolbar = false;
    boolean SupportMultiWindows = true;
    boolean ShowWebButton = ShowWebBtn; //whatsapp button example
    //========================================
    //SET YOUR WEBSITE URL in constants class under Elements folder
    String MainUrl = jsonUrl;

    //BottomToolbar Image Buttons
    ImageView bottomToolbar_img_1;
    ImageView bottomToolbar_img_2;
    ImageView bottomToolbar_img_3;
    ImageView bottomToolbar_img_4;
    ImageView bottomToolbar_img_5;
    ImageView bottomToolbar_img_6;
    ImageView bottomtoolbar_btn_7;
    ImageView imageWiFiOn;
    ImageView imageWiFiOFF;
    RelativeLayout x_toolbar;
    ConstraintLayout bottom_server_layout;
    ImageView drawer_menu_btn;
    LinearLayout drawerItem1;
    LinearLayout drawerItem2;
    LinearLayout drawerItem3;
    LinearLayout drawerItem4;
    LinearLayout drawerItem5;
    LinearLayout drawerItem6;
    LinearLayout drawerItem7;
    ImageView drawerImg1;
    ImageView drawerImg2;
    ImageView drawerImg3;
    ImageView drawerImg4;
    ImageView drawerImg5;
    ImageView drawerImg6;
    TextView drawerItemtext1;
    TextView drawerItemtext2;
    TextView drawerItemtext3;
    TextView drawerItemtext4;
    TextView drawerItemtext5;
    TextView drawerItemtext6;
    ImageView drawer_header_img;
    TextView drawer_header_text;
    LinearLayout drawerHeaderBg;
    Handler handler = new Handler();
    Runnable runnable;
    TextView toolbartitleText;
    TextView textSyncMode;
    // TextView textSystemState;
    TextView textSynIntervals;
    TextView countDownTime;
    TextView textLocation;
    TextView textStatusProcess;

    private WebView mWebviewPop;
    private AdView mAdView;
    private SwipeRefreshLayout swipeView;

    private EditText urlEdittext;
    private SharedPreferences preferences;
    private ProgressBar simpleProgressbar;
    private View mCustomView;
    private int mOriginalSystemUiVisibility;
    private int mOriginalOrientation;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    private InterstitialAd mInterstitialAd;
    private RelativeLayout windowContainer;
    private ProgressBar windowProgressbar;
    private RelativeLayout mContainer;
    private String mCM;
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private ProgressDialog mProgressDialog;
    private SharedPreferences prefs;
    private Dialog mydialog;
    private RatingBar ratingbar;
    private String lasturl;
    private ImageView web_button;
    private ImageView imageCirclGreenOnline;
    private ImageView imageCircleBlueOffline;
    // private LinearLayout web_button_root_layout; /// change
    //  private ConstraintLayout webx_layout; /// change

    private TextView errorCode;
    private TextView errorautoConnect;
    ImageButton errorReloadButton;


    private PowerManager powerManager;
    private PowerManager.WakeLock wakeLock;


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {


                    return false;
                }
            }
        }

        return true;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface", "ClickableViewAccessibility", "WakelockTimeout"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darktheme", false)) {

            setTheme(R.style.DarkTheme);
        }


        setContentView(R.layout.webactivity_layout);

        myHandler = new Handler(Looper.getMainLooper());

        mContext = WebActivity.this;
        //  AdvancedControls.CompletionReciever(WebActivity.this);

        mContainer = findViewById(R.id.web_container);
        windowContainer = findViewById(R.id.window_container);
        windowProgressbar = findViewById(R.id.WindowProgressBar);

        data = getIntent().getData();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefs = mContext.getSharedPreferences("apprater", 0);
        UrlIntent = getIntent();

        bottomToolBar = findViewById(R.id.bottom_toolbar_container);
        progressDialog = new ProgressDialog(WebActivity.this);
        simpleProgressbar = findViewById(R.id.SimpleProgressBar);
        horizontalProgressFramelayout = findViewById(R.id.frameLayoutHorizontalProgress);
        x_toolbar = findViewById(R.id.x_toolbar);
        errorlayout = findViewById(R.id.errorLayout);
        errorCode = findViewById(R.id.errorinfo);
        errorautoConnect = findViewById(R.id.autoreconnect);
        errorReloadButton = findViewById(R.id.ErrorReloadButton);
        drawer_menu = findViewById(R.id.native_drawer_menu);
        drawer_menu_btn = findViewById(R.id.drawer_menu_Btn);
        bottom_server_layout = findViewById(R.id.bottom_server_layout);

        mAdView = findViewById(R.id.adView);


        HorizontalProgressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);
        swipeView = findViewById(R.id.swipeLayout);
        urlEdittext = findViewById(R.id.urledittextbox);
        urllayout = findViewById(R.id.urllayoutroot);

        web_button = findViewById(R.id.web_button);

        // webx_layout = findViewById(R.id.webx_layout);
        bottomToolbar_img_1 = findViewById(R.id.bottomtoolbar_btn_1);
        bottomToolbar_img_2 = findViewById(R.id.bottomtoolbar_btn_2);
        bottomToolbar_img_3 = findViewById(R.id.bottomtoolbar_btn_3);
        bottomToolbar_img_4 = findViewById(R.id.bottomtoolbar_btn_4);
        bottomToolbar_img_5 = findViewById(R.id.bottomtoolbar_btn_5);
        bottomToolbar_img_6 = findViewById(R.id.bottomtoolbar_btn_6);
        bottomtoolbar_btn_7 = findViewById(R.id.bottomtoolbar_btn_7);
        imageCircleBlueOffline = findViewById(R.id.imageCircleBlueOffline);
        imageCirclGreenOnline = findViewById(R.id.imageCirclGreenOnline);

        MyApplication.incrementRunningActivities();

        bottomtoolbar_btn_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowHideViews(drawer_menu);
            }
        });


        drawerItem1 = findViewById(R.id.drawer_item_1);
        drawerItem2 = findViewById(R.id.drawer_item_2);
        drawerItem3 = findViewById(R.id.drawer_item_3);
        drawerItem4 = findViewById(R.id.drawer_item_4);
        drawerItem5 = findViewById(R.id.drawer_item_5);
        drawerItem6 = findViewById(R.id.drawer_item_6);

        drawerItem7 = findViewById(R.id.drawer_item_7);


        ImageView scroolToEnd = findViewById(R.id.scroolToEnd);
        ImageView scroolToStart = findViewById(R.id.scroolTostart);
        final HorizontalScrollView horizontalScrollView = findViewById(R.id.horizontalScrollView2);

        scroolToEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int scrollAmount = (int) (horizontalScrollView.getChildAt(0).getWidth() * 0.2);
                horizontalScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        horizontalScrollView.smoothScrollBy(scrollAmount, 0);
                    }
                });
            }
        });

        scroolToStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int scrollAmount = (int) (horizontalScrollView.getChildAt(0).getWidth() * 0.30);
                horizontalScrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        horizontalScrollView.smoothScrollBy(-scrollAmount, 0);
                    }
                });
            }
        });


        drawerItem7.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View view) {
                ShowHideViews(drawer_menu);
                Intent intent = new Intent(getApplicationContext(), QRSanActivity.class);
                startActivity(intent);
            }
        });


        drawerImg1 = findViewById(R.id.drawer_item_img_1);
        drawerImg2 = findViewById(R.id.drawer_item_img_2);
        drawerImg3 = findViewById(R.id.drawer_item_img_3);
        drawerImg4 = findViewById(R.id.drawer_item_img_4);
        drawerImg5 = findViewById(R.id.drawer_item_img_5);
        drawerImg6 = findViewById(R.id.drawer_item_img_6);


        drawerItemtext1 = findViewById(R.id.drawer_item_text_1);
        drawerItemtext2 = findViewById(R.id.drawer_item_text_2);
        drawerItemtext3 = findViewById(R.id.drawer_item_text_3);
        drawerItemtext4 = findViewById(R.id.drawer_item_text_4);
        drawerItemtext5 = findViewById(R.id.drawer_item_text_5);
        drawerItemtext6 = findViewById(R.id.drawer_item_text_6);

        drawer_header_img = findViewById(R.id.drawer_headerImg);
        drawer_header_text = findViewById(R.id.drawer_header_text);
        drawerHeaderBg = findViewById(R.id.drawerheaderBg);
        toolbartitleText = findViewById(R.id.toolbarTitleText);
        textSyncMode = findViewById(R.id.textSyncMode);
        textSynIntervals = findViewById(R.id.textSynIntervals);
        countDownTime = findViewById(R.id.countDownTime);
        textLocation = findViewById(R.id.textLocation);
        textStatusProcess = findViewById(R.id.textStatusProcess);

        imageWiFiOFF = findViewById(R.id.imageWiFiOFF);
        imageWiFiOn = findViewById(R.id.imageWiFiOn);


        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (drawer_menu.isShown()) {
                    AnimateHide(drawer_menu);
                    drawer_menu.setVisibility(View.GONE);
                    webView.setAlpha(1);
                }
                return false;
            }
        });


        powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YourApp::MyWakelockTag");
        wakeLock.acquire();

        web_button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onClick(View v) {
                // Handle click event here
                final AlphaAnimation buttonClick = new AlphaAnimation(0.1F, 0.4F);
                v.startAnimation(buttonClick);
                HandleRemoteCommand(Web_button_link);
            }
        });


        web_button.setOnTouchListener(new View.OnTouchListener() {
            float dX, dY;
            int lastAction;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        v.setY(event.getRawY() + dY);
                        v.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        // Delay before performing click action
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (lastAction == MotionEvent.ACTION_DOWN) {
                                    v.performClick();
                                }
                            }
                        }, 300); // Adjust delay time as needed
                        break;

                    default:
                        return false;
                }
                return true;


            }

        });


        swipeView.setEnabled(false);
        swipeView.setRefreshing(false);

        InitializeRemoteData();
        InitiatePreferences();
        InitiateComponents();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InitiatePermissions(); // <---Permissions can be added / removed in this method, also remove / add in manifest
                CheckUpdate();

            }
        }, 8000);

        InitiateAds();

        if (Notifx_service) {

            isAppOpen = true;


            startService(new Intent(this, RemotexNotifier.class));
            IntentFilter filter = new IntentFilter("notifx_ready");
            BroadcastReceiver receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {


                    try {
                        if (!Notif_Shown) {
                            showNotifxDialog(WebActivity.this);
                        }

                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }

                }
            };
            mContext.registerReceiver(receiver, filter);
        }


        //// Iniliazing the webview

        try {
            showToast(mContext, Constants.Launching_Content);

            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedBiometric.edit();
            editor.remove(Constants.CALL_RE_SYNC_MANGER);
            editor.apply();


            String use_offline = sharedBiometric.getString(Constants.USE_OFFLINE_FOLDER, "");


            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (use_offline.equals(Constants.USE_OFFLINE_FOLDER)) {

                if (networkInfo != null && networkInfo.isConnected()) {
                    online_Load_Webview_Logic();
                } else {

                    offline_Folder_Loagic();
                }

            } else {

                online_Load_Webview_Logic();
            }


        } catch ( Exception e ) {
        }


    }

    private void offline_Folder_Loagic() {

        try {
            SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);


            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);

            String use_offline = sharedBiometric.getString(Constants.USE_OFFLINE_FOLDER, "");

            String fil_CLO = my_DownloadClass.getString(Constants.getFolderClo, "");
            String fil_DEMO = my_DownloadClass.getString(Constants.getFolderSubpath, "");


            if (use_offline.equals(Constants.USE_OFFLINE_FOLDER)) {

                if (!fil_CLO.isEmpty() && !fil_DEMO.isEmpty()) {
                    loadOffline_Saved_Path_Offline_Webview(fil_CLO, fil_DEMO);
                    // showToast(mContext, "Launching CLO/DEMo... when offline folder checked");
                } else {


                    showPopForTVConfiguration(Constants.UnableToFindIndex);
                    // showToast(mContext, "Pop for ... when offline folder checked");

                }
            }
        } catch ( Exception e ) {
        }


    }

    private void online_Load_Webview_Logic() {
        try {


            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
            SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);

            String get_AppMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");
            String imgAllowLunchFromOnline = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "");

            String imagSwtichEnableManualOrNot = sharedBiometric.getString(Constants.imagSwtichEnableManualOrNot, "");

            String getSaved_manaul_index_edit_url_Input = my_DownloadClass.getString(Constants.getSaved_manaul_index_edit_url_Input, "");


            String Tapped_OnlineORoffline = my_DownloadClass.getString(Constants.Tapped_OnlineORoffline, "");

            String syncUrl = my_DownloadClass.getString(Constants.syncUrl, "");
            String fil_CLO = my_DownloadClass.getString(Constants.getFolderClo, "");
            String fil_DEMO = my_DownloadClass.getString(Constants.getFolderSubpath, "");


            if (imgAllowLunchFromOnline.equals(Constants.imgAllowLunchFromOnline)) {

                try {
                    if (Tapped_OnlineORoffline.equals(Constants.tapped_launchOnline)) {

                        // let check if manual url was enabled
                        if (imagSwtichEnableManualOrNot.equals(Constants.imagSwtichEnableManualOrNot)) {

                            if (!getSaved_manaul_index_edit_url_Input.isEmpty()) {
                                load_Launch_Online_Mode(getSaved_manaul_index_edit_url_Input);
                            } else {
                                showPopForTVConfiguration(Constants.badRequest);
                                showToast(mContext, "Bad manual url");

                            }

                        } else {


                            // No manual url is allowed
                            // showToast(mContext, "launched online checked to launch single url appended");
                            if (!fil_CLO.isEmpty() && !fil_DEMO.isEmpty()) {

                                String url = my_DownloadClass.getString(Constants.get_ModifiedUrl, "");
                                String imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "");

                                if (imagSwtichPartnerUrl.equals(Constants.imagSwtichPartnerUrl)) {
                                    String vurl = "https://cp.cloudappserver.co.uk/app_base/public/" + fil_CLO + "/" + fil_DEMO + "/App/index.html";
                                    load_Launch_Online_Mode(vurl);

                                } else {
                                    String appended_url = url + "/" + fil_CLO + "/" + fil_DEMO + "/App/index.html";
                                    load_Launch_Online_Mode(appended_url);
                                }

                            } else {
                                showPopForTVConfiguration(Constants.badRequest);
                                //  showToast(mContext, "single url appended 22");
                            }
                        }


                    } else if (Tapped_OnlineORoffline.equals(Constants.tapped_launchOffline)) {
                        //   showToast(mContext, "launched online checked to launch path from storage");
                        loadOffline_Saved_Path_Offline_Webview(fil_CLO, fil_DEMO);
                    } else {


                        if (fil_CLO.isEmpty() && fil_DEMO.isEmpty() && syncUrl.isEmpty()) {

                            if (get_AppMode.equals(Constants.TV_Mode)) {
                                showPopForTVConfiguration(Constants.compleConfiguration);
                                //  showToast(mContext, "Tv mode111111");
                            } else if (Tapped_OnlineORoffline.equals(Constants.tapped_launchOffline) || Tapped_OnlineORoffline.equals(Constants.tapped_launchOnline)) {
                                showPopForTVConfiguration(Constants.compleConfiguration);
                                //    showToast(mContext, " Tv mode222222");
                            } else {
                                loadTheMainWebview();
                                // showPopForTVConfiguration();
                                //    showToast(mContext, "Tv mode3333");
                            }
                        } else {
                            //   showToast(mContext,"no congig no img");

                            loadTheMainWebview();
                        }


                    }


                } catch ( Exception e ) {
                }

            } else {

                try {


                    if (Tapped_OnlineORoffline.equals(Constants.tapped_launchOnline)) {
                        // showToast(mContext, "launched not checked, we start original URl ");

                        // let check if manual url was enabled
                        if (imagSwtichEnableManualOrNot.equals(Constants.imagSwtichEnableManualOrNot)) {

                            if (!getSaved_manaul_index_edit_url_Input.isEmpty()) {
                                load_Launch_Online_Mode(getSaved_manaul_index_edit_url_Input);
                            } else {
                                showPopForTVConfiguration(Constants.badRequest);
                                showToast(mContext, "Bad manual url");

                            }

                        } else {

                            if (!fil_CLO.isEmpty() && !fil_DEMO.isEmpty()) {
                                // String vurl = "https://cp.cloudappserver.co.uk/app_base/public/" + fil_CLO + "/" + fil_DEMO + "/App/index.html";
                                // load_Launch_Online_Mode(vurl);

                                String url = my_DownloadClass.getString(Constants.get_ModifiedUrl, "");
                                String imagSwtichPartnerUrl = sharedBiometric.getString(Constants.imagSwtichPartnerUrl, "");

                                if (imagSwtichPartnerUrl.equals(Constants.imagSwtichPartnerUrl)) {
                                    String vurl = "https://cp.cloudappserver.co.uk/app_base/public/" + fil_CLO + "/" + fil_DEMO + "/App/index.html";
                                    load_Launch_Online_Mode(vurl);

                                } else {
                                    String appended_url = url + "/" + fil_CLO + "/" + fil_DEMO + "/App/index.html";
                                    load_Launch_Online_Mode(appended_url);
                                }


                            } else {
                                showPopForTVConfiguration(Constants.UnableToFindIndex);
                            }

                        }


                    } else if (Tapped_OnlineORoffline.equals(Constants.tapped_launchOffline)) {
                        //  showToast(mContext, "launched not checked, Starting with path CLO/DEMO...");
                        loadOffline_Saved_Path_Offline_Webview(fil_CLO, fil_DEMO);
                    } else {

                        // loadTheMainWebview();


                        if (fil_CLO.isEmpty() && fil_DEMO.isEmpty() && syncUrl.isEmpty()) {

                            if (get_AppMode.equals(Constants.TV_Mode)) {
                                showPopForTVConfiguration(Constants.compleConfiguration);
                                // showToast(mContext, "img pop Tv mode111111");

                            } else if (Tapped_OnlineORoffline.equals(Constants.tapped_launchOffline) || Tapped_OnlineORoffline.equals(Constants.tapped_launchOnline)) {
                                showPopForTVConfiguration(Constants.compleConfiguration);
                                //  showToast(mContext, "img pop Tv mode222222");
                            } else {
                                loadTheMainWebview();
                                // showPopForTVConfiguration();
                                // showToast(mContext, "img pop Tv mode3333");
                            }
                        } else {
                            loadTheMainWebview();
                            //  showToast(mContext, "No config yet .. Original Url ");
                        }


                    }


                } catch ( Exception e ) {
                }


            }

        } catch ( Exception e ) {
        }
    }


    private void load_Launch_Online_Mode(String url) {


        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);


        webSettings.setDatabaseEnabled(false);
        webSettings.setDomStorageEnabled(false);
        webSettings.setSupportZoom(false);

        webSettings.setUserAgentString(webSettings.getUserAgentString().replace("wv", ""));
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(false);


        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowFileAccess(false);

        if (SupportMultiWindows) {
            webSettings.setSupportMultipleWindows(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        }

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollViewCallbacks(this);
        webView.setSaveEnabled(true);


        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView.setWebViewClient(new AdvancedWebViewClient());
        webView.setWebChromeClient(new AdvancedWebChromeClient());
        webView.setDownloadListener(new Downloader());

        WebView.setWebContentsDebuggingEnabled(true);

        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearSslPreferences();
        webView.clearView();
        webView.clearMatches();


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String get_imagShowOnlineStatus = sharedBiometric.getString(Constants.imagShowOnlineStatus, "");


        if (networkInfo != null && networkInfo.isConnected()) {
            webView.loadUrl(url);

            if (!get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)) {
                imageCirclGreenOnline.setVisibility(View.VISIBLE);
                imageCircleBlueOffline.setVisibility(View.INVISIBLE);

            } else {
                imageCirclGreenOnline.setVisibility(View.INVISIBLE);
                imageCircleBlueOffline.setVisibility(View.INVISIBLE);
            }

        } else {
            showPopForTVConfiguration(Constants.badRequest);
        }


    }

    private void loadTheMainWebview() {

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);


        webSettings.setDatabaseEnabled(false);
        webSettings.setDomStorageEnabled(false);
        webSettings.setSupportZoom(false);

        webSettings.setUserAgentString(webSettings.getUserAgentString().replace("wv", ""));
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(false);

        webSettings.setJavaScriptEnabled(true);

        webSettings.setAllowFileAccess(false);

        if (SupportMultiWindows) {
            webSettings.setSupportMultipleWindows(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        }

        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setScrollViewCallbacks(this);
        webView.setSaveEnabled(false);


        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView.setWebViewClient(new AdvancedWebViewClient());
        webView.setWebChromeClient(new AdvancedWebChromeClient());
        webView.setDownloadListener(new Downloader());

        WebView.setWebContentsDebuggingEnabled(true);

        webView.clearCache(true);
        webView.clearHistory();
        webView.clearFormData();
        webView.clearSslPreferences();
        webView.clearView();
        webView.clearMatches();

        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String get_imagShowOnlineStatus = sharedBiometric.getString(Constants.imagShowOnlineStatus, "");

        if (!get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)) {
            imageCirclGreenOnline.setVisibility(View.VISIBLE);
            imageCircleBlueOffline.setVisibility(View.INVISIBLE);

        } else {
            imageCirclGreenOnline.setVisibility(View.INVISIBLE);
            imageCircleBlueOffline.setVisibility(View.INVISIBLE);

        }

        if (UrlIntent.hasExtra("url")) {
            webView.loadUrl(Objects.requireNonNull(getIntent().getStringExtra("url")));
        } else if (data != null) {
            webView.loadUrl(data.toString());
        } else {

            if (LoadLastWebPageOnAccidentalExit) {
                String lurl = preferences.getString("lasturl", "");
                if (((lurl.startsWith("http") | lurl.startsWith("https")))) {
                    webView.loadUrl(lurl);
                } else {
                    webView.loadUrl(MainUrl);
                }
            } else {
                webView.loadUrl(MainUrl);
            }
        }

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void loadOffline_Saved_Path_Offline_Webview(String CLO, String DEMO) {
        try {

            simpleProgressbar.setVisibility(View.GONE);

            String filename = "/index.html";

            String finalFolderPathDesired = "/" + CLO + "/" + DEMO + "/" + Constants.App;

            String destinationFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Syn2AppLive/" + finalFolderPathDesired;

            String filePath = "file://" + destinationFolder + filename;


            File myFile = new File(destinationFolder, File.separator + filename);

            if (myFile.exists()) {
                webView = findViewById(R.id.webview);

                WebSettings webSettings = webView.getSettings();
                webSettings.setJavaScriptEnabled(true);
                webSettings.setSupportZoom(true);

                webSettings.setAllowFileAccess(true);
                webSettings.setAllowContentAccess(true);
                webSettings.setDomStorageEnabled(false);
                webSettings.setDatabaseEnabled(false);

                webSettings.setMediaPlaybackRequiresUserGesture(false);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

                webSettings.setLoadWithOverviewMode(true);
                webSettings.setUseWideViewPort(true);
                webSettings.setLoadsImagesAutomatically(true);

                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
                webView.setWebViewClient(new WebActivity.AdvancedWebViewClient());
                webView.setWebChromeClient(new WebActivity.AdvancedWebChromeClient());
                webView.setDownloadListener(new Downloader());

                webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

                WebView.setWebContentsDebuggingEnabled(true);

                webView.clearCache(true);
                webView.clearHistory();
                webView.clearFormData();
                webView.clearSslPreferences();
                webView.clearView();
                webView.clearMatches();


                webView.loadUrl(filePath);


                SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
                String get_imagShowOnlineStatus = sharedBiometric.getString(Constants.imagShowOnlineStatus, "");

                if (!get_imagShowOnlineStatus.equals(Constants.imagShowOnlineStatus)) {
                    imageCirclGreenOnline.setVisibility(View.INVISIBLE);
                    imageCircleBlueOffline.setVisibility(View.VISIBLE);
                }else {
                    imageCirclGreenOnline.setVisibility(View.INVISIBLE);
                    imageCircleBlueOffline.setVisibility(View.INVISIBLE);

                }


            } else {


                SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
                String use_offline = sharedBiometric.getString(Constants.USE_OFFLINE_FOLDER, "");
                String get_AppMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");


                SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);
                String Tapped_OnlineORoffline = my_DownloadClass.getString(Constants.Tapped_OnlineORoffline, "");


                if (use_offline.equals(Constants.USE_OFFLINE_FOLDER) || get_AppMode.equals(Constants.TV_Mode) || Tapped_OnlineORoffline.equals(Constants.tapped_launchOffline)) {
                    showPopForTVConfiguration(Constants.UnableToFindIndex);
                } else {
                    loadTheMainWebview();
                }


            }


        } catch ( Exception e ) {
        }

    }

    public void showNotifxDialog(final Context context) {


        String lastNotifxId = preferences.getString("lastId", "");
        if (NotifAvailable & !lastNotifxId.matches(Notif_ID)) {
            try {


                CustomNotificationLayoutBinding binding = CustomNotificationLayoutBinding.inflate(getLayoutInflater());
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setView(binding.getRoot());

                final AlertDialog dialog = builder.create();

                dialog.setCanceledOnTouchOutside(false);
                dialog.setCancelable(false);

                // Set the background of the AlertDialog to be transparent
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }


                TextView notifTitle = binding.notifTitle;
                TextView notifDesc = binding.notifDesc;
                TextView closeThis = binding.closeNotif;
                TextView notifButton = binding.notifActionButton;
                ImageView imageView = binding.notifImg;


                closeThis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });

                notifButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Notif_button_action.startsWith("https") | Notif_button_action.startsWith("https")) {

                            if (NotifLinkExternal) {

                                webView.loadUrl(Notif_button_action);

                            } else {
                                redirectStore(Notif_button_action);
                            }

                            dialog.dismiss();
                            dialog.cancel();
                        } else if (Notif_button_action.matches("dismiss")) {
                            dialog.dismiss();
                            dialog.cancel();
                        }
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });

                notifTitle.setText(Notif_title);
                notifDesc.setText(Html.fromHtml(Notif_desc));

                Glide.with(context)
                        .load(Notif_Img_url) // image url
                        .placeholder(R.drawable.img_logo_icon) // any placeholder to load at start
                        .error(R.drawable.img_logo_icon)  // any image in case of error
                        .into(imageView);  // imageview object


                dialog.setCancelable(false);


                if (NotifSound) {
                    MediaPlayer mp = MediaPlayer.create(context, R.raw.alertx);
                    mp.setVolume((float) 0.1, (float) 0.1);
                    mp.start();
                }


                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("lastId", Notif_ID).apply();

                Notif_Shown = true;


                try {
                    dialog.show();
                } catch ( Exception e ) {
                    e.printStackTrace();
                }

            } catch ( Exception e ) {
                e.printStackTrace();
            }

        }

    }


    private void CheckUpdate() {

        try {
            CurrVersion = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();

        }

        if (UpdateAvailable & !CurrVersion.equals(NewVersion)) {
            UpdateApp(UpdateUrl, ForceUpdate);
            Log.d("RemoteConfig", "-- Update available - current version is --" + CurrVersion + "- remote version is --" + NewVersion);

        } else {
            Log.d("RemoteConfig", "No Update or version are equal ");
        }


    }


    private void InitializeRemoteData() {

        bottomToolbar_img_1.setOnClickListener(imgClk);
        bottomToolbar_img_2.setOnClickListener(imgClk);
        bottomToolbar_img_3.setOnClickListener(imgClk);
        bottomToolbar_img_4.setOnClickListener(imgClk);
        bottomToolbar_img_5.setOnClickListener(imgClk);
        bottomToolbar_img_6.setOnClickListener(imgClk);

        ConfigureRemoteImageData(bottomBtn1ImgUrl, bottomToolbar_img_1);
        ConfigureRemoteImageData(bottomBtn2ImgUrl, bottomToolbar_img_2);
        ConfigureRemoteImageData(bottomBtn3ImgUrl, bottomToolbar_img_3);
        ConfigureRemoteImageData(bottomBtn4ImgUrl, bottomToolbar_img_4);
        ConfigureRemoteImageData(bottomBtn5ImgUrl, bottomToolbar_img_5);
        ConfigureRemoteImageData(bottomBtn6ImgUrl, bottomToolbar_img_6);

        ConfigureRemoteImageData(Web_button_Img_link, web_button);


        if (ShowDrawer) {
            ConfigureRemoteImageData(drawerMenuImgUrl, drawer_menu_btn);


            ConfigureRemoteImageData(drawerMenuItem2ImgUrl, drawerImg2);
            ConfigureRemoteImageData(drawerMenuItem3ImgUrl, drawerImg3);
            ConfigureRemoteImageData(drawerMenuItem4ImgUrl, drawerImg4);
            ConfigureRemoteImageData(drawerMenuItem5ImgUrl, drawerImg5);
            ConfigureRemoteImageData(drawerMenuItem6ImgUrl, drawerImg6);
            ConfigureRemoteImageData(drawerMenuItem1ImgUrl, drawerImg1);

            ConfigureRemoteImageData(drawerHeaderImgUrl, drawer_header_img);

        }
    }

    private void ConfigureRemoteImageData(String url, ImageView view) {

//        showToast(mContext,view.toString()+" "+url);
        try {
            if (url == null | url.equals("null"))
                return;
        } catch ( Exception e ) {
            e.printStackTrace();
        }


        try {
            if (url.endsWith("svg")) {
                GlideToVectorYou
                        .init()
                        .with(this)
                        .withListener(new GlideToVectorYouListener() {
                            @Override
                            public void onLoadFailed() {
                            }

                            @Override
                            public void onResourceReady() {
                            }
                        })
                        .setPlaceHolder(R.drawable.demo_btn_24, R.drawable.demo_btn_24)

                        .load(Uri.parse(url), view);

            } else {
                Glide.with(this)
                        .load(url) // image url
                        .placeholder(R.drawable.demo_btn_24) // any placeholder to load at start
                        .error(R.drawable.demo_btn_24)  // any image in case of error
                        .into(view);  // imageview object
            }
        } catch ( Exception e ) {
            e.printStackTrace();

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void HandleRemoteCommand(String command) {


        if (command.equals("openSettings")) {

            //    webView.loadUrl("about:blank");
            webView.stopLoading();
            webView.destroy();

            Intent myactivity = new Intent(WebActivity.this, SettingsActivity.class);
            startActivity(myactivity);
            finish();
            showToast(mContext, "Please wait..");


        } else if (command.equals("webGoBack")) {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                showToast(mContext, "No back page");
            }
        } else if (command.equals("webGoForward")) {
            if (webView.canGoForward()) {
                webView.goForward();
            } else {
                showToast(mContext, "No forward page");
            }
        } else if (command.equals("reload")) {
            webView.reload();
        } else if (command.equals("sharePage")) {
            ShareItem(webView.getOriginalUrl(), "Check Out This!", " ");
        } else if (command.equals("goHome")) {
            webView.loadUrl(jsonUrl);

        } else if (command.equals("openDrawer")) {
            ShowHideViews(drawer_menu);

        } else if (command.equals("ExitApp")) {
            finish();

        } else if (command.equals("ScanCode")) {
            Intent intent = new Intent(getApplicationContext(), QRSanActivity.class);
            startActivity(intent);
            finish();
        } else if (command.equals("null")) {

        } else {

            webView.loadUrl(command);
        }
    }

    private void ShowHideViews(View Myview) {
        if (Myview.getVisibility() == View.GONE) {
            AnimateShow(Myview);
            Myview.setVisibility(View.VISIBLE);
            webView.setAlpha(0.5F);


        } else if (Myview.getVisibility() == View.VISIBLE) {

            AnimateHide(Myview);
            Myview.setVisibility(View.GONE);
            webView.setAlpha(1);


        }

    }

    private void TryRating() {
        if (preferences.getBoolean("dontshowagain", false)) {
            return;
        }


        SharedPreferences.Editor editor = prefs.edit();
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);
        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch) {
                showRateDialog();
            }
        }
        editor.apply();
    }

    private void showRateDialog() {
        mydialog = new Dialog(this);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                if (fromUser) {
                    ratingBar.setRating((float) Math.ceil(rating));
                }
            }
        });
        mydialog.show();
    }

    @Override
    public void onPause() {
        isAppOpen = false;

        try {
            if (myHandler != null) {
                myHandler.removeCallbacks(runnableDn);
            }
        } catch ( Exception ignored ) {
            // Ignore the exception
        }

        super.onPause();
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {


        try {
            if (myHandler != null) {
                myHandler.removeCallbacks(runnableDn);
            }

            TextView textDownladByes = findViewById(R.id.textDownladByes);
            ProgressBar progressBarPref = findViewById(R.id.progressBarPref);
            MyDownloadMangerClass downloadManager = new MyDownloadMangerClass();

            downloadManager.getDownloadStatus(progressBarPref, textDownladByes, getApplicationContext());


            if (myHandler != null) {
                myHandler.postDelayed(runnableDn, 500);
            }


            connectivityReceiver = new ConnectivityReceiver();

            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectivityReceiver, intentFilter);


        } catch ( Exception e ) {
            e.printStackTrace();
        }


        try {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateSyncView();
                }
            }, 1000);


            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        } catch ( Exception e ) {
        }


        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constants.SEND_SERVICE_NOTIFY);
            registerReceiver(broadcastReceiver, intentFilter);


            IntentFilter filter333 = new IntentFilter(Constants.RECIVER_PROGRESS);
            registerReceiver(Reciver_Progress, filter333);

            IntentFilter filter22 = new IntentFilter(Constants.SEND_UPDATE_TIME_RECIEVER);
            registerReceiver(Send_Time_Update_Reciver, filter22);


        } catch ( Exception e ) {
        }


        try {


            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);

            isAppOpen = true;

            if (jsonUrl == null) {
                Intent intent = new Intent(mContext, Splash.class);
                startActivity(intent);
                finish();
            }


            String getToHideQRCode = sharedBiometric.getString(Constants.HIDE_QR_CODE, "");
            String get_drawer = sharedBiometric.getString(Constants.HIDE_DRAWER_ICON, "");
            //  String get_imagEnableDownloadStatus = sharedBiometric.getString(Constants.imagEnableDownloadStatus, "");
            String get_imagEnableDownloadStatus = sharedBiometric.getString(Constants.showDownloadSyncStatus, "");


            if (get_imagEnableDownloadStatus.equals(Constants.showDownloadSyncStatus)) {
                bottom_server_layout.setVisibility(View.VISIBLE);
            } else {
                bottom_server_layout.setVisibility(View.GONE);
            }


            if (get_drawer.equals(Constants.HIDE_DRAWER_ICON)) {
                bottomtoolbar_btn_7.setVisibility(View.VISIBLE);
            } else {
                bottomtoolbar_btn_7.setVisibility(View.GONE);
            }


            if (getToHideQRCode.equals(Constants.HIDE_QR_CODE)) {
                drawerItem7.setVisibility(View.INVISIBLE);
            } else {
                drawerItem7.setVisibility(View.VISIBLE);
            }

            String getUrlFromScanner = getIntent().getStringExtra(Constants.QR_CODE_KEY);

            if (getUrlFromScanner != null) {
                if ((getUrlFromScanner.startsWith("https://") || getUrlFromScanner.startsWith("http://"))) {
                    webView.loadUrl(getUrlFromScanner);

                }

            }


            if (ChangeListener) {
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                ChangeListener = false;

            }


            SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);


            String fil_CLO = my_DownloadClass.getString(Constants.getFolderClo, "");
            String fil_DEMO = my_DownloadClass.getString(Constants.getFolderSubpath, "");

            // use to control Sync start
            String Manage_My_Sync_Start = my_DownloadClass.getString(Constants.Manage_My_Sync_Start, "");


            String get_intervals = sharedBiometric.getString(Constants.imagSwtichEnableSyncOnFilecahnge, "");

            if (!fil_CLO.isEmpty() && !fil_DEMO.isEmpty() && Manage_My_Sync_Start.isEmpty()) {

                if (get_intervals != null && get_intervals.equals(Constants.imagSwtichEnableSyncOnFilecahnge)) {
                    if (!ServiceUtils.foregroundServiceRunning(getApplicationContext())) {
                        stopService(new Intent(getApplicationContext(), OnChnageService.class));
                        startService(new Intent(getApplicationContext(), SyncInterval.class));

                        // showToast(mContext, "Sync On Interval Activated");

                    }


                } else {
                    if (!ServiceUtils.foregroundServiceRunningOnChange(getApplicationContext())) {
                        stopService(new Intent(getApplicationContext(), SyncInterval.class));
                        startService(new Intent(getApplicationContext(), OnChnageService.class));

                        // showToast(mContext, "Sync On Change Activated");

                    }


                }

            }


        } catch ( Exception e ) {
        }


        super.onResume();

    }

    @SuppressLint("SetTextI18n")
    private void updateSyncView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                try {

                    SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);

                    SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);

                    //   TextView textStatusProcess = findViewById(R.id.textStatusProcess);

                    String getFolderClo = my_DownloadClass.getString(Constants.getFolderClo, "");
                    String getFolderSubpath = my_DownloadClass.getString(Constants.getFolderSubpath, "");
                    String zip = my_DownloadClass.getString("Zip", "");
                    String get_progress = my_DownloadClass.getString(Constants.SynC_Status, "");

                    myDownloadStatus();


                    Long get_SavedTime = my_DownloadClass.getLong(Constants.SAVED_CN_TIME, 0);

                    if (get_SavedTime == 0) {
                        //
                    } else {
                        restoreTimerState();

                    }


                    String finalFolderPath = "LN: " + getFolderClo + "/" + getFolderSubpath;

                    if (!getFolderClo.isEmpty() && !getFolderSubpath.isEmpty()) {
                        textLocation.setText(finalFolderPath);
                    } else {
                        textLocation.setText("LN: --");
                    }


                    if (!zip.isEmpty()) {

                        if (zip.equals("App")) {
                            textSyncMode.setText("SM: Api");

                        } else {
                            textSyncMode.setText("SM: " + zip);
                        }

                    } else {
                        textSyncMode.setText("SM: --");
                    }


                    //  String get_intervals = sharedBiometric.getString(Constants.imagSwtichEnableSyncOnFilecahnge, "");

                    if (!getFolderClo.isEmpty() && !getFolderSubpath.isEmpty()) {

                        long getTimeDefined = my_DownloadClass.getLong(Constants.getTimeDefined, 0);

                        if (getTimeDefined != 0L) {
                            textSynIntervals.setText("ST: " + getTimeDefined + " Mins");
                        } else {
                            textSynIntervals.setText("ST: --");
                        }

                        if (isConnected()) {
                            if (!get_progress.isEmpty()) {
                                textStatusProcess.setText(get_progress + "");
                            } else {
                                textStatusProcess.setText("PR: Running");
                            }
                        } else {
                            textStatusProcess.setText("No Internet");
                        }


                    }


                } catch ( Exception e ) {
                }

            }
        });


    }


    @Override
    protected void onDestroy() {
        isAppOpen = false;
        if (LoadLastWebPageOnAccidentalExit) {
            preferences.edit().putString("lasturl", webView.getOriginalUrl()).apply();
        }


        try {


            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }


            MyApplication.decrementRunningActivities();

            //  stopService(new Intent(WebActivity.this, SyncInterval.class));
            //  stopService(new Intent(WebActivity.this, SyncInterval.class));

            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedBiometric.edit();
            String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
            editor.remove(did_user_input_passowrd);
            editor.apply();

            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(Send_Time_Update_Reciver);
            unregisterReceiver(Reciver_Progress);

        } catch ( Exception e ) {
        }


        try {
            if (myHandler != null) {
                myHandler.removeCallbacks(runnableDn);
            }
        } catch ( Exception ignored ) {
            // Ignore the exception
        }


        super.onDestroy();
    }

    private void checkPermission(String permission) {
        if (ContextCompat.checkSelfPermission(this,
                permission)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, 3);
        }
    }

    private void InitiatePermissions() {


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(WebActivity.this, PERMISSIONS, PERMISSION_ALL);

        }
    }


    @Override
    protected void onStop() {
        try {
            unregisterReceiver(broadcastReceiver);
            unregisterReceiver(Send_Time_Update_Reciver);
            unregisterReceiver(Reciver_Progress);

            unregisterReceiver(connectivityReceiver);

            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


            if (wakeLock != null && wakeLock.isHeld()) {
                wakeLock.release();
            }


        } catch ( Exception e ) {
        }


        try {
            if (myHandler != null) {
                myHandler.removeCallbacks(runnableDn);
            }
        } catch ( Exception ignored ) {
            // Ignore the exception
        }


        super.onStop();
    }

    private void InitiateComponents() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {


                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.execute(splashUrl);
                    Log.d("Remote Execution", "Downloading splash img");
                }


            }
        }, 10000);


        SharedPreferences sharedBiometric = getApplicationContext().getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);
        String get_floating_bar = sharedBiometric.getString(Constants.SHOW_FLOATING_BAR, "");


        if (ShowWebButton && !get_floating_bar.equals(Constants.SHOW_FLOATING_BAR)) {
            web_button.setVisibility(View.VISIBLE);

        } else {
            web_button.setVisibility(View.GONE);
        }


        if (RequestRunTimePermissions) {
            InitiatePermissions();
        }

        if (ShowHorizontalProgress) {
            horizontalProgressFramelayout.setVisibility(View.VISIBLE);
        }

        if (AllowRating) {
            TryRating();
        }


        if (ShowBottomBar) {

            try {
                if (ChangeBottombarBgColor) {
                    if (!(bottomBarBgColor == null)) {
                        bottomToolBar.setBackgroundColor(Color.parseColor(bottomBarBgColor));
                    }

                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            bottomToolBar.setVisibility(View.VISIBLE);

        }

        if (ShowSimpleProgressBar) {
            simpleProgressbar.setVisibility(View.VISIBLE);
        }


        if (EnableSwipeRefresh) {


            swipeView.setEnabled(true);
            swipeView.setColorSchemeColors(getResources().getColor(R.color.app_color_accent));

            swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    webView.reload();
                    swipeView.setRefreshing(false);
                }
            });
        }


        if (ShowToolbar) {
            x_toolbar.setVisibility(View.VISIBLE);

            if (!(ToolbarTitleText.isEmpty())) {
                toolbartitleText.setText(ToolbarTitleText);
            }

            try {
                if (ChangeTittleTextColor & !(ToolbarTitleTextColor.isEmpty())) {

                    toolbartitleText.setTextColor(Color.parseColor(ToolbarTitleTextColor));
                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }

            try {
                if (ChangeToolbarBgColor & !(ToolbarBgColor.isEmpty())) {
                    if (preferences.getBoolean("darktheme", false)) {
                        x_toolbar.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));
                        bottomToolBar.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));
                        drawerHeaderBg.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));

                    } else {
                        x_toolbar.setBackgroundColor(Color.parseColor(ToolbarBgColor));
                        getWindow().setStatusBarColor(Color.parseColor(ToolbarBgColor));
                    }

                }
            } catch ( Exception e ) {
                e.printStackTrace();
            }

        }

        if (ShowDrawer) {

            drawer_menu_btn.setVisibility(View.VISIBLE);

            drawer_menu_btn.setOnClickListener(imgClk);

            drawerItem1.setOnClickListener(imgClk);
            drawerItem2.setOnClickListener(imgClk);
            drawerItem3.setOnClickListener(imgClk);
            drawerItem4.setOnClickListener(imgClk);
            drawerItem5.setOnClickListener(imgClk);
            drawerItem6.setOnClickListener(imgClk);

            drawer_header_img.setOnClickListener(imgClk);


            try {


                if (ChangeHeaderTextColor & !(drawerHeaderTextColor == null)) {

                    if (preferences.getBoolean("darktheme", false)) {
                        drawer_header_text.setTextColor(Color.WHITE);
                    } else {
                        drawer_header_text.setTextColor(Color.parseColor(drawerHeaderTextColor));
                    }
                }


                if (!(drawerHeaderBgColor == null) & ChangeDrawerHeaderBgColor) {

                    if (preferences.getBoolean("darktheme", false)) {
                        drawerHeaderBg.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));

                    } else {
                        drawerHeaderBg.setBackgroundColor(Color.parseColor(drawerHeaderBgColor));


                    }
                }

            } catch ( Exception e ) {
                e.printStackTrace();
            }

            HandleRemoteDrawerText(drawerItemtext1, drawerMenuItem1Text);
            HandleRemoteDrawerText(drawerItemtext2, drawerMenuItem2Text);
            HandleRemoteDrawerText(drawerItemtext3, drawerMenuItem3Text);
            HandleRemoteDrawerText(drawerItemtext4, drawerMenuItem4Text);
            HandleRemoteDrawerText(drawerItemtext5, drawerMenuItem5Text);
            HandleRemoteDrawerText(drawerItemtext6, drawerMenuItem6Text);

            HandleRemoteDrawerText(drawer_header_text, drawerHeaderText);

        }
    }

    private void HandleRemoteDrawerText(TextView textv, String text) {
        textv.setText(text);
    }

    private void ShareItem(String ShareText, String Subject, String ShareTitle) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, Subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, ShareText);
        startActivity(Intent.createChooser(sharingIntent, ShareTitle));


    }

    private void InitiateAds() {

        if (ShowInterstitialAd) {
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NotNull InitializationStatus initializationStatus) {
                }
            });
            AdRequest adRequest = new AdRequest.Builder().build();
            InterstitialAd.load(this, getString(R.string.interstitialadid), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    mInterstitialAd = interstitialAd;
                    Log.i(TAG, "onAdLoaded");
                }


                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the network_error
                    Log.i(TAG, loadAdError.getMessage());
                    mInterstitialAd = null;
                }
            });
        }
        if (ShowBannerAds) {

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });

            mAdView = findViewById(R.id.adView);
            mAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }

    }


    private void InitiatePreferences() {

        if (preferences.getBoolean("hidebottombar", false)) {
            ShowBottomBar = false;
        }

        if (preferences.getBoolean("swiperefresh", false)) {
            EnableSwipeRefresh = true;
        }

        if (preferences.getBoolean("nightmode", false)) {

            if (WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)) {
                WebSettingsCompat.setForceDark(webView.getSettings(), WebSettingsCompat.FORCE_DARK_ON);


            }
        }

        if (preferences.getBoolean("blockAds", false)) {
            BlockAds = true;

        }

        if (preferences.getBoolean("nativeload", false)) {
            ShowNativeLoadView = true;
            ShowSimpleProgressBar = false;

        }
        if (preferences.getBoolean("geolocation", false)) {
            webView.getSettings().setGeolocationEnabled(true);
            webView.getSettings().setGeolocationDatabasePath(mContext.getFilesDir().getPath());
            AllowGPSLocationAccess = true;
        }

        if (preferences.getBoolean("fullscreen", false)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

        if (preferences.getBoolean("immersive_mode", false)) {
            ShowToolbar = false;

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }

        if (preferences.getBoolean("permission_query", false)) {
            RequestRunTimePermissions = true;
        }

        if (preferences.getBoolean("loadLastUrl", false)) {
            LoadLastWebPageOnAccidentalExit = true;
        }
        if (preferences.getBoolean("autohideToolbar", false)) {
            AutoHideToolbar = true;
        }
    }


    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {


    }

    public void onDownMotionEvent() {

    }

    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

        if (scrollState == ScrollState.UP) {

            if (ShowHideBottomBarOnScroll) {
                if (ShowBottomBar) {
                    if (bottomToolBar.isShown()) {
                        bottomToolBar.setVisibility(View.GONE);

                    }
                }
            }

        } else if (scrollState == ScrollState.DOWN) {

            if (ShowHideBottomBarOnScroll) {
                if (ShowBottomBar) {
                    if (!bottomToolBar.isShown()) {
                        bottomToolBar.setVisibility(View.VISIBLE);

                    }
                }
            }

        }


        if (scrollState == ScrollState.UP) {

            if (AutoHideToolbar) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        x_toolbar.setVisibility(View.GONE);
                    }
                }, 700);
            }


        } else if (scrollState == ScrollState.DOWN) {
            if (AutoHideToolbar) {


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        x_toolbar.setVisibility(View.VISIBLE);
                    }
                }, 700);

            }
        }
    }


    public void launchurlboxurl(View view) {

        String directurl = urlEdittext.getText().toString();
        if (directurl.startsWith("http://") || directurl.startsWith("https://")) {
            webView.loadUrl(directurl);
            urllayout.setVisibility(View.GONE);

            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        } else {
            showToast(mContext, "Invalid url");
        }
    }

    public void hideurllayt(View view) {
        LinearLayout urllayout = findViewById(R.id.urllayoutroot);
        urllayout.setVisibility(View.GONE);

    }

    public void goHomeOnError(View view) {

        webView.loadUrl(MainUrl);

    }

    public void ExitOnError(View view) {
        finishAffinity();
        if (LoadLastWebPageOnAccidentalExit) {
            ClearLastUrl();
        }

    }

    private void AnimateShow(View view) {
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(),
                R.anim.slide_to_right);
        view.startAnimation(anim);


    }

    private void AnimateHide(View view) {
        Animation anim = AnimationUtils.loadAnimation(getBaseContext(),
                R.anim.slide_to_left);
        view.startAnimation(anim);


    }


    private void HideErrorPage(final String failingUrl, String description) {


        webView.loadUrl("about:blank");

        try {
            ClosePopupWindow(mWebviewPop);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        errorlayout.setVisibility(View.VISIBLE);
        errorCode.setText(description);
        errorReloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                webView.loadUrl(failingUrl);

            }
        });


        handler.postDelayed(runnable = new Runnable() {

            public void run() {
                handler.postDelayed(runnable, 4000);
                if (errorautoConnect.getVisibility() == View.GONE) {
                    errorautoConnect.setVisibility(View.VISIBLE);
                }

                errorautoConnect.setText("Auto Reconnect: Standby");


                if (AdvancedControls.checkInternetConnection(mContext)) {
                    errorautoConnect.setText("Auto Reconnect: Trying to connect..");

                } else {
                    webView.loadUrl(failingUrl);
                    errorlayout.setVisibility(View.GONE);
                    webView.clearHistory();
                    handler.removeCallbacks(runnable);

                }


            }
        }, 4000);
        try {
            ClosePopupWindow(mWebviewPop);
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();
        final String TAG = "YOUR-TAG-NAME";
        final int REQUEST_CHECK_SETTINGS = 0x1;

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);


        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(result1 -> {
            final Status status = result1.getStatus();

            switch (status.getStatusCode()) {
                case LocationSettingsStatusCodes.SUCCESS:
                    Log.i(TAG, "All location settings are satisfied.");


                    break;

                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                    Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");


                    try {
                        status.startResolutionForResult(WebActivity.this, REQUEST_CHECK_SETTINGS);
                    } catch ( IntentSender.SendIntentException e ) {
                        Log.i(TAG, "PendingIntent unable to execute request.");

                    }
                    break;
                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                    Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");

                    break;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            results = new Uri[]{Uri.parse(mCM)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        ClipData clipData = intent.getClipData();
                        if (clipData != null) {
                            results = new Uri[clipData.getItemCount()];
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                ClipData.Item item = clipData.getItemAt(i);
                                results[i] = item.getUri();
                            }
                        }
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                        }
                    }
                }
            }
            try {
                mUMA.onReceiveValue(results);
                mUMA = null;
            } catch ( Exception e ) {
                e.printStackTrace();
            }

        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }


    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    @Override
    public void onBackPressed() {

        if (drawer_menu.getVisibility() == View.VISIBLE) {
            drawer_menu.setVisibility(View.GONE);
        }

        if (windowContainer.getVisibility() == View.VISIBLE) {
            if (mWebviewPop.canGoBack()) {
                mWebviewPop.goBack();
            } else {
                ClosePopupWindow(mWebviewPop);

            }
        } else if (windowContainer.getVisibility() == View.GONE) {
            if (webView.canGoBack()) {
                webView.goBack();

            } else {
                if (ClearCacheOnExit) {
                    webView.clearCache(true);
                }

                if (AskToExit) {
                    ShowExitDialogue();
                    if (LoadLastWebPageOnAccidentalExit) {
                        ClearLastUrl();
                    }
                } else {
                    ClearLastUrl();

                    webView.stopLoading();
                    webView.destroy();

                    Intent myactivity = new Intent(WebActivity.this, SettingsActivity.class);
                    startActivity(myactivity);
                    finish();
                    showToast(mContext, "Please wait..");


                }
            }

        }
    }


    private void ShowExitDialogue() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.img_logo_icon)
                .setTitle("Exit")
                .setMessage("Are you sure to Exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClearLastUrl();
                        System.exit(0);
                    }

                })
                .setNegativeButton("No", null)
                .show();
    }

    private void ClearLastUrl() {
        SharedPreferences pp = PreferenceManager.getDefaultSharedPreferences(mContext);
        pp.edit().remove("lasturl").apply();

    }

    public void ClosePopupWindow(View view) {

        windowProgressbar.setVisibility(View.GONE);
        preferences = getPreferences(MODE_PRIVATE);
        preferences.edit().putString("proshow", "noshow").apply();
        mContainer.removeAllViews();
        windowContainer.setVisibility(View.GONE);
        mWebviewPop.destroy();
        if (simpleProgressbar.getVisibility() == View.VISIBLE) {
            simpleProgressbar.setVisibility(View.GONE);
        }


    }


    public void UpdateApp(final String updateUrl, boolean forceUpdate) {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(UpdateTitle)
                .setMessage(UpdateMessage)
                .setCancelable(!forceUpdate)
                .setNeutralButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (forceUpdate) {
                            finish();
                        } else {
                            dialog.dismiss();
                        }
                    }
                })

                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                redirectStore(updateUrl);
                                if (forceUpdate) {
                                    finish();
                                }
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (forceUpdate) {
                                    finish();
                                }

                            }
                        }).create();

        try {
            dialog.show();
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    private void redirectStore(String updateUrl) {
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @SuppressLint("StaticFieldLeak")
    private class DownloadFile extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            preferences.edit().putString("splashStarted", "started").apply();
            Log.d("Remote Execution", "Downloading splash img in bg");

            mProgressDialog = new ProgressDialog(WebActivity.this);
            mProgressDialog.setMax(100);

        }

        protected void onProgressUpdate(Integer... progress) {
            mProgressDialog.setProgress((progress[0]));


            super.onProgressUpdate(progress);

        }

        @Override
        protected String doInBackground(String... aurl) {

            try {

                URL url = new URL(aurl[0]);
                URLConnection connection = url.openConnection();


                connection.connect();
                int fileLength = connection.getContentLength();
                int tickSize = 2 * fileLength / 100;
                int nextProgress = tickSize;

                Log.d("ANDRO_ASYNC", "Lenght of file: " + fileLength);

                InputStream input = new BufferedInputStream(url.openStream());


                String filename = "splash.png";

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(String.valueOf(path));
                File outputFile = new File(file, filename);

                OutputStream output = new FileOutputStream(outputFile);

                byte[] data = new byte[1024 * 1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    if (total >= nextProgress) {
                        nextProgress = (int) ((total / tickSize + 1) * tickSize);
                        this.publishProgress((int) (total * 100 / fileLength));
                    }
                    output.write(data, 0, count);
                }


                output.flush();
                output.close();
                input.close();
                mProgressDialog.setProgress(0);

            } catch ( Exception e ) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {


            if (mProgressDialog.getProgress() == 100) {
                preferences.edit().putString("splashStarted", "finished").apply();
                Log.d("Remote Execution", "Downloading splash img 100 - success");
            }

        }
    }


    private class AdvancedWebViewClient extends WebViewClient {

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            if (BlockAds) {
                if (url.contains("googleads.g.doubleclick.net")) {
                    InputStream textStream = new ByteArrayInputStream("".getBytes());
                    return getTextWebResource(textStream);
                }

            }
            return super.shouldInterceptRequest(view, url);
        }

        private WebResourceResponse getTextWebResource(InputStream data) {
            return new WebResourceResponse("text/plain", "UTF-8", data);
        }

        @Override

        public boolean shouldOverrideUrlLoading(WebView view, String url) {

//            webView.setAlpha(0.9F);
            if (AllowOnlyHostUrlInApp) {
                if (!url.contains(constants.filterdomain)) {
                    webView.stopLoading();

                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    return true;
                }
            }

            if (url.startsWith("http://") || url.startsWith("file:///") || url.startsWith("https://") || url.startsWith("setup://"))
                return false;

            try {
                Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                // forbid launching activities without BROWSABLE category
                assert intent != null;
                intent.addCategory("android.intent.category.BROWSABLE");
                // forbid explicit call
                intent.setComponent(null);
                // forbid Intent with selector Intent
                intent.setSelector(null);
                // start the activity by the Intent
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                view.getContext().startActivity(intent);

            } catch ( Exception e ) {
                Log.i(TAG, "shouldOverrideUrlLoading Exception:" + e.getMessage());
                Toast.makeText(mContext, "The app or ACTIVITY not found. Error Message:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            return true;
        }


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


            if (drawer_menu.getVisibility() == View.VISIBLE) {
                drawer_menu.setVisibility(View.GONE);
            }
            if (ShowSimpleProgressBar) {
                simpleProgressbar.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public void onPageFinished(WebView view, String url) {
            try {
                lasturl = url;

                if (ShowSimpleProgressBar) {
                    simpleProgressbar.setVisibility(View.GONE);
                }


                if (LoadLastWebPageOnAccidentalExit) {
                    preferences.edit().putString("lasturl", url).apply();
                }


                if (ShowInterstitialAd) {
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(WebActivity.this);
                    } else {
                        Log.d("TAG", "The interstitial ad wasn't ready yet.");
                    }
                }


                view.clearCache(true);


            } catch ( Exception e ) {
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            if (description.matches("net::ERR_FAILED")) {

            } else {

                HideErrorPage(failingUrl, description);

            }

            super.onReceivedError(view, errorCode, description, failingUrl);
        }
    }

    private class AdvancedWebChromeClient extends WebChromeClient {


        public void onShowCustomView(View view,
                                     CustomViewCallback callback) {

            if (mCustomView != null) {
                onHideCustomView();
                return;
            }

            mCustomView = view;
            mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            mOriginalOrientation = getRequestedOrientation();

            mCustomViewCallback = callback;

            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            decor.addView(mCustomView, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                View.SYSTEM_UI_FLAG_IMMERSIVE);
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        @Override
        public void onHideCustomView() {
            // 1. Remove the custom view
            FrameLayout decor = (FrameLayout) getWindow().getDecorView();
            decor.removeView(mCustomView);
            mCustomView = null;

            getWindow().getDecorView()
                    .setSystemUiVisibility(mOriginalSystemUiVisibility);
            setRequestedOrientation(mOriginalOrientation);

            mCustomViewCallback.onCustomViewHidden();
            mCustomViewCallback = null;

        }

        public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
            callback.invoke(origin, true, false);
            if (AllowGPSLocationAccess) {
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION);
                webView.getSettings().setGeolocationEnabled(true);
                displayLocationSettingsRequest(mContext);
            } else {
                showToast(mContext, "Location requested, You can enable location in settings");
            }

        }


        @Override
        public void onPermissionRequest(PermissionRequest request) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                request.grant(request.getResources());
                final String[] requestedResources = request.getResources();
                for (String r : requestedResources) {
                    if (r.equals(PermissionRequest.RESOURCE_AUDIO_CAPTURE)) {
                        checkPermission(Manifest.permission.RECORD_AUDIO);
                        checkPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS);

                    }


                    for (String h : requestedResources) {
                        if (h.equals(PermissionRequest.RESOURCE_VIDEO_CAPTURE)) {
                            checkPermission(PermissionRequest.RESOURCE_VIDEO_CAPTURE);
//                            checkPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS);

                        }
                    }
                }

            }

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);


            if (ShowHorizontalProgress) {
                HorizontalProgressBar.setProgress(newProgress);
            }


            String name = preferences.getString("proshow", "");


            if (newProgress == 100) {


                if (name.equals("show")) {
                    windowProgressbar.setVisibility(View.GONE);
                }

                try {
                    if (ShowProgressDialogue) {
                        progressDialog.cancel();
                        progressDialog.dismiss();
                        progressDialog.hide();
                    }
                    if (ShowToolbarProgress) {
                        tbarprogress.setVisibility(View.GONE);
                    }

                    if (ShowHorizontalProgress) {
                        HorizontalProgressBar.setVisibility(View.GONE);
                    }

                    if (ShowSimpleProgressBar) {
                        simpleProgressbar.setVisibility(View.GONE);
                    }

                } catch ( Exception e ) {
                    e.printStackTrace();
                }


            } else {


                if (name.equals("show")) {
                    windowProgressbar.setVisibility(View.VISIBLE);
                }


                try {

                    if (ShowHorizontalProgress) {
                        HorizontalProgressBar.setVisibility(View.VISIBLE);
                    }


                    if (ShowProgressDialogue) {
                        progressDialog.setMessage("Loading");
                        progressDialog.setCancelable(false);
                        progressDialog.show();
                    }


                    if (ShowSimpleProgressBar) {
                        simpleProgressbar.setVisibility(View.VISIBLE);

                    }

                    if ((ShowToolbarProgress)) {
                        tbarprogress.setVisibility(View.VISIBLE);
                    }


                } catch ( Exception e ) {
                    e.printStackTrace();
                }


            }
        }


        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog,
                                      boolean isUserGesture, Message resultMsg) {
            Log.e("", "onCreateWindow called");

            preferences = getPreferences(MODE_PRIVATE);
            preferences.edit().putString("proshow", "show").apply();
            windowContainer.setVisibility(View.VISIBLE);


            mWebviewPop = new WebView(WebActivity.this);

            WebSettings webSettings = mWebviewPop.getSettings();
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDisplayZoomControls(false);

            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);

            //   webSettings.setAppCacheEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setSupportZoom(false);

            webSettings.setUserAgentString(webSettings.getUserAgentString().replace("wv", ""));
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.acceptThirdPartyCookies(mWebviewPop);

            webSettings.setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
            webSettings.setAllowFileAccess(false);

            if (SupportMultiWindows) {
                webSettings.setSupportMultipleWindows(true);
                webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            }

            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

            mWebviewPop.setSaveEnabled(true);


            webSettings.setMediaPlaybackRequiresUserGesture(false);

            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);


            WebView.setWebContentsDebuggingEnabled(true);

            mWebviewPop.setLayoutParams(new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));


            mWebviewPop.setWebViewClient(new AdvancedWebViewClient());
            mWebviewPop.setWebChromeClient(new AdvancedWebChromeClient());
            //  mWebviewPop.setDownloadListener(new Downloader());

            mContainer.addView(mWebviewPop);
            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(mWebviewPop);

            resultMsg.sendToTarget();
            return true;
        }

        @Override
        public void onCloseWindow(WebView window) {
            Log.v("TEST", "onCloseWindow");

            ClosePopupWindow(mWebviewPop);
        }

        @Override
        public void onRequestFocus(WebView view) {
            Log.v("TEST", "onRequestFocus");
            super.onRequestFocus(view);
        }


        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            this.openFileChooser(uploadMsg, acceptType, null);
        }

        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String
                capture) {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            WebActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"),
                    FILECHOOSER_RESULTCODE);
        }


        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                FileChooserParams fileChooserParams) {
            if (!hasPermissions(WebActivity.this, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showToast(WebActivity.this, "Please allow the Requested Permissions and try again");
                InitiatePermissions();
                storagecamrequest = true;

                webView.loadUrl(lasturl);
            } else {

                if (mUMA != null) {
                    mUMA.onReceiveValue(null);
                }
                mUMA = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(WebActivity.this.getPackageManager()) != null) {
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCM);
                    } catch ( IOException ex ) {
                        Log.e(TAG, "Image file creation failed", ex);
                    }
                    if (photoFile != null) {
                        mCM = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);

                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    contentSelectionIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                }
                contentSelectionIntent.setType("*/*");
                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "File Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
                chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(chooserIntent, FCR);
            }
            return true;
        }


    }


    @SuppressLint({"MissingInflatedId", "UseCompatLoadingForDrawables"})
    private void showPopForTVConfiguration(String message) {


        CustomOfflinePopLayoutBinding binding = CustomOfflinePopLayoutBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
        builder.setView(binding.getRoot());
        final AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String get_AppMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");


        TextView textContinuPasswordDai3 = binding.textContinuPasswordDai3;
        TextView textContinue = binding.textContinue;
        TextView textDescription = binding.textDescription;
        ImageView imgCloseDialog = binding.imgCloseDialog;
        ImageView imageView24 = binding.imageView24;


        if (!message.isEmpty()) {
            textDescription.setText(message);
        }

        if (message.equals(Constants.UnableToFindIndex)) {
            imageView24.setBackground(getResources().getDrawable(R.drawable.ic_folder_24));

        } else if (message.equals(Constants.badRequest)) {
            imageView24.setBackground(getResources().getDrawable(R.drawable.ic_wifi_no_internet));
        } else {
            imageView24.setBackground(getResources().getDrawable(R.drawable.ic_sync_cm));
        }


        SharedPreferences.Editor editor222 = sharedBiometric.edit();
        textContinuPasswordDai3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WebActivity.this, SettingsActivity.class));
                finish();
                editor222.putString(Constants.SAVE_NAVIGATION, Constants.WebViewPage);
                editor222.apply();

                showToast(mContext, "Please wait");
            }
        });


        textContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (get_AppMode.equals(Constants.TV_Mode) || jsonUrl == null) {
                    showToast(mContext, "Tap The Back Button to Go Settings Page");
                }

                loadTheMainWebview();

                alertDialog.dismiss();
            }
        });


        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (get_AppMode.equals(Constants.TV_Mode) || jsonUrl == null) {
                    showToast(mContext, "Tap The Back Button to Go Settings Page");
                }
                loadTheMainWebview();
                alertDialog.dismiss();
            }
        });


        alertDialog.show();


    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null && intent.getAction().equals(Constants.SEND_SERVICE_NOTIFY)) {

                online_Load_Webview_Logic();
                //   showToast(mContext, "Refresh Web View");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateSyncView();
                    }
                }, 1000);


            }

        }
    };

    private final BroadcastReceiver Send_Time_Update_Reciver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction() != null && intent.getAction().equals(Constants.SEND_UPDATE_TIME_RECIEVER)) {

                //    showToast(mContext, "DL error");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateSyncView();
                    }
                }, 1000);


            }

        }
    };


    private final BroadcastReceiver Reciver_Progress = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Constants.RECIVER_PROGRESS)) {

                myDownloadStatus();
            }
        }
    };

    private void myDownloadStatus() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (isConnected()) {


                    try {
                        SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);

                        //   TextView textStatusProcess = findViewById(R.id.textStatusProcess);
                        String get_progress = my_DownloadClass.getString(Constants.SynC_Status, "");


                        if (!get_progress.isEmpty()) {
                            textStatusProcess.setText(get_progress + "");
                        } else {
                            textStatusProcess.setText("PR: Running");
                        }
                    } catch ( Exception e ) {
                    }


                } else {
                    textStatusProcess.setText("No Internet");
                }


            }
        });

    }


    private void restoreTimerState() {
        SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);

        long savedTime = my_DownloadClass.getLong(Constants.SAVED_CN_TIME, 0);
        long currentTime = System.currentTimeMillis();

        if (savedTime > currentTime) {
            remainingTime = savedTime - currentTime;
            startTimer(remainingTime);
        } else {
            remainingTime = 0;
        }
    }

    private void startTimer(long milliseconds) {
        countdownTimer = new CountDownTimer(milliseconds, 1000) {
            @Override
            public void onFinish() {

            }

            @Override
            public void onTick(long millisUntilFinished) {
                try {

                    long totalSecondsRemaining = millisUntilFinished / 1000;
                    long minutesUntilFinished = totalSecondsRemaining / 60;
                    long remainingSeconds = totalSecondsRemaining % 60;

                    // Adjusting minutes if seconds are in the range of 0-59
                    if (remainingSeconds == 0 && minutesUntilFinished > 0) {
                        minutesUntilFinished--;
                        remainingSeconds = 59;
                    }

                    String displayText = String.format("CD: %d:%02d", minutesUntilFinished, remainingSeconds);
                    countDownTime.setText(displayText);


                    remainingTime = millisUntilFinished;

                } catch ( Exception e ) {
                }
            }
        };
        countdownTimer.start();
    }

    private Runnable runnableDn = new Runnable() {
        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
        @Override
        public void run() {
            TextView textDownladByes = findViewById(R.id.textDownladByes);
            ProgressBar progressBarPref = findViewById(R.id.progressBarPref);
            MyDownloadMangerClass downloadManager = new MyDownloadMangerClass();

            downloadManager.getDownloadStatus(progressBarPref, textDownladByes, getApplicationContext());
            myHandler.postDelayed(this, 500);
        }
    };


    public class ConnectivityReceiver extends BroadcastReceiver {

        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

                    try {

                        int SPLASH_TIME_OUT = 1000;
                        textStatusProcess.setText("Connecting..");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {

                                    imageWiFiOn.setVisibility(View.GONE);
                                    imageWiFiOFF.setVisibility(View.VISIBLE);

                                    myDownloadStatus();


                                } catch ( Exception e ) {
                                }

                            }
                        }, SPLASH_TIME_OUT);


                    } catch ( Exception ignored ) {
                    }


                } else {

                    // No internet Connection
                    try {
                        int SPLASH_TIME_OUT = 1000;
                        imageWiFiOn.setVisibility(View.VISIBLE);
                        imageWiFiOFF.setVisibility(View.GONE);
                        textStatusProcess.setText("No Internet");


                    } catch ( Exception e ) {
                    }

                }

                // No internet Connection


            } catch ( Exception ignored ) {
            }
        }

    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }


    /// we added

    private class Downloader implements DownloadListener {

        @Override
        public void onDownloadStart(final String url, final String userAgent, String contentDisposition, String mimetype, long contentLength) {

            constants.currentDownloadFileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
            constants.currentDownloadFileMimeType = mimetype;
            if (url.startsWith("blob:")) {
                Toast.makeText(getApplicationContext(), "Downloading blob file ", Toast.LENGTH_SHORT).show();
//                webView.loadUrl(JavaScriptInterface.getBase64StringFromBlobUrl(url));
            } else {


                File file = new File(Environment.
                        getExternalStoragePublicDirectory(Environment
                                .DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + constants.currentDownloadFileName);

                if (file.exists()) {
                    new AlertDialog.Builder(WebActivity.this)
                            .setIcon(R.mipmap.ic_launcher)
                            .setTitle("File already exists")
                            .setMessage("A  file with same name already exist, continue download?")
                            .setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            downloadDialog(url, userAgent, contentDisposition, mimetype);
                                        }
                                    }

                            ).setNegativeButton("Cancel", null)
                            .setNeutralButton("Actions", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            AdvancedControls.DownloadFinishedAction(WebActivity.this);
                                        }

                                    }
                            )
                            .show();

                } else {
                    downloadDialog(url, userAgent, contentDisposition, mimetype);

                }

            }


        }


        public void downloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype) {

            final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
            currentDownloadFileName = filename;
            currentDownloadFileMimeType = mimetype;
            preferences = getPreferences(MODE_PRIVATE);
            preferences.edit().putString("downloadedfilename", filename).apply();


            AlertDialog.Builder builder = new AlertDialog.Builder(WebActivity.this);
            builder.setTitle("File Download");
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setMessage(("You want download") + ' ' + filename + "?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Toast.makeText(mContext, "Downloading " + filename, Toast.LENGTH_LONG).show();
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    //cookie
                    String cookie = CookieManager.getInstance().getCookie(url);
                    //Add cookie and User-Agent to request
                    request.addRequestHeader("Cookie", cookie);
                    request.addRequestHeader("User-Agent", userAgent);
                    //file scanned by MediaScannar
                    request.allowScanningByMediaScanner();
                    //Download is visible and its progress, after completion too.
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//
                    //DownloadManager created
                    DownloadManager downloadManager = (DownloadManager) WebActivity.this.getSystemService(DOWNLOAD_SERVICE);
                    //Saving files in Download folder
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
                    //download enqued
                    assert downloadManager != null;
                    try {
                        downloadManager.enqueue(request);
                    } catch ( Exception e ) {
                        e.printStackTrace();
                        Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
                    }

//                    errorlayout.setVisibility(View.GONE);

                }

            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //cancel the dialog if Cancel clicks
                    dialog.cancel();
                    Toast.makeText(mContext, "Downloading Cancelled " + filename, Toast.LENGTH_SHORT).show();

                }

            });
            //alertdialog shows.
            builder.show();


        }
    }


}



