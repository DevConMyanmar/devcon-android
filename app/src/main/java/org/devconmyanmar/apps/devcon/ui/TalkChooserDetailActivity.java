package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import org.devconmyanmar.apps.devcon.R;

import static org.devconmyanmar.apps.devcon.Config.POSITION;


/**
 * Created by yemyatthu on 11/13/14.
 */
public class TalkChooserDetailActivity extends BaseActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_talk_chooser);
    getSupportFragmentManager().beginTransaction().add(R.id.fragment_container,TalkChooserDetailFragment.newInstance(String.valueOf(getIntent().getIntExtra(POSITION,0)))).commit();
  }
}
