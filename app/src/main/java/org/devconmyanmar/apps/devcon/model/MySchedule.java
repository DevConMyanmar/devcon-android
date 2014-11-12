package org.devconmyanmar.apps.devcon.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
@DatabaseTable(tableName = "my_schedule")
public class MySchedule {
  @DatabaseField private int id;
  @DatabaseField private String title;
  @DatabaseField private String start;
  @DatabaseField private String end;
  @DatabaseField private boolean clickBlock;
  @DatabaseField private String subTitle;
  @DatabaseField private int date;
  @DatabaseField private int associatedTalkId;

  public MySchedule() {
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getEnd() {
    return end;
  }

  public void setEnd(String end) {
    this.end = end;
  }

  public String getStart() {
    return start;
  }

  public void setStart(String start) {
    this.start = start;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isClickBlock() {
    return clickBlock;
  }

  public void setClickBlock(boolean clickBlock) {
    this.clickBlock = clickBlock;
  }

  public String getSubTitle() {
    return subTitle;
  }

  public void setSubTitle(String subTitle) {
    this.subTitle = subTitle;
  }

  public int getDate() {
    return date;
  }

  public void setDate(int date) {
    this.date = date;
  }

  public int getAssociatedTalkId() {
    return associatedTalkId;
  }

  public void setAssociatedTalkId(int associatedTalkId) {
    this.associatedTalkId = associatedTalkId;
  }
}

