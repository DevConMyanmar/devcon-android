package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.widget.ListView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
public class ContributorsActivity extends BaseActivity {

  @InjectView(R.id.contributor_list) ListView contributorList;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contributors);
    ButterKnife.inject(this);
  }

}
