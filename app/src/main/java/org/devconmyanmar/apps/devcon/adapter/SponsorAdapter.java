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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Sponsor;

/**
 * Created by Ye Lin Aung on 14/11/15.
 */
public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.SponsorVH> {

  private List<Sponsor> mSponsors = new ArrayList<Sponsor>();
  private LayoutInflater mInflater;
  private Context mContext;

  public SponsorAdapter(Context mContext) {
    this.mContext = mContext;
    mInflater = LayoutInflater.from(mContext);
  }

  @Override public SponsorVH onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
    View mView = mInflater.inflate(R.layout.row_sponsor, parent, false);
    return new SponsorVH(mView);
  }

  @Override public void onBindViewHolder(SponsorVH holder, int position) {
    Sponsor s = mSponsors.get(position);
    holder.sponsorName.setText(s.getName());
    int id =
        mContext.getResources().getIdentifier(s.getLogo(), "drawable", mContext.getPackageName());
    holder.sponsorImage.setImageResource(id);
  }

  public void replaceWith(List<Sponsor> sponsors) {
    this.mSponsors = sponsors;
    notifyDataSetChanged();
  }

  @Override public long getItemId(int i) {
    return 0;
  }

  @Override public int getItemCount() {
    return mSponsors.size();
  }

  static class SponsorVH extends RecyclerView.ViewHolder {
    @Bind(R.id.sponsor_img) ImageView sponsorImage;
    @Bind(R.id.sponsor_name) TextView sponsorName;

    public SponsorVH(View view) {
      super(view);
      ButterKnife.bind(this, view);
    }
  }
}
