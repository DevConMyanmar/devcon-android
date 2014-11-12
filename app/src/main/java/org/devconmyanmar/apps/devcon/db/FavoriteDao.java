package org.devconmyanmar.apps.devcon.db;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;
import org.devconmyanmar.apps.devcon.model.MySchedule;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class FavoriteDao {
  private static final String TAG = makeLogTag(TalkDao.class);
  private Dao<MySchedule, Integer> favDao;
  private ConnectionSource source;

  public FavoriteDao(Context ctx) {
    DbMgr dbManager = new DbMgr();
    DbHelper dbHelper = dbManager.getHelper(ctx);
    try {
      favDao = dbHelper.getFavDao();
      source = dbHelper.getConnectionSource();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int create(MySchedule mySchedule) {
    try {
      return favDao.create(mySchedule);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public List<MySchedule> getAll() throws SQLException {
    return favDao.queryBuilder().query();
  }

  public MySchedule getFavById(int id) {
    try {
      QueryBuilder<MySchedule, Integer> qb = favDao.queryBuilder();
      qb.distinct().where().eq("id", id);
      PreparedQuery<MySchedule> pq = qb.prepare();
      return favDao.queryForFirst(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<MySchedule> getFavByStartTime(String startTime) {
    try {
      QueryBuilder<MySchedule, Integer> qb = favDao.queryBuilder();
      qb.where().eq("start", startTime);
      PreparedQuery<MySchedule> pq = qb.prepare();
      return favDao.query(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<MySchedule> getFavByEndTime(String endTime) {
    try {
      QueryBuilder<MySchedule, Integer> qb = favDao.queryBuilder();
      qb.where().eq("end", endTime);
      PreparedQuery<MySchedule> pq = qb.prepare();
      return favDao.query(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void createOrUpdate(MySchedule mySchedule) {
    try {
      favDao.createOrUpdate(mySchedule);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteAll() throws SQLException {
    LOGD(TAG, "deleting ..");
    TableUtils.clearTable(source, MySchedule.class);
  }
}