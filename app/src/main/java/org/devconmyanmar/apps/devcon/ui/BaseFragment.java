package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseFragment extends Fragment {
  protected Context mContext;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = getActivity();

  }
}
