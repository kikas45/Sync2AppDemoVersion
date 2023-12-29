package remotex.com.remotewebview;


import static remotex.com.remotewebview.AdvancedControls.showToast;
import static remotex.com.remotewebview.constants.AllowOnlyHostUrlInApp;
import static remotex.com.remotewebview.constants.ChangeBottombarBgColor;
import static remotex.com.remotewebview.constants.ChangeDrawerHeaderBgColor;
import static remotex.com.remotewebview.constants.ChangeHeaderTextColor;
import static remotex.com.remotewebview.constants.ChangeTittleTextColor;
import static remotex.com.remotewebview.constants.ChangeToolbarBgColor;
import static remotex.com.remotewebview.constants.CurrVersion;
import static remotex.com.remotewebview.constants.ForceUpdate;
import static remotex.com.remotewebview.constants.GooglePlayLink;
import static remotex.com.remotewebview.constants.NewVersion;
import static remotex.com.remotewebview.constants.NotifAvailable;
import static remotex.com.remotewebview.constants.NotifLinkExternal;
import static remotex.com.remotewebview.constants.NotifSound;
import static remotex.com.remotewebview.constants.Notif_ID;
import static remotex.com.remotewebview.constants.Notif_Img_url;
import static remotex.com.remotewebview.constants.Notif_Shown;
import static remotex.com.remotewebview.constants.Notif_button_action;
import static remotex.com.remotewebview.constants.Notif_desc;
import static remotex.com.remotewebview.constants.Notif_title;
import static remotex.com.remotewebview.constants.Notifx_service;
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
import static remotex.com.remotewebview.constants.bottomBarBgColor;
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
import static remotex.com.remotewebview.constants.isAppOpen;
import static remotex.com.remotewebview.constants.jsonUrl;
import static remotex.com.remotewebview.constants.splashUrl;

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
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.text.Html;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
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
import com.google.android.material.snackbar.Snackbar;


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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import remotex.com.remotewebview.additionalSettings.QRSanActivity;
import remotex.com.remotewebview.additionalSettings.ReSyncActivity;
import remotex.com.remotewebview.additionalSettings.utils.Constants;
import remotex.com.remotewebview.databinding.CustomConfirmExitDialogBinding;
import remotex.com.remotewebview.databinding.CustomEmailSucessLayoutBinding;
import remotex.com.remotewebview.glidetovectoryou.GlideToVectorYou;
import remotex.com.remotewebview.glidetovectoryou.GlideToVectorYouListener;



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

    protected ObservableWebView webView;


    // for permission
    // for permission
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;
    private static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static String READ_STORAGE_PERMISSION;

    private int REQUEST_CODE = 11;

    private ConnectivityReceiver connectivityReceiver;


    RelativeLayout drawer_menu;
    public View.OnClickListener imgClk = new View.OnClickListener() {
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

                case R.id.drawer_item_7:
                    HandleRemoteCommand(drawerMenuItem7Url);
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
    LinearLayout btm;
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
    RelativeLayout webconbt;
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            // you can add more permissions here, and in manifest (both required)

    };


    /*  BOOLEAN SWITCHES CONTROL
 HERE YOU CAN ENABLE OR DISABLE FEATURES
 BY SETTING THE VALUE - true OR false
*/
    //Progress
    boolean ShowHorizontalProgress = false; //shows a horizontal progressbar
    boolean ShowToolbarProgress = false; // show rotating progressbar on toolbar (unstable)
    boolean ShowProgressDialogue = false; // the main progress dialogue
    boolean ShowSimpleProgressBar = true; //  a simple progressbar
    boolean ShowNativeLoadView = false; // a progress style like native loading, it may have bugs
    boolean EnableSwipeRefresh = false; // pull to refresh
    //Ads
    boolean ShowBannerAds = constants.ShowAdmobBanner; // if set true, this will show admob banner ads
    boolean ShowInterstitialAd = constants.ShowAdmobInterstitial; /*if set true, it will show fullscreen ad,
    For test, the interstitial ad code to show is currently placed in the webview client's OnPagefinished method,
     so it will show ad everytime a page loaded
    i suggest you place it in other convenient places*/

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
    RelativeLayout x_toolbar;
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
    ImageView drawerImg7;
    TextView drawerItemtext1;
    TextView drawerItemtext2;
    TextView drawerItemtext3;
    TextView drawerItemtext4;
    TextView drawerItemtext5;
    TextView drawerItemtext6;
    TextView drawerItemtext7;
    ImageView drawer_header_img;
    TextView drawer_header_text;
    LinearLayout drawerHeaderBg;
    LinearLayout bottom_toolbar_container;
    Handler handler = new Handler();
    Runnable runnable;
    TextView toolbartitleText;

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
    private RelativeLayout native_drawer_menu;
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
    private RelativeLayout web_button_root_layout;
    private LinearLayout web_button_container;
    private TextView errorCode;
    private TextView errorautoConnect;
    ImageButton errorReloadButton;


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

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface", "ClickableViewAccessibility", "CutPasteId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darktheme", false)) {

            setTheme(R.style.DarkTheme);
        }


        setContentView(R.layout.webactivity_layout);


        connectivityReceiver = new ConnectivityReceiver();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityReceiver, intentFilter);



        //This Project Developed by ZIDDIQUE ABU (www.zidsworld.com)

        /*Copyright  - www.zidsworld.com and Ziddique Abu
         License granted only to you (the original purchaser of this code) under the condition
         that you should not publish, transmit this source code other than for your personal usage
         (Use this code to build apps for your own websites only,
         to build apps for your clients, a business version of this code Must be used
         - you can contact us for business version, ).

        Continue using this source code means you accept this agreement.
        if this agreement is violated, we reserve the right to stop providing support and update or disable this code.
        Thank You for respecting the hard work of this source code developer*/

        mContext = WebActivity.this;
        AdvancedControls.CompletionReciever(WebActivity.this);

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
        btm = findViewById(R.id.btm);

        mAdView = findViewById(R.id.adView);


        HorizontalProgressBar = findViewById(R.id.progressbar);
        webView = findViewById(R.id.webview);
        swipeView = findViewById(R.id.swipeLayout);
        urlEdittext = findViewById(R.id.urledittextbox);
        urllayout = findViewById(R.id.urllayoutroot);

        webconbt = findViewById(R.id.webco);
        web_button = findViewById(R.id.web_button);
        web_button_container = findViewById(R.id.webx);
        web_button_root_layout = findViewById(R.id.web_button_layout);
        bottomToolbar_img_1 = findViewById(R.id.bottomtoolbar_btn_1);
        bottomToolbar_img_2 = findViewById(R.id.bottomtoolbar_btn_2);
        bottomToolbar_img_3 = findViewById(R.id.bottomtoolbar_btn_3);
        bottomToolbar_img_4 = findViewById(R.id.bottomtoolbar_btn_4);
        bottomToolbar_img_5 = findViewById(R.id.bottomtoolbar_btn_5);
        bottomToolbar_img_6 = findViewById(R.id.bottomtoolbar_btn_6);

        drawerItem1 = findViewById(R.id.drawer_item_1);
        drawerItem2 = findViewById(R.id.drawer_item_2);
        drawerItem3 = findViewById(R.id.drawer_item_3);
        drawerItem4 = findViewById(R.id.drawer_item_4);
        drawerItem5 = findViewById(R.id.drawer_item_5);
        drawerItem6 = findViewById(R.id.drawer_item_6);
        drawerItem7 = findViewById(R.id.drawer_item_7);

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
        drawerItemtext7 = findViewById(R.id.drawer_item_text_7);

        drawer_header_img = findViewById(R.id.drawer_headerImg);
        drawer_header_text = findViewById(R.id.drawer_header_text);
        drawerHeaderBg = findViewById(R.id.drawerheaderBg);
        toolbartitleText = findViewById(R.id.toolbarTitleText);
        bottom_toolbar_container = findViewById(R.id.bottom_toolbar_container);


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


        web_button_container.setOnTouchListener(new View.OnTouchListener() {


            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = v.getX() - event.getRawX();
                        dY = v.getY() - event.getRawY();
//                        web_button.setOnClickListener(null);

                        break;

                    case MotionEvent.ACTION_MOVE:
                        v.setY(event.getRawY() + dY);
                        v.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;

                        break;

                    case MotionEvent.ACTION_UP:
                        if (lastAction == MotionEvent.ACTION_DOWN)
                            break;

//                    case MotionEvent.ACTION_BUTTON_PRESS:
//
//                        web_button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);
//                                v.startAnimation(buttonClick);
//                                webView.loadUrl(Web_button_link);
//                            }
//                        });
//                        break;

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





    }

    private void stratDiffrentWebviewTypes() {
        String unzipManualValue = getIntent().getStringExtra("unzipManual");
        if (unzipManualValue != null) {
            String filename = "/index.html";
            SharedPreferences savedDownloadPath = getSharedPreferences(Constants.SAVE_M_DOWNLOAD_PATH, MODE_PRIVATE);
            String getFolderClo = savedDownloadPath.getString("getFolderClo", "");
            String getFolderSubpath = savedDownloadPath.getString("getFolderSubpath", "");
            String Extracted = savedDownloadPath.getString("Extracted", "");

            String finalFolderPathDesired = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Extracted;
            String destinationFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Syn2AppLive/" + finalFolderPathDesired;

            File myFile = new File(destinationFolder, filename);
            if (myFile.exists()) {
                loadOfflineWebviewPage();
            }else {
                claingTheLongWebView();
            }

        }else {
            claingTheLongWebView();
        }

    }

    private void claingTheLongWebView() {
        @SuppressLint("CommitPrefEdits")
        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String imgLunchOnline = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "");

        if (imgLunchOnline.equals(Constants.imgAllowLunchFromOnline)) {

            String filename = "/index.html";
            SharedPreferences savedDownloadPath = getSharedPreferences(Constants.SAVE_M_DOWNLOAD_PATH, MODE_PRIVATE);
            String getFolderClo = savedDownloadPath.getString("getFolderClo", "");
            String getFolderSubpath = savedDownloadPath.getString("getFolderSubpath", "");
            String Extracted = savedDownloadPath.getString("Extracted", "");

            String finalFolderPathDesired = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Extracted;
            String destinationFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Syn2AppLive/" + finalFolderPathDesired;

            File myFile = new File(destinationFolder, filename);
            if (myFile.exists()) {
                loadOfflineWebviewPage();
            }else {
                loadTheMainWebview();
            }

        }

        else {
            loadTheMainWebview();
        }
    }


   private void loadOffline_When_No_Internet_connection(){

           String filename = "/index.html";
           SharedPreferences savedDownloadPath = getSharedPreferences(Constants.SAVE_M_DOWNLOAD_PATH, MODE_PRIVATE);
           String getFolderClo = savedDownloadPath.getString("getFolderClo", "");
           String getFolderSubpath = savedDownloadPath.getString("getFolderSubpath", "");
           String Extracted = savedDownloadPath.getString("Extracted", "");

           String finalFolderPathDesired = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Extracted;
           String destinationFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Syn2AppLive/" + finalFolderPathDesired;

           File myFile = new File(destinationFolder, filename);
           if (myFile.exists()) {
               loadOfflineWebviewPage();
           }else {
               loadTheMainWebview();
               showToastMessage("unable to find index file");
           }

   }

    private void loadTheMainWebview() {

        WebSettings webSettings = webView.getSettings();
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);

        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(false);

        // webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSupportZoom(false);

        webSettings.setUserAgentString(webSettings.getUserAgentString().replace("wv", ""));
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.acceptThirdPartyCookies(webView);

        webSettings.setJavaScriptEnabled(true);
