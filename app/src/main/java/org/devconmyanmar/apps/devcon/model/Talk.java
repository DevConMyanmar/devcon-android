
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

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
@DatabaseTable(tableName = "talks")
public class Talk {

  @DatabaseField(id = true) private int id;
  @DatabaseField private String title;
  @DatabaseField private String description;
  @DatabaseField private String photo;
  @DatabaseField private String date;
  @DatabaseField private boolean favourite;
  @DatabaseField private int talk_type;
  @DatabaseField private String room;
  @DatabaseField private String from_time;
  @DatabaseField private String to_time;
  @DatabaseField private String speakers;

  public Talk() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPhoto() {
    return photo;
  }

  public void setPhoto(String photo) {
    this.photo = photo;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public boolean isFavourite() {
    return favourite;
  }

  public void setFavourite(boolean favourite) {
    this.favourite = favourite;
  }

  public int getTalk_type() {
    return talk_type;
  }

  public void setTalk_type(int talk_type) {
    this.talk_type = talk_type;
  }

  public String getRoom() {
    return room;
  }

  public void setRoom(String room) {
    this.room = room;
  }

  public String getFrom_time() {
    return from_time;
  }

  public void setFrom_time(String from_time) {
    this.from_time = from_time;
  }

  public String getTo_time() {
    return to_time;
  }

  public void setTo_time(String to_time) {
    this.to_time = to_time;
  }

  public String getSpeakers() {
    return speakers;
  }

  public void setSpeakers(String speakers) {
    this.speakers = speakers;
  }
}
