package org.devconmyanmar.apps.devcon.db;

import android.content.Context;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.List;
import org.devconmyanmar.apps.devcon.model.Speaker;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class SpeakerDao {
  public static final String EMPTY_REC = "just_empty";
  private Dao<Speaker, Integer> speakerDao;
  private ConnectionSource source;

  public SpeakerDao(Context ctx) {
    DbMgr dbManager = new DbMgr();
    DbHelper dbHelper = dbManager.getHelper(ctx);
    try {
      speakerDao = dbHelper.getSpeakerDao();
      source = dbHelper.getConnectionSource();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public int create(Speaker speaker) {
    try {
      return speakerDao.create(speaker);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return 0;
  }

  public List<Speaker> getAll() throws SQLException {
    return speakerDao.queryBuilder().query();
  }

  public Speaker getSpeakerById(int id) {
    try {
      QueryBuilder<Speaker, Integer> qb = speakerDao.queryBuilder();
      qb.distinct().where().eq("id", id);
      PreparedQuery<Speaker> pq = qb.prepare();
      return speakerDao.queryForFirst(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public String getSpeakerNameById(String id) {
    try {
      QueryBuilder<Speaker, Integer> qb = speakerDao.queryBuilder();
      qb.where().eq("id", id);
      // THIS IS HOW I KILL NPE wahaha
      PreparedQuery<Speaker> pq = qb.prepare();
      if (qb.countOf() == 0) {
        return "";
      } else {
        return speakerDao.queryForFirst(pq).getName();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "";
  }
}
