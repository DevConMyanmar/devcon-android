package org.devconmyanmar.apps.devcon.db;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;
import org.devconmyanmar.apps.devcon.model.Talk;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class TalkDao {
  private Dao<Talk, Integer> talkDao;
  private ConnectionSource source;

  public TalkDao(Context ctx) {
    DbMgr dbManager = new DbMgr();
    DbHelper dbHelper = dbManager.getHelper(ctx);
    try {
      talkDao = dbHelper.getTalkDao();
      source = dbHelper.getConnectionSource();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int create(Talk talk) {
    try {
      return talkDao.create(talk);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public List<Talk> getAll() throws SQLException {
    return talkDao.queryBuilder().query();
  }

  public Talk getTalkById(int id) {
    try {
      QueryBuilder<Talk, Integer> qb = talkDao.queryBuilder();
      qb.distinct().where().eq("id", id);
      PreparedQuery<Talk> pq = qb.prepare();
      return talkDao.queryForFirst(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
