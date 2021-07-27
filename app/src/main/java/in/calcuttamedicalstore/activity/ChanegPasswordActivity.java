package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.RestResponse;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import retrofit2.Call;

@SuppressLint("NonConstantResourceId")
public class ChanegPasswordActivity extends AppCompatActivity implements GetResult.MyListener {

  @BindView(R.id.ed_password)
  TextInputEditText edPassword;

  @BindView(R.id.ed_password1)
  TextInputLayout edPassword1;

  @BindView(R.id.ed_conpassword)
  TextInputEditText edConpassword;

  @BindView(R.id.ed_conpassword1)
  TextInputLayout edConpassword1;

  @BindView(R.id.btn_submit)
  TextView btnSubmit;

  String phoneNumber;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_chaneg_password);
    ButterKnife.bind(this);
    phoneNumber = getIntent().getStringExtra("phone");
  }

  @OnClick(R.id.btn_submit)
  public void onClick() {
    if (validation()) setPassword();
  }

  public boolean validation() {

    if (Objects.requireNonNull(edPassword.getText()).toString().isEmpty()) {
      edPassword.setError("Enter Password");
      return false;
    }
    if (Objects.requireNonNull(edConpassword.getText()).toString().isEmpty()) {
      edConpassword.setError("Enter Confirm");
      return false;
    }
    if (!edConpassword.getText().toString().equals(edPassword.getText().toString())) {
      edConpassword.setError("Mismatch Password");
      edPassword.setError("Mismatch Password");
      return false;
    }
    return true;
  }

  private void setPassword() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("pin", phoneNumber);
      jsonObject.put("password", Objects.requireNonNull(edPassword.getText()).toString());
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface()
              .getPinmatch((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    Gson gson = new Gson();
    RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
    Toast.makeText(ChanegPasswordActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG)
        .show();
    if (response.getResult().equals("true")) {
      Intent intent = new Intent(ChanegPasswordActivity.this, LoginActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      startActivity(intent);
      finish();
    }
  }
}
