package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Payment {

  @SerializedName("data")
  private List<PaymentItem> mData;

  @SerializedName("ResponseCode")
  private String mResponseCode;

  @SerializedName("ResponseMsg")
  private String mResponseMsg;

  @SerializedName("Result")
  private String mResult;

  public List<PaymentItem> getData() {
    return mData;
  }

  public void setData(List<PaymentItem> data) {
    mData = data;
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
