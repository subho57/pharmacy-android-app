package in.calcuttamedicalstore.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.adapter.ReletedItemAdp;
import in.calcuttamedicalstore.database.DatabaseHelper;
import in.calcuttamedicalstore.database.MyCart;
import in.calcuttamedicalstore.fragment.ItemListFragment;
import in.calcuttamedicalstore.model.Price;
import in.calcuttamedicalstore.model.ProductItem;
import in.calcuttamedicalstore.model.ReletedProduct;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.CustPrograssbar;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

@SuppressLint("NonConstantResourceId")
public class ItemDetailsActivity extends AppCompatActivity
    implements GetResult.MyListener, ReletedItemAdp.ItemClickListener {
  @SuppressLint("StaticFieldLeak")
  public static ItemDetailsActivity itemDetailsActivity = null;

  ProductItem productItem;

  @BindView(R.id.img_back)
  ImageView imgBack;

  @BindView(R.id.img_cart)
  ImageView imgCart;

  @BindView(R.id.txt_tcount)
  TextView txtTcount;

  @BindView(R.id.lvl_cart)
  RelativeLayout lvlCart;

  @BindView(R.id.txt_title)
  TextView txtTitle;

  @BindView(R.id.txt_desc)
  TextView txtDesc;

  @BindView(R.id.lvl_pricelist)
  LinearLayout lvlPricelist;

  @BindView(R.id.btn_addtocart)
  TextView btnAddtocart;

  ArrayList<Price> priceslist;
  DatabaseHelper databaseHelper;
  SessionManager sessionManager;

  @BindView(R.id.spinner)
  Spinner spinner;

  @BindView(R.id.txt_price)
  TextView txtPrice;

  @BindView(R.id.txt_item_offer)
  TextView txtItemOffer;

  @BindView(R.id.txt_seler)
  TextView txtSeler;

  @BindView(R.id.viewPager)
  ViewPager viewPager;

  @BindView(R.id.tabview)
  TabLayout tabview;

  @BindView(R.id.recycler_releted)
  RecyclerView recyclerReleted;

  CustPrograssbar custPrograssbar;

  public static ItemDetailsActivity getInstance() {
    return itemDetailsActivity;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    itemDetailsActivity = null;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_item_details);
    ButterKnife.bind(this);
    itemDetailsActivity = this;
    LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(ItemDetailsActivity.this);
    mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
    recyclerReleted.setLayoutManager(mLayoutManager1);
    custPrograssbar = new CustPrograssbar();
    sessionManager = new SessionManager(this);
    databaseHelper = new DatabaseHelper(ItemDetailsActivity.this);
    productItem = getIntent().getParcelableExtra("MyClass");
    priceslist = getIntent().getParcelableArrayListExtra("MyList");
    recyclerReleted.setItemAnimator(new DefaultItemAnimator());

    onSetdata();
  }

  @SuppressLint("SetTextI18n")
  private void onSetdata() {

    if (productItem != null) {
      txtTitle.setText("" + productItem.getProductName());
      txtDesc.setText("" + productItem.getShortDesc());
      txtSeler.setText("" + productItem.getSellerName());
      List<String> Arealist = new ArrayList<>();
      for (int i = 0; i < priceslist.size(); i++) {
        Arealist.add(priceslist.get(i).getProductType());
      }
      ArrayAdapter<String> dataAdapter =
          new ArrayAdapter<>(this, R.layout.spinner_layout, Arealist);
      spinner.setAdapter(dataAdapter);
      updateItem();
    }

    spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            if (productItem.getmDiscount() > 0) {
              double res =
                  (Double.parseDouble(priceslist.get(position).getProductPrice()) / 100.0f)
                      * productItem.getmDiscount();
              res = Double.parseDouble(priceslist.get(position).getProductPrice()) - res;
              txtItemOffer.setText(
                  sessionManager.getStringData(SessionManager.currncy)
                      + priceslist.get(position).getProductPrice());
              txtItemOffer.setPaintFlags(
                  txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
              txtPrice.setText(sessionManager.getStringData(SessionManager.currncy) + res);
              txtItemOffer.setText(
                  sessionManager.getStringData(SessionManager.currncy)
                      + priceslist.get(position).getProductPrice());
            } else {
              txtItemOffer.setVisibility(View.GONE);
              txtPrice.setText(
                  sessionManager.getStringData(SessionManager.currncy)
                      + priceslist.get(position).getProductPrice());
            }
            setJoinPlayrList(lvlPricelist, productItem, priceslist.get(position));
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
    List<String> myList = new ArrayList<>();
    myList.add(productItem.getProductImage());
    if (productItem.getProductRelatedImage() != null
        && productItem.getProductRelatedImage().length() != 0) {
      myList.addAll(Arrays.asList(productItem.getProductRelatedImage().split(",")));
      tabview.setupWithViewPager(viewPager, true);
    }
    MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(this, myList);
    viewPager.setAdapter(myCustomPagerAdapter);
    reletedProduct();
  }

  @SuppressLint("SetTextI18n")
  public void updateItem() {
    Cursor res = databaseHelper.getAllData();
    if (res.getCount() == 0) {
      txtTcount.setText("0");
    } else {
      txtTcount.setText("" + res.getCount());
    }
  }

  @OnClick({R.id.img_back, R.id.lvl_cart, R.id.btn_addtocart})
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.img_back:
      case R.id.btn_addtocart:
        finish();
        break;
      case R.id.lvl_cart:
        fragment();
        break;
      default:
        break;
    }
  }

  @SuppressLint("SetTextI18n")
  private void setJoinPlayrList(LinearLayout lnrView, ProductItem datum, Price price) {

    lnrView.removeAllViews();
    final int[] count = {0};
    DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
    LayoutInflater inflater = LayoutInflater.from(this);
    @SuppressLint("InflateParams")
    View view = inflater.inflate(R.layout.custome_additem, null);
    TextView txtcount = view.findViewById(R.id.txtcount);
    LinearLayout lvl_addremove = view.findViewById(R.id.lvl_addremove);
    LinearLayout lvl_addcart = view.findViewById(R.id.lvl_addcart);
    LinearLayout img_mins = view.findViewById(R.id.img_mins);
    LinearLayout img_plus = view.findViewById(R.id.img_plus);
    MyCart myCart = new MyCart();
    myCart.setPid(datum.getId());
    myCart.setImage(datum.getProductImage());
    myCart.setTitle(datum.getProductName());
    myCart.setWeight(price.getProductType());
    myCart.setCost(price.getProductPrice());
    myCart.setDiscount(datum.getmDiscount());
    int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
    if (qrt != -1) {
      count[0] = qrt;
      txtcount.setText("" + count[0]);
      lvl_addremove.setVisibility(View.VISIBLE);
      lvl_addcart.setVisibility(View.GONE);
    } else {
      lvl_addremove.setVisibility(View.GONE);
      lvl_addcart.setVisibility(View.VISIBLE);
    }
    img_mins.setOnClickListener(
        v -> {
          count[0] = Integer.parseInt(txtcount.getText().toString());

          count[0] = count[0] - 1;
          if (count[0] <= 0) {
            lvl_addremove.setVisibility(View.GONE);
            lvl_addcart.setVisibility(View.VISIBLE);
            txtcount.setText("0");
            helper.deleteRData(myCart.getPid(), myCart.getCost());
          } else {
            txtcount.setVisibility(View.VISIBLE);
            txtcount.setText("" + count[0]);
            myCart.setQty(String.valueOf(count[0]));
            helper.insertData(myCart);
          }
          updateItem();
          if (ItemListFragment.itemListFragment != null)
            ItemListFragment.itemListFragment.updateItem();
        });

    img_plus.setOnClickListener(
        v -> {
          count[0] = Integer.parseInt(txtcount.getText().toString());

          count[0] = count[0] + 1;
          txtcount.setText("" + count[0]);
          myCart.setQty(String.valueOf(count[0]));
          Log.e("Insert", "--> " + helper.insertData(myCart));
          updateItem();
          if (ItemListFragment.itemListFragment != null)
            ItemListFragment.itemListFragment.updateItem();
        });
    lvl_addcart.setOnClickListener(
        v -> {
          lvl_addcart.setVisibility(View.GONE);
          lvl_addremove.setVisibility(View.VISIBLE);
          count[0] = Integer.parseInt(txtcount.getText().toString());

          count[0] = count[0] + 1;
          txtcount.setText("" + count[0]);
          myCart.setQty(String.valueOf(count[0]));
          Log.e("INsert", "--> " + helper.insertData(myCart));
          updateItem();
          if (ItemListFragment.itemListFragment != null)
            ItemListFragment.itemListFragment.updateItem();
        });
    lnrView.addView(view);
  }

  public void fragment() {
    SessionManager.iscart = true;
    finish();
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      custPrograssbar.closePrograssBar();
      if (callNo.equalsIgnoreCase("1")) {
        Gson gson = new Gson();
        ReletedProduct reletedProduct = gson.fromJson(result.toString(), ReletedProduct.class);
        if (reletedProduct.getResult().equalsIgnoreCase("true")) {
          ReletedItemAdp reletedItemAdp =
              new ReletedItemAdp(ItemDetailsActivity.this, reletedProduct.getData(), this);
          recyclerReleted.setAdapter(reletedItemAdp);
        }
      }
    } catch (Exception ignored) {

    }
  }

  @Override
  public void onItemClick(ProductItem productItem, int position) {
    this.productItem = productItem;
    priceslist = productItem.getPrice();
    onSetdata();
  }

  private void reletedProduct() {

    custPrograssbar.prograssCreate(ItemDetailsActivity.this);
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("cid", productItem.getCatId());
      jsonObject.put("sid", productItem.getSubcatId());
      jsonObject.put("pid", productItem.getId());
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface().related((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "1");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    List<String> imageList;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, List<String> bannerDatumList) {
      this.context = context;
      this.imageList = bannerDatumList;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
      return imageList.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
      return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, final int position) {
      View itemView = layoutInflater.inflate(R.layout.item_image, container, false);
      ImageView imageView = itemView.findViewById(R.id.imageView);
      Glide.with(ItemDetailsActivity.this)
          .load(APIClient.baseUrl + "/" + imageList.get(position))
          .placeholder(R.drawable.empty)
          .into(imageView);
      container.addView(itemView);

      return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
      container.removeView((LinearLayout) object);
    }
  }
}
