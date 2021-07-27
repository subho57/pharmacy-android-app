package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentItem implements Serializable {

  @SerializedName("cred_title")
  private String mCredTitle;

  @SerializedName("cred_value")
  private String mCredValue;

  @SerializedName("id")
  private String mId;

  @SerializedName("img")
  private String mImg;

  @SerializedName("status")
  private String mStatus;

  @SerializedName("title")
  private String mTitle;

  public String getCredTitle() {
    return mCredTitle;
  }

  public void setCredTitle(String credTitle) {
    mCredTitle = credTitle;
  }

  public String getCredValue() {
    return mCredValue;
  }

  public void setCredValue(String credValue) {
    mCredValue = credValue;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getImg() {
    return mImg;
  }

  public void setImg(String img) {
    mImg = img;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }

  public String getTitle() {
    return mTitle;
  }

  public void setTitle(String title) {
    mTitle = title;
  }
}
