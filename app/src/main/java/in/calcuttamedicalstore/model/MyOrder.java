package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MyOrder {

  @SerializedName("order_date")
  private String mOrderDate;

  @SerializedName("productinfo")
  private List<Productinfo> mProductinfo;

  @SerializedName("ResponseCode")
  private String mResponseCode;

  @SerializedName("ResponseMsg")
  private String mResponseMsg;

  @SerializedName("Result")
  private String mResult;

  @SerializedName("status")
  private String mStatus;

  @SerializedName("timesloat")
  private String mTimesloat;

  @SerializedName("total_amt")
  private double mTotalAmt;

  @SerializedName("Israted")
  private String mIsrated;

  @SerializedName("orderid")
  private String mOrderid;

  @SerializedName("d_charge")
  private String dCharge;

  @SerializedName("rider_name")
  private String riderName;

  @SerializedName("rider_mobile")
  private String riderMobile;

  @SerializedName("p_method")
  private String pMethod;

  @SerializedName("address")
  private String address;

  @SerializedName("address_type")
  private String addressType;

  @SerializedName("customer_name")
  private String customerName;

  @SerializedName("counpon_discount")
  private String counponDiscount;

  @SerializedName("tax")
  private double tax;

  @SerializedName("Sub_total")
  private double subTotal;

  @SerializedName("wallet_discount")
  private String walletDiscount;

  public String getWalletDiscount() {
    return walletDiscount;
  }

  public void setWalletDiscount(String walletDiscount) {
    this.walletDiscount = walletDiscount;
  }

  public String getCounponDiscount() {
    return counponDiscount;
  }

  public void setCounponDiscount(String counponDiscount) {
    this.counponDiscount = counponDiscount;
  }

  public double getSubTotal() {
    return subTotal;
  }

  public void setSubTotal(double subTotal) {
    this.subTotal = subTotal;
  }

  public double getTax() {
    return tax;
  }

  public void setTax(double tax) {
    this.tax = tax;
  }

  public String getdCharge() {
    return dCharge;
  }

  public void setdCharge(String dCharge) {
    this.dCharge = dCharge;
  }

  public String getmOrderid() {
    return mOrderid;
  }

  public void setmOrderid(String mOrderid) {
    this.mOrderid = mOrderid;
  }

  public String getmIsrated() {
    return mIsrated;
  }

  public void setmIsrated(String mIsrated) {
    this.mIsrated = mIsrated;
  }

  public String getOrderDate() {
    return mOrderDate;
  }

  public void setOrderDate(String orderDate) {
    mOrderDate = orderDate;
  }

  public List<Productinfo> getProductinfo() {
    return mProductinfo;
  }

  public void setProductinfo(List<Productinfo> productinfo) {
    mProductinfo = productinfo;
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

  public String getStatus() {
    String upperString = mStatus.substring(0, 1).toUpperCase() + mStatus.substring(1).toLowerCase();

    return upperString;
  }

  public void setStatus(String status) {
    mStatus = status;
  }

  public String getTimesloat() {
    return mTimesloat;
  }

  public void setTimesloat(String timesloat) {
    mTimesloat = timesloat;
  }

  public double getTotalAmt() {

    return mTotalAmt;
  }

  public void setTotalAmt(double totalAmt) {

    mTotalAmt = totalAmt;
  }

  public String getRiderName() {
    return riderName;
  }

  public void setRiderName(String riderName) {
    this.riderName = riderName;
  }

  public String getRiderMobile() {
    return riderMobile;
  }

  public void setRiderMobile(String riderMobile) {
    this.riderMobile = riderMobile;
  }

  public String getpMethod() {

    return pMethod;
  }

  public void setpMethod(String pMethod) {
    this.pMethod = pMethod;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getAddressType() {
    return addressType;
  }

  public void setAddressType(String addressType) {
    this.addressType = addressType;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }
}
