package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.Area;
import in.calcuttamedicalstore.model.AreaD;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.utils.SessionManager.area;

public class SelectAreaActivity extends AppCompatActivity implements GetResult.MyListener {
  List<AreaD> areaDS = new ArrayList<>();

  @SuppressLint("NonConstantResourceId")
  @BindView(R.id.spinner)
  Spinner spinner;

  String areaSelect;
  SessionManager sessionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_select_area);
    ButterKnife.bind(this);
    sessionManager = new SessionManager(SelectAreaActivity.this);
    getArea();
    spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            areaSelect = areaDS.get(position).getName();
            sessionManager.setStringData(area, areaSelect);
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
  }

  private void getArea() {
    Call<JsonObject> call = APIClient.getInterface().getArea();
    GetResult getResult = new GetResult();
    getResult.setMyListener(this);
    getResult.callForLogin(call, "2");
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("2")) {
        Gson gson = new Gson();
        Area area = gson.fromJson(result.toString(), Area.class);
        areaDS = area.getData();
        List<String> Arealist = new ArrayList<>();
        for (int i = 0; i < areaDS.size(); i++) {
          if (areaDS.get(i).getStatus().equalsIgnoreCase("1")) {
            Arealist.add(areaDS.get(i).getName());
          }
        }
        ArrayAdapter<String> dataAdapter =
            new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arealist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("NonConstantResourceId")
  @OnClick(R.id.btn_next)
  public void onClick() {
    startActivity(new Intent(SelectAreaActivity.this, LoginActivity.class));
  }
}
