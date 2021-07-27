package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

public class UpdateAddress {

  @SerializedName("Address")
  private Address address;

  @SerializedName("ResponseCode")
  private String mResponseCode;

  @SerializedName("ResponseMsg")
  private String mResponseMsg;

  @SerializedName("Result")
  private String mResult;

  public Address getAddress() {
    return address;
  }

  public void setAddress(Address address) {
    this.address = address;
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
