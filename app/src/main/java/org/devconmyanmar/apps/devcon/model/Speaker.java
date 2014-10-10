package org.devconmyanmar.apps.devcon.model;

import io.realm.RealmObject;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class Speaker extends RealmObject {
  private int id;
  private String name;
  private String title;
  private String description;
  private String photo;

  public Speaker() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
}
