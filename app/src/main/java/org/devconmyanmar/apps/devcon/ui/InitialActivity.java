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

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.utils.DataUtils;
import org.devconmyanmar.apps.devcon.utils.SharePref;

/**
 * Created by Ye Lin Aung on 14/11/13.
 */
public class InitialActivity extends BaseActivity {

  @Bind(R.id.initial_progress_bar) ProgressBar mInitialProgress;
  @Bind(R.id.initial_progress_text) TextView mLoadingText;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_initial);
    ButterKnife.bind(this);
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
        Thread.sleep(3000);
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
