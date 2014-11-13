package org.devconmyanmar.apps.devcon.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.utils.DataUtils;
import org.devconmyanmar.apps.devcon.utils.SharePref;

/**
 * Created by Ye Lin Aung on 14/11/13.
 */
public class InitialActivity extends BaseActivity {

  @InjectView(R.id.initial_progress_bar) ProgressBar mInitialProgress;
  @InjectView(R.id.initial_progress_text) TextView mLoadingText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_initial);
    ButterKnife.inject(this);
    mInitialProgress.setVisibility(View.GONE);
    if (!SharePref.getInstance(InitialActivity.this).isFirstTime()) {
      goToTalkList();
    } else {
      new loadDataAsync().execute();
    }
  }

  @Override public void onResume() {
    super.onResume();
  }

  private void goToTalkList() {
    Intent i = new Intent(this, TalkListActivity.class);
    startActivity(i);
    finish();
  }

  private class loadDataAsync extends AsyncTask<Void, Void, Void> {

    @Override protected void onPreExecute() {
      super.onPreExecute();
      mInitialProgress.setVisibility(View.VISIBLE);
      mLoadingText.setVisibility(View.VISIBLE);
      mLoadingText.setText(getText(R.string.initial_loading));
    }

    @Override protected Void doInBackground(Void... voids) {
      try {
        Thread.sleep(5000);
        if (SharePref.getInstance(InitialActivity.this).isFirstTime()) {
          new DataUtils(InitialActivity.this).loadFromAssets();
          SharePref.getInstance(InitialActivity.this).noLongerFirstTime();
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return null;
    }

    @Override protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      mInitialProgress.setVisibility(View.GONE);
      goToTalkList();
    }
  }
}
