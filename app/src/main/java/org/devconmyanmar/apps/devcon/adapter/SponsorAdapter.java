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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Sponsor;
import org.devconmyanmar.apps.devcon.utils.TimeUtils;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Ye Lin Aung on 14/11/15.
 */
public class SponsorAdapter extends BaseAdapter implements StickyListHeadersAdapter {

  private List<Sponsor> mSponsors = new ArrayList<Sponsor>();
  private LayoutInflater mInflater;
  private Context mContext;

  public SponsorAdapter() {
  }

  public SponsorAdapter(Context mContext) {
    this.mContext = mContext;
    mInflater = LayoutInflater.from(mContext);
  }

  public void replaceWith(List<Sponsor> sponsors) {
    this.mSponsors = sponsors;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mSponsors.size();
  }

  @Override public Object getItem(int i) {
    return mSponsors.get(i);
  }

  @Override public long getItemId(int i) {
    return 0;
  }

  @Override public View getView(int i, View view, ViewGroup viewGroup) {
    LayoutInflater mInflater =
        (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    View rootView = view;
    SponsorVH sponsorVH;
    if (rootView != null) {
      sponsorVH = (SponsorVH) rootView.getTag();
    } else {
      rootView = mInflater.inflate(R.layout.row_sponsor, viewGroup, false);
      sponsorVH = new SponsorVH(rootView);
      rootView.setTag(sponsorVH);
    }
    Sponsor s = (Sponsor) getItem(i);
    sponsorVH.sponsorName.setText(s.getName());
    int id =
        mContext.getResources().getIdentifier(s.getName(), "drawable", mContext.getPackageName());

    sponsorVH.sponsorImage.setImageResource(id);

    return rootView;
  }

  @Override public View getHeaderView(int i, View view, ViewGroup viewGroup) {
    HeaderViewHolder holder;
    if (view == null) {
      holder = new HeaderViewHolder();
      view = mInflater.inflate(R.layout.sponsor_header, viewGroup, false);
      assert view != null;
      holder.header = (TextView) view.findViewById(R.id.room_name);
      view.setTag(holder);
    } else {
      holder = (HeaderViewHolder) view.getTag();
    }

    CharSequence headerChar = TimeUtils.getSponsorName(mSponsors.get(i).getSponsorType());
    holder.header.setText(headerChar);
    holder.header.invalidate();

    return view;
  }

  @Override public long getHeaderId(int i) {
    return mSponsors.get(i).getSponsorType().subSequence(0, 1).charAt(0);
  }

  static class SponsorVH {
    @Bind(R.id.sponsor_img) ImageView sponsorImage;
    @Bind(R.id.sponsor_name) TextView sponsorName;

    public SponsorVH(View view) {
      ButterKnife.bind(this, view);
    }
  }

  static class HeaderViewHolder {
    TextView header;
  }
}
