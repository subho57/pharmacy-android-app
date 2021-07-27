package in.calcuttamedicalstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import in.calcuttamedicalstore.R;

public class Info2Fragment extends Fragment {

  public Info2Fragment() {}

  public static Info2Fragment newInstance() {
    Info2Fragment fragment = new Info2Fragment();
    Bundle args = new Bundle();
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_info2, container, false);
  }
}