//        webView.addJavascriptInterface(new JavaScriptInterface(this), "Android");
        webSettings.setAllowFileAccess(false);

        if (SupportMultiWindows) {
            webSettings.setSupportMultipleWindows(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        }

        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setScrollViewCallbacks(this);
        webView.setSaveEnabled(true);


        webSettings.setMediaPlaybackRequiresUserGesture(false);

        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING);
        webView.setWebViewClient(new AdvancedWebViewClient());
        webView.setWebChromeClient(new AdvancedWebChromeClient());
        webView.setDownloadListener(new Downloader());

        WebView.setWebContentsDebuggingEnabled(true);


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
//                      if (MainUrl.equals("")){
//                          webView.loadUrl("file:///android_asset/tests.html");
//                      } else {
                    webView.loadUrl(MainUrl);
//                      }

                }
            } else {
                webView.loadUrl(MainUrl);
            }
        }

    }


    @SuppressLint("SetJavaScriptEnabled")
    private void loadOfflineWebviewPage() {

        bottomToolBar.setVisibility(View.GONE);
        web_button_root_layout.setVisibility(View.GONE);
        x_toolbar.setVisibility(View.GONE);
        drawer_menu.setVisibility(View.GONE);
        btm.setVisibility(View.GONE);
        bottom_toolbar_container.setVisibility(View.GONE);


        String filename = "/index.html";


        SharedPreferences savedDownloadPath = getSharedPreferences(Constants.SAVE_M_DOWNLOAD_PATH, MODE_PRIVATE);
        String getFolderClo = savedDownloadPath.getString("getFolderClo", "");
        String getFolderSubpath = savedDownloadPath.getString("getFolderSubpath", "");
        String Extracted = savedDownloadPath.getString("Extracted", "");

        String finalFolderPathDesired = "/" + getFolderClo + "/" + getFolderSubpath + "/" + Extracted;

        String destinationFolder = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Syn2AppLive/" + finalFolderPathDesired;

        String filePath = "file://" + destinationFolder + filename;

        ProgressBar SimpleProgressBar = findViewById(R.id.SimpleProgressBar);

        File myFile = new File(destinationFolder, filename);

        if (myFile.exists()) {
            webView = findViewById(R.id.webview);
            webView.setWebViewClient(new WebViewClient());

            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(true);

            webSettings.setAllowFileAccess(true);
            webSettings.setAllowContentAccess(true);
            webSettings.setDomStorageEnabled(true);

         //   webSettings.setMediaPlaybackRequiresUserGesture(false);
         //   webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

            webSettings.setLoadWithOverviewMode(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadsImagesAutomatically(true);


            webView.loadUrl(filePath);
            SimpleProgressBar.setVisibility(View.GONE);

        }

    }


    public class ConnectivityReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            try {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

                    try {
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    stratDiffrentWebviewTypes();
                                } catch ( Exception ignored ) {
                                }
                            }
                        }, 5000);

                    } catch ( Exception ignored ) {
                    }
                } else {

                    try {
                        loadOffline_When_No_Internet_connection();
                    } catch ( Exception ignored ) {
                    }
                }


            } catch ( Exception ignored ) {
            }
        }

    }




    public void showNotifxDialog(final Context context) {


        String lastNotifxId = preferences.getString("lastId", "");
        if (NotifAvailable & !lastNotifxId.matches(Notif_ID)) {
            try {
                Dialog dialog = new Dialog(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.notif_layout, null, false);
                TextView notifTitle = view.findViewById(R.id.notif_title);
                TextView notifDesc = view.findViewById(R.id.notif_desc);
                TextView closeThis = view.findViewById(R.id.close_notif);
                closeThis.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
                Button notifButton = view.findViewById(R.id.notif_action_button);
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
                ImageView imageView = view.findViewById(R.id.notif_img);
                notifTitle.setText(Notif_title);
                notifDesc.setText(Html.fromHtml(Notif_desc));

                Glide.with(context)
                        .load(Notif_Img_url) // image url
                        .placeholder(R.mipmap.ic_launcher_round) // any placeholder to load at start
                        .error(R.mipmap.ic_launcher_round)  // any image in case of error
                        .into(imageView);  // imageview object


                dialog.setContentView(view);
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


    ///for permissions by powell david


    ///for permissions by powell david


    private void CheckUpdate() {

        try {
            CurrVersion = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch ( PackageManager.NameNotFoundException e ) {
            e.printStackTrace();

        }

//          showToast(mContext,NewVersion+" "+currentVersion);
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
            ConfigureRemoteImageData(drawerMenuItem7ImgUrl, drawerImg7);

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

    private void HandleRemoteCommand(String command) {


        if (command.equals("openSettings")) {

            Intent myactivity = new Intent(WebActivity.this, SettingsActivity.class);
            startActivity(myactivity);

            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedBiometric.edit();
            String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
            editor.remove(did_user_input_passowrd);
            editor.apply();

            //   showToastMessage("Settings is called");

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
        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);
        // Get date of first launch
        Long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least x days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                showRateDialog();
            }
        }
        editor.apply();
    }

    private void showRateDialog() {
        mydialog = new Dialog(this);
//        mydialog.setContentView(R.layout.rating_layout);
//        ratingbar = mydialog.findViewById(R.id.ratingbar);
//        TextView dialoguetxt = mydialog.findViewById(R.id.ratetext);
//        dialoguetxt.setText("If you enjoy using " + getResources().getString(R.string.app_name) + " please take a moment to rate it");

        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(final RatingBar ratingBar, final float rating, final boolean fromUser) {
                if (fromUser) {
                    ratingBar.setRating((float) Math.ceil(rating));
//                    i= ratingbar.getRating();
//                    tst=Float.toString(i);
                }
            }
        });
        mydialog.show();
    }

    @Override
    public void onPause() {

        isAppOpen = false;
        super.onPause();
    }

    @Override
    public void onResume() {
        isAppOpen = true;


        String getUrlFromScanner = getIntent().getStringExtra(Constants.QR_CODE_KEY);

        if (jsonUrl == null) {
            Intent intent = new Intent(mContext, Splash.class);
            startActivity(intent);
            finish();
        }


        try {
            if (getUrlFromScanner != null) {
                if ((getUrlFromScanner.startsWith("https://") || getUrlFromScanner.startsWith("http://"))) {
                    webView.loadUrl(getUrlFromScanner);
                } else {
                    Toast.makeText(mContext, "Required to perform other task", Toast.LENGTH_SHORT).show();
                }

            }
        }catch ( Exception e ){}


//        this.recreate();
        if (ChangeListener) {
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
            startActivity(getIntent());
            overridePendingTransition(0, 0);
            ChangeListener = false;

        }
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        try {

            isAppOpen = false;
            if (LoadLastWebPageOnAccidentalExit) {
                preferences.edit().putString("lasturl", webView.getOriginalUrl()).apply();
            }


            unregisterReceiver(connectivityReceiver);

            if (connectivityReceiver != null) {
                connectivityReceiver = null;
            }

            // Saving a value in SharedPreferences
            SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedBiometric.edit();
            String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
            editor.remove(did_user_input_passowrd);
            editor.apply();


        }catch ( Exception e ){}

        super.onDestroy();


    }

    @Override
    protected void onStop() {
        super.onStop();


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

    private void InitiateComponents() {


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (hasPermissions(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)) {


                    DownloadFile downloadFile = new DownloadFile();
                    downloadFile.execute(splashUrl);
                    Log.d("Remote Execution", "Downloading splash img");
                }
//                        AdvancedControls.DownloadRemoteFile(mContext,splashUrl);


            }
        }, 10000);


        if (ShowWebButton) {
            SharedPreferences sharedBiometric = getApplicationContext().getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);
            String getTvMode = sharedBiometric.getString(Constants.App_Mode, "");

            if (getTvMode.equals(Constants.App_Mode)){
                web_button_root_layout.setVisibility(View.VISIBLE);
            }else {
                web_button_root_layout.setVisibility(View.INVISIBLE);
            }

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


        //Add or change bottombar details here, change bottombar menu items names in  Res > Menu
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


