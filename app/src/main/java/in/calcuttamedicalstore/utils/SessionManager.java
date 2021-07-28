package in.calcuttamedicalstore.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import in.calcuttamedicalstore.model.Address;
import in.calcuttamedicalstore.model.User;

public class SessionManager {
  public static final String login = "login";
  public static final String isopen = "isopen";
  public static final String userdata = "Userdata";
  public static final String address1 = "address";
  public static final String area = "area";
  public static final String currncy = "currncy";
  public static final String privacy = "privacy_policy";
  public static final String tremcodition = "tremcodition";
  public static final String callsupport = "callsupport";
  public static final String aboutUs = "about_us";
  public static final String contactUs = "contact_us";
  public static final String oMin = "o_min";
  public static final String razKey = "raz_key";
  public static final String tax = "tax";
  public static final String CURRUNCY = "currncy";
  public static final String COUPON = "coupon";
  public static final String COUPONID = "couponid";
  public static final String WALLET = "wallet";
  public static boolean iscart = false;
  private final SharedPreferences mPrefs;
  SharedPreferences.Editor mEditor;

  @SuppressLint("CommitPrefEdits")
  public SessionManager(Context context) {
    mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    mEditor = mPrefs.edit();
  }

  public void setStringData(String key, String val) {
    mEditor.putString(key, val);
    mEditor.commit();
  }

  public String getStringData(String key) {
    return mPrefs.getString(key, "");
  }

  public void setFloatData(String key, float val) {
    mEditor.putFloat(key, val);
    mEditor.commit();
  }

  public float getFloatData(String key) {
    return mPrefs.getFloat(key, 0);
  }

  public void setBooleanData(String key, Boolean val) {
    mEditor.putBoolean(key, val);
    mEditor.commit();
  }

  public boolean getBooleanData(String key) {
    return mPrefs.getBoolean(key, false);
  }

  public void setIntData(String key, int val) {
    mEditor.putInt(key, val);
    mEditor.commit();
  }

  public int getIntData(String key) {
    return mPrefs.getInt(key, 0);
  }

  public User getUserDetails() {
    return new Gson().fromJson(mPrefs.getString(userdata, ""), User.class);
  }

  public void setUserDetails(User val) {
    mEditor.putString(userdata, new Gson().toJson(val));
    mEditor.commit();
  }

  public Address getAddress() {
    return new Gson().fromJson(mPrefs.getString(address1, ""), Address.class);
  }

  public void setAddress(Address val) {
    mEditor.putString(address1, new Gson().toJson(val));
    mEditor.commit();
  }

  public void logoutUser() {
    mEditor.clear();
    mEditor.commit();
  }
}
