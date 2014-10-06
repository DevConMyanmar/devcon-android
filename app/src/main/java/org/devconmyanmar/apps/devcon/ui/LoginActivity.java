package org.devconmyanmar.apps.devcon.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.listeners.OnLoginListener;
import org.devconmyanmar.apps.devcon.R;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGE;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGI;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

public class LoginActivity extends Activity {

  private static final String TAG = makeLogTag(LoginActivity.class);
  private SimpleFacebook mSimpleFacebook;
  private OnLoginListener onLoginListener;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.inject(this);

    ActionBar mActionBar = getActionBar();
    if (mActionBar != null) {
      mActionBar.hide();
    }

    onLoginListener = new OnLoginListener() {

      @Override
      public void onFail(String reason) {
        LOGE(TAG, reason);
        Log.w(TAG, "Failed to login");
      }

      @Override
      public void onException(Throwable throwable) {
        LOGE(TAG, throwable.getMessage());
      }

      @Override
      public void onThinking() {
        // show progress bar or something to the user while login is
        // happening
        LOGE(TAG, "Thinking...");
      }

      @Override
      public void onLogin() {
        // change the state of the button or do whatever you want
        LOGI(TAG, "Logged in");
      }

      @Override
      public void onNotAcceptingPermissions(Permission.Type type) {
        LOGE(TAG, String.format("You didn't accept %s permissions", type.name()));
      }
    };
  }

  @Override protected void onResume() {
    super.onResume();
    mSimpleFacebook = SimpleFacebook.getInstance(this);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    mSimpleFacebook.onActivityResult(this, requestCode, resultCode, data);
    super.onActivityResult(requestCode, resultCode, data);
  }

  @SuppressWarnings("method unused") @OnClick(R.id.login_with_facebook) void login() {
    mSimpleFacebook.login(onLoginListener);
  }
}
