package in.calcuttamedicalstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.activity.ItemDetailsActivity;
import in.calcuttamedicalstore.database.DatabaseHelper;
import in.calcuttamedicalstore.database.MyCart;
import in.calcuttamedicalstore.model.Price;
import in.calcuttamedicalstore.model.ProductItem;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.utils.SessionManager;

import static in.calcuttamedicalstore.fragment.ItemListFragment.itemListFragment;
import static in.calcuttamedicalstore.utils.SessionManager.currncy;

public class ItemAdp extends RecyclerView.Adapter<ItemAdp.ViewHolder> {
  private final List<ProductItem> mData;
  private final LayoutInflater mInflater;
  Context mContext;
  SessionManager sessionManager;
  private ItemClickListener mClickListener;

  public ItemAdp(Context context, List<ProductItem> data) {
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
    View view = mInflater.inflate(R.layout.item_custome, parent, false);
    return new ViewHolder(view);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
    ProductItem datum = mData.get(position);
    if (datum.getStock() == 0) {
      holder.lvlOutofstock.setVisibility(View.VISIBLE);
    } else {
      holder.lvlOutofstock.setVisibility(View.GONE);
    }
    Glide.with(mContext)
        .load(APIClient.baseUrl + datum.getProductImage())
        .thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize))
        .into(holder.imgIcon);
    holder.txtTitle.setText("" + datum.getProductName());
    holder.imgIcon.setOnClickListener(
        v ->
            mContext.startActivity(
                new Intent(mContext, ItemDetailsActivity.class)
                    .putExtra("MyClass", datum)
                    .putParcelableArrayListExtra("MyList", datum.getPrice())));
    if (datum.getmDiscount() > 0) {
      holder.lvlOffer.setVisibility(View.VISIBLE);
      holder.txtOffer.setText(datum.getmDiscount() + "% Off");
    } else {
      holder.lvlOffer.setVisibility(View.GONE);
    }
    List<String> Arealist = new ArrayList<>();
    for (int i = 0; i < datum.getPrice().size(); i++) {

      Arealist.add(datum.getPrice().get(i).getProductType());
    }
    ArrayAdapter<String> dataAdapter =
        new ArrayAdapter<>(mContext, R.layout.spinner_layout, Arealist);
    dataAdapter.setDropDownViewResource(R.layout.spinner_layout);
    holder.spinner.setAdapter(dataAdapter);
    holder.spinner.setOnItemSelectedListener(
        new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (datum.getmDiscount() > 0) {
              double res =
                  (Double.parseDouble(datum.getPrice().get(position).getProductPrice()) / 100.0f)
                      * datum.getmDiscount();
              res = Double.parseDouble(datum.getPrice().get(position).getProductPrice()) - res;
              holder.txtItemOffer.setText(
                  sessionManager.getStringData(currncy)
                      + datum.getPrice().get(position).getProductPrice());
              holder.txtItemOffer.setPaintFlags(
                  holder.txtItemOffer.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
              holder.txtPrice.setText(
                  sessionManager.getStringData(currncy) + new DecimalFormat("##.##").format(res));
              holder.txtItemOffer.setText(
                  sessionManager.getStringData(currncy)
                      + datum.getPrice().get(position).getProductPrice());
            } else {
              holder.txtItemOffer.setVisibility(View.GONE);
              holder.txtPrice.setText(
                  sessionManager.getStringData(currncy)
                      + datum.getPrice().get(position).getProductPrice());
            }
            setJoinPlayrList(holder.lvlSubitem, datum, datum.getPrice().get(position));
          }

          @Override
          public void onNothingSelected(AdapterView<?> parent) {}
        });
  }

  @Override
  public int getItemCount() {
    return mData.size();
  }

  @SuppressLint("SetTextI18n")
  private void setJoinPlayrList(LinearLayout lnrView, ProductItem datum, Price price) {
    lnrView.removeAllViews();
    final int[] count = {0};
    DatabaseHelper helper = new DatabaseHelper(lnrView.getContext());
    LayoutInflater inflater = LayoutInflater.from(mContext);
    @SuppressLint("InflateParams")
    View view = inflater.inflate(R.layout.custome_prize, null);
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
            txtcount.setText("" + count[0]);
            lvl_addremove.setVisibility(View.GONE);
            lvl_addcart.setVisibility(View.VISIBLE);
            helper.deleteRData(myCart.getPid(), myCart.getCost());
          } else {
            txtcount.setVisibility(View.VISIBLE);
            txtcount.setText("" + count[0]);
            myCart.setQty(String.valueOf(count[0]));
            helper.insertData(myCart);
          }
          itemListFragment.updateItem();
        });

    img_plus.setOnClickListener(
        v -> {
          count[0] = Integer.parseInt(txtcount.getText().toString());
          count[0] = count[0] + 1;
          txtcount.setText("" + count[0]);
          myCart.setQty(String.valueOf(count[0]));
          Log.e("INsert", "--> " + helper.insertData(myCart));
          itemListFragment.updateItem();
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
          itemListFragment.updateItem();
        });
    lnrView.addView(view);
  }

  public interface ItemClickListener {
    void onItemClick(View view, int position);
  }

  @SuppressLint("NonConstantResourceId")
  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.txt_offer)
    TextView txtOffer;

    @BindView(R.id.txt_item_offer)
    TextView txtItemOffer;

    @BindView(R.id.txt_price)
    TextView txtPrice;

    @BindView(R.id.lvl_subitem)
    LinearLayout lvlSubitem;

    @BindView(R.id.lvl_offer)
    LinearLayout lvlOffer;

    @BindView(R.id.lvl_outofstock)
    LinearLayout lvlOutofstock;

    @BindView(R.id.spinner)
    Spinner spinner;

    @BindView(R.id.img_icon)
    ImageView imgIcon;

    ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
      itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
      if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
    }
  }
}
