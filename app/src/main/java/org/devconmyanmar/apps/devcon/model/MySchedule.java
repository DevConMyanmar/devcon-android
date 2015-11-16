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

package org.devconmyanmar.apps.devcon.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ye Lin Aung on 14/11/12.
 */
@DatabaseTable(tableName = "my_schedule") public class MySchedule {
  @DatabaseField(id = true) private int id;
  @DatabaseField private String title;
  @DatabaseField private String start;
  @DatabaseField private String end;
  @DatabaseField @SerializedName("click_block") private boolean clickBlock;
  @DatabaseField @SerializedName("sub_title") private String subTitle;
  @DatabaseField private int date;
  @DatabaseField @SerializedName("associated_talk") private String associatedTalkId;
  @DatabaseField @SerializedName("has_favorite") private boolean hasFavorite;
  @DatabaseField @SerializedName("favorite_talk") private int favoriteTalkId;

  public MySchedule() {
  }

  public boolean isHasFavorite() {
    return hasFavorite;
  }

  public void setHasFavorite(boolean hasFavorite) {
    this.hasFavorite = hasFavorite;
  }

  public int getFavoriteTalkId() {
    return favoriteTalkId;
  }

  public void setFavoriteTalkId(int favoriteTalkId) {
    this.favoriteTalkId = favoriteTalkId;
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

  public String getAssociatedTalkId() {
    return associatedTalkId;
  }

  public void setAssociatedTalkId(String associatedTalkId) {
    this.associatedTalkId = associatedTalkId;
  }
}

