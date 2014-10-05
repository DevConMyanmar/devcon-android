package org.devconmyanmar.apps.devcon.model;

import io.realm.RealmObject;
import java.util.List;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class Speaker extends RealmObject {
  private int id;
  private String name;
  private String title;
  private String description;
  private List<Talk> associated_talks;

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

  public List<Talk> getAssociated_talks() {
    return associated_talks;
  }

  public void setAssociated_talks(List<Talk> associated_talks) {
    this.associated_talks = associated_talks;
  }
}
