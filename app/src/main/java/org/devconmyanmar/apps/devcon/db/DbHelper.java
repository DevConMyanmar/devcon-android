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
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGE;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGI;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/08/06.
 */
public class DbHelper extends OrmLiteSqliteOpenHelper {

  private static final String DATABASE_NAME = "devcon.db";

  private static final int DATABASE_VERSION = 4;

  private static String TAG = makeLogTag(DbHelper.class);
  private Dao<Speaker, Integer> mSpeakerDao = null;
  private Dao<Talk, Integer> mTalkDao = null;
  private Dao<MySchedule,Integer> mFavDao = null;

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
      TableUtils.dropTable(connectionSource,MySchedule.class,true);
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

  public Dao<MySchedule,Integer> getFavDao() throws SQLException {
    if(mFavDao == null){
      mFavDao = getDao(MySchedule.class);
    }
    return mFavDao;
  }

  @Override public void close() {
    super.close();
    mSpeakerDao = null;
    mTalkDao = null;
    mFavDao = null;
  }
}

