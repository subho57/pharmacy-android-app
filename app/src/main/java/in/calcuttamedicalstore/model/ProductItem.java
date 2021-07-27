package in.calcuttamedicalstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProductItem implements Parcelable {

  public static final Creator<ProductItem> CREATOR =
      new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel in) {
          return new ProductItem(in);
        }

        @Override
        public ProductItem[] newArray(int size) {
          return new ProductItem[size];
        }
      };

  @SerializedName("id")
  private String mId;

  @SerializedName("price")
  private ArrayList<Price> mPrice;

  @SerializedName("product_image")
  private String mProductImage;

  @SerializedName("product_related_image")
  private String productRelatedImage;

  @SerializedName("product_name")
  private String mProductName;

  @SerializedName("seller_name")
  private String mSellerName;

  @SerializedName("short_desc")
  private String mShortDesc;

  @SerializedName("stock")
  private int mStock;

  @SerializedName("discount")
  private int mDiscount;

  @SerializedName("cat_id")
  private String catId;

  @SerializedName("subcat_id")
  private String subcatId;

  protected ProductItem(Parcel in) {
    mId = in.readString();
    mProductImage = in.readString();
    productRelatedImage = in.readString();
    mProductName = in.readString();
    mSellerName = in.readString();
    mShortDesc = in.readString();
    mStock = in.readInt();
    mDiscount = in.readInt();
    catId = in.readString();
    subcatId = in.readString();
  }

  public int getmDiscount() {
    return mDiscount;
  }

  public void setmDiscount(int mDiscount) {
    this.mDiscount = mDiscount;
  }

  public String getId() {
    return mId;
  }

  public void setId(String id) {
    mId = id;
  }

  public ArrayList<Price> getPrice() {
    return mPrice;
  }

  public void setPrice(ArrayList<Price> price) {
    mPrice = price;
  }

  public String getProductImage() {
    return mProductImage;
  }

  public void setProductImage(String productImage) {
    mProductImage = productImage;
  }

  public String getProductName() {
    return mProductName;
  }

  public void setProductName(String productName) {
    mProductName = productName;
  }

  public String getSellerName() {
    return mSellerName;
  }

  public void setSellerName(String sellerName) {
    mSellerName = sellerName;
  }

  public String getShortDesc() {
    return mShortDesc;
  }

  public void setShortDesc(String shortDesc) {
    mShortDesc = shortDesc;
  }

  public int getStock() {
    return mStock;
  }

  public void setStock(int stock) {
    mStock = stock;
  }

  public String getProductRelatedImage() {
    return productRelatedImage;
  }

  public void setProductRelatedImage(String productRelatedImage) {
    this.productRelatedImage = productRelatedImage;
  }

  public String getCatId() {
    return catId;
  }

  public void setCatId(String catId) {
    this.catId = catId;
  }

  public String getSubcatId() {
    return subcatId;
  }

  public void setSubcatId(String subcatId) {
    this.subcatId = subcatId;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(mId);
    dest.writeString(mProductImage);
    dest.writeString(productRelatedImage);
    dest.writeString(mProductName);
    dest.writeString(mSellerName);
    dest.writeString(mShortDesc);
    dest.writeInt(mStock);
    dest.writeInt(mDiscount);
    dest.writeString(catId);
    dest.writeString(subcatId);
  }
}
