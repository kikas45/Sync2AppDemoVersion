package sync2app.com.syncapplive.additionalSettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import sync2app.com.syncapplive.R;
import sync2app.com.syncapplive.additionalSettings.myApiDownload.FilesViewModel;
import sync2app.com.syncapplive.additionalSettings.myService.IntervalApiServiceSync;
import sync2app.com.syncapplive.additionalSettings.myService.OnChnageService;
import sync2app.com.syncapplive.additionalSettings.myService.SyncInterval;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;
import sync2app.com.syncapplive.additionalSettings.utils.ServiceUtils;

public class WebbyTestActivity extends AppCompatActivity {
    private FilesViewModel mUserViewModel;


    private int totalFiles = 0;
    private int currentDownloadIndex = 0;

    private CountDownTimer countdownTimer;

    private boolean isSyncRunning = false;


    private Button btnDownload;
    private TextView countDownTime;
    private TextView textStatusProcess;
    private TextView textDownladByes;
    private TextView textSynIntervals;
    private TextView textSyncMode;
    private TextView textLocation;
    private TextView textFilecount;
    private TextView textFileChange;

    private ProgressBar progressBarPref;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webby_test);

        mUserViewModel = new ViewModelProvider(this).get(FilesViewModel.class);

        btnDownload = findViewById(R.id.btnDownload);
        countDownTime = findViewById(R.id.countDownTime);
        textStatusProcess = findViewById(R.id.textStatusProcess);
        textDownladByes = findViewById(R.id.textDownladByes);
        textSynIntervals = findViewById(R.id.textSynIntervals);
        textSyncMode = findViewById(R.id.textSyncMode);
        textLocation = findViewById(R.id.textLocation);
        textFilecount = findViewById(R.id.textFilecount);
        textFileChange = findViewById(R.id.textFileChange);
        progressBarPref = findViewById(R.id.progressBarPref);


        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                stopService(new Intent(getApplicationContext(), IntervalApiServiceSync.class));
                stopService(new Intent(getApplicationContext(), SyncInterval.class));
                stopService(new Intent(getApplicationContext(), OnChnageService.class));
                if (!ServiceUtils.foregroundServiceMyAPiSyncInterval(getApplicationContext())) {
                    startService(new Intent(getApplicationContext(), IntervalApiServiceSync.class));

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter333 = new IntentFilter(Constants.RECIVER_PROGRESS);
        registerReceiver(Reciver_Progress, filter333);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(Reciver_Progress);
    }

    private final BroadcastReceiver Reciver_Progress = new BroadcastReceiver() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(Constants.RECIVER_PROGRESS)) {

                updateSyncView();

                myDownloadStatus();

            }
        }
    };


    @SuppressLint("SetTextI18n")
    private void updateSyncView() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                try {
                    SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);

                    //   TextView textStatusProcess = findViewById(R.id.textStatusProcess);
                    String numberOfFiles = my_DownloadClass.getString(Constants.numberOfFiles, "");
                    String filesChange = my_DownloadClass.getString(Constants.filesChange, "");
                    String dnBytes = my_DownloadClass.getString(Constants.textDownladByes, "");
                    String dnProgress = my_DownloadClass.getString(Constants.progressBarPref, "");

                    String get_progress = my_DownloadClass.getString(Constants.SynC_Status, "");



                    if (!get_progress.isEmpty()) {
                        textStatusProcess.setText(get_progress + "");
                    } else {
                        textStatusProcess.setText("PR: Running");
                    }



                    if (!numberOfFiles.isEmpty()) {
                        textFilecount.setText(numberOfFiles + "");
                    } else {
                        textFilecount.setText("NF:--");
                    }

                    if (!filesChange.isEmpty()) {
                        textFileChange.setText(filesChange + "");
                    } else {
                        textFileChange.setText("CF:--");
                    }

                    if (!dnBytes.isEmpty() && !dnBytes.equals("100%")) {
                        textDownladByes.setText(dnBytes + "");
                        textDownladByes.setVisibility(View.VISIBLE);
                    } else {
                        textDownladByes.setVisibility(View.GONE);
                    }


                    if (!dnProgress.isEmpty() && !dnProgress.equals("100")) {
                        int dnSieze = Integer.parseInt(dnProgress.toString());
                        progressBarPref.setProgress(dnSieze);
                        progressBarPref.setVisibility(View.VISIBLE);
                    } else {
                        progressBarPref.setVisibility(View.GONE);
                    }


                } catch ( Exception e ) {
                }


            }
        });


    }


    private void myDownloadStatus() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                try {
                    SharedPreferences my_DownloadClass = getSharedPreferences(Constants.MY_DOWNLOADER_CLASS, Context.MODE_PRIVATE);

                    String get_progress = my_DownloadClass.getString(Constants.SynC_Status, "");


                    if (!get_progress.isEmpty()) {
                        textStatusProcess.setText(get_progress + "");
                    } else {
                        textStatusProcess.setText("PR: Running");
                    }
                } catch ( Exception e ) {
                }


            }
        });

    }


}
