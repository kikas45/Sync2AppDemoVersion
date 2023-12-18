package remotex.com.remotewebview.additionalSettings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import remotex.com.remotewebview.R;
import remotex.com.remotewebview.WebActivity;
import remotex.com.remotewebview.additionalSettings.scanutil.CaptureAct;
import remotex.com.remotewebview.additionalSettings.utils.Constants;

public class QRSanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrsan);

        scanCode();

    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }


    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result ->
    {
        if (result.getContents() != null) {

            if (result.getContents().startsWith("https://") || result.getContents().startsWith("http://")) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra(Constants.QR_CODE_KEY, "" + result.getContents());
                startActivity(intent);
                finish();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(QRSanActivity.this);
                builder.setCancelable(false);
                builder.setTitle("Open");
                builder.setMessage(result.getContents());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        shareResultViewSharingIntent(result.getContents());
                        finish();
                        dialogInterface.dismiss();
                    }
                });


                builder.show();
            }}
    });


    public void shareResultViewSharingIntent (String result){
        Intent sharingIntent = new Intent(Intent.ACTION_SENDTO);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, result);
        startActivity(Intent.createChooser(sharingIntent, "Share file"));
    }


}