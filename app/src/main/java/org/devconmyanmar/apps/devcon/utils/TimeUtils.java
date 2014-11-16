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

  public static String getProperRoomName(String room) {
    if (room.equalsIgnoreCase("conference")) {
      return "Conference Room";
    } else if (room.equalsIgnoreCase("205")) {
      return "Room 205";
    } else if (room.equalsIgnoreCase("102")) {
      return "Room 102";
    } else if (room.equalsIgnoreCase("mcf")) {
      return "MCF Meeting Room";
    }

    return "N/A";
  }

  public static String getSponsorName(String room) {
    if (room.equalsIgnoreCase("diamond")) {
      return "Diamond Sponsors";
    } else if (room.equalsIgnoreCase("gold")) {
      return "Gold Sponsors";
    } else if (room.equalsIgnoreCase("silver")) {
      return "Silver Sponsors";
    } else if (room.equalsIgnoreCase("food")) {
      return "Food and Beverages Partner";
    } else if (room.equalsIgnoreCase("media")) {
      return "Media Partner";
    } else if (room.equalsIgnoreCase("venue")) {
      return "Venue Sponsors";
    }

    return "N/A";
  }
}
