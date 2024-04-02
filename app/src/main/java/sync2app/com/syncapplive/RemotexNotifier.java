package sync2app.com.syncapplive;

import static sync2app.com.syncapplive.constants.NotifAvailable;
import static sync2app.com.syncapplive.constants.NotifLinkExternal;

import static sync2app.com.syncapplive.constants.NotifSound;
import static sync2app.com.syncapplive.constants.Notif_ID;
import static sync2app.com.syncapplive.constants.Notif_Img_url;
import static sync2app.com.syncapplive.constants.Notif_Shown;
import static sync2app.com.syncapplive.constants.Notif_button_action;
import static sync2app.com.syncapplive.constants.Notif_desc;
import static sync2app.com.syncapplive.constants.Notif_title;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import sync2app.com.syncapplive.additionalSettings.utils.Constants;

public class RemotexNotifier extends Service {
    @Nullable
    Handler handler = new Handler();
    Runnable runnable;
    int notif_frequency = 30000;

    SharedPreferences preferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("RemoteX","RemoteX Notification Service Active!");

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences SIMPLE_SAVED_PASSWORD = getSharedPreferences(Constants.SIMPLE_SAVED_PASSWORD, Context.MODE_PRIVATE);

        String get_tMaster = SIMPLE_SAVED_PASSWORD.getString(Constants.get_editTextMaster, "");
        String get_UserID = SIMPLE_SAVED_PASSWORD.getString(Constants.get_UserID, "");
        String get_LicenseKey = SIMPLE_SAVED_PASSWORD.getString(Constants.get_LicenseKey, "");


        String NotificationUrl = get_tMaster + "/"  + get_UserID + "/" + get_LicenseKey +  "/AppConfig/remotexNotif.json ";


        assert handler != null;
        handler.postDelayed(runnable = new Runnable() {

            public void run() {
                handler.postDelayed(runnable, notif_frequency);


//                        handler.removeCallbacks(runnable);

                NotifApiCall(getApplicationContext(), NotificationUrl);


            }
        }, 10000);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onStart(Intent intent, int startid) {

    }


    public void NotifApiCall(Context context, String url) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        showCustomDialog(getApplicationContext());

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject remoteNotifJson = jsonObject.getJSONObject("remoteNotif");

                            NotifAvailable = remoteNotifJson.getBoolean("liveNotifAvailable");
                            Notif_ID = remoteNotifJson.getString("liveNotifID");
                            String lastNotifxId = preferences.getString("lastId", "");
                            Notif_title = remoteNotifJson.getString("liveNotifTitle");
                            Notif_desc = remoteNotifJson.getString("liveNotifDesc");
                            Notif_Img_url = remoteNotifJson.getString("liveNotifImage");
                            Notif_button_action = remoteNotifJson.getString("liveNotifUrl");

                            NotifLinkExternal = remoteNotifJson.getBoolean("liveNotifLinkExternal");
//                            NotifShowInUi = remoteNotifJson.getBoolean("liveNotifShowInUi");
                            NotifSound = remoteNotifJson.getBoolean("liveNotifSound");
                            Intent intent = new Intent("notifx_ready");    //action: "msg"
                            getApplicationContext().sendBroadcast(intent);

                            if (NotifAvailable & !lastNotifxId.matches(Notif_ID)) {
                                Notif_Shown = false;
                                new Handler().postDelayed(new Runnable() {
                                    public void run() {
                                        String lastPanelID = preferences.getString("lastIdPanel", "");
                                        if (!lastPanelID.matches(Notif_ID)){
                                            SendNotifX(Notif_title, Notif_desc);
                                        }

                                    }
                                }, 5000);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }

                    }


                }, new Response.ErrorListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onErrorResponse(VolleyError error) {
//                infotext.setText("Error occurred! =" + error.toString());

            }
        });


// Add the request to the RequestQueue.
        queue.add(stringRequest);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }

    private void SendNotifX(String Title, String messageBody) {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("lastIdPanel", Notif_ID).apply();

        Intent intent = new Intent(this, WebActivity.class);
        if (Notif_button_action.startsWith("http")|Notif_button_action.startsWith("https")){
            intent.putExtra("url", Notif_button_action);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                null;
        notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(Title)
                .setContentText(Html.fromHtml(messageBody))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}

