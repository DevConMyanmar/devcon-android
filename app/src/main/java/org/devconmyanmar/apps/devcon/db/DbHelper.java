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
import android.database.sqlite.SQLiteDatabase;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import org.devconmyanmar.apps.devcon.model.MySchedule;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.model.Sponsor;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGE;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGI;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/08/06.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

  private static final String DATABASE_NAME = "devcon.db";

  private static final int DATABASE_VERSION = 2;

  private static String TAG = makeLogTag(DbHelper.class);
  private Dao<Speaker, Integer> mSpeakerDao = null;
  private Dao<Talk, Integer> mTalkDao = null;
  private Dao<MySchedule, Integer> mMyScheduleDao = null;
  private Dao<Sponsor, Integer> mSponsorDao = null;

  public DbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
    try {
      LOGI(TAG, "onCreate -- creating");
      TableUtils.createTable(connectionSource, Speaker.class);
      TableUtils.createTable(connectionSource, Talk.class);
      TableUtils.createTable(connectionSource, MySchedule.class);
      TableUtils.createTable(connectionSource, Sponsor.class);
    } catch (SQLException e) {
      LOGE(TAG, "Can't create database", e);
      e.printStackTrace();
    }
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i,
      int i2) {
    LOGI(TAG, "onUpgrade");
    try {
      TableUtils.dropTable(connectionSource, Speaker.class, true);
      TableUtils.dropTable(connectionSource, Talk.class, true);
      TableUtils.dropTable(connectionSource, MySchedule.class, true);
      onCreate(sqLiteDatabase, connectionSource);
    } catch (SQLException e) {
      LOGE(TAG, "Can't drop databases", e);
      throw new RuntimeException(e);
    }
  }

  public Dao<Speaker, Integer> getSpeakerDao() throws SQLException {
    if (mSpeakerDao == null) {
      mSpeakerDao = getDao(Speaker.class);
    }
    return mSpeakerDao;
  }

  public Dao<Talk, Integer> getTalkDao() throws SQLException {
    if (mTalkDao == null) {
      mTalkDao = getDao(Talk.class);
    }
    return mTalkDao;
  }

  public Dao<MySchedule, Integer> getFavDao() throws SQLException {
    if (mMyScheduleDao == null) {
      mMyScheduleDao = getDao(MySchedule.class);
    }
    return mMyScheduleDao;
  }

  public Dao<Sponsor, Integer> getSponorDao() throws SQLException {
    if (mSponsorDao == null) {
      mSponsorDao = getDao(Sponsor.class);
    }
    return mSponsorDao;
  }

  @Override public void close() {
    super.close();
    mSpeakerDao = null;
    mTalkDao = null;
    mMyScheduleDao = null;
    mSpeakerDao = null;
  }
}

