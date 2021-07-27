package in.calcuttamedicalstore.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Noti implements Parcelable {

  public static final Creator<Noti> CREATOR =
      new Creator<Noti>() {
        @Override
        public Noti createFromParcel(Parcel in) {
          return new Noti(in);
        }

        @Override
        public Noti[] newArray(int size) {
          return new Noti[size];
        }
      };

  @SerializedName("id")
  @Expose
  private String id;

  @SerializedName("title")
  @Expose
  private String title;

  @SerializedName("img")
  @Expose
  private String img;

  @SerializedName("msg")
  @Expose
  private String msg;

  @SerializedName("date")
  @Expose
  private String date;

  @SerializedName("IS_READ")
  @Expose
  private int iSread;

  protected Noti(Parcel in) {
    id = in.readString();
    title = in.readString();
    img = in.readString();
    msg = in.readString();
    date = in.readString();
    iSread = in.readInt();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImg() {
    return img;
  }

  public void setImg(String img) {
    this.img = img;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getiSread() {
    return iSread;
  }

  public void setiSread(int iSread) {
    this.iSread = iSread;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(id);
    dest.writeString(title);
    dest.writeString(img);
    dest.writeString(msg);
    dest.writeString(date);
    dest.writeInt(iSread);
  }
}