//            webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                @Override
//                public void onScrollChanged() {
//                    if (webView.getScrollY() == 0) {
            swipeView.setEnabled(true);
//                    } else {
//                        swipeView.setEnabled(false);
//                    }
//                }
//            });
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


//            setSupportActionBar(x_toolbar);
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
            drawerItem7.setOnClickListener(imgClk);

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
            HandleRemoteDrawerText(drawerItemtext7, drawerMenuItem7Text);

            HandleRemoteDrawerText(drawer_header_text, drawerHeaderText);
//            drawer_header_text.setText(Html.fromHtml(drawerHeaderText));
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
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
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


    // This is the code to show interstitial ads, this can be placed under a button or event
//            if (ShowInterstitialAd) {
//                if (mInterstitialAd.isLoaded()) {
//                    mInterstitialAd.show();


    private void InitiatePreferences() {

        if (preferences.getBoolean("hidebottombar", false)) {
            ShowBottomBar = false;
        }

        if (preferences.getBoolean("swiperefresh", false)) {
            EnableSwipeRefresh = true;
        }

        if (preferences.getBoolean("darktheme", false)) {
//            x_toolbar.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));
//            bottomToolBar.setBackgroundColor(getResources().getColor(R.color.darkthemeColor));

//            navigationView.setBackgroundColor(getResources().getColor(R.color.darktoolbar));

//            drawer.setBackgroundColor(getResources().getColor(R.color.white));
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

    //For Top Right Option Menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        if (ShowOptionMenu) {
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();

//        if (id == R.id.action_settings) {
//            Intent intent = new Intent(WebActivity.this, SettingsActivity.class);
//            startActivity(intent);
//            return true;
//        } else if (id == R.id.about) {
//
//
//            Intent intent = new Intent(WebActivity.this, About.class);
//            startActivity(intent);
//            return true;
//        } else if (id == R.id.share) {
//
//            ShareItem("Check out this  " + webView.getUrl(), null, null);
//            return true;
//        } else if (id == R.id.exit) {
//            if (ClearCacheOnExit) {
//                webView.clearCache(true);
//            }
//
//            if (LoadLastWebPageOnAccidentalExit) {
//                ClearLastUrl();
//            }
//            finish();
//
//            try {
//                finishAffinity();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//}
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

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
//            errorlayout.setVisibility(View.GONE);


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
//                    errorautoConnect.setText("Auto Reconnect: Standby");

                } else {
                    webView.loadUrl(failingUrl);
                    errorlayout.setVisibility(View.GONE);
//                    webView.clearHistory();
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
                        // Show the dialog by calling startResolutionForResult(), and check the result
                        // in onActivityResult().
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
            //Check if response is positive
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

    //MANAGING BACK BUTTON
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
                    // finish();

                    showExitConfirmationDialog();
//                    SharedPreferences sharedBiometric = getSharedPreferences(Constants.IS_PASSWORD_ADDED, Context.MODE_PRIVATE);
//                    String password_added = sharedBiometric.getString(Constants.password_added, "");
//
//                    if (!password_added.equals(Constants.password_added)){
//                        showExitConfirmationDialog();
//                    }

                }
            }

        }
    }


    private void ShowExitDialogue() {
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
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
//        Toast.makeText(mContext, "called", Toast.LENGTH_SHORT).show();
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

    public void Ratenow(View view) {

        //Only if user gives 5 star rating, app will go to playstore
        String ratingValue = String.valueOf(ratingbar.getRating());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("dontshowagain", true);
        editor.apply();
        if (ratingValue.matches("5.0") | ratingValue.matches("4.0")) {

            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(GooglePlayLink)));
            mydialog.dismiss();
        } else {
            showToast(mContext, "Thanks for your feedback");
            mydialog.dismiss();
        }
    }


    public void RatingNotnow(View view) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        mydialog.dismiss();
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


    public void handleWebBtn(View view) {


        final AlphaAnimation buttonClick = new AlphaAnimation(0.1F, 0.4F);
        view.startAnimation(buttonClick);
        HandleRemoteCommand(Web_button_link);
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("StaticFieldLeak")
    private class DownloadFile extends AsyncTask<String, Integer, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            preferences.edit().putString("splashStarted", "started").apply();
            Log.d("Remote Execution", "Downloading splash img in bg");


//            String filename = "splash.png";
//
//
            mProgressDialog = new ProgressDialog(WebActivity.this);
            // Set your progress dialog Title
//            mProgressDialog.setTitle("Download in Progress");
//            // Set your progress dialog Message
//            mProgressDialog.setMessage("Downloading " + filename);
//            mProgressDialog.setIndeterminate(false);
//            mProgressDialog.setCancelable(false);
            mProgressDialog.setMax(100);
//            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            mProgressDialog.setButton("Hide", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    mProgressDialog.dismiss();
//                }
//            });
            // Show progress dialog
//            mProgressDialog.show();

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

                Log.d(

                        "ANDRO_ASYNC", "Lenght of file: " + fileLength);

                InputStream input = new BufferedInputStream(url.openStream());


//                String hurl = url.toString();
//                Uri hi = Uri.parse(hurl);
//                String fileExtenstion = MimeTypeMap.getFileExtensionFromUrl(hi.toString());
//                String filename = URLUtil.guessFileName(hi.toString(), null, fileExtenstion);

                String filename = "splash.png";

                File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                        Environment.getExternalStorageDirectory()
//                        + "/" + getString(R.string.app_name)
//                        + "/Downloads";
                File file = new File(String.valueOf(path));
//                file.mkdirs();
                File outputFile = new File(file, filename);

//                preferences = getPreferences(MODE_PRIVATE);
//                preferences.edit().putString("downloadedfilepath", path + "/" + filename).apply();

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
//                mProgressDialog.dismiss();

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

//            showToast(getApplicationContext(),"finished"+mProgressDialog.getProgress());
//666


//            File file = new File(name);
//            Toast.makeText(mContext, name, Toast.LENGTH_SHORT).show();
//            Intent testIntent = new Intent(Intent.ACTION_VIEW);
//            testIntent.setType("pdf");
//            Intent intent = new Intent();
//
//            intent.setAction(Intent.ACTION_VIEW);
//            Uri uri = Uri.fromFile(file);
//            intent.setDataAndType(uri, "*/*");
//            try {
//                startActivity(intent);
//            } catch (Exception e) {
//                Toast.makeText(mContext,
//                        "No PDF viewer is available, please download one from Play store",
//                        Toast.LENGTH_LONG).show();
//
//            }
//
//
//        }
//    }
//
//    public String getStringname(String string){
//        String filename = urlEdittext.getText().toString();
//        if (filename.endsWith("mp4")){
//
//        }

//        return filename;
//    }
        }
    }

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

    @SuppressWarnings("StatementWithEmptyBody")
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


            try {
                if (AllowOnlyHostUrlInApp) {
                    if (!url.contains(constants.filterdomain)) {
                        webView.stopLoading();

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                        return true;
                    }
                }

                if (url.startsWith("http://") || url.startsWith("file:///") || url.startsWith("https://") || url.startsWith("setup://"))
                    return false;

            }catch ( Exception e ){
                Log.i(TAG, "shouldOverrideUrlLoading Exception:" + e.getMessage());
                Toast.makeText(mContext, "Invalid JSON format :" + e.getMessage(), Toast.LENGTH_LONG).show();

            }



            //Custom App Opening Handler
