package in.calcuttamedicalstore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.calcuttamedicalstore.MyApplication;
import in.calcuttamedicalstore.model.ProductItem;

public class Utiles {
  public static boolean isRef = false;
  public static boolean isSelect = false;
  public static int seletAddress = 0;
  public static int isvarification = -1;
  public static boolean isrates = false;
  public static List<ProductItem> productItems = new ArrayList<>();

  public static String getDate() {
    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public static String getIMEI(Context context) {

    @SuppressLint("HardwareIds")
    String unique_id =
        Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    // Log.e("unique_id", "-->" + unique_id);
    return unique_id;
  }

  public static boolean internetChack() {
    ConnectivityManager ConnectionManager =
        (ConnectivityManager) MyApplication.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
    return networkInfo != null && networkInfo.isConnected();
  }
}
