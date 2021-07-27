package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

public class MainDataHome {

  @SerializedName("raz_key")
  private String razKey;

  @SerializedName("logo")
  private String logo;

  @SerializedName("currency")
  private String currency;

  @SerializedName("privacy_policy")
  private String privacyPolicy;

  @SerializedName("callsupport")
  private String callsupport;

  @SerializedName("about_us")
  private String aboutUs;

  @SerializedName("terms")
  private String terms;

  @SerializedName("contact_us")
  private String contactUs;

  @SerializedName("o_min")
  private int oMin;

  @SerializedName("tax")
  private String tax;

  @SerializedName("maintaince")
  private int maintaince;

  public String getLogo() {
    return logo;
  }

  public String getTax() {
    return tax;
  }

  public void setTax(String tax) {
    this.tax = tax;
  }

  public String getContactUs() {
    return contactUs;
  }

  public void setContactUs(String contactUs) {
    this.contactUs = contactUs;
  }

  public String getRazKey() {
    return razKey;
  }

  public void setRazKey(String razKey) {
    this.razKey = razKey;
  }

  public String getCurrency() {
    return currency;
  }

  public void setCurrency(String currency) {
    this.currency = currency;
  }

  public String getCallsupport() {
    return callsupport;
  }

  public void setCallsupport(String callsupport) {
    this.callsupport = callsupport;
  }

  public String getPrivacyPolicy() {
    return privacyPolicy;
  }

  public void setPrivacyPolicy(String privacyPolicy) {
    this.privacyPolicy = privacyPolicy;
  }

  public String getAboutUs() {
    return aboutUs;
  }

  public void setAboutUs(String aboutUs) {
    this.aboutUs = aboutUs;
  }

  public int getoMin() {
    return oMin;
  }

  public void setoMin(int oMin) {
    this.oMin = oMin;
  }

  public String getTerms() {
    return terms;
  }

  public void setTerms(String terms) {
    this.terms = terms;
  }

  public int getMaintaince() {
    return maintaince;
  }

  public void setMaintaince(int maintaince) {
    this.maintaince = maintaince;
  }
}
