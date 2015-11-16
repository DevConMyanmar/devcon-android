package org.devconmyanmar.apps.devcon.ui;

import android.view.View;
import org.devconmyanmar.apps.devcon.model.Speaker;

/**
 * Created by yelinaung on 11/16/15.
 */
public interface SpeakerClickListener {
  void onSpeakerClick(Speaker speaker, View v, int position);
}
