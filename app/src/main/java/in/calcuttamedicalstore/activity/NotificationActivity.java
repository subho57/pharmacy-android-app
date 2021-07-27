package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.Noti;
import in.calcuttamedicalstore.model.ResNoti;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

@SuppressLint("NonConstantResourceId")
public class NotificationActivity extends AppCompatActivity implements GetResult.MyListener {

  @BindView(R.id.lvl_myorder)
  LinearLayout lvlMyorder;

  @BindView(R.id.txt_notiempty)
  TextView txtNotiempty;

  User user;
  SessionManager sessionManager;
  List<Noti> notiList;
  Context context;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
    ButterKnife.bind(this);
    context = this;
    Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setTitle("Notification");
    sessionManager = new SessionManager(NotificationActivity.this);
    user = sessionManager.getUserDetails();
    getNotification();
  }

  private void getNotification() {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("uid", user.getId());
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface().getNoti((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("SetTextI18n")
  private void setNotiList(LinearLayout lnrView, List<Noti> list) {
    lnrView.removeAllViews();
    int a = 0;
    if (list != null && list.size() > 0) {
      for (int i = 0; i < list.size(); i++) {
        Noti noti = list.get(i);
        LayoutInflater inflater = LayoutInflater.from(NotificationActivity.this);
        a = a + 1;
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.custome_noti, null);
        LinearLayout lvl_bgcolor = view.findViewById(R.id.lvl_bgcolor);
        TextView txt_name = view.findViewById(R.id.txt_orderid);
        ImageView imgNoti = view.findViewById(R.id.imag_noti);
        txt_name.setText(" " + noti.getTitle());
        Glide.with(this)
            .asBitmap()
            .load(APIClient.baseUrl + noti.getImg())
            .placeholder(R.drawable.empty_noti)
            .into(imgNoti);
        if (noti.getiSread() == 0) {
          lvl_bgcolor.setBackgroundColor(getResources().getColor(R.color.colorGrey));
        } else {
          lvl_bgcolor.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        }
        lnrView.addView(view);
        lvl_bgcolor.setOnClickListener(
            v -> {
              noti.setiSread(1);
              startActivity(
                  new Intent(NotificationActivity.this, NotificationDetailsActivity.class)
                      .putExtra("myclass", noti));
            });
      }
    }
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("1")) {
        Gson gson = new Gson();
        ResNoti resNoti = gson.fromJson(result.toString(), ResNoti.class);
        if (resNoti.getResult().equalsIgnoreCase("true")) {
          notiList = new ArrayList<>();
          notiList = resNoti.getData();
          if (notiList.size() != 0) {
            txtNotiempty.setVisibility(View.GONE);
            setNotiList(lvlMyorder, notiList);
          }
        } else {
          txtNotiempty.setVisibility(View.VISIBLE);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    if (notiList != null && notiList.size() != 0) setNotiList(lvlMyorder, notiList);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
}
