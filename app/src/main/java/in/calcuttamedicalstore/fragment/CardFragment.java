package in.calcuttamedicalstore.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.HomeActivity;
import in.calcuttamedicalstore.activity.LoginActivity;
import in.calcuttamedicalstore.database.DatabaseHelper;
import in.calcuttamedicalstore.database.MyCart;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.utils.SessionManager;

import static in.calcuttamedicalstore.utils.SessionManager.currncy;
import static in.calcuttamedicalstore.utils.SessionManager.login;
import static in.calcuttamedicalstore.utils.SessionManager.oMin;

@SuppressLint("NonConstantResourceId")
public class CardFragment extends Fragment {
  @BindView(R.id.txt_notfound)
  TextView txtNotfound;

  @BindView(R.id.lvl_notfound)
  LinearLayout lvlNotfound;

  @BindView(R.id.my_recycler_view)
  RecyclerView recyclerView;

  Unbinder unbinder;
  DatabaseHelper databaseHelper;
  List<MyCart> myCarts;

  @BindView(R.id.txt_item)
  TextView txtItem;

  @BindView(R.id.totleAmount)
  TextView totleAmount;

  @BindView(R.id.lvlbacket)
  LinearLayout lvlBacket;

  StaggeredGridLayoutManager gridLayoutManager;

