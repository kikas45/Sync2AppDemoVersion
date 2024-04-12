package sync2app.com.syncapplive.additionalSettings.myCompleteDownload;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import java.io.File;
import java.util.List;

import sync2app.com.syncapplive.R;

public class SyncPage extends AppCompatActivity {
    Button btnDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_page);

        btnDownload = findViewById(R.id.btnDownload);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getZipDownloads();
            }
        });
    }





    private void getZipDownloads() {


        //fetch item
        new Thread(() -> {


            String saveMyFileToStorage = "/MyAPiDownloads/App";

            // Adjusting the file path to save the downloaded file
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), saveMyFileToStorage);

            if (!dir.exists()) {
                dir.mkdirs();
            }

            String getFile_name = "index.html";

            String getFileUrl = "https://cloudappserver.co.uk/cp/app_base/public/CLO/DE_MO_2021000/App/index.html";


            File fileDestination = new File(dir.getAbsolutePath(), getFile_name);

            //start download
            new ZipDownloader(new DownloadHelper() {
                @Override
                public void afterExecutionIsComplete() {

                    //update file
                    new Thread(() -> {

                        /// update view model
                        Log.d("getZipDownloads", getFile_name.toString() );

                    }).start();


                }

                @Override
                public void whenExecutionStarts() {

                    Log.d("getZipDownloads", "Started" );
                }

                @Override
                public void whileInProgress(int i) {

                    //set individual progress

                    Log.d("getZipDownloads", "whileInProgress: " + i);


                }

            }).execute(getFileUrl, fileDestination.getAbsolutePath());


        }).start();

    }

}
