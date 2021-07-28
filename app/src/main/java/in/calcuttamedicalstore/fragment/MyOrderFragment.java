package in.calcuttamedicalstore.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import butterknife.Unbinder;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.HomeActivity;
import in.calcuttamedicalstore.activity.MyOrderListActivity;
import in.calcuttamedicalstore.model.Order;
import in.calcuttamedicalstore.model.OrderDatum;
import in.calcuttamedicalstore.model.RestResponse;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.CustPrograssbar;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.utils.SessionManager.currncy;

@SuppressLint("NonConstantResourceId")
public class MyOrderFragment extends Fragment implements GetResult.MyListener {
  @BindView(R.id.lvl_mycard)
  LinearLayout lvlMycard;

  Unbinder unbinder;

  @BindView(R.id.txt_notfound)
  TextView txtNotfound;

  @BindView(R.id.lvl_notfound)
  LinearLayout lvlNotfound;

  SessionManager sessionManager;
  User user;
  List<OrderDatum> orderData;
  CustPrograssbar custPrograssbar;
  int positionOrd;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_my_order, container, false);
    custPrograssbar = new CustPrograssbar();
    sessionManager = new SessionManager(getActivity());
    user = sessionManager.getUserDetails();
    orderData = new ArrayList<>();
    HomeActivity.getInstance().setFrameMargin(0);
    getHistry();
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  private void getHistry() {
    custPrograssbar.prograssCreate(getActivity());
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("uid", user.getId());
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface().getHistory((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void callback(JsonObject result, String callNo) {
    try {

      if (callNo.equalsIgnoreCase("1")) {
        Gson gson = new Gson();
        Order order = gson.fromJson(result.toString(), Order.class);
        if (order.getResult().equals("true")) {
          orderData = new ArrayList<>();
          orderData.addAll(order.getData());
          if (orderData.size() != 0) {
            setJoinPlayrList(lvlMycard, orderData);
          } else {
            custPrograssbar.closePrograssBar();
            lvlNotfound.setVisibility(View.VISIBLE);
            txtNotfound.setText("" + order.getResponseMsg());
          }
        } else {
          custPrograssbar.closePrograssBar();
          lvlNotfound.setVisibility(View.VISIBLE);
          txtNotfound.setText("" + order.getResponseMsg());
        }

      } else if (callNo.equalsIgnoreCase("2")) {
        // Log.e("Response", "-->" + result);
        Gson gson = new Gson();
        RestResponse response = gson.fromJson(result.toString(), RestResponse.class);
        Toast.makeText(getActivity(), response.getResponseMsg(), Toast.LENGTH_LONG).show();
        if (response.getResult().equalsIgnoreCase("true")) {
          OrderDatum datum = orderData.get(positionOrd);
          datum.setStatus("cancelled");
          orderData.set(positionOrd, datum);
          setJoinPlayrList(lvlMycard, orderData);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("SetTextI18n")
  private void setJoinPlayrList(LinearLayout lnrView, List<OrderDatum> orderData) {
    if (lnrView == null) {
      return;
    }
    lnrView.removeAllViews();
    int a = 0;
    if (orderData != null && orderData.size() > 0) {
      for (int i = 0; i < orderData.size(); i++) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        a = a + 1;
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.custome_oder, null);
        TextView txt_orderid = view.findViewById(R.id.txt_orderid);
        TextView txt_info = view.findViewById(R.id.txt_info);
        TextView txt_ordcancel = view.findViewById(R.id.txt_ordcancel);
        LinearLayout lvl_cancel = view.findViewById(R.id.lvl_cancel);
        TextView txt_status = view.findViewById(R.id.txt_status);
        TextView txt_total = view.findViewById(R.id.txt_total);
        txt_orderid.setText("Order ID:" + orderData.get(i).getId());
        txt_total.setText(sessionManager.getStringData(currncy) + orderData.get(i).getTotalamt());
        if (orderData.get(i).getStatus().equalsIgnoreCase("completed")) {
          txt_status.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
        if (orderData.get(i).getStatus().equalsIgnoreCase("pending")
            || orderData
                .get(i)
                .getStatus()
                .equalsIgnoreCase(getResources().getString(R.string.pic_myslf))) {
          lvl_cancel.setVisibility(View.VISIBLE);
        } else {
          lvl_cancel.setVisibility(View.GONE);
        }
        txt_status.setText("" + orderData.get(i).getStatus());
        lnrView.addView(view);
        int finalI = i;
        txt_ordcancel.setOnClickListener(
            v -> {
              AlertDialog.Builder builder;
              builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
              // Setting message manually and performing action on button click
              builder
                  .setMessage("Are you sure cancel this order ?")
                  .setCancelable(false)
                  .setPositiveButton(
                      "Yes",
                      (dialog, id) -> {
                        positionOrd = finalI;
                        getOdercancle(orderData.get(finalI).getId());
                      })
                  .setNegativeButton(
                      "No",
                      (dialog, id) -> {
                        //  Action for 'NO' Button
                        dialog.cancel();
                      });
              AlertDialog alert = builder.create();
              alert.show();
            });
        txt_info.setOnClickListener(
            v ->
                Objects.requireNonNull(getActivity())
                    .startActivity(
                        new Intent(getActivity(), MyOrderListActivity.class)
                            .putExtra("oid", orderData.get(finalI).getId())));
      }
      custPrograssbar.closePrograssBar();
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onResume() {
    super.onResume();
    HomeActivity.getInstance().serchviewHide();
    HomeActivity.getInstance().setFrameMargin(0);
    if (orderData.size() > 0) {
      getHistry();
    }
  }

  private void getOdercancle(String id) {
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("oid", id);
      jsonObject.put("uid", user.getId());
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface()
              .getOdercancle((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "2");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}
