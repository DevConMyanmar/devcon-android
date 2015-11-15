/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Devcon Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.devconmyanmar.apps.devcon.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.readystatesoftware.systembartint.SystemBarTintManager;
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
  @Bind(R.id.toolbar) Toolbar mToolbar;
  @Bind(R.id.contributor_list) ListView contributorList;
  @Bind(R.id.contributor_loading_progress) ProgressBar loadingProgress;
  @Bind(R.id.no_connection_msg) TextView noConnectionMsg;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contributors);
    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    mToolbar.setTitle(getString(R.string.about_contributors));
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    if (ConnectionUtils.isOnline(this)) {
      getContrib();
    } else {
      noConnectionMsg.setVisibility(View.VISIBLE);
      contributorList.setEmptyView(noConnectionMsg);
    }

    if (Build.VERSION.SDK_INT >= 19) {
      SystemBarTintManager tintManager = new SystemBarTintManager(this);
      tintManager.setStatusBarTintEnabled(true);
      tintManager.setNavigationBarTintEnabled(false);
      tintManager.setTintColor(getResources().getColor(R.color.translucent_actionbar_background));
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
