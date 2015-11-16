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
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.ui.SpeakerClickListener;
import org.devconmyanmar.apps.devcon.ui.SpeakerViewHolder;
import org.devconmyanmar.apps.devcon.ui.widget.SpeakerItemView;

/**
 * Created by Ye Lin Aung on 14/10/09.
 */
public class SpeakerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  public Context mContext;

  private List<Speaker> mSpeakers = new ArrayList<Speaker>();

  private SpeakerClickListener speakerClickListener;

  public SpeakerAdapter(Context context, SpeakerClickListener speakerClickListener) {
    this.mContext = context;
    this.speakerClickListener = speakerClickListener;
  }

  public SpeakerAdapter(Context context) {
    this.mContext = context;
  }

  public void replaceWith(List<Speaker> speakers) {
    this.mSpeakers = speakers;
    notifyDataSetChanged();
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
    SpeakerItemView itemView =
        (SpeakerItemView) mInflater.inflate(R.layout.speaker_layout, parent, false);
    return new SpeakerViewHolder(mContext, itemView, speakerClickListener);
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    SpeakerViewHolder speakerViewHolder = (SpeakerViewHolder) holder;
    speakerViewHolder.bindTo(mSpeakers.get(position));
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public int getItemCount() {
    return mSpeakers.size();
  }
}
