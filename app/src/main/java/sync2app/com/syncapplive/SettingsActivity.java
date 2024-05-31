package sync2app.com.syncapplive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


import sync2app.com.syncapplive.additionalSettings.ReSyncActivity;
import sync2app.com.syncapplive.additionalSettings.autostartAppOncrash.Methods;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;
import sync2app.com.syncapplive.databinding.CustomSettingsPageBinding;


public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;
    ImageView close_bs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darktheme", false)) {
            setTheme(R.style.DarkThemeSettings);
        }
        setContentView(R.layout.settings_activity);

        MyApplication.incrementRunningActivities();

        Methods.addExceptionHandler(this);


        close_bs = findViewById(R.id.close_bs);

        close_bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WebActivity.class));
                finish();
            }
        });


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.decrementRunningActivities();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), WebActivity.class));
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Start the WebviewActivity
                Intent intent = new Intent(this, WebActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    public static class SettingsFragment extends PreferenceFragmentCompat {

        @SuppressLint("InflateParams")
        private void showPopForTestComfirmantion() {


            CustomSettingsPageBinding binding = CustomSettingsPageBinding.inflate(getLayoutInflater());
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());

            builder.setView(binding.getRoot());

            final AlertDialog alertDialog = builder.create();

            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.setCancelable(false);

            // Set the background of the AlertDialog to be transparent
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }

            final TextView textForGetPassword = binding.textLoginAdmin2;
            textForGetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();

                }
            });

            alertDialog.show();
        }


        @RequiresApi(api = Build.VERSION_CODES.P)
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);




            SwitchPreference swipe_switch = findPreference("swiperefresh");
            EditTextPreference serverUrl_field = findPreference("surl");


            SwitchPreference location_switch = findPreference("geolocation");
            SwitchPreference dark_switch = findPreference("darktheme");
            SwitchPreference night_switch = findPreference("nightmode");
            SwitchPreference fullscr_switch = findPreference("fullscreen");
            SwitchPreference nativ_loader_switch = findPreference("nativeload");
            SwitchPreference blockAds_switch = findPreference("blockAds");
            SwitchPreference immersive_switch = findPreference("immersive_mode");
            SwitchPreference permissions_switch = findPreference("permission_query");
            SwitchPreference lastpage_switch = findPreference("loadLastUrl");
            SwitchPreference autoToolbar_switch = findPreference("autohideToolbar");
            SwitchPreference appModeOrTvMode = findPreference("appModeOrTvMode");
            SwitchPreference hideQRCode = findPreference("hideQRCode");

            SwitchPreference shwoFloatingButton = findPreference("shwoFloatingButton");
            SwitchPreference hide_bottom_switch = findPreference("hidebottombar");
            SwitchPreference hide_drawer_icon = findPreference("hide_drawer_icon");
            SwitchPreference useOfflineFolderOrNot = findPreference("useOfflineFolderOrNot");
            SwitchPreference enableCacheMode = findPreference("enableCacheMode");


            Preference go_to_sync_page = findPreference("go_to_sync_page");


            SharedPreferences sharedBiometric = getContext().getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);
            SharedPreferences myDownloadClass = getContext().getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedBiometric.edit();
            SharedPreferences.Editor editor3333 = myDownloadClass.edit();


            String get_AppMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");
            String get_hideQRCode = sharedBiometric.getString(Constants.HIDE_QR_CODE, "");

            String get_drawerMenu = sharedBiometric.getString(Constants.HIDE_DRAWER_ICON, "");

            String get_showFloatingButton = sharedBiometric.getString(Constants.SHOW_FLOATING_BAR, "");
            String get_auto_hide_toolbar = sharedBiometric.getString(Constants.AUTO_HIDE_TOOL_BAR, "");
            String get_hide_bottomBar = sharedBiometric.getString(Constants.HIDE_BOTTOM_BAR, "");
            String enable_CacheMode = sharedBiometric.getString(Constants.Enable_CacheMode, "");

            String get_useOfflineFolderOrNot = sharedBiometric.getString(Constants.USE_OFFLINE_FOLDER, "");

            if (get_AppMode.equals(Constants.TV_Mode)) {

                appModeOrTvMode.setChecked(true);

                appModeOrTvMode.setTitle("TV App Mode");

                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.TV_Mode);
                editor.apply();


            } else {
                //   WebActivity.ChangeListener = true;
                appModeOrTvMode.setChecked(false);

                appModeOrTvMode.setTitle("Mobile App Mode");

                editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode);
                editor.apply();


            }


            if (enable_CacheMode.equals(Constants.Enable_CacheMode)) {

                enableCacheMode.setTitle("Disable Cache Mode");
            } else {
                enableCacheMode.setTitle("Enable Cache Mode");
            }


            if (get_auto_hide_toolbar.equals(Constants.AUTO_HIDE_TOOL_BAR)) {
                autoToolbar_switch.setTitle("Auto Hide Toolbar ON");
            } else {
                autoToolbar_switch.setTitle("Auto Hide Toolbar Off");
            }


            if (get_hide_bottomBar.equals(Constants.HIDE_BOTTOM_BAR)) {
                hide_bottom_switch.setTitle("Hide Bottom Bar ( TV  or TouchScreen Mode)");
            } else {
                hide_bottom_switch.setTitle("Show Bottom Bar ( TV  or TouchScreen Mode)");
            }


            if (get_hideQRCode.equals(Constants.HIDE_QR_CODE)) {
                hideQRCode.setTitle("Hide QR Code");
            } else {
                hideQRCode.setTitle("Show QR Code");
            }


            if (get_useOfflineFolderOrNot.equals(Constants.USE_OFFLINE_FOLDER)) {
                useOfflineFolderOrNot.setTitle("If not connected use App offline Folder");

            } else {
                useOfflineFolderOrNot.setTitle("If not connected use App offline Page");


            }


            if (get_showFloatingButton.equals(Constants.SHOW_FLOATING_BAR)) {
                shwoFloatingButton.setTitle("Hide Floating Menu (TV or TouchScreen Mode)");
            } else {

                shwoFloatingButton.setTitle("Show Floating Menu (TV or TouchScreen Mode)");

            }


            if (get_drawerMenu.equals(Constants.HIDE_DRAWER_ICON)) {
                hide_drawer_icon.setTitle("Show Bottom Menu Icon (TV or TouchScreen mode)");
            } else {

                hide_drawer_icon.setTitle("Hide Bottom Menu Icon (TV or TouchScreen mode)");

            }


            if (appModeOrTvMode != null) {


                appModeOrTvMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {

                            WebActivity.ChangeListener = true;
                            appModeOrTvMode.setTitle("TV App Mode");

                            editor.putString(Constants.imgStartAppRestartOnTvMode, Constants.imgStartAppRestartOnTvMode);
                            editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.TV_Mode);
                            editor.apply();


                            showPopForTestComfirmantion();

                        } else {

                            appModeOrTvMode.setTitle("Mobile App Mode");
                            WebActivity.ChangeListener = true;

                            editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode);
                            editor.remove(Constants.imgStartAppRestartOnTvMode);
                            editor.apply();

                        }
                        return true;
                    }
                });

            }


            if (go_to_sync_page != null) {
                go_to_sync_page.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        Intent intent = new Intent(getActivity(), ReSyncActivity.class);
                        startActivity(intent);
                        getActivity().finish();

                        editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                        editor.putString(Constants.SAVE_NAVIGATION, Constants.SettingsPage);
                        editor.apply();

                        return true;
                    }
                });
            }






            if (autoToolbar_switch != null) {
                autoToolbar_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {
                            WebActivity.ChangeListener = true;
                            autoToolbar_switch.setTitle("Auto Hide Toolbar ON");
                            editor.putString(Constants.AUTO_HIDE_TOOL_BAR, Constants.AUTO_HIDE_TOOL_BAR);
                            editor.apply();


                        } else {
                            WebActivity.ChangeListener = true;
                            autoToolbar_switch.setTitle("Auto Hide Toolbar Off");

                            editor.remove(Constants.AUTO_HIDE_TOOL_BAR);
                            editor.apply();

                        }
                        return true;
                    }
                });

            }


            if (enableCacheMode != null) {
                enableCacheMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {

                            enableCacheMode.setTitle("Disable Cache Mode");
                            editor.putString(Constants.Enable_CacheMode, Constants.Enable_CacheMode);
                            editor.apply();

                        } else {
                            enableCacheMode.setTitle("Enable Cache Mode");
                            editor.remove(Constants.Enable_CacheMode);
                            editor.apply();

                        }

                        return true;
                    }
                });

            }


            if (useOfflineFolderOrNot != null) {
                useOfflineFolderOrNot.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {

                            WebActivity.ChangeListener = true;
                            useOfflineFolderOrNot.setTitle("If not connected use App offline Folder");
                            editor.putString(Constants.USE_OFFLINE_FOLDER, Constants.USE_OFFLINE_FOLDER);
                            editor.apply();


                        } else {

                            WebActivity.ChangeListener = true;
                            useOfflineFolderOrNot.setTitle("If not connected use App offline Page");
                            editor.remove(Constants.USE_OFFLINE_FOLDER);
                            editor.apply();


                        }
                        return true;
                    }
                });

            }


            if (hide_drawer_icon != null) {
                hide_drawer_icon.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {

                            hide_drawer_icon.setTitle("Show Bottom Menu Icon (TV or TouchScreen mode)");
                            editor.putString(Constants.HIDE_DRAWER_ICON, Constants.HIDE_DRAWER_ICON);
                            editor.apply();

                        } else {
                            hide_drawer_icon.setTitle("Hide Bottom Menu Icon (TV or TouchScreen mode)");

                            editor.remove(Constants.HIDE_DRAWER_ICON);
                            editor.apply();
                        }
                        return true;
                    }
                });

            }


            /// start of qr code logic

            if (hideQRCode != null) {
                hideQRCode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {
                            // WebActivity.ChangeListener = true;
                            hideQRCode.setTitle("Hide QR Code");
                            editor.putString(Constants.HIDE_QR_CODE, Constants.HIDE_QR_CODE);
                            editor.apply();

                        } else {
                            // WebActivity.ChangeListener = true;
                            hideQRCode.setTitle("Show QR Code");
                            editor.remove(Constants.HIDE_QR_CODE);
                            editor.apply();
                        }
                        return true;
                    }
                });


                if (shwoFloatingButton != null) {
                    shwoFloatingButton.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                        @Override
                        public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                            boolean isItemOn = (Boolean) isChanged;
                            if (isItemOn) {
                                WebActivity.ChangeListener = true;

                                shwoFloatingButton.setTitle("Hide Floating Menu (TV or TouchScreen Mode)");


                                editor.putString(Constants.SHOW_FLOATING_BAR, Constants.SHOW_FLOATING_BAR);
                                editor.apply();

                            } else {
                                WebActivity.ChangeListener = true;

                                shwoFloatingButton.setTitle("Show Floating Menu (TV or TouchScreen Mode)");

                                editor.remove(Constants.SHOW_FLOATING_BAR);
                                editor.apply();
                            }
                            return true;
                        }
                    });


                    /// the end of qr code logic

                    if (hide_bottom_switch != null) {
                        hide_bottom_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                            @Override
                            public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                boolean isItemOn = (Boolean) isChanged;
                                if (isItemOn) {

                                    WebActivity.ChangeListener = true;

                                    editor.putString(Constants.HIDE_BOTTOM_BAR, Constants.HIDE_BOTTOM_BAR);
                                    editor.apply();
                                    hide_bottom_switch.setTitle("Hide Bottom Bar ( TV or TouchScreen Mode)");


                                } else {

                                    hide_bottom_switch.setTitle("Show Bottom Bar ( TV or TouchScreen Mode)");
                                    WebActivity.ChangeListener = true;
                                    editor.remove(Constants.HIDE_BOTTOM_BAR);
                                    editor.apply();

                                }
                                return true;
                            }
                        });

                        if (swipe_switch != null) {
                            swipe_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                @Override
                                public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                    boolean isItemOn = (Boolean) isChanged;
                                    if (isItemOn) {
                                        WebActivity.ChangeListener = true;
                                    } else {
                                        WebActivity.ChangeListener = true;
                                    }
                                    return true;
                                }
                            });

                            if (lastpage_switch != null) {
                                lastpage_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                    @Override
                                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                        boolean isItemOn = (Boolean) isChanged;
                                        if (isItemOn) {
                                            WebActivity.ChangeListener = true;
                                        } else {
                                            WebActivity.ChangeListener = true;
                                        }
                                        return true;
                                    }
                                });
                            }

                            if (location_switch != null) {
                                location_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                    @Override
                                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                        boolean isItemOn = (Boolean) isChanged;
                                        if (isItemOn) {
                                            WebActivity.ChangeListener = true;
                                        } else {
                                            WebActivity.ChangeListener = true;
                                        }
                                        return true;
                                    }
                                });
                            }


                            if (dark_switch != null) {
                                dark_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                    @Override
                                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                        boolean isItemOn = (Boolean) isChanged;
                                        if (isItemOn) {
                                            WebActivity.ChangeListener = true;

                                        } else {
                                            WebActivity.ChangeListener = true;
                                        }
                                        return true;
                                    }
                                });
                            }

                            if (night_switch != null) {
                                night_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                    @Override
                                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                        boolean isItemOn = (Boolean) isChanged;
                                        if (isItemOn) {
                                            WebActivity.ChangeListener = true;
                                        } else {
                                            WebActivity.ChangeListener = true;
                                        }
                                        return true;
                                    }
                                });
                                if (fullscr_switch != null) {
                                    fullscr_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                        @Override
                                        public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                            boolean isItemOn = (Boolean) isChanged;
                                            if (isItemOn) {
                                                WebActivity.ChangeListener = true;
                                            } else {
                                                WebActivity.ChangeListener = true;
                                            }
                                            return true;
                                        }
                                    });


                                    if (nativ_loader_switch != null) {
                                        nativ_loader_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                            @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                            @Override
                                            public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                                boolean isItemOn = (Boolean) isChanged;
                                                if (isItemOn) {
                                                    WebActivity.ChangeListener = true;
                                                } else {
                                                    WebActivity.ChangeListener = true;
                                                }
                                                return true;
                                            }
                                        });
                                        if (blockAds_switch != null) {
                                            blockAds_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                                @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                                @Override
                                                public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                                    boolean isItemOn = (Boolean) isChanged;
                                                    if (isItemOn) {
                                                        WebActivity.ChangeListener = true;
                                                    } else {
                                                        WebActivity.ChangeListener = true;
                                                    }
                                                    return true;
                                                }
                                            });

                                            if (immersive_switch != null) {
                                                immersive_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                                    @Override
                                                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                                        boolean isItemOn = (Boolean) isChanged;
                                                        if (isItemOn) {
                                                            WebActivity.ChangeListener = true;
                                                        } else {
                                                            WebActivity.ChangeListener = true;
                                                        }
                                                        return true;
                                                    }
                                                });

                                                if (permissions_switch != null) {
                                                    permissions_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                                                        @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                                                        @Override
                                                        public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                                                            boolean isItemOn = (Boolean) isChanged;
                                                            if (isItemOn) {
                                                                WebActivity.ChangeListener = true;
                                                            } else {
                                                                WebActivity.ChangeListener = true;

                                                            }
                                                            return true;
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (!constants.ShowServerUrlSetUp) {

                        try {
                            serverUrl_field.setVisible(false);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }

                    }
                    if (!constants.ShowToolbar) {

                        try {
                            autoToolbar_switch.setVisible(false);
                        } catch ( Exception e ) {
                            e.printStackTrace();
                        }

                        if (!constants.ShowBottomBar) {

                            try {
                                hide_bottom_switch.setVisible(false);
                            } catch ( Exception e ) {
                                e.printStackTrace();
                            }
                        }
                    }
                }


            }
        }
    }

}

