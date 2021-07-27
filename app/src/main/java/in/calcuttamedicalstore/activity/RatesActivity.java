package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.CustPrograssbar;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.utils.Utiles.isrates;

@SuppressLint("NonConstantResourceId")
public class RatesActivity extends BaseActivity implements GetResult.MyListener {

  @BindView(R.id.ed_feedback)
  EditText edFeedback;

  User user;
  SessionManager sessionManager;
  CustPrograssbar custPrograssbar;

  @BindView(R.id.lvl_vgood)
  LinearLayout lvlVgood;

  @BindView(R.id.lvl_good)
  LinearLayout lvlGood;

  @BindView(R.id.lvl_notgood)
  LinearLayout lvlNotgood;

  String id;
  int selectRate = 0;

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_rates);
    ButterKnife.bind(this);
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    sessionManager = new SessionManager(RatesActivity.this);
    user = sessionManager.getUserDetails();
    custPrograssbar = new CustPrograssbar();
    Intent intent = getIntent();
    id = intent.getStringExtra("oid");
  }

  private void userFeedBack() {
    custPrograssbar.prograssCreate(RatesActivity.this);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("msg", edFeedback.getText().toString());
      jsonObject.put("rate", String.valueOf(selectRate));
      jsonObject.put("uid", user.getId());
      jsonObject.put("oid", id);
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface().rates((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    custPrograssbar.closePrograssBar();
    Log.e("Feedback Respons", ":" + result);
    if (result != null) {
      try {
        JSONObject object = new JSONObject(result.toString());
        Toast.makeText(RatesActivity.this, "" + object.getString("ResponseMsg"), Toast.LENGTH_SHORT)
            .show();
        if (object.getString("Result").equals("true")) {
          edFeedback.setText("");
          isrates = true;
          finish();
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  }

  private boolean validation() {
    if (edFeedback.getText().toString().isEmpty()) {
      edFeedback.setError("Enter FeedBack");
      return false;
    }
    if (selectRate == 0) {
      Toast.makeText(RatesActivity.this, "Pleas select Rates..", Toast.LENGTH_LONG).show();
      return false;
    }
    return true;
  }

  @OnClick({R.id.img_vgood, R.id.img_good, R.id.img_notgood, R.id.btn_submit})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.img_vgood:
        selectRate = 1;
        imgClick(1);
        break;
      case R.id.img_good:
        selectRate = 2;
        imgClick(2);
        break;
      case R.id.img_notgood:
        selectRate = 3;
        imgClick(3);
        break;
      case R.id.btn_submit:
        if (validation()) {
          userFeedBack();
        }
        break;
      default:
        break;
    }
  }

  public void imgClick(int item) {
    switch (item) {
      case 1:
        lvlGood.setBackgroundResource(R.drawable.rounded_search);
        lvlVgood.setBackgroundResource(R.drawable.rounded_search1);
        lvlNotgood.setBackgroundResource(R.drawable.rounded_search);
        break;
      case 2:
        lvlGood.setBackgroundResource(R.drawable.rounded_search1);
        lvlVgood.setBackgroundResource(R.drawable.rounded_search);
        lvlNotgood.setBackgroundResource(R.drawable.rounded_search);
        break;
      case 3:
        lvlGood.setBackgroundResource(R.drawable.rounded_search);
        lvlVgood.setBackgroundResource(R.drawable.rounded_search);
        lvlNotgood.setBackgroundResource(R.drawable.rounded_search1);
        break;
      default:
        break;
    }
  }
}
