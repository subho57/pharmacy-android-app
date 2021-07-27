package in.calcuttamedicalstore;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;

public class MyApplication extends Application {
  private static final String ONESIGNAL_APP_ID = "812ce2b0-4bd1-4e9f-9b9c-a8cd053d837c";

  @SuppressLint("StaticFieldLeak")
  public static Context mContext;

  @Override
  public void onCreate() {
    super.onCreate();
    mContext = this;
    OneSignal.initWithContext(this);
    OneSignal.setAppId(ONESIGNAL_APP_ID);
  }
}
