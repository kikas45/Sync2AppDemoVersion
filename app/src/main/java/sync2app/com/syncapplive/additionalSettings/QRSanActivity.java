package sync2app.com.syncapplive.additionalSettings;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sync2app.com.syncapplive.MyApplication;
import sync2app.com.syncapplive.R;
import sync2app.com.syncapplive.WebActivity;
import sync2app.com.syncapplive.additionalSettings.scanutil.CaptureAct;
import sync2app.com.syncapplive.additionalSettings.utils.Constants;

public class QRSanActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrsan);
        MyApplication.decrementRunningActivities();
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

            String content = result.getContents();


            // Check for email
            if (content.contains("MATMSG:TO") && content.contains("SUB") && content.contains("BODY")) {

                String emailTo = extractValue(content, "MATMSG:TO:");
                String emailSubject = extractValue(content, "SUB:");
                String emailBody = extractValue(content, "BODY:");

                sendEmail(emailTo, emailSubject, emailBody);


            }

            // Check for website link
            else if (content.startsWith("https://") || content.startsWith("http://")) {

                if (content.contains("https://wa.me/")) {
                    openWhatsApp(result.getContents());

                }else {
                    Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                    intent.putExtra(Constants.QR_CODE_KEY, "" + result.getContents());
                    startActivity(intent);
                    finish();

                }



            }

            // Check for phone number
            else if (content.contains("tel:")) {

                Pattern phoneNumberPattern = Pattern.compile("^tel:\\+?[0-9]+$");
                Matcher matcher = phoneNumberPattern.matcher(content);
                if (matcher.find()) {
                    String phoneNumber = matcher.group().substring("tel:".length());
                    makeCall(phoneNumber);
                } else {
                    showToast("No valid phone number found");
                }

            }

            // Check for SMS
            else if (content.contains("SMSTO:")) {
                String phoneNumber = extractValueSms(content, "SMSTO:");
                if (!phoneNumber.isEmpty()) {
                    // Send SMS with the extracted phone number
                    sendSMS(phoneNumber);
                } else {
                    showToast("No phone number found for SMS");
                }


            }

            else {
                sendPlainText(content);
            }




        }
    });


    private String extractValue(String text, String startTag) {
        int startIndex = text.indexOf(startTag);
        if (startIndex != -1) {
            startIndex += startTag.length();
            int endIndex = text.indexOf(";", startIndex);
            if (endIndex != -1) {
                return text.substring(startIndex, endIndex);
            }
        }
        return "";
    }




    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


    void sendEmail(String emailTo, String emailSubject, String emailBody) {


        String uriText = "mailto:" + Uri.encode(emailTo) +
                "?subject=" + Uri.encode(emailSubject) +
                "&body=" + Uri.encode(emailBody);

        Uri uri = Uri.parse(uriText);

        // Intent for sending email
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(uri);

        try {
            startActivity(emailIntent);
            finish();
        } catch (ActivityNotFoundException e) {
            // Handle case where no email app is available
            Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show();
        }
    }


    void makeCall( String phoneNumber ) {


        // Create intent to dial the phone number
        Intent callIntent = new Intent(Intent.ACTION_DIAL);

        // Set the phone number in the data field of the intent
        callIntent.setData(Uri.parse("tel:" + Uri.encode(phoneNumber)));

        try {
            // Start the activity to initiate the call
            startActivity(callIntent);
            finish();
        } catch (ActivityNotFoundException e) {
            // Handle case where no dialer app is available
            Toast.makeText(this, "No dialer app found", Toast.LENGTH_SHORT).show();
        }
    }



    void sendSMS(String phoneNumber) {
        //String phoneNumber = "+2349013337583";

        // Create intent to send an SMS
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);

        // Set the phone number in the data field of the intent
        smsIntent.setData(Uri.parse("smsto:" + Uri.encode(phoneNumber)));

        try {
            // Start the activity to initiate the SMS
            startActivity(smsIntent);
            finish();
        } catch (ActivityNotFoundException e) {
            // Handle case where no SMS app is available
            Toast.makeText(this, "No SMS app found", Toast.LENGTH_SHORT).show();
        }
    }


    void sendPlainText(String text) {
        // Create the intent to send plain text
        Intent sendIntent = new Intent(Intent.ACTION_SEND);

        // Set the type of the content
        sendIntent.setType("text/plain");

        // Set the text content
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);

        try {
            // Start the activity to open the app for sending plain text
            startActivity(Intent.createChooser(sendIntent, "Send text via:"));
            finish();
        } catch (ActivityNotFoundException e) {
            // Handle case where no app is available for sending text
            Toast.makeText(this, "No app found to send text", Toast.LENGTH_SHORT).show();
        }
    }




    void openWhatsApp(String result) {
        try {
            URI uri = new URI(result);

            // Get the phone number from the path of the URI
            String phoneNumber = uri.getPath().replace("/", "");

            // Remove any non-numeric characters from the phone number
            phoneNumber = phoneNumber.replaceAll("[^0-9]", "");

            // Check if the phone number is empty
            if (phoneNumber.isEmpty()) {
                throw new IllegalArgumentException("Phone number not found in the URI");
            }

            // Get the text parameter from the query part of the URI
            String query = uri.getQuery();
            String text = null;
            if (query != null) {
                String[] queryParams = query.split("&");
                for (String param : queryParams) {
                    if (param.startsWith("text=")) {
                        text = param.substring("text=".length());
                        break;
                    }
                }
            }

            // Encode the text message
            String encodedText = text != null ? Uri.encode(text) : "";

            // Create the URI for sending a WhatsApp message
            Uri whatsappUri = Uri.parse("https://wa.me/" + phoneNumber + "?text=" + encodedText);

            // Create the intent to open WhatsApp
            Intent intent = new Intent(Intent.ACTION_VIEW, whatsappUri);

            // Start the activity to open WhatsApp
            startActivity(intent);
            finish();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Phone number not found in the URI", Toast.LENGTH_SHORT).show();
        } catch (ActivityNotFoundException e) {
            // Handle case where WhatsApp is not installed
            Toast.makeText(this, "WhatsApp is not installed", Toast.LENGTH_SHORT).show();
        }
    }



    private String extractValueSms(String text, String startTag) {
        int startIndex = text.indexOf(startTag);
        if (startIndex != -1) {
            startIndex += startTag.length();
            int endIndex = text.indexOf(":", startIndex);
            if (endIndex != -1) {
                return text.substring(startIndex, endIndex);
            }
        }
        return "";
    }


    public void shareResultViewSharingIntent (String result){
        Intent sharingIntent = new Intent(Intent.ACTION_SENDTO);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, result);
        startActivity(Intent.createChooser(sharingIntent, "Share file"));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        MyApplication.incrementRunningActivities();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}