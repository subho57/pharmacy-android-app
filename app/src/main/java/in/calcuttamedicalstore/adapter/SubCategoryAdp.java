package in.calcuttamedicalstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.SubcatItem;
import in.calcuttamedicalstore.retrofit.APIClient;

public class SubCategoryAdp extends RecyclerView.Adapter<SubCategoryAdp.MyViewHolder> {

  private final Context mContext;
  private final List<SubcatItem> categoryList;
  private final RecyclerTouchListener listener;

  public SubCategoryAdp(
      Context mContext, List<SubcatItem> categoryList, final RecyclerTouchListener listener) {
    this.mContext = mContext;
    this.categoryList = categoryList;
    this.listener = listener;
  }

  @NotNull
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
    return new MyViewHolder(itemView);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {
    SubcatItem category = categoryList.get(position);
    holder.title.setText(category.getName() + "(" + category.getCount() + ")");
    Glide.with(mContext)
        .load(APIClient.baseUrl + category.getImg())
        .thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize))
        .into(holder.thumbnail);
    holder.thumbnail.setOnClickListener(
        v ->
            listener.onClickItem(
                v,
                Integer.parseInt(category.getCatId()),
                Integer.parseInt(category.getId()),
                category.getImg()));
  }

  @Override
  public int getItemCount() {
    return categoryList.size();
  }

  public interface RecyclerTouchListener {
    void onClickItem(View v, int cid, int scid, String img);

    void onLongClickItem(View v, int position);
  }

  public static class MyViewHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public ImageView thumbnail;

    public MyViewHolder(View view) {
      super(view);
      title = view.findViewById(R.id.txt_title);
      thumbnail = view.findViewById(R.id.imageView);
    }
  }
}
