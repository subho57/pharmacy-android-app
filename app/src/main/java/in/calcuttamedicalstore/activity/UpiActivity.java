package in.calcuttamedicalstore.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.PaymentItem;

import static in.calcuttamedicalstore.fragment.HomeFragment.curr_order_num;
import static in.calcuttamedicalstore.fragment.OrderSumrryFragment.paymentsucsses;
import static in.calcuttamedicalstore.fragment.OrderSumrryFragment.tragectionID;
import static in.calcuttamedicalstore.retrofit.APIClient.baseUrl;

public class UpiActivity extends AppCompatActivity {

  //    SessionManager sessionManager;
  //    User user;
  final int UPI_PAYMENT = 123;
  PaymentItem paymentItem;

  public static boolean isConnectionAvailable(Context context) {
    ConnectivityManager connectivityManager =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    if (connectivityManager != null) {
      NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
      return netInfo != null
          && netInfo.isConnected()
          && netInfo.isConnectedOrConnecting()
          && netInfo.isAvailable();
    }
    return false;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_upi);

    // display UPI GIF Logo
    ImageView imageView = findViewById(R.id.upiLogo);
    Glide.with(this).asGif().load(R.drawable.upi_logo).into(imageView);

    /*
     * Currently only MyJio UPI App is tested to work with this
     * GooglePay, PayTM, AmazonPay, BHiM UPI gives error : Bank Limit Reached
     * For integrating Google Pay, refer : https://developers.google.com/pay/india/api/web/create-payment-method
     * For integrating PayTM, refer : https://developer.paytm.com/docs/upi-solutions/integration/
     */

    // get user details
    //        sessionManager = new SessionManager(this);
    //        user = sessionManager.getUserDetails();

    // Start Payment
    paymentItem = (PaymentItem) getIntent().getSerializableExtra("detail");
    assert paymentItem != null;
    String upiId = paymentItem.getCredValue();
    String name = getResources().getString(R.string.app_name);
    double amount = getIntent().getDoubleExtra("amount", 0.0);

    // get Current Order Number
    String note = "Order #" + curr_order_num;
    payUsingUpi(Double.toString(amount), upiId, name, note);
  }

  void payUsingUpi(String amount, String upiId, String name, String note) {

    Uri uri =
        new Uri.Builder()
            .scheme("upi")
            .authority("pay")

            // critical params
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", "Concept Classes")
            .appendQueryParameter("mc", "8211")
            .appendQueryParameter("tr", "OrderNo" + curr_order_num)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .appendQueryParameter("url", baseUrl)

            // additional params
            .appendQueryParameter("mam", amount)
            .appendQueryParameter("refUrl", baseUrl)
            .appendQueryParameter("ref-url", baseUrl)
            .build();

    Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
    upiPayIntent.setData(uri);

    // will always show a dialog to user to choose an app
    Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

    // check if intent resolves
    if (null != chooser.resolveActivity(getPackageManager())) {
      startActivityForResult(chooser, UPI_PAYMENT);
    } else {
      Toast.makeText(this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT)
          .show();
    }
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == UPI_PAYMENT) {
      if ((RESULT_OK == resultCode) || (resultCode == 11)) {
        if (data != null) {
          String trxt = data.getStringExtra("response");
          Log.d("UPI", "onActivityResult: " + trxt);
          ArrayList<String> dataList = new ArrayList<>();
          dataList.add(trxt);
          upiPaymentDataOperation(dataList);
        } else {
          Log.d("UPI", "onActivityResult: " + "Return data is null");
          ArrayList<String> dataList = new ArrayList<>();
          dataList.add("nothing");
          upiPaymentDataOperation(dataList);
        }
      } else {
        Log.d("UPI", "onActivityResult: " + "Return data is null"); // when user simply backs
        // without payment
        ArrayList<String> dataList = new ArrayList<>();
        dataList.add("nothing");
        upiPaymentDataOperation(dataList);
      }
    }
  }

  private void upiPaymentDataOperation(ArrayList<String> data) {
    if (isConnectionAvailable(this)) {
      String str = data.get(0);
      Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
      String paymentCancel = "";
      if (str == null) str = "discard";
      String status = "";
      String approvalRefNo = "";
      String[] response = str.split("&");
      for (String s : response) {
        String[] equalStr = s.split("=");
        if (equalStr.length >= 2) {
          if (equalStr[0].toLowerCase().equalsIgnoreCase("Status".toLowerCase())) {
            status = equalStr[1].toLowerCase();
          } else if (equalStr[0].toLowerCase().equalsIgnoreCase("ApprovalRefNo".toLowerCase())
              || equalStr[0].toLowerCase().equalsIgnoreCase("txnRef".toLowerCase())) {
            approvalRefNo = equalStr[1];
          }
        } else {
          paymentCancel = "Payment cancelled by user";
        }
      }

      if (status.equalsIgnoreCase("success")) {
        // Code to handle successful transaction here.
        tragectionID = approvalRefNo;
        paymentsucsses = 1;
        Toast.makeText(this, "Transaction Successful!!", Toast.LENGTH_SHORT).show();
        Log.d("UPI", "responseStr: " + approvalRefNo);
      } else if ("Payment cancelled by user.".equalsIgnoreCase(paymentCancel)) {
        Toast.makeText(this, "Payment cancelled by User.", Toast.LENGTH_SHORT).show();
      } else {
        Toast.makeText(this, "Transaction failed. Please try again", Toast.LENGTH_SHORT).show();
      }
    } else {
      Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
    }
    finish();
  }
}
