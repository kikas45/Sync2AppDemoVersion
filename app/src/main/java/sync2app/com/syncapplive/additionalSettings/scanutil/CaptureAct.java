package sync2app.com.syncapplive.additionalSettings.scanutil;

import android.content.Intent;
import android.widget.Toast;

import com.journeyapps.barcodescanner.CaptureActivity;

import sync2app.com.syncapplive.R;
import sync2app.com.syncapplive.WebActivity;

public class CaptureAct extends CaptureActivity

{

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), WebActivity.class);
        startActivity(intent);
        finish();
    }
}
