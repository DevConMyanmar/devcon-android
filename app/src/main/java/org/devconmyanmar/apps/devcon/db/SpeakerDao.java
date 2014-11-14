
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

  public Speaker getSpeakerById(String id) {
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

  public void deleteAll() throws SQLException {
    TableUtils.clearTable(source, Speaker.class);
  }
}
