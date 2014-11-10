package org.devconmyanmar.apps.devcon.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Ye Lin Aung on 14/08/06.
 */
public class DbMgr {
  private DbHelper dbHelper = null;

  public DbHelper getHelper(Context context) {
    if (dbHelper == null) {
      dbHelper = OpenHelperManager.getHelper(context, DbHelper.class);
    }
    return dbHelper;
  }

  //releases the helper once usages has ended
  public void releaseHelper(DbHelper helper) {
    if (dbHelper != null) {
      OpenHelperManager.releaseHelper();
      dbHelper = null;
    }
  }
}
