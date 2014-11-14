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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Contributor;
import org.devconmyanmar.apps.devcon.ui.widget.ContributorItemView;

/**
 * Created by Ye Lin Aung on 14/10/09.
 */
public class ContributorAdapter extends BindableAdapter<Contributor> {

  public Context mContext;

  private List<Contributor> mContributors = new ArrayList<Contributor>();

  public ContributorAdapter(Context context) {
    super(context);
    this.mContext = context;
  }

  public void replaceWith(List<Contributor> contributors) {
    this.mContributors = contributors;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mContributors.size();
  }

  @Override public Contributor getItem(int position) {
    return mContributors.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(R.layout.contributor_layout, container, false);
  }

  @Override public void bindView(Contributor contributor, int position, View view) {
    ((ContributorItemView) view).bindTo(contributor, mContext);
  }
}
