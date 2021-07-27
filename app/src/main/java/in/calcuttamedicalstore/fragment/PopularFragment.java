package in.calcuttamedicalstore.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.HomeActivity;
import in.calcuttamedicalstore.activity.ItemDetailsActivity;
import in.calcuttamedicalstore.adapter.ReletedItemAllAdp;
import in.calcuttamedicalstore.model.ProductItem;
import in.calcuttamedicalstore.model.User;
import in.calcuttamedicalstore.utils.SessionManager;

import static in.calcuttamedicalstore.utils.SessionManager.iscart;
import static in.calcuttamedicalstore.utils.Utiles.productItems;

public class PopularFragment extends Fragment implements ReletedItemAllAdp.ItemClickListener {
  @SuppressLint("NonConstantResourceId")
  @BindView(R.id.recycler_view)
  RecyclerView reyCategory;

  SessionManager sessionManager;
  User userData;
  ReletedItemAllAdp itemAdp;

  public PopularFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_category, container, false);
    ButterKnife.bind(this, view);
    sessionManager = new SessionManager(getActivity());
    userData = sessionManager.getUserDetails();
    HomeActivity.getInstance().setFrameMargin(0);
    reyCategory.setHasFixedSize(true);
    reyCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
    itemAdp = new ReletedItemAllAdp(getActivity(), productItems, this);
    reyCategory.setAdapter(itemAdp);
    return view;
  }

  @Override
  public void onItemClick(ProductItem productItem, int position) {
    Objects.requireNonNull(getActivity())
        .startActivity(
            new Intent(getActivity(), ItemDetailsActivity.class)
                .putExtra("MyClass", productItem)
                .putParcelableArrayListExtra("MyList", productItem.getPrice()));
  }

  @Override
  public void onResume() {
    super.onResume();
    HomeActivity.getInstance().titleChange();
    HomeActivity.getInstance().setFrameMargin(60);
    if (itemAdp != null) {
      itemAdp.notifyDataSetChanged();
    }
    if (iscart) {
      iscart = false;
      CardFragment fragment = new CardFragment();
      HomeActivity.getInstance().callFragment(fragment);
    }
  }
}
