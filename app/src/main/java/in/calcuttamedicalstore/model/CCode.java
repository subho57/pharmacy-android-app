package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

public class CCode {

  @SerializedName("ccode")
  private String mCcode;

  @SerializedName("id")
  private String mId;

  @SerializedName("status")
  private String mStatus;

  public String getCcode() {
    return mCcode;
  }

  public void setCcode(String ccode) {
    mCcode = ccode;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public String getStatus() {
    return mStatus;
  }

  public void setStatus(String status) {
    mStatus = status;
  }
}
