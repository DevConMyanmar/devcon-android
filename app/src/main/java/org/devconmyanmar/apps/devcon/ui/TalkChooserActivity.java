package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import org.devconmyanmar.apps.devcon.R;

/**
 * Created by yemyatthu on 11/13/14.
 */
public class TalkChooserActivity extends BaseActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_talk_chooser);

    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,TalkChooserFragment.getInstance(getIntent().getStringExtra("START_TIME"),getIntent().getStringExtra("END_TIME"),getIntent().getStringExtra("TALK_ID"))).commit();

  }
}
