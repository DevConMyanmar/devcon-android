package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.ui.widget.SpeakerItemView;

/**
 * Created by Ye Lin Aung on 14/10/09.
 */
public class SpeakerAdapter extends BindableAdapter<Speaker> {

  public Context mContext;

  private List<Speaker> mSpeakers = new ArrayList<Speaker>();

  public SpeakerAdapter(Context context, Context mContext) {
    super(context);
    this.mContext = mContext;
  }

  public void replaceWith(List<Speaker> speakers) {
    this.mSpeakers = speakers;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mSpeakers.size();
  }

  @Override public Speaker getItem(int position) {
    return mSpeakers.get(position);
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public View newView(LayoutInflater inflater, int position, ViewGroup container) {
    return inflater.inflate(R.layout.speaker_layout, container, false);
  }

  @Override public void bindView(Speaker speaker, int position, View view) {
    ((SpeakerItemView) view).bindTo(speaker, mContext);
  }
}
