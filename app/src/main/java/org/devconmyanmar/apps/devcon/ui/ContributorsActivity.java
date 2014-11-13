package org.devconmyanmar.apps.devcon.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.List;
import org.devconmyanmar.apps.devcon.Config;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.adapter.ContributorAdapter;
import org.devconmyanmar.apps.devcon.model.Contributor;
import org.devconmyanmar.apps.devcon.sync.SyncContributorsService;
import org.devconmyanmar.apps.devcon.utils.ConnectionUtils;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
public class ContributorsActivity extends BaseActivity {

  private static final String ORG = "DevConMyanmar";
  private static final String REPO = "devcon-android-2014";
  private static final String TAG = makeLogTag(ContributorsActivity.class);
  @InjectView(R.id.toolbar) Toolbar mToolbar;
  @InjectView(R.id.contributor_list) ListView contributorList;
  @InjectView(R.id.contributor_loading_progress) ProgressBar loadingProgress;
  @InjectView(R.id.no_connection_msg) TextView noConnectionMsg;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contributors);
    ButterKnife.inject(this);
    setSupportActionBar(mToolbar);
    mToolbar.setTitle(getString(R.string.about_contributors));
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_ab_back_mtrl_am_alpha);

    if (ConnectionUtils.isOnline(this)) {
      getContrib();
    } else {
      noConnectionMsg.setVisibility(View.VISIBLE);
      contributorList.setEmptyView(noConnectionMsg);
    }
  }

  private void getContrib() {
    showProgress(true, contributorList, loadingProgress);
    RestAdapter contribAdapter = new RestAdapter.Builder().setEndpoint(Config.GITHUB_URL)
        .setLogLevel(RestAdapter.LogLevel.BASIC)
        .build();
    SyncContributorsService contribService = contribAdapter.create(SyncContributorsService.class);
    contribService.listRepos(ORG, REPO, new Callback<List<Contributor>>() {
      @Override public void success(List<Contributor> contributors, Response response) {
        ContributorAdapter contributorAdapter = new ContributorAdapter(mContext);
        contributorAdapter.replaceWith(contributors);
        contributorList.setAdapter(contributorAdapter);
        showProgress(false, contributorList, loadingProgress);
      }

      @Override public void failure(RetrofitError error) {
        LOGD(TAG, "error : " + error.getResponse());
        showProgress(false, contributorList, loadingProgress);
      }
    });
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
