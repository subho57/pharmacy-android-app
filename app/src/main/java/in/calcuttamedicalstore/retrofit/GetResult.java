package in.calcuttamedicalstore.retrofit;

import android.widget.Toast;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import in.calcuttamedicalstore.MyApplication;
import in.calcuttamedicalstore.utils.Utiles;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetResult {
  public static MyListener myListener;

  public void callForLogin(Call<JsonObject> call, String callno) {
    if (!Utiles.internetChack()) {
      Toast.makeText(
              MyApplication.mContext, "Please Check Your Internet Connection", Toast.LENGTH_SHORT)
          .show();
    } else {
      call.enqueue(
          new Callback<JsonObject>() {
            @Override
            public void onResponse(
                @NotNull Call<JsonObject> call, @NotNull Response<JsonObject> response) {
              // Log.e("message", " : " + response.message());
              // Log.e("body", " : " + response.body());
              // Log.e("callno", " : " + callno);
              myListener.callback(response.body(), callno);
            }

            @Override
            public void onFailure(@NotNull Call<JsonObject> call, @NotNull Throwable t) {
              myListener.callback(null, callno);
              call.cancel();
              t.printStackTrace();
            }
          });
    }
  }

  public void setMyListener(MyListener Listener) {
    myListener = Listener;
  }

  public interface MyListener {
    // you can define any parameter as per your requirement
    void callback(JsonObject result, String callNo);
  }
}
