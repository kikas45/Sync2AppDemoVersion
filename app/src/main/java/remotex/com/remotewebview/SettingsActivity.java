package remotex.com.remotewebview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import remotex.com.remotewebview.additionalSettings.AdditionalSettingsActivity;
import remotex.com.remotewebview.additionalSettings.ReSyncActivity;
import remotex.com.remotewebview.additionalSettings.utils.Constants;
import remotex.com.remotewebview.databinding.CustomConfirmExitDialogBinding;
import remotex.com.remotewebview.databinding.CustomEmailSucessLayoutBinding;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    String Admin_Password = "1234";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darktheme", false)) {
            setTheme(R.style.DarkThemeSettings);
        }
        setContentView(R.layout.settings_activity);

        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
        String imgEnablePassword = sharedBiometric.getString(Constants.imgEnablePassword, "");

        if (!did_user_input_passowrd.equals(Constants.Did_User_Input_PassWord) || imgEnablePassword.equals(Constants.imgEnablePassword)) {
            showExitConfirmationDialog();
        } else {
            //do nothing
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }


    @SuppressLint("InflateParams")
    private void showExitConfirmationDialog() {


        CustomConfirmExitDialogBinding binding = CustomConfirmExitDialogBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(binding.getRoot());

        final AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);

        // Set the background of the AlertDialog to be transparent
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        @SuppressLint("CommitPrefEdits")
        SharedPreferences simpleSavedPassword = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);


        EditText editTextText2 = binding.editTextText2;

        TextView textHome = binding.textHome;
        TextView textLoginAdmin2 = binding.textLoginAdmin2;
        TextView textExit = binding.textExit;
        TextView textSettings = binding.textSettings;
        TextView textReSync = binding.textReSync;
        TextView textLaunch = binding.textLaunch;
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


        if (imgEnablePassword.equals(Constants.imgEnablePassword)) {
            editTextText2.setText(simpleAdminPassword);
            editTextText2.setEnabled(false);
            showToastMessage("edit  input password filled");
        } else if (did_user_input_passowrd.equals(Constants.Did_User_Input_PassWord)) {
            editTextText2.setEnabled(true);
            editTextText2.setText(simpleAdminPassword);
        } else {
            editTextText2.setEnabled(true);
            //editTextText2.setGravity(Gravity.CENTER);
        }


        if (imgLaunch.equals(Constants.imgAllowLunchFromOnline)) {
            textLaunch.setText("Launch offline");
        } else {
            textLaunch.setText("Launch online");
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
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");
                String editTextText = editTextText2.getText().toString().trim();
                if (editTextText.equals(simpleAdminPassword)) {
                    alertDialog.dismiss();
                    hideKeyBoard(editTextText2);
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();
                    alertDialog.dismiss();
                } else {
                    editTextText2.setError("Wrong password");
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


        textLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
                String imgLaunch = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "");
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                showToastMessage("Logic ongoing");

       /*         if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
                    if (imgLaunch.equals(Constants.imgAllowLunchFromOnline)) {
                        String fileName = "index.html";
                        File unzipLocation = new File(
                                Environment.getExternalStorageDirectory().getAbsolutePath(),
                                "Syn2AppLive/WebPageZip/" + fileName
                        );

                        if (unzipLocation.exists()) {
                            ;
                            //  String filePath = "file://" + unzipLocation.getAbsolutePath();
                            String filePath = "https://www.cnbc.com/world/?region=world";


                            showToastMessage(filePath);
                        } else {
                            showToastMessage("location not found");
                        }

                        editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                        editor.apply();

                        alertDialog.dismiss();

                    } else {
                        showToastMessage("App already load online");
                        hideKeyBoard(editTextText2);
                        alertDialog.dismiss();

                        editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                        editor.apply();

                    }
                } else {
                    hideKeyBoard(editTextText2);
                    editTextText2.setError("Wrong password");
                    showToastMessage("Wrong password");
                }
*/

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

        //  alertDialog222.setCanceledOnTouchOutside(false);
        //  alertDialog222.setCancelable(false);

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
                    //    showToastMessage("An email is sent to " + editTextText);
                    hideKeyBoard(editTextInputUrl);
                    alertDialog222.dismiss();
                    showPopEmailForget();
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
                alertDialog222.dismiss();
                showExitConfirmationDialog();
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


    private void showToastMessage(String messages) {
        try {
            Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_SHORT).show();
        } catch ( Exception ignored ) {
        }
    }

    private void hideKeyBoard(EditText editText) {
        try {
            editText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch ( Exception ignored ) {
        }
    }


    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.preferences, rootKey);


            SwitchPreference swipe_switch = findPreference("swiperefresh");
            EditTextPreference serverUrl_field = findPreference("surl");


            SwitchPreference hide_bottom_switch = findPreference("hidebottombar");
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
            Preference additionalSettingsPreference = findPreference("additional_settings_key");


            SharedPreferences sharedBiometric = getContext().getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedBiometric.edit();

            String getTvMode = sharedBiometric.getString(Constants.App_Mode, "");
            String get_AppMode = sharedBiometric.getString(Constants.App_Mode, "");

            if (appModeOrTvMode != null) {


                if (getTvMode.equals(Constants.TV_Mode)) {
                    appModeOrTvMode.setChecked(true);
                }

                if (get_AppMode.equals(Constants.App_Mode)) {
                    appModeOrTvMode.setChecked(false);
                }


                Handler handler1 = new Handler(Looper.getMainLooper());
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        editor.remove(Constants.App_Mode);
                        editor.remove(Constants.TV_Mode);
                    }

                }, 300);

                appModeOrTvMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
                    @Override
                    public boolean onPreferenceChange(Preference arg0, Object isChanged) {
                        boolean isItemOn = (Boolean) isChanged;
                        if (isItemOn) {

                            WebActivity.ChangeListener = true;
                            appModeOrTvMode.setTitle("Tv Mode");

                            editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.MY_TV_OR_APP_MODE);
                            editor.remove(Constants.App_Mode);
                            editor.apply();

                            //  fullscr_switch.setChecked(false);
                            autoToolbar_switch.setChecked(true);
                            hide_bottom_switch.setChecked(true);
                            swipe_switch.setChecked(true);
                            immersive_switch.setChecked(true);


                        } else {

                            appModeOrTvMode.setTitle("App Mode");
                            WebActivity.ChangeListener = true;
                            editor.remove(Constants.MY_TV_OR_APP_MODE);
                            editor.putString(Constants.App_Mode, Constants.App_Mode);
                            editor.apply();


                            //   fullscr_switch.setChecked(true);
                            autoToolbar_switch.setChecked(false);
                            hide_bottom_switch.setChecked(false);
                            swipe_switch.setChecked(false);
                            immersive_switch.setChecked(false);


                        }
                        return true;
                    }
                });

            }


            if (additionalSettingsPreference != null) {
                additionalSettingsPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        Intent intent = new Intent(getActivity(), AdditionalSettingsActivity.class);
                        startActivity(intent);

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

                        } else {
                            WebActivity.ChangeListener = true;
                        }
                        return true;
                    }
                });

            }

            if (hide_bottom_switch != null) {
                hide_bottom_switch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
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
