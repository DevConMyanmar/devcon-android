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

package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;
import org.devconmyanmar.apps.devcon.R;

/**
 * Created by yemyatthu on 11/10/14.
 */
public class DrawerListAdapter extends BaseAdapter {
  private String[] mNavDrawerItems;
  private Context mContext;
  private int mCurrentSelectedPosition;

  public DrawerListAdapter(Context context) {
    mContext = context;
  }

  public void setChecked(int currentSelectedPosition) {
    mCurrentSelectedPosition = currentSelectedPosition;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return 3;
  }

  @Override public Object getItem(int i) {
    return mNavDrawerItems[i];
  }

  @Override public long getItemId(int i) {
    return i;
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    mNavDrawerItems = mContext.getResources().getStringArray(R.array.nav_drawer_items);
    ArrayList<Integer> drawerIcons = new ArrayList<>();
    drawerIcons.add(R.drawable.ic_drawer_my_schedule);
    drawerIcons.add(R.drawable.ic_drawer_explore);
    drawerIcons.add(R.drawable.ic_drawer_people_met);

    ViewHolder holder;
    if (view != null) {
      holder = (ViewHolder) view.getTag();
    } else {
      view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
          R.layout.drawer_list_item, viewGroup, false);
      holder = new ViewHolder(view);
      view.setTag(holder);
    }
    holder.mText1.setText((String) getItem(i));
    if (mCurrentSelectedPosition == i) {
      holder.mText1.setTextColor(ContextCompat.getColor(mContext, R.color.theme_primary));
    } else {
      holder.mText1.setTextColor(ContextCompat.getColor(mContext, android.R.color.black));
    }
    holder.mDrawerIcon.setImageResource(drawerIcons.get(i));
    if (mCurrentSelectedPosition == i) {
      holder.mDrawerIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.theme_primary));
    } else {
      holder.mDrawerIcon.clearColorFilter();
    }

    return view;
  }

  /**
   * This class contains all butterknife-binded Views & Layouts from layout file
   * 'drawer_list_item.xml'
   * for easy to all layout elements.
   *
   * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers
   *         (http://inmite.github.io)
   */
  static class ViewHolder {
    @Bind(android.R.id.text1) TextView mText1;
    @Bind(R.id.drawer_icon) ImageView mDrawerIcon;

    ViewHolder(View view) {
      ButterKnife.bind(this, view);
    }
  }
}
