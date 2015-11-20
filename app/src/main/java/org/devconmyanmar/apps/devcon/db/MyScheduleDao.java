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
import com.google.gson.Gson;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.model.MySchedule;
import org.devconmyanmar.apps.devcon.model.Talk;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/11/10.
 */
public class MyScheduleDao {
  private static final String TAG = makeLogTag(TalkDao.class);
  private Dao<MySchedule, Integer> favDao;
  private TalkDao talkDao;
  private ConnectionSource source;

  public MyScheduleDao(Context ctx) {
    DbMgr dbManager = new DbMgr();
    DbHelper dbHelper = dbManager.getHelper(ctx);
    try {
      favDao = dbHelper.getFavDao();
      source = dbHelper.getConnectionSource();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    talkDao = new TalkDao(ctx);
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

  public List<Talk> favedTalk(MySchedule mySchedule) {
    ArrayList<Talk> mTalks = new ArrayList<>();
    ArrayList<Talk> tempTalks = flatternSpeakers(mySchedule.getAssociatedTalkId());
    for (Talk talk : tempTalks) {
      //LOGD(TAG, "talk isFavourite ? " + talk.isFavourite());
      if (talk.isFavourite()) {
        mTalks.add(talk);
      }
    }

    return mTalks;
  }

  private ArrayList<Talk> flatternSpeakers(String talkIds) {
    ArrayList<Talk> mTalks = new ArrayList<Talk>();
    String id[] = new Gson().fromJson(talkIds, String[].class);
    for (String anId : id) {
      mTalks.add(talkDao.getTalkById(Integer.valueOf(anId)));
    }

    return mTalks;
  }

  public void createOrUpdate(MySchedule mySchedule) {
    try {
      favDao.createOrUpdate(mySchedule);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
