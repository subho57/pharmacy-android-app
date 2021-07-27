package in.calcuttamedicalstore.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PayTmInitiateTransactionApiResponse {

  @SerializedName("body")
  @Expose
  private Body body;

  public Body getBody() {
    return body;
  }

  public class ResultInfo {

    @SerializedName("resultStatus")
    @Expose
    private String resultStatus;

    @SerializedName("resultCode")
    @Expose
    private String resultCode;

    @SerializedName("resultMsg")
    @Expose
    private String resultMsg;

    public String getResultStatus() {
      return resultStatus;
    }

    public String getResultCode() {
      return resultCode;
    }

    public String getResultMsg() {
      return resultMsg;
    }
  }

  public class Body {

    @SerializedName("resultInfo")
    @Expose
    private ResultInfo resultInfo;

    @SerializedName("txnToken")
    @Expose
    private String txnToken;

    @SerializedName("isPromoCodeValid")
    @Expose
    private boolean isPromoCodeValid;

    @SerializedName("authenticated")
    @Expose
    private boolean authenticated;

    public ResultInfo getResultInfo() {
      return resultInfo;
    }

    public String getTxnToken() {
      return txnToken;
    }

    public boolean isIsPromoCodeValid() {
      return isPromoCodeValid;
    }

    public boolean isAuthenticated() {
      return authenticated;
    }
  }
}
