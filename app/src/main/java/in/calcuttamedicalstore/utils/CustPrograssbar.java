package in.calcuttamedicalstore.utils;

import android.app.ProgressDialog;
import android.content.Context;

public class CustPrograssbar {
  public static ProgressDialog progressDialog;

  public void prograssCreate(Context context) {
    try {
      if (progressDialog == null || !progressDialog.isShowing()) {
        progressDialog = new ProgressDialog(context);
        if (!progressDialog.isShowing()) {

          progressDialog.setMessage("Progress...");
          progressDialog.show();
        }
      }
    } catch (Exception ignored) {

    }
  }

  public void closePrograssBar() {
    if (progressDialog != null) {
      try {
        progressDialog.cancel();
      } catch (Exception ignored) {
      }
    }
  }
}
