package in.calcuttamedicalstore.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.PaymentItem;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.utils.SessionManager;

import static in.calcuttamedicalstore.fragment.HomeFragment.website_logo;
import static in.calcuttamedicalstore.fragment.OrderSumrryFragment.paymentsucsses;
import static in.calcuttamedicalstore.fragment.OrderSumrryFragment.tragectionID;
import static in.calcuttamedicalstore.retrofit.APIClient.baseUrl;

public class RazerpayActivity extends AppCompatActivity implements PaymentResultListener {
  private static final String TAG = RazerpayActivity.class.getSimpleName();
  SessionManager sessionManager;
  double amount = 0;
  User user;
  PaymentItem paymentItem;

  @Override
  public void onCreate(Bundle bundle) {
    super.onCreate(bundle);
    setContentView(R.layout.activity_razorpay);
    createOrder();
    /*
    To ensure faster loading of the Checkout form,
     call this method as early as possible in your checkout flow.
    */
    Checkout.preload(getApplicationContext());
    sessionManager = new SessionManager(this);
    user = sessionManager.getUserDetails();
    amount = getIntent().getIntExtra("amount", 0);
    paymentItem = (PaymentItem) getIntent().getSerializableExtra("detail");
    startPayment();
  }

  public void createOrder() {}

  public void startPayment() {
    /** Reference to current activity */
    final Activity activity = this;
    /** Instantiate Checkout */
    final Checkout co = new Checkout();
    co.setKeyID(paymentItem.getCredValue());
    /** Set your logo here */
    co.setImage(R.drawable.ic_onesignal_large_icon_default);
    /** Pass your payment options to the Razorpay Checkout as a JSONObject */
    try {
      JSONObject options = new JSONObject();
      options.put("key", paymentItem.getCredValue());
      options.put("name", getResources().getString(R.string.app_name));
      options.put("image", baseUrl + website_logo);
      //      options.put("order_id", " ");
      options.put("theme.color", "#FF5A6A");
      options.put("currency", "INR");
      //            double total = Double.parseDouble(amount);
      //            total = total * 100;
      options.put("amount", (int) (amount * 100)); // pass amount in currency subunits
      options.put("remember_customer", true); // Enables card saving feature.
      options.put("send_sms_hash", true); // OTP is auto-read.

      options.put("prefill.name", user.getName());
      options.put("prefill.email", user.getEmail());
      options.put("prefill.contact", user.getMobile());
      JSONObject retryObj = new JSONObject();
      retryObj.put("enabled", true);
      retryObj.put("max_count", 4);
      options.put("retry", retryObj);
      co.open(activity, options);
    } catch (Exception e) {
      Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_LONG).show();
      Log.e(TAG, "Error in starting Razorpay Checkout", e);
      e.printStackTrace();
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
    }
    return super.onOptionsItemSelected(item);
  }

  /**
   * The name of the function has to be onPaymentSuccess Wrap your code in try catch, as shown, to
   * ensure that this method runs correctly
   */
  @SuppressWarnings("unused")
  @Override
  public void onPaymentSuccess(String s) {
    try {
      tragectionID = s;
      paymentsucsses = 1;
      Toast.makeText(this, "Payment Successful: " + s, Toast.LENGTH_SHORT).show();
      finish();
    } catch (Exception e) {
      Log.e(TAG, "Exception in onPaymentSuccess", e);
    }
  }

  /**
   * The name of the function has to be onPaymentError Wrap your code in try catch, as shown, to
   * ensure that this method runs correctly
   */
  @SuppressWarnings("unused")
  @Override
  public void onPaymentError(int i, String s) {
    try {
      Toast.makeText(this, "Payment failed: " + i + " " + s, Toast.LENGTH_SHORT).show();
      finish();
    } catch (Exception e) {
      Log.e(TAG, "Exception in onPaymentError", e);
    }
  }
}
