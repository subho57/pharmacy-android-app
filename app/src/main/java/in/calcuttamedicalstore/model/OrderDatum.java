package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

public class OrderDatum {

  @SerializedName("id")
  private String mId;

  @SerializedName("oid")
  private String mOid;

  @SerializedName("order_date")
  private String mOrderDate;

  @SerializedName("status")
  private String mStatus;

  @SerializedName("total")
  private String totalamt;

  public String getTotalamt() {
    return totalamt;
  }

  public void setTotalamt(String totalamt) {
    this.totalamt = totalamt;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getOid() {
    return mOid;
  }

  public void setOid(String oid) {
    mOid = oid;
  }

  public String getOrderDate() {
    return mOrderDate;
  }

  public void setOrderDate(String orderDate) {
    mOrderDate = orderDate;
  }

  public String getStatus() {
    String upperString = mStatus.substring(0, 1).toUpperCase() + mStatus.substring(1).toLowerCase();

    return upperString;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
