package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
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
import in.calcuttamedicalstore.model.Address;
import in.calcuttamedicalstore.model.Area;
import in.calcuttamedicalstore.model.AreaD;
import in.calcuttamedicalstore.model.UpdateAddress;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.CustPrograssbar;
import in.calcuttamedicalstore.utils.SessionManager;
import in.calcuttamedicalstore.utils.Utiles;
import retrofit2.Call;

import static in.calcuttamedicalstore.utils.Utiles.isRef;

@SuppressLint("NonConstantResourceId")
public class AddressActivity extends BaseActivity implements GetResult.MyListener {
  @BindView(R.id.ed_username)
  EditText edUsername;

  @BindView(R.id.ed_type)
  EditText edType;

  @BindView(R.id.ed_landmark)
  EditText edLandmark;

  SessionManager sessionManager;

  @BindView(R.id.ed_hoousno)
  EditText edHoousno;

  @BindView(R.id.ed_society)
  EditText edSociety;

  @BindView(R.id.ed_pinno)
  EditText edPinno;

  String areaSelect;
  List<AreaD> areaDS = new ArrayList<>();

  @BindView(R.id.spinner)
  Spinner spinner;

  @BindView(R.id.bt_picker)
  Button btPicker;

  User user;
  Address address;
  int PLACE_PICKER_REQUEST = 1;
  double latitude = 0.0, longitude = 0.0;
  CustPrograssbar custPrograssbar;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_address);
    ButterKnife.bind(this);
    sessionManager = new SessionManager(AddressActivity.this);
    user = sessionManager.getUserDetails();
    address = (Address) getIntent().getSerializableExtra("MyClass");
    custPrograssbar = new CustPrograssbar();
    spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            areaSelect = areaDS.get(position).getName();
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
    getArea();
    if (address != null) setcountaint(address);
    else edUsername.setText("" + user.getName());
    btPicker.setOnClickListener(
        view -> {
          PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
          try {
            startActivityForResult(builder.build(AddressActivity.this), PLACE_PICKER_REQUEST);
          } catch (GooglePlayServicesRepairableException
              | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
          }
        });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PLACE_PICKER_REQUEST) {
      if (resultCode == RESULT_OK) {
        Place place = PlacePicker.getPlace(this, data);
        latitude = place.getLatLng().latitude;
        longitude = place.getLatLng().longitude;
      }
    }
  }

  @SuppressLint("SetTextI18n")
  private void setcountaint(Address address) {
    edUsername.setText("" + address.getName());
    edType.setText("" + address.getName());
    edHoousno.setText("" + address.getHno());
    edSociety.setText("" + address.getSociety());
    edPinno.setText("" + address.getPincode());
    edLandmark.setText("" + address.getLandmark());
    edType.setText("" + address.getType());
    latitude = address.getLatitude();
    longitude = address.getLongitude();
  }

  private void getArea() {
    Call<JsonObject> call = APIClient.getInterface().getArea();
    GetResult getResult = new GetResult();
    getResult.setMyListener(this);
    getResult.callForLogin(call, "2");
  }

  @OnClick(R.id.txt_save)
  public void onViewClicked() {
    if (validation()) {
      if (address != null) {
        updateUser(address.getId());
      } else {
        updateUser("0");
      }
    }
  }

  private void updateUser(String aid) {
    custPrograssbar.prograssCreate(AddressActivity.this);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("uid", user.getId());
      jsonObject.put("aid", aid);
      jsonObject.put("name", edUsername.getText().toString());
      jsonObject.put("hno", edHoousno.getText().toString());
      jsonObject.put("society", edSociety.getText().toString());
      jsonObject.put("area", areaSelect);
      jsonObject.put("landmark", edLandmark.getText().toString());
      jsonObject.put("pincode", edPinno.getText().toString());
      jsonObject.put("type", edType.getText().toString());
      jsonObject.put("mobile", user.getMobile());
      jsonObject.put("password", user.getPassword());
      jsonObject.put("latitude", latitude);
      jsonObject.put("longitude", longitude);
      jsonObject.put("imei", Utiles.getIMEI(AddressActivity.this));
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface()
              .updateAddress((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("1")) {
        Gson gson = new Gson();
        UpdateAddress response = gson.fromJson(result.toString(), UpdateAddress.class);
        custPrograssbar.closePrograssBar();
        Toast.makeText(AddressActivity.this, "" + response.getResponseMsg(), Toast.LENGTH_LONG)
            .show();
        if (response.getResult().equals("true")) {
          sessionManager.setAddress(response.getAddress());
          isRef = true;
          finish();
        }
      } else if (callNo.equalsIgnoreCase("2")) {
        Gson gson = new Gson();
        Area area = gson.fromJson(result.toString(), Area.class);
        areaDS = area.getData();
        List<String> arrayList = new ArrayList<>();
        for (int i = 0; i < areaDS.size(); i++) {
          if (areaDS.get(i).getStatus().equalsIgnoreCase("1")) {
            arrayList.add(areaDS.get(i).getName());
          }
        }
        ArrayAdapter<String> dataAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        int spinnerPosition = dataAdapter.getPosition(address.getArea());
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
    if (edType.getText().toString().isEmpty()) {
      edType.setError("Specify Type: Home/Office");
      return false;
    }
    if (edHoousno.getText().toString().isEmpty()) {
      edHoousno.setError("Enter House No");
      return false;
    }
    if (edSociety.getText().toString().isEmpty()) {
      edSociety.setError("Enter Society");
      return false;
    }
    if (edLandmark.getText().toString().isEmpty()) {
      edLandmark.setError("Enter Landmark");
      return false;
    }
    if (edPinno.getText().toString().isEmpty()) {
      edPinno.setError("Enter Pincode");
      return false;
    }
    return true;
  }
}
