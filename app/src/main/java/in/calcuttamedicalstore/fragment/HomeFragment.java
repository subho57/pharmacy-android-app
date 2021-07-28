package in.calcuttamedicalstore.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.HomeActivity;
import in.calcuttamedicalstore.activity.ItemDetailsActivity;
import in.calcuttamedicalstore.adapter.CategoryAdp;
import in.calcuttamedicalstore.adapter.ReletedItemAdp;
import in.calcuttamedicalstore.adapter.ReletedItemDaynamicAdp;
import in.calcuttamedicalstore.database.DatabaseHelper;
import in.calcuttamedicalstore.model.BannerItem;
import in.calcuttamedicalstore.model.CatItem;
import in.calcuttamedicalstore.model.DynamicData;
import in.calcuttamedicalstore.model.Home;
import in.calcuttamedicalstore.model.ProductItem;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.AutoScrollViewPager;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.activity.HomeActivity.homeActivity;
import static in.calcuttamedicalstore.activity.HomeActivity.txtNoti;
import static in.calcuttamedicalstore.utils.SessionManager.aboutUs;
import static in.calcuttamedicalstore.utils.SessionManager.callsupport;
import static in.calcuttamedicalstore.utils.SessionManager.contactUs;
import static in.calcuttamedicalstore.utils.SessionManager.currncy;
import static in.calcuttamedicalstore.utils.SessionManager.iscart;
import static in.calcuttamedicalstore.utils.SessionManager.oMin;
import static in.calcuttamedicalstore.utils.SessionManager.privacy;
import static in.calcuttamedicalstore.utils.SessionManager.razKey;
import static in.calcuttamedicalstore.utils.SessionManager.tax;
import static in.calcuttamedicalstore.utils.SessionManager.tremcodition;
import static in.calcuttamedicalstore.utils.Utiles.productItems;

