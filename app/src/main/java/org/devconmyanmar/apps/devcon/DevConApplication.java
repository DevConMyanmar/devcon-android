package org.devconmyanmar.apps.devcon;

import android.app.Application;
import com.sromku.simple.fb.Permission;
import com.sromku.simple.fb.SimpleFacebook;
import com.sromku.simple.fb.SimpleFacebookConfiguration;

/**
 * Created by Ye Lin Aung on 14/10/04.
 */
public class DevConApplication extends Application {

  @Override public void onCreate() {
    super.onCreate();

    Permission[] permissions = new Permission[] {
        Permission.EMAIL,
        Permission.PUBLISH_ACTION
    };

    SimpleFacebookConfiguration configuration = new SimpleFacebookConfiguration.Builder()
        .setAppId("719288351499456")
        .setNamespace("devcon")
        .setPermissions(permissions)
        .build();

    SimpleFacebook.setConfiguration(configuration);

  }
}
