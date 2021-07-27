package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.CCode;
import in.calcuttamedicalstore.model.Contry;
import in.calcuttamedicalstore.model.LoginUser;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.SessionManager;
import in.calcuttamedicalstore.utils.Utiles;
import retrofit2.Call;

import static in.calcuttamedicalstore.utils.Utiles.isvarification;

@SuppressLint("NonConstantResourceId")
public class ProfileActivity extends BaseActivity implements GetResult.MyListener {

  SessionManager sessionManager;
  User user;

  @BindView(R.id.ed_username)
  EditText edUsername;

  @BindView(R.id.ed_email)
  EditText edEmail;

  @BindView(R.id.ed_alternatmob)
  EditText edAlternatmob;

  @BindView(R.id.ed_password)
  EditText edPassword;

  @BindView(R.id.spinner)
  Spinner spinner;

  List<CCode> cCodes = new ArrayList<>();
  String codeSelect;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_profile);
    ButterKnife.bind(this);
    sessionManager = new SessionManager(ProfileActivity.this);
    user = sessionManager.getUserDetails();
    setcountaint(user);
    getCode();
    spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            codeSelect = cCodes.get(position).getCcode();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
  }

  @SuppressLint("SetTextI18n")
  private void setcountaint(User user) {
    edUsername.setText("" + user.getName());
    edEmail.setText("" + user.getEmail());
    edAlternatmob.setText("" + user.getMobile());
    edPassword.setText("" + user.getPassword());
  }

  @OnClick(R.id.txt_save)
  public void onViewClicked() {
    if (validation()) {

      if (edAlternatmob.getText().toString().equalsIgnoreCase(user.getMobile())
          && codeSelect.equalsIgnoreCase(user.getCcode())) {
        updateUser();
      } else {
        isvarification = 2;
        User user1 = new User();
        user1.setId(user.getId());
        user1.setEmail(edEmail.getText().toString());
        user1.setMobile(edAlternatmob.getText().toString());
        user1.setName(edUsername.getText().toString());
        user1.setPassword(edPassword.getText().toString());
        user1.setCcode(codeSelect);
        startActivity(
            new Intent(ProfileActivity.this, VerifyPhoneActivity.class)
                .putExtra("code", codeSelect)
                .putExtra("phone", edAlternatmob.getText().toString())
                .putExtra("user", user1));
      }
    }
  }

  private void updateUser() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("uid", user.getId());
      jsonObject.put("name", edUsername.getText().toString());
      jsonObject.put("email", edEmail.getText().toString());
      jsonObject.put("mobile", edAlternatmob.getText().toString());
      jsonObject.put("ccode", codeSelect);
      jsonObject.put("password", edPassword.getText().toString());
      jsonObject.put("imei", Utiles.getIMEI(ProfileActivity.this));
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface()
              .updateProfile((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void getCode() {
    Call<JsonObject> call = APIClient.getInterface().getCode();
    GetResult getResult = new GetResult();
    getResult.setMyListener(this);
    getResult.callForLogin(call, "2");
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("1")) {
        Gson gson = new Gson();
        LoginUser response = gson.fromJson(result.toString(), LoginUser.class);
        Toast.makeText(ProfileActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG)
            .show();
        if (response.getResult().equals("true")) {
          sessionManager.setUserDetails(response.getUser());
          startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
          finish();
        }
      } else if (callNo.equalsIgnoreCase("2")) {
        Gson gson = new Gson();
        Contry contry = gson.fromJson(result.toString(), Contry.class);
        cCodes = contry.getData();
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < cCodes.size(); i++) {
          if (cCodes.get(i).getStatus().equalsIgnoreCase("1")) {
            arrayList.add(cCodes.get(i).getCcode());
          }
        }
        ArrayAdapter<String> dataAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int spinnerPosition = dataAdapter.getPosition(user.getCcode());
        spinner.setSelection(spinnerPosition);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean validation() {
    if (edUsername.getText().toString().isEmpty()) {
      edUsername.setError("Enter Name");
      return false;
    }
    if (!edEmail.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")) {
      edEmail.setError("Enter Valid Email");
      return false;
    }
    if (edAlternatmob.getText().toString().length() != 10) {
      edAlternatmob.setError("Enter Valid Mobile No");
      return false;
    }
    if (edPassword.getText().toString().isEmpty()) {
      edPassword.setError("Enter Password");
      return false;
    }
    return true;
  }
}
