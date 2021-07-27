package in.calcuttamedicalstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.HomeActivity;
import in.calcuttamedicalstore.model.Payment;
import in.calcuttamedicalstore.model.PaymentItem;
import in.calcuttamedicalstore.model.Times;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.CustPrograssbar;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.utils.SessionManager.COUPON;

@SuppressLint("NonConstantResourceId")
public class PlaceOrderFragment extends Fragment
    implements View.OnClickListener, GetResult.MyListener {

  @BindView(R.id.radiogroup)
  RadioGroup rdgTime;

  Unbinder unbinder;

  @BindView(R.id.txt_selectdate)
  TextView txtSelectdate;

  @BindView(R.id.lvl_paymnet)
  LinearLayout lvlPaymnet;

  int day = 1;
  CustPrograssbar custPrograssbar;
  SessionManager sessionManager;

  public PlaceOrderFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_plase_order, container, false);
    unbinder = ButterKnife.bind(this, view);
    custPrograssbar = new CustPrograssbar();
    sessionManager = new SessionManager(getActivity());
    getTimeSlot();
    txtSelectdate.setText("" + getCurrentDate());
    HomeActivity.getInstance().setFrameMargin(0);
    return view;
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
  }

  private void getTimeSlot() {
    custPrograssbar.prograssCreate(getActivity());
    Call<JsonObject> call = APIClient.getInterface().getTimeslot();
    GetResult getResult = new GetResult();
    getResult.setMyListener(this);
    getResult.callForLogin(call, "1");
  }

  private void getPayment() {
    Call<JsonObject> call = APIClient.getInterface().getpaymentgateway();
    GetResult getResult = new GetResult();
    getResult.setMyListener(this);
    getResult.callForLogin(call, "2");
  }

  @Override
  public void onClick(View v) {}

  @SuppressLint("SetTextI18n")
  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("1")) {
        RadioButton rdbtn = null;
        Log.e("Response", "->" + result);
        Gson gson = new Gson();
        Times times = gson.fromJson(result.toString(), Times.class);
        for (int i = 0; i < times.getData().size(); i++) {
          rdbtn = new RadioButton(getActivity());
          rdbtn.setId(View.generateViewId());
          rdbtn.setText(
              times.getData().get(i).getMintime() + " - " + times.getData().get(i).getMaxtime());
          rdbtn.setOnClickListener(this);
          rdgTime.addView(rdbtn);
        }
        assert rdbtn != null;
        rdgTime.check(rdbtn.getId());
        getPayment();
      } else if (callNo.equalsIgnoreCase("2")) {
        custPrograssbar.closePrograssBar();
        Gson gson = new Gson();
        Payment payment = gson.fromJson(result.toString(), Payment.class);
        setJoinPlayrList(lvlPaymnet, payment.getData());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressLint("SetTextI18n")
  private void setJoinPlayrList(LinearLayout lnrView, List<PaymentItem> paymentList) {
    lnrView.removeAllViews();
    for (int i = 0; i < paymentList.size(); i++) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      PaymentItem paymentItem = paymentList.get(i);
      @SuppressLint("InflateParams")
      View view = inflater.inflate(R.layout.custome_paymen, null);
      ImageView imageView = view.findViewById(R.id.img_icon);
      TextView txt_title = view.findViewById(R.id.txt_title);
      txt_title.setText("" + paymentList.get(i).getTitle());
      Glide.with(Objects.requireNonNull(getActivity()))
          .load(APIClient.baseUrl + "/" + paymentList.get(i).getImg())
          .thumbnail(Glide.with(getActivity()).load(R.drawable.ezgifresize))
          .into(imageView);
      view.setOnClickListener(
          v -> {
            try {

              int selectedId = rdgTime.getCheckedRadioButtonId();
              RadioButton selectTime = rdgTime.findViewById(selectedId);
              sessionManager.setIntData(COUPON, 0);
              OrderSumrryFragment fragment = new OrderSumrryFragment();
              Bundle bundle = new Bundle();
              bundle.putString("DATE", txtSelectdate.getText().toString());
              bundle.putString("TIME", selectTime.getText().toString());
              bundle.putString("PAYMENT", paymentItem.getTitle());
              bundle.putSerializable("PAYMENTDETAILS", paymentItem);
              fragment.setArguments(bundle);
              HomeActivity.getInstance().callFragment(fragment);

            } catch (Exception e) {
              e.printStackTrace();
            }
          });
      lnrView.addView(view);
    }
  }

  @OnClick()
  public void onViewClicked() {}

  @OnClick({R.id.img_ldate, R.id.img_rdate})
  public void onViewClicked(View view) {
    switch (view.getId()) {
      case R.id.img_ldate:
        minusDate(txtSelectdate.getText().toString());
        break;
      case R.id.img_rdate:
        addDate(txtSelectdate.getText().toString());
        break;
      default:
        break;
    }
  }

  private String getCurrentDate() {
    Date d = Calendar.getInstance().getTime();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
    String formattedDate = df.format(d);
    try {
      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, day); // number of days to add
      formattedDate = df.format(c.getTime());
      c.setTime(Objects.requireNonNull(df.parse(formattedDate)));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return formattedDate;
  }

  @SuppressLint("SetTextI18n")
  private void addDate(String dt) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    Date strDate;
    try {
      strDate = sdf.parse(dt);
      assert strDate != null;
      if ((System.currentTimeMillis() + 432000000) < strDate.getTime()) {
        Log.e("date change ", "--> 1");
        return;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {

      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, day); // number of days to add
      dt = sdf.format(c.getTime());
      c.setTime(Objects.requireNonNull(sdf.parse(dt)));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    day++;
    txtSelectdate.setText("" + dt);
  }

  @SuppressLint("SetTextI18n")
  private void minusDate(String dt) {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
    Date strDate;
    try {
      strDate = sdf.parse(dt);
      assert strDate != null;
      if ((System.currentTimeMillis() + 86400000) > strDate.getTime()) {
        Log.e("date change ", "--> 1");
        return;
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    day--;
    try {

      Calendar c = Calendar.getInstance();
      c.add(Calendar.DATE, day); // number of days to add
      dt = sdf.format(c.getTime());
      c.setTime(Objects.requireNonNull(sdf.parse(dt)));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    txtSelectdate.setText("" + dt);
  }
}
