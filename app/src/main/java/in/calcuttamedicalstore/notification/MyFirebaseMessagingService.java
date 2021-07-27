package in.calcuttamedicalstore.notification;

import android.annotation.SuppressLint;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class MyFirebaseMessagingService extends FirebaseMessagingService {

  @Override
  public void onMessageReceived(@NotNull RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
  }
}
