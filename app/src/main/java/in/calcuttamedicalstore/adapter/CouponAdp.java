package in.calcuttamedicalstore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.model.Couponlist;
import in.calcuttamedicalstore.retrofit.APIClient;
import in.calcuttamedicalstore.utils.SessionManager;

import static in.calcuttamedicalstore.utils.SessionManager.CURRUNCY;

public class CouponAdp extends RecyclerView.Adapter<CouponAdp.MyViewHolder> {

  private final Context mContext;
  private final List<Couponlist> couponlists;
  private final RecyclerTouchListener listener;
  private final int amount;
  private final int quantity;
  SessionManager sessionManager;

  public CouponAdp(
      Context mContext,
      List<Couponlist> categoryList,
      final RecyclerTouchListener listener,
      int amount,
      int quantity) {
    this.mContext = mContext;
    this.couponlists = categoryList;
    this.listener = listener;
    this.amount = amount;
    this.quantity = quantity;
    sessionManager = new SessionManager(mContext);
  }

  public static void makeTextViewResizable(
      final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

    if (tv.getTag() == null) {
      tv.setTag(tv.getText());
    }
    ViewTreeObserver vto = tv.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(
        new ViewTreeObserver.OnGlobalLayoutListener() {

          @SuppressWarnings("deprecation")
          @Override
          public void onGlobalLayout() {

            ViewTreeObserver obs = tv.getViewTreeObserver();
            obs.removeGlobalOnLayoutListener(this);
            if (maxLine == 0) {
              int lineEndIndex = tv.getLayout().getLineEnd(0);
              String text =
                  tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1)
                      + " "
                      + expandText;
              tv.setText(text);
              tv.setMovementMethod(LinkMovementMethod.getInstance());
              tv.setText(
                  addClickablePartTextViewResizable(
                      Html.fromHtml(tv.getText().toString()), tv, expandText, viewMore),
                  TextView.BufferType.SPANNABLE);
            } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
              int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
              String text =
                  tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1)
                      + " "
                      + expandText;
              tv.setText(text);
              tv.setMovementMethod(LinkMovementMethod.getInstance());
              tv.setText(
                  addClickablePartTextViewResizable(
                      Html.fromHtml(tv.getText().toString()), tv, expandText, viewMore),
                  TextView.BufferType.SPANNABLE);
            } else {
              int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
              String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
              tv.setText(text);
              tv.setMovementMethod(LinkMovementMethod.getInstance());
              tv.setText(
                  addClickablePartTextViewResizable(
                      Html.fromHtml(tv.getText().toString()), tv, expandText, viewMore),
                  TextView.BufferType.SPANNABLE);
            }
          }
        });
  }

  private static SpannableStringBuilder addClickablePartTextViewResizable(
      final Spanned strSpanned,
      final TextView tv,
      final String spanableText,
      final boolean viewMore) {
    String str = strSpanned.toString();
    SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

    if (str.contains(spanableText)) {

      ssb.setSpan(
          new MySpannable(false) {
            @Override
            public void onClick(@NotNull View widget) {
              if (viewMore) {
                tv.setLayoutParams(tv.getLayoutParams());
                tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                tv.invalidate();
                makeTextViewResizable(tv, -1, "See Less", false);
              } else {
                tv.setLayoutParams(tv.getLayoutParams());
                tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                tv.invalidate();
                makeTextViewResizable(tv, 3, ".. See More", true);
              }
            }
          },
          str.indexOf(spanableText),
          str.indexOf(spanableText) + spanableText.length(),
          0);
    }
    return ssb;
  }

  @NotNull
  @Override
  public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.item_coupon, parent, false);
    return new MyViewHolder(itemView);
  }

  @SuppressLint("SetTextI18n")
  @Override
  public void onBindViewHolder(final MyViewHolder holder, int position) {

    Couponlist coupon = couponlists.get(position);
    Glide.with(mContext)
        .load(APIClient.baseUrl + coupon.getCImg())
        .thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize))
        .into(holder.imgCode);

    if (amount < coupon.getMinAmt() || quantity < coupon.getMinQuan()) {
      holder.txtApply.setTextColor(mContext.getResources().getColor(R.color.colorGrey1));
      holder.txtApply.setEnabled(false);
    } else {
      holder.txtApply.setEnabled(true);
      holder.txtApply.setTextColor(mContext.getResources().getColor(R.color.colorWhite));
    }
    holder.txtApply.setText(sessionManager.getStringData(CURRUNCY) + " " + coupon.getCValue());
    holder.txtCoupon.setText("" + coupon.getCouponCode());
    holder.txtTitel.setText("" + coupon.getCouponTitle());
    holder.txtminamt.setText("Minimum Cart Amt.: ₹" + coupon.getMinAmt());
    holder.txtminquan.setText(("Minimum Cart Qty.: " + coupon.getMinQuan()));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
      holder.txtDesc.setText(Html.fromHtml(coupon.getCDesc(), Html.FROM_HTML_MODE_COMPACT));
    } else {
      holder.txtDesc.setText(Html.fromHtml(coupon.getCDesc()));
    }
    makeTextViewResizable(holder.txtDesc, 3, "See More", true);
    holder.txtApply.setOnClickListener(v -> listener.onClickItem(v, coupon));
  }

  @Override
  public int getItemCount() {
    return couponlists.size();
  }

  public interface RecyclerTouchListener {
    void onClickItem(View v, Couponlist coupon);
  }

  @SuppressLint("NonConstantResourceId")
  public static class MyViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.txt_coupon)
    TextView txtCoupon;

    @BindView(R.id.txt_apply)
    TextView txtApply;

    @BindView(R.id.txt_titel)
    TextView txtTitel;

    @BindView(R.id.txt_desc)
    TextView txtDesc;

    @BindView(R.id.img_code)
    CircleImageView imgCode;

    @BindView(R.id.txt_minamt)
    TextView txtminamt;

    @BindView(R.id.txt_minquan)
    TextView txtminquan;

    public MyViewHolder(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }

  public static class MySpannable extends ClickableSpan {

    private final boolean isUnderline;

    /** Constructor */
    public MySpannable(boolean isUnderline) {
      this.isUnderline = isUnderline;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
      ds.setUnderlineText(isUnderline);
      ds.setColor(Color.parseColor("#ffce33"));
    }

    @Override
    public void onClick(@NotNull View widget) {}
  }
}
