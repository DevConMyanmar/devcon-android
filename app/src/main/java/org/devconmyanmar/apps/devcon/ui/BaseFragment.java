package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import io.realm.Realm;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseFragment extends Fragment {
  protected Context mContext;
  protected Realm realm;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = getActivity();
    this.realm = Realm.getInstance(mContext);
  }
}
