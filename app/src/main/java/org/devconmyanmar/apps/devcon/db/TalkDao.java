
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
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.List;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;
import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class TalkDao {
  private static final String TAG = makeLogTag(TalkDao.class);
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

  public List<Talk> getTalkByDay(String date) {
    try {
      QueryBuilder<Talk, Integer> qb = talkDao.queryBuilder();
      qb.where().eq("date", date);
      PreparedQuery<Talk> pq = qb.prepare();
      return talkDao.query(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public List<Talk> getFavTalks() {
    try {
      QueryBuilder<Talk, Integer> qb = talkDao.queryBuilder();
      qb.where().eq("favourite", true);
      PreparedQuery<Talk> pq = qb.prepare();
      return talkDao.query(pq);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public void createOrUpdate(Talk talk) {
    try {
      talkDao.createOrUpdate(talk);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void deleteAll() throws SQLException {
    LOGD(TAG, "deleting ..");
    TableUtils.clearTable(source, Talk.class);
  }
}
