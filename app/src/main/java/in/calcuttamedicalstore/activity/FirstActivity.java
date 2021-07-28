package in.calcuttamedicalstore.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;

import androidx.appcompat.app.AlertDialog;

import in.calcuttamedicalstore.R;
import in.calcuttamedicalstore.utils.SessionManager;
import in.calcuttamedicalstore.utils.Utiles;
import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;

public class FirstActivity extends ActivityManagePermission {
  SessionManager sessionManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_first);
    sessionManager = new SessionManager(FirstActivity.this);
    int SPLASH_TIME_OUT = 2000;
    new Handler()
        .postDelayed(
            () -> {
              if (Utiles.internetChack()) {
                if (sessionManager.getBooleanData(SessionManager.login)) {
                  startActivity(new Intent(FirstActivity.this, HomeActivity.class));
                } else if (sessionManager.getBooleanData(SessionManager.isopen)) {
                  Pair[] pair = new Pair[2];
                  pair[0] = new Pair<>(findViewById(R.id.app_logo), "splash_icon");
                  pair[1] = new Pair<>(findViewById(R.id.splashscreen), "login");
                  ActivityOptions option =
                      ActivityOptions.makeSceneTransitionAnimation(FirstActivity.this, pair);
                  startActivity(
                      new Intent(FirstActivity.this, LoginActivity.class), option.toBundle());
                } else {
                  startActivity(new Intent(FirstActivity.this, InfoActivity.class));
                }
                finish();
              } else {
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(FirstActivity.this);
                builder
                    .setMessage("Please Check Your Internet Connection")
                    .setCancelable(false)
                    .setPositiveButton(
                        "Exit",
                        (dialog, id) -> {
                          // Log.e("tem", dialog + "" + id);
                          finish();
                        });
                AlertDialog alert = builder.create();
                alert.show();
              }
            },
            SPLASH_TIME_OUT);
  }
}
