
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
