package in.calcuttamedicalstore.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DynamicData {

  @SerializedName("title")
  private String title;

  @SerializedName("product_data")
  private List<ProductItem> dynamicItems;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<ProductItem> getDynamicItems() {
    return dynamicItems;
  }

  public void setDynamicItems(List<ProductItem> dynamicItems) {
    this.dynamicItems = dynamicItems;
  }
}
