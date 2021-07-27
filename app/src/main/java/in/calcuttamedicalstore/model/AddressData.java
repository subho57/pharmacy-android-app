package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddressData {

  @SerializedName("ResponseCode")
  private String mResponseCode;

  @SerializedName("ResponseMsg")
  private String mResponseMsg;

  @SerializedName("Result")
  private String mResult;

  @SerializedName("Wallet")
  private float Wallet;

  @SerializedName("ResultData")
  private List<Address> mResultData;

  public float getWallet() {
    return Wallet;
  }

  public void setWallet(float wallet) {
    Wallet = wallet;
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

  public List<Address> getResultData() {
    return mResultData;
  }

  public void setResultData(List<Address> resultData) {
    mResultData = resultData;
  }
}
