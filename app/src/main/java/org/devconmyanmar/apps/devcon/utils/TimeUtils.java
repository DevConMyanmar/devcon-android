package org.devconmyanmar.apps.devcon.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Ye Lin Aung on 14/11/09.
 */
public class TimeUtils {

  public static String parseDateString(String dateString) {
    DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd");
    DateFormat writeFormat = new SimpleDateFormat("dd MMM");
    Date date = null;
    try {
      date = readFormat.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (date != null) {
      return writeFormat.format(date);
    }

    return null;
  }

  public static String parseFromToString(String dateString) {
    // 08:30:00Z
    DateFormat readFormat = new SimpleDateFormat("HH:mm");
    DateFormat writeFormat = new SimpleDateFormat("hh:mm a");
    Date date = null;
    try {
      date = readFormat.parse(dateString);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (date != null) {
      return writeFormat.format(date);
    }

    return null;
  }
}
