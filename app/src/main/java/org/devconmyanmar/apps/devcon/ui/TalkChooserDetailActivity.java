package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.Bundle;
import org.devconmyanmar.apps.devcon.R;

import static org.devconmyanmar.apps.devcon.Config.POSITION;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by yemyatthu on 11/13/14.
 */
public class TalkChooserDetailActivity extends BaseActivity {
  private String TAG = makeLogTag(TalkChooserDetailActivity.class);

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_talk_chooser);
    Integer talkId = getIntent().getIntExtra(POSITION, 0);
    LOGD(TAG,talkId+"");
    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,
        TalkChooserDetailFragment.newInstance(String.valueOf(talkId))).commit();
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    Intent i = new Intent(mContext,TalkListActivity.class);
    i.putExtra("Fragment",0);
    startActivity(i);

  }
}
