package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import org.devconmyanmar.apps.devcon.db.SpeakerDao;
import org.devconmyanmar.apps.devcon.db.TalkDao;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public abstract class BaseActivity extends ActionBarActivity {

  protected SpeakerDao speakerDao;
  protected TalkDao talkDao;
  private Context mContext;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mContext = getApplicationContext();

    speakerDao = new SpeakerDao(mContext);
    talkDao = new TalkDao(mContext);
  }
}
