package in.calcuttamedicalstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import in.calcuttamedicalstore.R;

public class Info1Fragment extends Fragment {

  public Info1Fragment() {}

  public static Info1Fragment newInstance() {
    Info1Fragment fragment = new Info1Fragment();
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
    return inflater.inflate(R.layout.fragment_info1, container, false);
  }
}
