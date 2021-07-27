package in.calcuttamedicalstore.retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
  public static final String APPEND_URL = "api/";
  public static final String baseUrl = "https://admin.calcuttamedicalstore.in/";
  static Retrofit retrofit = null;

  public static UserService getInterface() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
    retrofit =
        new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    return retrofit.create(UserService.class);
  }
}
