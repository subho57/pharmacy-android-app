package in.calcuttamedicalstore.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.PayTmInitiateTransactionApiResponse;
import in.calcuttamedicalstore.model.PaymentItem;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.CustPrograssbar;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.fragment.HomeFragment.curr_order_num;
import static in.calcuttamedicalstore.fragment.OrderSumrryFragment.paymentsucsses;
import static in.calcuttamedicalstore.fragment.OrderSumrryFragment.tragectionID;

public class PaytmActivity extends AppCompatActivity implements GetResult.MyListener {
  private static final String TAG = PaytmActivity.class.getSimpleName();
  final int ActivityRequestCode = 1;
  SessionManager sessionManager;
  double amount = 0;
  User user;
  PaymentItem paymentItem;
  CustPrograssbar custPrograssbar;
  String orderId = "OrderNo" + curr_order_num + "_" + new Date().getTime();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_paytm);
    sessionManager = new SessionManager(this);
    user = sessionManager.getUserDetails();
    amount = getIntent().getIntExtra("amount", 0);
    paymentItem = (PaymentItem) getIntent().getSerializableExtra("detail");
    custPrograssbar = new CustPrograssbar();
    custPrograssbar.prograssCreate(PaytmActivity.this);
    initiateTransaction();
  }

  private void initiateTransaction() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("uid", user.getId());
      jsonObject.put("orderId", orderId);
      jsonObject.put("amt", String.valueOf(amount));
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface()
              .initiateTransaction((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  void placeOrder(String txnToken) {
    PaytmOrder paytmOrder =
        new PaytmOrder(
            orderId,
            paymentItem.getCredTitle(),
            txnToken,
            String.valueOf(amount),
            "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=" + orderId);
    TransactionManager transactionManager =
        new TransactionManager(
            paytmOrder,
            new PaytmPaymentTransactionCallback() {
              @Override
              public void onTransactionResponse(Bundle bundle) {
                Toast.makeText(
                        getApplicationContext(),
                        "Payment Transaction response " + bundle.toString(),
                        Toast.LENGTH_LONG)
                    .show();
                // Log.e(TAG, "Response (onTransactionResponse) : ");
                if (bundle != null) {
                  tragectionID = bundle.get("BANKTXNID").toString();
                  paymentsucsses = 1;
                  custPrograssbar.closePrograssBar();
                  finish();
                }
              }

              @Override
              public void networkNotAvailable() {
                Toast.makeText(getApplicationContext(), "network not available", Toast.LENGTH_LONG)
                    .show();
              }

              @Override
              public void onErrorProceed(String s) {
                Toast.makeText(getApplicationContext(), "onErrorProcess " + s, Toast.LENGTH_LONG)
                    .show();
              }

              @Override
              public void clientAuthenticationFailed(String s) {
                Toast.makeText(getApplicationContext(), "Clientauth " + s, Toast.LENGTH_LONG)
                    .show();
              }

              @Override
              public void someUIErrorOccurred(String s) {
                Toast.makeText(getApplicationContext(), " UI error " + s, Toast.LENGTH_LONG).show();
              }

              @Override
              public void onErrorLoadingWebPage(int i, String s, String s1) {
                Toast.makeText(
                        getApplicationContext(),
                        " error loading web " + s + "--" + s1,
                        Toast.LENGTH_LONG)
                    .show();
              }

              @Override
              public void onBackPressedCancelTransaction() {
                Toast.makeText(getApplicationContext(), "backPress ", Toast.LENGTH_LONG).show();
              }

              @Override
              public void onTransactionCancel(String s, Bundle bundle) {
                Toast.makeText(
                        getApplicationContext(), " transaction cancel " + s, Toast.LENGTH_LONG)
                    .show();
              }
            });
    transactionManager.startTransaction(this, ActivityRequestCode);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == ActivityRequestCode && data != null) {
      Bundle bundle = data.getExtras();
      if (bundle != null) {
        tragectionID = bundle.get("BANKTXNID").toString();
        paymentsucsses = 1;
      }
      custPrograssbar.closePrograssBar();
      Toast.makeText(
              this,
              data.getStringExtra("nativeSdkForMerchantMessage") + data.getStringExtra("response"),
              Toast.LENGTH_SHORT)
          .show();
      finish();
    }
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("1")) {
        PayTmInitiateTransactionApiResponse response =
            new Gson().fromJson(result.toString(), PayTmInitiateTransactionApiResponse.class);
        if (response.getBody().getResultInfo().getResultStatus().equalsIgnoreCase("S")) {
          placeOrder(response.getBody().getTxnToken());
        } else {
          custPrograssbar.closePrograssBar();
          Toast.makeText(
                  PaytmActivity.this,
                  "ERROR "
                      + response.getBody().getResultInfo().getResultCode()
                      + ": "
                      + response.getBody().getResultInfo().getResultMsg(),
                  Toast.LENGTH_LONG)
              .show();
          finish();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
