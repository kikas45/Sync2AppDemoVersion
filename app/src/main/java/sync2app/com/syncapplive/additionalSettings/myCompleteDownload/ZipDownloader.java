package sync2app.com.syncapplive.additionalSettings.myCompleteDownload;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;


public class ZipDownloader extends AsyncTask<String, String, String> {

    private DownloadHelper helper;

    public ZipDownloader(DownloadHelper helper) {
        this.helper = helper;
    }

    public void onPreExecute() {
        super.onPreExecute();
        this.helper.whenExecutionStarts();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        this.cancel(true);
    }

    public String doInBackground(String... f_url) {
        try {

            //get strings
            String fileUrl = f_url[0];
            String theFileDestination = f_url[1];

            //check file
            File theDes = new File(theFileDestination);
            if (theDes.exists()){
                theDes.delete();
            }

            //parse url and connect to ftp
            URL url = new URL(fileUrl);
            URLConnection connection = url.openConnection();
            connection.connect();

            //get length of file for progress calculation
            int lenghtOfFile = connection.getContentLength();

            //save file to phone memory
            InputStream input = new BufferedInputStream(url.openStream(), 8192);
            OutputStream output = new FileOutputStream(theFileDestination, false);
            byte[] data = new byte[1024];
            long total = 0;
            while (true) {
                int read = input.read(data);
                int count = read;
                if (read != -1) {
                    total += (long) count;
                    try {
                        publishProgress(""+(int)((total*100)/lenghtOfFile));
                        output.write(data, 0, count);
                    } catch (Exception e) {
                        e = e;
                        Log.e("Error Occurred: ", e.getMessage());
                        return null;
                    }
                } else {
                    output.flush();
                    output.close();
                    input.close();
                    return null;
                }
            }


        } catch (Exception e2) {
            Log.e("Error Occurred: ", e2.getMessage());
            this.helper.afterExecutionIsComplete();
            return null;
        }
    }

    /* access modifiers changed from: protected */
    public void onProgressUpdate(String... progress) {
        this.helper.whileInProgress(Integer.parseInt(progress[0]));
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(String file_url) {
        this.helper.afterExecutionIsComplete();
    }

}
