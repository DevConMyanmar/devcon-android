package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import org.devconmyanmar.apps.devcon.R;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class UpdateFragment extends Fragment {
  public UpdateFragment() {
  }

  public static UpdateFragment getInstance() {
    return new UpdateFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getActivity().setTitle(getString(R.string.updates));
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_updates, container, false);
    ButterKnife.inject(this, rootView);
    return rootView;
  }
}