@SuppressLint("NonConstantResourceId")
public class HomeFragment extends Fragment
    implements CategoryAdp.RecyclerTouchListener,
        ReletedItemAdp.ItemClickListener,
        GetResult.MyListener,
        ReletedItemDaynamicAdp.ItemClickListener,
        View.OnClickListener {
  public static long curr_order_num;
  public static String website_logo;
  public HomeFragment homeListFragment;

  @BindView(R.id.viewPager)
  AutoScrollViewPager viewPager;

  @BindView(R.id.viewPager2)
  AutoScrollViewPager viewPager2;

  @BindView(R.id.tabview)
  TabLayout tabview;

  @BindView(R.id.tabview2)
  TabLayout tabview2;

  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  @BindView(R.id.recycler_releted)
  RecyclerView recyclerReleted;

  @BindView(R.id.lvl_selected)
  LinearLayout lvlSelected;

  Unbinder unbinder;

  @BindView(R.id.scl_main)
  ScrollView sclMain;

  @BindView(R.id.lvl_mantanasmode)
  LinearLayout lvlMantanasmode;

  @BindView(R.id.fabMain)
  FloatingActionButton fabMain;

  @BindView(R.id.fabOne)
  FloatingActionButton fabOne;

  @BindView(R.id.fabTwo)
  FloatingActionButton fabTwo;

  @BindView(R.id.fabThree)
  FloatingActionButton fabThree;

  @BindView(R.id.fabfour)
  FloatingActionButton fabfour;

  Float translationY = 100f;
  Boolean isMenuOpen = false;
  OvershootInterpolator interpolator = new OvershootInterpolator();
  CategoryAdp adapter;
  ReletedItemAdp adapterReletedi;
  List<CatItem> categoryList;
  List<BannerItem> bannerDatumList;
  List<BannerItem> bannerDatumList2;
  SessionManager sessionManager;
  User user;
  List<DynamicData> dynamicDataList = new ArrayList<>();
  ReletedItemAdp reletedItemAdp;
  DatabaseHelper databaseHelper;
  private Context mContext;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_home, container, false);
    unbinder = ButterKnife.bind(this, view);
    bannerDatumList = new ArrayList<>();
    bannerDatumList2 = new ArrayList<>();
    sessionManager = new SessionManager(mContext);
    databaseHelper = new DatabaseHelper(getActivity());

    homeListFragment = this;
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
    mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
    recyclerReleted.setLayoutManager(mLayoutManager1);
    categoryList = new ArrayList<>();
    adapter = new CategoryAdp(mContext, categoryList, this);
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    recyclerView.setAdapter(adapter);
    adapterReletedi = new ReletedItemAdp(mContext, productItems, this);
    recyclerReleted.setItemAnimator(new DefaultItemAnimator());
    recyclerReleted.setAdapter(adapterReletedi);
    user = sessionManager.getUserDetails();

    fabOne.setAlpha(0f);
    fabTwo.setAlpha(0f);
    fabThree.setAlpha(0f);
    fabfour.setAlpha(0f);

    fabOne.setTranslationY(translationY);
    fabTwo.setTranslationY(translationY);
    fabThree.setTranslationY(translationY);
    fabfour.setTranslationY(translationY);

    fabMain.setOnClickListener(this);
    fabOne.setOnClickListener(this);
    fabTwo.setOnClickListener(this);
    fabThree.setOnClickListener(this);
    fabfour.setOnClickListener(this);
    closeMenu(false);
    getHome();
    return view;
  }

  private void setJoinPlayrList(LinearLayout lnrView, List<DynamicData> dataList) {

    lnrView.removeAllViews();
    for (int i = 0; i < dataList.size(); i++) {
      LayoutInflater inflater = LayoutInflater.from(getActivity());
      @SuppressLint("InflateParams")
      View view = inflater.inflate(R.layout.list_home_item, null);
      TextView itemTitle = view.findViewById(R.id.itemTitle);
      RecyclerView recycler_view_list = view.findViewById(R.id.recycler_view_list);
      itemTitle.setText(dataList.get(i).getTitle());
      ReletedItemDaynamicAdp itemAdp =
          new ReletedItemDaynamicAdp(mContext, dataList.get(i).getDynamicItems(), this);
      recycler_view_list.setLayoutManager(
          new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
      recycler_view_list.setAdapter(itemAdp);
      lnrView.addView(view);
    }
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onClickItem(String titel, int position, String img) {
    homeActivity.showMenu();
    Bundle args = new Bundle();
    args.putInt("id", position);
    args.putString("titel", titel);
    args.putString("img", img);
    Fragment fragment = new SubCategoryFragment();
    fragment.setArguments(args);
    HomeActivity.getInstance().callFragment(fragment);
  }

  @Override
  public void onLongClickItem(View v, int position) {
    // Log.e("posiotn", "" + position);
  }

  @Override
  public void onItemClick(ProductItem productItem, int position) {
    mContext.startActivity(
        new Intent(mContext, ItemDetailsActivity.class)
            .putExtra("MyClass", productItem)
            .putParcelableArrayListExtra("MyList", productItem.getPrice()));
  }

  private void openMenu() {
    isMenuOpen = !isMenuOpen;
    fabMain.animate().setInterpolator(interpolator).rotation(45f).setDuration(300).start();

    fabOne
        .animate()
        .translationY(0f)
        .alpha(1f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabOne.setVisibility(View.VISIBLE);
    fabTwo
        .animate()
        .translationY(0f)
        .alpha(1f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabTwo.setVisibility(View.VISIBLE);
    fabThree
        .animate()
        .translationY(0f)
        .alpha(1f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabThree.setVisibility(View.VISIBLE);
    fabfour
        .animate()
        .translationY(0f)
        .alpha(1f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabfour.setVisibility(View.VISIBLE);
  }

  private void closeMenu() {
    isMenuOpen = !isMenuOpen;

    fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

    fabOne
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabOne.setVisibility(View.GONE);
    fabTwo
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabTwo.setVisibility(View.GONE);
    fabThree
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabThree.setVisibility(View.GONE);
    fabfour
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabfour.setVisibility(View.GONE);
  }

  private void closeMenu(boolean value) {
    isMenuOpen = value;

    fabMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

    fabOne
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabOne.setVisibility(View.GONE);
    fabTwo
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabTwo.setVisibility(View.GONE);
    fabThree
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabThree.setVisibility(View.GONE);
    fabfour
        .animate()
        .translationY(translationY)
        .alpha(0f)
        .setInterpolator(interpolator)
        .setDuration(300)
        .start();
    fabfour.setVisibility(View.GONE);
  }

  private void openWhatsApp(String numberwhats) {
    String url = "https://api.whatsapp.com/send?phone=" + numberwhats;
    Intent i = new Intent(Intent.ACTION_VIEW);
    i.setData(Uri.parse(url));
    startActivity(i);
  }

  private void shareApp() {
    try {
      Intent shareIntent = new Intent(Intent.ACTION_SEND);
      shareIntent.setType("text/plain");
      shareIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
      String shareMessage =
          "\nGet medicines delivered to your home. Calcutta Medical Stores got you covered for all your pharma needs.\nVisit our website @ https://www.calcuttamedicalstore.in\nOr download our app to order now!!\n\n";
      shareMessage =
          shareMessage
              + "https://play.google.com/store/apps/details?id="
              + getActivity().getPackageName()
              + "\n\n";
      shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
      startActivity(Intent.createChooser(shareIntent, "Choose one"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @OnClick({
    R.id.txt_viewll,
    R.id.txt_viewllproduct,
    R.id.fabMain,
    R.id.fabOne,
    R.id.fabTwo,
    R.id.fabThree,
    R.id.fabfour
  })
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.txt_viewll:
        CategoryFragment fragment = new CategoryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("arraylist", (Serializable) categoryList);
        fragment.setArguments(bundle);
        HomeActivity.getInstance().callFragment(fragment);
        break;
      case R.id.txt_viewllproduct:
        PopularFragment fragmentp = new PopularFragment();
        HomeActivity.getInstance().callFragment(fragmentp);
        break;
      case R.id.fabMain:
        if (isMenuOpen) {
          fabOne.setVisibility(View.GONE);
          fabTwo.setVisibility(View.GONE);
          fabThree.setVisibility(View.GONE);
          fabfour.setVisibility(View.GONE);
          closeMenu();
        } else {
          fabOne.setVisibility(View.VISIBLE);
          fabTwo.setVisibility(View.VISIBLE);
          fabThree.setVisibility(View.VISIBLE);
          fabfour.setVisibility(View.VISIBLE);
          openMenu();
        }
        break;
      case R.id.fabOne:
        shareApp();
        break;
      case R.id.fabTwo:
        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY
                | Intent.FLAG_ACTIVITY_NEW_DOCUMENT
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
          startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
          startActivity(
              new Intent(
                  Intent.ACTION_VIEW,
                  Uri.parse(
                      "http://play.google.com/store/apps/details?id="
                          + getActivity().getPackageName())));
        }
        break;
      case R.id.fabThree:
        String smsNumber = sessionManager.getStringData(callsupport);
        openWhatsApp(smsNumber);
        break;

      case R.id.fabfour:
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + sessionManager.getStringData(callsupport)));
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED) {
          requestPermissions(new String[] {Manifest.permission.CALL_PHONE}, 1);
          break;
        }
        startActivity(intent);
        break;
      default:
        break;
    }
  }

  private void getHome() {
    HomeActivity.custPrograssbar.prograssCreate(getActivity());
    JSONObject jsonObject = new JSONObject();
    try {
      jsonObject.put("uid", user.getId());
      JsonParser jsonParser = new JsonParser();
      Call<JsonObject> call =
          APIClient.getInterface().getHome((JsonObject) jsonParser.parse(jsonObject.toString()));
      GetResult getResult = new GetResult();
      getResult.setMyListener(this);
      getResult.callForLogin(call, "homepage");
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    HomeActivity.getInstance().setdata();
    HomeActivity.getInstance().setFrameMargin(60);
    HomeActivity.getInstance().serchviewShow();
    if (user != null)
      HomeActivity.getInstance().titleChange("Hello " + user.getName().split(" ")[0]);

    if (dynamicDataList != null) {
      setJoinPlayrList(lvlSelected, dynamicDataList);
    }
    if (reletedItemAdp != null) {
      reletedItemAdp.notifyDataSetChanged();
    }
    if (iscart) {
      iscart = false;
      CardFragment fragment = new CardFragment();
      HomeActivity.getInstance().callFragment(fragment);
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      if (callNo.equalsIgnoreCase("homepage")) {
        HomeActivity.custPrograssbar.closePrograssBar();
        bannerDatumList = new ArrayList<>();
        bannerDatumList2 = new ArrayList<>();
        categoryList = new ArrayList<>();
        Gson gson = new Gson();
        Home home = gson.fromJson(result.toString(), Home.class);
        curr_order_num = home.getResultHome().getOrderNum();
        website_logo = home.getResultHome().getMainData().getLogo();

        if (home.getResultHome().getMainData().getMaintaince() == 1) {
          sclMain.setVisibility(View.GONE);
          lvlMantanasmode.setVisibility(View.VISIBLE);
          databaseHelper.deleteCard();
          HomeActivity.getInstance().hideActionbar();
          return;
        }
        categoryList.addAll(home.getResultHome().getCatItems());
        adapter = new CategoryAdp(mContext, categoryList, this);
        recyclerView.setAdapter(adapter);

        bannerDatumList.addAll(home.getResultHome().getBannerItems());
        MyCustomPagerAdapter myCustomPagerAdapter =
            new MyCustomPagerAdapter(mContext, bannerDatumList);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.startAutoScroll();
        viewPager.setInterval(3000);
        viewPager.setCycle(true);
        viewPager.setStopScrollWhenTouch(true);
        tabview.setupWithViewPager(viewPager, true);

        bannerDatumList2.addAll(home.getResultHome().getBannerItems2());
        MyCustomPagerAdapter myCustomPagerAdapter2 =
            new MyCustomPagerAdapter(mContext, bannerDatumList2);
        viewPager2.setAdapter(myCustomPagerAdapter2);
        viewPager2.startAutoScroll();
        viewPager2.setInterval(3000);
        viewPager2.setCycle(true);
        viewPager2.setStopScrollWhenTouch(true);
        tabview2.setupWithViewPager(viewPager2, true);

        reletedItemAdp = new ReletedItemAdp(mContext, home.getResultHome().getProductItems(), this);
        recyclerReleted.setAdapter(reletedItemAdp);
        if (home.getResultHome().getRemainNotification() <= 0) {
          txtNoti.setVisibility(View.GONE);
        } else {
          txtNoti.setVisibility(View.VISIBLE);
          txtNoti.setText("" + home.getResultHome().getRemainNotification());
        }
        sessionManager.setStringData(currncy, home.getResultHome().getMainData().getCurrency());
        sessionManager.setStringData(
            callsupport, home.getResultHome().getMainData().getCallsupport());
        sessionManager.setStringData(
            privacy, home.getResultHome().getMainData().getPrivacyPolicy());
        sessionManager.setStringData(aboutUs, home.getResultHome().getMainData().getAboutUs());
        sessionManager.setStringData(contactUs, home.getResultHome().getMainData().getContactUs());
        sessionManager.setStringData(tremcodition, home.getResultHome().getMainData().getTerms());
        sessionManager.setIntData(oMin, home.getResultHome().getMainData().getoMin());
        sessionManager.setStringData(razKey, home.getResultHome().getMainData().getRazKey());
        sessionManager.setStringData(tax, home.getResultHome().getMainData().getTax());
        HomeActivity.getInstance().setTxtWallet((int) (home.getResultHome().getWallet()) + "");
        productItems = home.getResultHome().getProductItems();
        dynamicDataList = home.getResultHome().getDynamicData();
        setJoinPlayrList(lvlSelected, dynamicDataList);
      }

    } catch (Exception ignored) {
    }
  }

  @Override
  public void onAttach(@NotNull Context context) {
    super.onAttach(context);
    mContext = context;
  }

  public class MyCustomPagerAdapter extends PagerAdapter {
    Context context;
    List<BannerItem> bannerDatumList;
    LayoutInflater layoutInflater;

    public MyCustomPagerAdapter(Context context, List<BannerItem> bannerDatumList) {
      this.context = context;
      this.bannerDatumList = bannerDatumList;
      layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
      return bannerDatumList.size();
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
      return view == object;
    }

    @NotNull
    @Override
    public Object instantiateItem(@NotNull ViewGroup container, final int position) {
      View itemView = layoutInflater.inflate(R.layout.item_banner, container, false);
      ImageView imageView = itemView.findViewById(R.id.imageView);
      Glide.with(mContext)
          .load(APIClient.baseUrl + "/" + bannerDatumList.get(position).getBimg())
          .placeholder(R.drawable.empty)
          .into(imageView);
      container.addView(itemView);
      imageView.setOnClickListener(
          v -> {
            if (!bannerDatumList.get(position).getCid().equalsIgnoreCase("0")
                && bannerDatumList.get(position).getSid().equalsIgnoreCase("0")) {
              homeActivity.showMenu();
              Bundle args = new Bundle();
              args.putInt("id", Integer.parseInt(bannerDatumList.get(position).getCid()));
              Fragment fragment = new SubCategoryFragment();
              fragment.setArguments(args);
              HomeActivity.getInstance().callFragment(fragment);
            } else if (!bannerDatumList.get(position).getCid().equalsIgnoreCase("0")
                && !bannerDatumList.get(position).getSid().equalsIgnoreCase("0")) {
              homeActivity.showMenu();
              Bundle args = new Bundle();
              args.putInt("cid", Integer.parseInt(bannerDatumList.get(position).getCid()));
              args.putInt("scid", Integer.parseInt(bannerDatumList.get(position).getSid()));
              Fragment fragment = new ItemListFragment();
              fragment.setArguments(args);
              HomeActivity.getInstance().callFragment(fragment);
            }
          });
      return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
      container.removeView((LinearLayout) object);
    }
  }
}