  SessionManager sessionManager;
  double total = 0;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card, container, false);
    unbinder = ButterKnife.bind(this, view);
    databaseHelper = new DatabaseHelper(getActivity());
    sessionManager = new SessionManager(getActivity());
    HomeActivity.getInstance().serchviewShow();
    myCarts = new ArrayList<>();

    gridLayoutManager = new StaggeredGridLayoutManager(1, 1);
    recyclerView.setLayoutManager(gridLayoutManager);
    Cursor res = databaseHelper.getAllData();
    if (res.getCount() == 0) {
      lvlNotfound.setVisibility(View.VISIBLE);
      txtNotfound.setText("Cart Empty");
      lvlBacket.setVisibility(View.GONE);
    }
    while (res.moveToNext()) {
      MyCart rModel = new MyCart();
      rModel.setId(res.getString(0));
      rModel.setPid(res.getString(1));
      rModel.setImage(res.getString(2));
      rModel.setTitle(res.getString(3));
      rModel.setWeight(res.getString(4));
      rModel.setCost(res.getString(5));
      rModel.setQty(res.getString(6));
      rModel.setDiscount(res.getInt(7));
      myCarts.add(rModel);
    }

    ItemAdp itemAdp = new ItemAdp(getActivity(), myCarts);
    recyclerView.setAdapter(itemAdp);
    updateItem();
    return view;
  }

  @SuppressLint("SetTextI18n")
  public void updateItem() {
    Cursor res = databaseHelper.getAllData();
    double totalRs = 0;
    double ress;
    int totalItem = 0;
    if (res.getCount() == 0) {
      lvlNotfound.setVisibility(View.VISIBLE);
      txtNotfound.setText("Cart Empty");
      lvlBacket.setVisibility(View.GONE);
    }
    while (res.moveToNext()) {
      MyCart rModel = new MyCart();
      rModel.setCost(res.getString(5));
      rModel.setQty(res.getString(6));
      rModel.setDiscount(res.getInt(7));
      ress = (Double.parseDouble(res.getString(5)) * rModel.getDiscount()) / 100;
      ress = Double.parseDouble(res.getString(5)) - ress;
      double temp = Integer.parseInt(res.getString(6)) * ress;
      totalRs = totalRs + temp;
      totalItem = totalItem + Integer.parseInt(res.getString(6));
    }
    total = Double.parseDouble(String.valueOf(totalRs));
    txtItem.setText(totalItem + " Items");
    totleAmount.setText(
        sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(totalRs));
    HomeActivity.getInstance().setFrameMargin(60);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @OnClick(R.id.txt_countinue)
  public void onViewClicked() {

    if (sessionManager.getBooleanData(login)) {
      if (sessionManager.getIntData(oMin) <= total) {
        HomeActivity.getInstance().serchviewHide();
        HomeActivity.getInstance().titleChange("Place Order Now");
        PlaceOrderFragment fragment = new PlaceOrderFragment();
        HomeActivity.getInstance().callFragment(fragment);
      } else {
        Toast.makeText(
                getActivity(),
                "Minimum order value of "
                    + sessionManager.getStringData(currncy)
                    + " "
                    + sessionManager.getIntData(oMin),
                Toast.LENGTH_SHORT)
            .show();
      }
    } else {
      startActivity(new Intent(getActivity(), LoginActivity.class));
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    HomeActivity.getInstance().serchviewShow();
    HomeActivity.getInstance().setFrameMargin(60);
    HomeActivity.getInstance().titleChange("My Cart");
  }

  public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
    final int[] count = {0};
    private final List<MyCart> mData;
    private final LayoutInflater mInflater;
    double[] totalAmount = {0};
    DatabaseHelper helper = new DatabaseHelper(getActivity());
    Context mContext;
    SessionManager sessionManager;

    public ItemAdp(Context context, List<MyCart> data) {
      this.mInflater = LayoutInflater.from(context);
      this.mData = data;
      this.mContext = context;
      WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
      Display display = wm.getDefaultDisplay();
      Point size = new Point();
      display.getSize(size);
      sessionManager = new SessionManager(mContext);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
      View view = mInflater.inflate(R.layout.custome_mycard, parent, false);
      return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int i) {
      MyCart cart = mData.get(i);
      Glide.with(Objects.requireNonNull(getActivity()))
          .load(APIClient.baseUrl + "/" + cart.getImage())
          .thumbnail(Glide.with(getActivity()).load(R.drawable.lodingimage))
          .into(holder.imgIcon);
      double res = (Double.parseDouble(cart.getCost()) * myCarts.get(i).getDiscount()) / 100;
      res = Double.parseDouble(cart.getCost()) - res;
      holder.txtGram.setText("  " + cart.getWeight() + "  ");
      holder.txtPrice.setText(
          sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(res));
      holder.txtTitle.setText("" + cart.getTitle());

      MyCart myCart = new MyCart();
      myCart.setPid(cart.getPid());
      myCart.setImage(cart.getImage());
      myCart.setTitle(cart.getTitle());
      myCart.setWeight(cart.getWeight());
      myCart.setCost(cart.getCost());
      myCart.setDiscount(cart.getDiscount());
      int qrt = helper.getCard(myCart.getPid(), myCart.getCost());
      if (qrt != -1) {
        count[0] = qrt;
        holder.txtcount.setText("" + count[0]);
        holder.txtcount.setVisibility(View.VISIBLE);
        holder.imgMins.setVisibility(View.VISIBLE);

      } else {
        holder.txtcount.setVisibility(View.INVISIBLE);
        holder.imgMins.setVisibility(View.INVISIBLE);
      }
      double ress = (Double.parseDouble(myCart.getCost()) / 100.0f) * myCart.getDiscount();
      ress = Double.parseDouble(myCart.getCost()) - ress;
      double temp = ress * qrt;
      totalAmount[0] = totalAmount[0] + temp;
      holder.imgMins.setOnClickListener(
          v -> {
            count[0] = Integer.parseInt(holder.txtcount.getText().toString());
            count[0] = count[0] - 1;
            if (count[0] <= 0) {
              holder.txtcount.setVisibility(View.INVISIBLE);
              holder.imgMins.setVisibility(View.INVISIBLE);
              holder.txtcount.setText("" + count[0]);
              helper.deleteRData(myCart.getPid(), myCart.getCost());
              myCarts.remove(cart);

              totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
              Toast.makeText(
                      getActivity(),
                      "" + myCart.getTitle() + " " + myCart.getWeight() + " is removed",
                      Toast.LENGTH_LONG)
                  .show();
              if (totalAmount[0] == 0) {
                lvlBacket.setVisibility(View.GONE);
              }
            } else {
              holder.txtcount.setVisibility(View.VISIBLE);
              holder.txtcount.setText("" + count[0]);
              myCart.setQty(String.valueOf(count[0]));
              totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
              helper.insertData(myCart);
            }
            notifyDataSetChanged();
            updateItem();
          });
      holder.imgPlus.setOnClickListener(
          v -> {
            holder.txtcount.setVisibility(View.VISIBLE);
            holder.imgMins.setVisibility(View.VISIBLE);
            count[0] = Integer.parseInt(holder.txtcount.getText().toString());
            totalAmount[0] = totalAmount[0] + Double.parseDouble(myCart.getCost());
            count[0] = count[0] + 1;
            holder.txtcount.setText("" + count[0]);
            myCart.setQty(String.valueOf(count[0]));
            helper.insertData(myCart);
            updateItem();
          });
      holder.imgDelete.setOnClickListener(
          v -> {
            AlertDialog myDelete =
                new AlertDialog.Builder(getActivity())
                    .setTitle("Remove Item")
                    .setMessage("Are you sure?")
                    .setIcon(R.drawable.ic_delete)
                    .setPositiveButton(
                        "Delete",
                        (dialog, whichButton) -> {
                          // Log.d("sdj", "" + whichButton);
                          dialog.dismiss();
                          totalAmount[0] = totalAmount[0] - Double.parseDouble(myCart.getCost());
                          helper.deleteRData(myCart.getPid(), myCart.getCost());
                          myCarts.remove(cart);
                          updateItem();
                          notifyDataSetChanged();
                        })
                    .setNegativeButton(
                        "cancel",
                        (dialog, which) -> {
                          // Log.d("sdj", "" + which);
                          dialog.dismiss();
                        })
                    .create();
            myDelete.show();
          });
    }

    @Override
    public int getItemCount() {
      return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
      @BindView(R.id.img_icon)
      ImageView imgIcon;

      @BindView(R.id.txt_title)
      TextView txtTitle;

      @BindView(R.id.txt_price)
      TextView txtPrice;

      @BindView(R.id.txt_gram)
      TextView txtGram;

      @BindView(R.id.img_delete)
      ImageView imgDelete;

      @BindView(R.id.img_mins)
      LinearLayout imgMins;

      @BindView(R.id.txtcount)
      TextView txtcount;

      @BindView(R.id.img_plus)
      LinearLayout imgPlus;

      @BindView(R.id.lvl_addremove)
      LinearLayout lvlAddremove;

      ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
      }
    }
  }
}
