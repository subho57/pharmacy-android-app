package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

public class Couponlist {

  @SerializedName("c_desc")
  private String mCDesc;

  @SerializedName("c_img")
  private String mCImg;

  @SerializedName("c_t_mode")
  private String mCTMode;

  @SerializedName("coupon_code")
  private String couponCode;

  @SerializedName("coupon_title")
  private String couponTitle;

  @SerializedName("c_type")
  private String mCType;

  @SerializedName("c_value")
  private String mCValue;

  @SerializedName("cdate")
  private String mCdate;

  @SerializedName("id")
  private String mId;

  @SerializedName("min_amt")
  private int mMinAmt;

  @SerializedName("min_quan")
  private int mMinQuan;

  @SerializedName("n_use")
  private String mNUse;

  public String getCDesc() {
    return mCDesc;
  }

  public void setCDesc(String cDesc) {
    mCDesc = cDesc;
  }

  public String getCImg() {
    return mCImg;
  }

  public void setCImg(String cImg) {
    mCImg = cImg;
  }

  public String getCTMode() {
    return mCTMode;
  }

  public void setCTMode(String cTMode) {
    mCTMode = cTMode;
  }

  public String getCouponCode() {
    return couponCode;
  }

  public void setCouponCode(String couponCode) {
    this.couponCode = couponCode;
  }

  public String getCType() {
    return mCType;
  }

  public void setCType(String cType) {
    mCType = cType;
  }

  public String getCValue() {
    return mCValue;
  }

  public void setCValue(String cValue) {
    mCValue = cValue;
  }

  public String getCdate() {
    return mCdate;
  }

  public void setCdate(String cdate) {
    mCdate = cdate;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public int getMinAmt() {
    return mMinAmt;
  }

  public void setMinAmt(int minAmt) {
    mMinAmt = minAmt;
  }

  public int getMinQuan() {
    return mMinQuan;
  }

  public void setMinQuan(int minQuan) {
    mMinQuan = minQuan;
  }

  public String getNUse() {
    return mNUse;
  }

  public void setNUse(String nUse) {
    mNUse = nUse;
  }

  public String getCouponTitle() {
    return couponTitle;
  }

  public void setCouponTitle(String couponTitle) {
    this.couponTitle = couponTitle;
  }
}