//            if (url.contains("opensettings")) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                Intent intent;
//                PackageManager pm =getApplicationContext().getPackageManager();
//                intent=pm.getLaunchIntentForPackage("package name of app that u want open");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);

//            }


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
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

//            view.loadUrl("file:///android_asset/nointernet.html");
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

            // 2. Restore the state to it's original form
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
//            Log.d(TAG, request.getOrigin().toString());
//            Toast.makeText(mContext, "something requested", Toast.LENGTH_SHORT).show();

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
//                if (request.getResources().contains("AUDIO")){
//                    checkPermission(Manifest.permission.RECORD_AUDIO);
//                }

//                request.grant(request.getResources());
            }

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);


//            if (newProgress==50){
//                animatewv(view);
//                view.setVisibility(View.VISIBLE);
//            }

            if (ShowHorizontalProgress) {
                HorizontalProgressBar.setProgress(newProgress);
            }


            String name = preferences.getString("proshow", "");


            if (newProgress == 100) {


//                        if (view.getVisibility() == View.GONE) {
//                            view.setVisibility(View.VISIBLE);
//                            animateWVShow(view);
//                        }


                if (name.equals("show")) {
                    windowProgressbar.setVisibility(View.GONE);
                }


                try {

//                    animatewv(view);
//                    view.setVisibility(View.VISIBLE);
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

            // webSettings.setAppCacheEnabled(true);
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
            mWebviewPop.setDownloadListener(new Downloader());

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


    @SuppressLint("InflateParams")
    private void showExitConfirmationDialog() {


        CustomConfirmExitDialogBinding binding = CustomConfirmExitDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(binding.getRoot());

        final AlertDialog alertDialog = builder.create();



        // Set the background of the AlertDialog to be transparent
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @SuppressLint("CommitPrefEdits")
        SharedPreferences simpleSavedPassword = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);


        EditText editTextText2 =binding.editTextText2;

        TextView textHome = binding.textHome;
        TextView textLoginAdmin2 = binding.textLoginAdmin2;
        TextView textExit = binding.textExit;
        TextView textSettings = binding.textSettings;
        TextView textReSync = binding.textReSync;
        TextView textLaunchOnline = binding.textLaunchOnline;
        TextView textLaunchOffline = binding.textLaunchOffline;
        TextView textForgetPassword = binding.textForgetPasswordHome;
        TextView textCanCellDialog = binding.textCanCellDialog;



        ImageView imgToggle = binding.imgToggle;
        ImageView imgToggleNzotVisible = binding.imgToggleNzotVisible;

        imgToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgToggle.setVisibility(View.INVISIBLE);
                imgToggleNzotVisible.setVisibility(View.VISIBLE);
                editTextText2.setTransformationMethod(null);
                editTextText2.setSelection(editTextText2.length());
            }
        });

        imgToggleNzotVisible.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgToggle.setVisibility(View.VISIBLE);
                imgToggleNzotVisible.setVisibility(View.INVISIBLE);
                editTextText2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                editTextText2.setSelection(editTextText2.length());
            }
        });




        textCanCellDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String imgLaunch = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "");

        SharedPreferences.Editor editor = sharedBiometric.edit();


        String imgEnablePassword = sharedBiometric.getString(Constants.imgEnablePassword, "");
        String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
        String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");


        if (imgEnablePassword.equals(Constants.imgEnablePassword)  ) {
            editTextText2.setText(simpleAdminPassword);
            editTextText2.setEnabled(false);
        } else  if (did_user_input_passowrd.equals(Constants.Did_User_Input_PassWord) ) {
            editTextText2.setEnabled(true);
            editTextText2.setText(simpleAdminPassword);
        }else {
            editTextText2.setEnabled(true);
        }


        textReSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();
                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
                    hideKeyBoard(editTextText2);
                    startActivity(new Intent(getApplicationContext(), ReSyncActivity.class));
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();

                } else {
                    hideKeyBoard(editTextText2);
                    showToastMessage("Wrong password");
                    editTextText2.setError("Wrong password");
                }
            }
        });


        textSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myactivity = new Intent(WebActivity.this, SettingsActivity.class);
                startActivity(myactivity);

                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");
                String editTextText = editTextText2.getText().toString().trim();

                if (editTextText.equals(simpleAdminPassword)) {
                    alertDialog.dismiss();
                    hideKeyBoard(editTextText2);
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();

                }
            }
        });


        textExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        textForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopChangePassowrdDialog();
                alertDialog.dismiss();
            }
        });


        textLaunchOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {

                    loadOffline_When_No_Internet_connection();
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();

                    alertDialog.dismiss();

                } else {
                    hideKeyBoard(editTextText2);
                    editTextText2.setError("Wrong password");
                    showToastMessage("Wrong password");
                }

            }
        });


        textLaunchOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {

                    loadTheMainWebview();
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();

                    alertDialog.dismiss();

                } else {
                    hideKeyBoard(editTextText2);
                    editTextText2.setError("Wrong password");
                    showToastMessage("Wrong password");
                }

            }
        });


        textHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyBoard(editTextText2);
                moveTaskToBack(true);

            }
        });


        textLoginAdmin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");
                String editTextText = editTextText2.getText().toString().trim();


                if (editTextText.equals(simpleAdminPassword)) {
                    alertDialog.dismiss();
                    hideKeyBoard(editTextText2);

                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();

                } else if (imgEnablePassword.equals(Constants.imgEnablePassword)) {
                    alertDialog.dismiss();
                    hideKeyBoard(editTextText2);

                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();

                } else {
                    hideKeyBoard(editTextText2);
                    showSnackBar("Wrong password");
                    editTextText2.setError("Wrong password");
                }


            }
        });

        alertDialog.show();
    }

    @SuppressLint("MissingInflatedId")
    private void showPopChangePassowrdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.custom_forget_password_email_layout, null);

        builder.setView(dialogView);

        final AlertDialog alertDialog222 = builder.create();

        alertDialog222.setCanceledOnTouchOutside(false);
        alertDialog222.setCancelable(false);

        if (alertDialog222.getWindow() != null) {
            alertDialog222.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        final EditText editTextInputUrl = dialogView.findViewById(R.id.eitTextEnterPassword);
        final TextView textContinuPassword = dialogView.findViewById(R.id.textContinuPassword);
        final SwitchCompat imgEnablePassword = dialogView.findViewById(R.id.imgEnablePassword);
        final ImageView imgCloseDialog2 = dialogView.findViewById(R.id.imgCloseDialogForegetPassword);
        final View divider2 = dialogView.findViewById(R.id.divider2);
        final ProgressBar progressBar = dialogView.findViewById(R.id.progressBar);
        final TextView textForGetPassword = dialogView.findViewById(R.id.textForGetPassword);
        // final TextView textSucessfulEmailSent = dialogView.findViewById(R.id.textSucessfulEmailSent);


        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String imgIsemailVisbile = sharedBiometric.getString(Constants.imagEnableEmailVisisbility, "");

        SharedPreferences simpleSavedPassword = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);
        String isSavedEmail = simpleSavedPassword.getString(Constants.isSavedEmail, "");

        // showToastMessage(isSavedEmail.toString());
        if (imgIsemailVisbile.equals(Constants.imagEnableEmailVisisbility)) {
            if (!isSavedEmail.isEmpty()) {
                editTextInputUrl.setText(isSavedEmail + "");
                editTextInputUrl.setEnabled(false);
                textForGetPassword.setText("Continue with  registered email");
            }
        } else {
            editTextInputUrl.setEnabled(true);
            textForGetPassword.setText("Input registered email");
        }


        textContinuPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextText = editTextInputUrl.getText().toString().trim();
                if (isValidEmail(editTextText)) {
                    //  showToastMessage("An email is sent to " + editTextText);
                    showPopEmailForget();
                    hideKeyBoard(editTextInputUrl);
                    alertDialog222.dismiss();
                } else {
                    hideKeyBoard(editTextInputUrl);
                    editTextInputUrl.setError("Wrong email format");
                    showToastMessage("Wrong email format");
                }
            }
        });


        imgCloseDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitConfirmationDialog();
                alertDialog222.dismiss();
                hideKeyBoard(editTextInputUrl);
            }
        });

        alertDialog222.show();
    }

    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @SuppressLint("MissingInflatedId")
    private void showPopEmailForget() {
        // Inflate the custom layout
        CustomEmailSucessLayoutBinding binding = CustomEmailSucessLayoutBinding.inflate(getLayoutInflater());

        // Create AlertDialog Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(binding.getRoot());

        // Create the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Set background drawable to be transparent
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Apply actions to views in the binding
        binding.textEmailSendOkayBtn.setOnClickListener(view -> {
            alertDialog.dismiss();
        });

        // Show the AlertDialog
        alertDialog.show();
    }


    private void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void hideKeyBoard(EditText editText) {
        try {
            editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch ( Exception ignored ) {
        }
    }


    private void showSnackBar(String message) {
        try {
            Snackbar.make(bottomToolBar, message, Snackbar.LENGTH_LONG).show();
        } catch ( Exception ignored ) {
        }
    }



}

