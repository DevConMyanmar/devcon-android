package org.devconmyanmar.apps.devcon.model;

import io.realm.RealmObject;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class Talk extends RealmObject {

  private int id;
  private String title;
  private String description;
  private String photo;
  private String date;
  private boolean favourite;
  private int talk_type;
  private String room;
  private String from_time;
  private String to_time;

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
}
