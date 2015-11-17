package org.devconmyanmar.apps.devcon.ui;

import android.view.View;
import org.devconmyanmar.apps.devcon.model.Talk;

/**
 * Created by yelinaung on 11/17/15.
 */
public interface TalkClickListener {
  void onTalkClick(Talk talk, View v, int position);
}
