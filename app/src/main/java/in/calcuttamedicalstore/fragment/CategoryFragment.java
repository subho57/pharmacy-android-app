package in.calcuttamedicalstore.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.HomeActivity;
import in.calcuttamedicalstore.adapter.CategoryAdp;
import in.calcuttamedicalstore.model.CatItem;
import in.calcuttamedicalstore.model.Category;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.retrofit.GetResult;
import in.calcuttamedicalstore.utils.SessionManager;
import retrofit2.Call;

import static in.calcuttamedicalstore.activity.HomeActivity.homeActivity;

@SuppressLint("NonConstantResourceId")
public class CategoryFragment extends Fragment
    implements CategoryAdp.RecyclerTouchListener, GetResult.MyListener {
  @BindView(R.id.recycler_view)
  RecyclerView recyclerView;

  CategoryAdp adapter;
  List<CatItem> categoryList;
  Unbinder unbinder;
  SessionManager sessionManager;
  User user;

  public CategoryFragment() {
    // Required empty public constructor
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_category, container, false);
    unbinder = ButterKnife.bind(this, view);
    categoryList = new ArrayList<>();
    sessionManager = new SessionManager(getActivity());
    user = sessionManager.getUserDetails();
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
    recyclerView.setItemAnimator(new DefaultItemAnimator());
    HomeActivity.getInstance().setFrameMargin(60);
    getCategory();
    return view;
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
  public void onLongClickItem(View v, int position) {}

  private void getCategory() {
    HomeActivity.custPrograssbar.prograssCreate(getActivity());
    Call<JsonObject> call = APIClient.getInterface().getCat();
    GetResult getResult = new GetResult();
    getResult.setMyListener(this);
    getResult.callForLogin(call, "1");
  }

  @Override
  public void callback(JsonObject result, String callNo) {
    try {
      HomeActivity.custPrograssbar.closePrograssBar();
      if (callNo.equalsIgnoreCase("1")) {
        Gson gson = new Gson();
        Category category = gson.fromJson(result.toString(), Category.class);
        categoryList = category.getData();
        adapter = new CategoryAdp(getActivity(), categoryList, this);
        recyclerView.setAdapter(adapter);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    HomeActivity.getInstance().serchviewShow();
    HomeActivity.getInstance().setFrameMargin(60);
  }
}
