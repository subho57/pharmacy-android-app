package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Coupon {

  @SerializedName("couponlist")
  private List<Couponlist> mCouponlist;

  @SerializedName("ResponseCode")
  private String mResponseCode;

  @SerializedName("ResponseMsg")
  private String mResponseMsg;

  @SerializedName("Result")
  private String mResult;

  public List<Couponlist> getCouponlist() {
    return mCouponlist;
  }

  public void setCouponlist(List<Couponlist> couponlist) {
    mCouponlist = couponlist;
  }

  public String getResponseCode() {
    return mResponseCode;
  }

  public void setResponseCode(String responseCode) {
    mResponseCode = responseCode;
  }

  public String getResponseMsg() {
    return mResponseMsg;
  }

  public void setResponseMsg(String responseMsg) {
    mResponseMsg = responseMsg;
  }

  public String getResult() {
    return mResult;
  }

  public void setResult(String result) {
    mResult = result;
  }
}
