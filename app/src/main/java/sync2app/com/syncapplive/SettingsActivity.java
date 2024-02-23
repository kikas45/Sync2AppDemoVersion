package sync2app.com.syncapplive;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


import java.util.regex.Matcher;
import java.util.regex.Pattern;


import sync2app.com.syncapplive.additionalSettings.AdditionalSettingsActivity;
import sync2app.com.syncapplive.additionalSettings.MainHelpers.GMailSender;
import sync2app.com.syncapplive.additionalSettings.MaintenanceActivity;
import sync2app.com.syncapplive.additionalSettings.PasswordActivity;
import sync2app.com.syncapplive.additionalSettings.ReSyncActivity;
import sync2app.com.syncapplive.additionalSettings.myService.NotificationService;
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;
import sync2app.com.syncapplive.databinding.CustomConfirmExitDialogBinding;
import sync2app.com.syncapplive.databinding.CustomEmailSucessLayoutBinding;
import sync2app.com.syncapplive.databinding.CustomForgetPasswordEmailLayoutBinding;
import sync2app.com.syncapplive.databinding.CustomSettingsPageBinding;
import sync2app.com.syncapplive.databinding.ProgressDialogLayoutBinding;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences preferences;

    String Admin_Password = "1234";
    Handler handler = new Handler();

    private Dialog customProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean("darktheme", false)) {
            setTheme(R.style.DarkThemeSettings);
        }
        setContentView(R.layout.settings_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MyApplication.incrementRunningActivities();



        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
        String imgEnablePassword = sharedBiometric.getString(Constants.imgEnablePassword, "");

    /*    if (!did_user_input_passowrd.equals(Constants.Did_User_Input_PassWord) || imgEnablePassword.equals(Constants.imgEnablePassword)) {
            showExitConfirmationDialog();
        } else {
            //do nothing
        }*/


        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        showExitConfirmationDialog();


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.decrementRunningActivities();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), WebActivity.class));
        finish();


    }
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;

        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo != null && networkInfo.isConnected();
    }


    private void sendMessage(final String reciverEmail, final String myMessage) {


        showCustomProgressDialog("Sending Email");

        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(Constants.Sender_email_Address, Constants.Sender_email_Password);
                    sender.sendMail(Constants.Subject, "Your Password is\n" + myMessage, Constants.Sender_name, reciverEmail);
                    Log.d("mylog", "Email Sent Successfully");

                    // Show toast message on UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show_Pop_Up_Email_Sent_Sucessful("Email sent", "Kindly check email to view password");
                            customProgressDialog.dismiss();
                        }
                    });


                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());

                    // Show toast message on UI thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            show_Pop_Up_Email_Sent_Sucessful("Failed!", "Unable to send email");
                            customProgressDialog.dismiss();
                        }
                    });
                }
            }
        });
        sender.start();
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private void showCustomProgressDialog(String message) {
        try {
            customProgressDialog = new Dialog(this);
            ProgressDialogLayoutBinding binding = ProgressDialogLayoutBinding.inflate(LayoutInflater.from(this));
            customProgressDialog.setContentView(binding.getRoot());
            customProgressDialog.setCancelable(false);
            customProgressDialog.setCanceledOnTouchOutside(false);
            customProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            binding.textLoading.setText(message);

            binding.imgCloseDialog.setVisibility(View.GONE);
            binding.imagSucessful.setBackground(getApplicationContext().getDrawable(R.drawable.ic_email_read_24));

            customProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
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
        TextView textSettings = binding.textAppSettings;
        TextView textAppAdmin = binding.textAppAdmin;
        TextView textReSync = binding.textReSync;
        TextView textLaunchOnlineJson = binding.textLaunchOnlineJson;
        TextView textLaunchOnline = binding.textLaunchOnline;
        TextView textLaunchOffline = binding.textLaunchOffline;
        TextView textForgetPassword = binding.textForgetPasswordHome;
        TextView textCanCellDialog = binding.textCanCellDialog;
        ImageView imagePassowrdSettings = binding.imagePassowrdSettings;
        ImageView imgClearCatch = binding.imgClearCatch;
        ImageView imgWifi = binding.imgWifi;
        ImageView imgMaintainace = binding.imgMaintainace;


        ImageView imgToggle = binding.imgToggle;
        ImageView imgToggleNzotVisible = binding.imgToggleNzotVisible;


        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedBiometric.edit();

        String imgLaunch = sharedBiometric.getString(Constants.imgAllowLunchFromOnline, "");

        String imgEnablePassword = sharedBiometric.getString(Constants.imgEnablePassword, "");
        String did_user_input_passowrd = sharedBiometric.getString(Constants.Did_User_Input_PassWord, "");
        String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");


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
                SharedPreferences sharedBiometric = getApplicationContext().getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);
                String getTvMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");

                startActivity(new Intent(getApplicationContext(), WebActivity.class));
                finish();


            }
        });


        if (imgEnablePassword.equals(Constants.imgEnablePassword)) {
            editTextText2.setText(simpleAdminPassword);
            editTextText2.setEnabled(false);
        } else if (did_user_input_passowrd.equals(Constants.Did_User_Input_PassWord)) {
            editTextText2.setEnabled(true);
            editTextText2.setText(simpleAdminPassword);
        } else {
            editTextText2.setEnabled(true);
        }


        textReSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();
                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
                    hideKeyBoard(editTextText2);

                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                    editor.putString(Constants.SAVE_NAVIGATION, Constants.SettingsPage);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), ReSyncActivity.class));
                    finish();
                    alertDialog.dismiss();

                } else {
                    hideKeyBoard(editTextText2);
                    showToastMessage("Wrong password");
                    editTextText2.setError("Wrong password");
                }
            }
        });


        imagePassowrdSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();
                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
                    hideKeyBoard(editTextText2);
                    startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();
                    alertDialog.dismiss();

                } else {
                    hideKeyBoard(editTextText2);
                    showToastMessage("Wrong password");
                    editTextText2.setError("Wrong password");
                }
            }
        });


        imgWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();
                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
                    hideKeyBoard(editTextText2);
                    Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                    startActivity(intent);
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();
                    alertDialog.dismiss();

                } else {
                    hideKeyBoard(editTextText2);
                    showToastMessage("Wrong password");
                    editTextText2.setError("Wrong password");
                }
            }
        });


        imgClearCatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
                    hideKeyBoard(editTextText2);
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);

                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);
                    editor.apply();
                    alertDialog.dismiss();
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
                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {
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


        textAppAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");
                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {


                    hideKeyBoard(editTextText2);
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                    editor.putString(Constants.SAVE_NAVIGATION, Constants.SettingsPage);
                    editor.apply();

                    Intent myactivity = new Intent(SettingsActivity.this, AdditionalSettingsActivity.class);
                    startActivity(myactivity);
                    finish();

                    alertDialog.dismiss();


                } else {

                    hideKeyBoard(editTextText2);
                    showToastMessage("Wrong password");
                    editTextText2.setError("Wrong password");
                }
            }
        });


        imgMaintainace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");
                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {


                    hideKeyBoard(editTextText2);
                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                    editor.putString(Constants.SAVE_NAVIGATION, Constants.SettingsPage);
                    editor.apply();

                    Intent myactivity = new Intent(SettingsActivity.this, MaintenanceActivity.class);
                    startActivity(myactivity);
                    finish();

                    alertDialog.dismiss();


                } else {

                    hideKeyBoard(editTextText2);
                    showToastMessage("Wrong password");
                    editTextText2.setError("Wrong password");
                }
            }
        });


        textExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                stopService(new Intent(SettingsActivity.this, NotificationService.class));
                stopService(new Intent(SettingsActivity.this, OnChnageService.class));
                finish();
                finishAffinity();

            }
        });


        textForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopChangePassowrdDialog();
                alertDialog.dismiss();
            }
        });

        SharedPreferences mydownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorED = mydownloadClass.edit();

        textLaunchOnlineJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {


                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                    editor.remove(Constants.imgAllowLunchFromOnline);
                    editor.apply();

                    editorED.remove(Constants.Tapped_OnlineORoffline);
                    //  editorED.remove(Constants.getFolderClo);
                    // editorED.remove(Constants.getFolderSubpath);
                    //   editorED.remove(Constants.syncUrl);
                    editorED.apply();

                    Intent intent = new Intent(getApplicationContext(), Splash.class);
                    startActivity(intent);
                    finish();

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

                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                SharedPreferences sharedBiometric = getApplicationContext().getSharedPreferences(Constants.SHARED_BIOMETRIC, MODE_PRIVATE);
                String getTvMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");


                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {

                    if (getTvMode.equals(Constants.TV_Mode)) {

                        editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                        editorED.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOnline);
                        //  editorED.remove(Constants.getFolderClo);
                        // editorED.remove(Constants.getFolderSubpath);
                        //   editorED.remove(Constants.syncUrl);
                        editorED.apply();

                        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                        startActivity(intent);
                        finish();

                        alertDialog.dismiss();

                    } else {


                        editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                        editor.remove(Constants.imgAllowLunchFromOnline);
                        editor.apply();

                        editorED.remove(Constants.Tapped_OnlineORoffline);
                        //  editorED.remove(Constants.getFolderClo);
                        // editorED.remove(Constants.getFolderSubpath);
                        //   editorED.remove(Constants.syncUrl);
                        editorED.apply();

                        Intent intent = new Intent(getApplicationContext(), Splash.class);
                        startActivity(intent);
                        finish();

                        alertDialog.dismiss();


                    }


                } else {
                    hideKeyBoard(editTextText2);
                    editTextText2.setError("Wrong password");
                    showToastMessage("Wrong password");
                }

            }
        });

        textLaunchOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

                String editTextText = editTextText2.getText().toString().trim();

                if (imgEnablePassword.equals(Constants.imgEnablePassword) || editTextText.equals(simpleAdminPassword)) {


                    editor.putString(Constants.Did_User_Input_PassWord, Constants.Did_User_Input_PassWord);

                    editorED.putString(Constants.Tapped_OnlineORoffline, Constants.tapped_launchOffline);
                    //  editorED.remove(Constants.getFolderClo);
                    // editorED.remove(Constants.getFolderSubpath);
                    //   editorED.remove(Constants.syncUrl);
                    editorED.apply();

                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    startActivity(intent);
                    finish();

                    alertDialog.dismiss();


                } else {
                    hideKeyBoard(editTextText2);
                    editTextText2.setError("Wrong password");
                    showToastMessage("Wrong password");
                }

            }
        });


        String get_AppMode = sharedBiometric.getString(Constants.MY_TV_OR_APP_MODE, "");


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

        CustomForgetPasswordEmailLayoutBinding binding = CustomForgetPasswordEmailLayoutBinding.inflate(getLayoutInflater());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(binding.getRoot());
        final AlertDialog alertDialog = builder.create();

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setCancelable(false);


        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }


        final TextView editTextInputUrl = binding.eitTextEnterPassword;
        final TextView textContinuPassword = binding.textContinuPassword;
        final ImageView imgCloseDialog2 = binding.imgCloseDialogForegetPassword;
        final TextView textForGetPassword = binding.textForGetPassword;
        final View divider2 = binding.divider2;

        SharedPreferences sharedBiometric = getSharedPreferences(Constants.SHARED_BIOMETRIC, Context.MODE_PRIVATE);
        String imgIsemailVisbile = sharedBiometric.getString(Constants.imagEnableEmailVisisbility, "");

        SharedPreferences simpleSavedPassword = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);
        String isSavedEmail = simpleSavedPassword.getString(Constants.isSavedEmail, "");


        if (imgIsemailVisbile.equals(Constants.imagEnableEmailVisisbility)) {
            if (!isSavedEmail.isEmpty()) {
                editTextInputUrl.setText(isSavedEmail + "");
               // textForGetPassword.setText("Default Email");
                divider2.setVisibility(View.VISIBLE);
                editTextInputUrl.setVisibility(View.VISIBLE);
            }
        } else {
            editTextInputUrl.setEnabled(true);
         //   textForGetPassword.setText("Default Email");
            divider2.setVisibility(View.GONE);
            editTextInputUrl.setVisibility(View.GONE);
        }


        String simpleAdminPassword = simpleSavedPassword.getString(Constants.simpleSavedPassword, "");

        textContinuPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSavedEmail.isEmpty() && isValidEmail(isSavedEmail)) {
                    if (isConnected()) {
                        sendMessage(isSavedEmail, simpleAdminPassword);
                        alertDialog.dismiss();

                    }else {
                        showToastMessage("No internet Connection");
                    }

                } else {
                    showToastMessage("Default Email Reminder not found");
                }
            }
        });


        imgCloseDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                showExitConfirmationDialog();
               // hideKeyBoard(editTextInputUrl);
            }
        });

        alertDialog.show();
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


    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(\\.[a-zA-Z]{2,})?";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    @SuppressLint("MissingInflatedId")
    private void show_Pop_Up_Email_Sent_Sucessful(String title, String body) {
        // Inflate the custom layout
        CustomEmailSucessLayoutBinding binding = CustomEmailSucessLayoutBinding.inflate(getLayoutInflater());

        // Create AlertDialog Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(binding.getRoot());
        alertDialogBuilder.setCancelable(false);

        // Create the AlertDialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // Set background drawable to be transparent
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        // Apply actions to views in the binding
        binding.textEmailSendOkayBtn.setOnClickListener(view -> {

            alertDialog.dismiss();
            showExitConfirmationDialog();
        });

        binding.textSucessful.setText(title);
        binding.textBodyMessage.setText(body);

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


            Preference additionalSettingsPreference = findPreference("additional_settings_key");


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

                            editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.TV_Mode);
                            editor.apply();


                            showPopForTestComfirmantion();

                        } else {

                            appModeOrTvMode.setTitle("Mobile App Mode");
                            WebActivity.ChangeListener = true;

                            editor.putString(Constants.MY_TV_OR_APP_MODE, Constants.App_Mode);
                            editor.apply();

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

