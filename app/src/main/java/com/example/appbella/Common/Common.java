package com.example.appbella.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.appbella.Model.Addon;
import com.example.appbella.Model.Favorite;
import com.example.appbella.Model.LocationHelper;
import com.example.appbella.Model.Product;
import com.example.appbella.Model.ProductCategory;
import com.example.appbella.Model.Service;
import com.example.appbella.Model.ServiceCategory;
import com.example.appbella.Model.User;
import com.example.appbella.R;
import com.example.appbella.Retrofit.IFCMService;
import com.example.appbella.Retrofit.RetrofitClient;

import java.util.HashSet;
import java.util.Set;

public class Common {

    public static final String API_RESTAURANT_ENDPOINT = "http://192.168.0.13:3000/";

    // later, secure it with Firebase Remote Config
    public static final String API_KEY = "1234";
    public static final int DEFAULT_COLUMN_COUNT = 0;
    public static final int FULL_WIDTH_COLUMN = 1;
    public static final String REMEMBER_FBID = "REMEMBER_FBID";
    public static final String API_KEY_TAG = "API_KEY";
    public static final String NOTIFIC_TITLE = "title";
    public static final String NOTIFIC_CONTENT = "content";

    public static User currentUser;
    public static Addon currentAddon;
    public static ServiceCategory currentServiceCategory;
    public static Set<Addon> addonList = new HashSet<>();
    public static Favorite currentFavorite;
    public static String user;
    public static LocationHelper currentlocation;
    public static Product currentProduct;
    public static Service currentService;
    public static ProductCategory currentProductCategory;

    public static IFCMService getFCMService() {
        return RetrofitClient.getInstance("https://fcm.googleapis.com/").create(IFCMService.class);
    }

    public static String convertStatusToString(int orderStatus) {
        switch (orderStatus) {
            case 0:
                return "Placed";
            case 1:
                return "Shipping";
            case 2:
                return "Shipped";
            case -1:
                return "Cancelled";
            default:
                return "Cancelled";
        }
    }

    public static void showNotification(Context context, int notiId, String title, String body, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null) {
            pendingIntent = PendingIntent.getActivity(context, notiId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        String NOTIFICATION_CHANNEL_ID = "ydkim2110_restaurant";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "My restaurant Notification", NotificationManager.IMPORTANCE_DEFAULT);

            notificationChannel.setDescription("My restaurant Client App");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                NOTIFICATION_CHANNEL_ID);

        builder.setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon));

        if (pendingIntent != null) {
            builder.setContentIntent(pendingIntent);
        }

        Notification mNotification = builder.build();
        notificationManager.notify(notiId, mNotification);


    }
}
