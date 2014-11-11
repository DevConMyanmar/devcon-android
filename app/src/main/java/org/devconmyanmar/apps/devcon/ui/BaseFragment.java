package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;
import org.devconmyanmar.apps.devcon.ui.widget.CustomSwipeRefreshLayout;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseFragment extends Fragment {

  protected SpeakerDao speakerDao;
  protected TalkDao talkDao;
  protected Context mContext;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.mContext = getActivity();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
  }

  public void showRefreshProgress(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setRefreshing(true);
  }

  public void hideRefreshProgress(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setRefreshing(false);
  }

  public void enableSwipe(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setEnabled(true);
  }

  public void disableSwipe(CustomSwipeRefreshLayout mSwipeRefreshWidget) {
    mSwipeRefreshWidget.setEnabled(false);
  }
}
