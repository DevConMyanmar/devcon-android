package org.devconmyanmar.apps.devcon.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.util.ArrayList;
import java.util.List;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Talk;
import org.devconmyanmar.apps.devcon.ui.widget.ForegroundImageView;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.makeLogTag;

/**
 * Created by Ye Lin Aung on 14/10/05.
 */
public class ScheduleAdapter extends BaseAdapter {

  private static final int VIEW_TYPE_KEYNOTE = 1;
  private static final int VIEW_TYPE_NORMAL = 2;
  private static final int VIEW_TYPE_LIGHTNING = 3;
  private static final String TAG = makeLogTag(ScheduleAdapter.class);

  private static final int VIEW_TYPE_COUNT = 3;

  private List<Talk> mTalks = new ArrayList<Talk>();
  private Context mContext;



  public ScheduleAdapter(Context mContext) {
    this.mContext = mContext;
  }

  public void replaceWith(List<Talk> talks) {
    this.mTalks = talks;
    notifyDataSetChanged();
  }

  @Override public int getCount() {
    return mTalks.size();
  }

  @Override public Talk getItem(int position) {
    return mTalks.get(position);
  }

  @Override public long getItemId(int position) {
    return mTalks.get(position).getId();
  }

  @Override public int getViewTypeCount() {
    return VIEW_TYPE_COUNT;
  }

  @Override public int getItemViewType(int position) {
    Talk talk = getItem(position);

    return position % VIEW_TYPE_COUNT;

    //switch (talk.getTalk_type()) {
    //  case 0 :
    //    return VIEW_TYPE_KEYNOTE;
    //  case 1:
    //    return VIEW_TYPE_NORMAL;
    //  case 2:
    //    return VIEW_TYPE_LIGHTNING;
    //}
    //return 0;
  }

  @Override public View getView(int position, View view, ViewGroup parent) {

    Talk mTalk = getItem(position);
    KeynoteViewHolder keynoteViewHolder;
    NormalViewHolder normalViewHolder;
    LightningViewHolder lightningViewHolder;

    //LOGD(LOG_TAG, "talk type " + mTalk.getTalkType());
    //LOGD(TAG, "item view type " + getItemViewType(position));

    LayoutInflater mInflater =
        (LayoutInflater) mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    View rootView = view;

    switch (getItemViewType(position)) {
      case 0:
        if (rootView != null) {
          keynoteViewHolder = (KeynoteViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_keynote, parent, false);
          keynoteViewHolder = new KeynoteViewHolder(rootView);
          rootView.setTag(keynoteViewHolder);
        }

        //Glide.with(mContext)
        //    .load(mTalk.getPhoto())
        //    .fitCenter()
        //    .error(R.drawable.dc14)
        //    .placeholder(R.drawable.dc14)
        //    .crossFade()
        //    .into(keynoteViewHolder.mKeynoteBackground);

        keynoteViewHolder.mKeynoteTitle.setText(mTalk.getTitle());
        return rootView;
      case 1:
        if (rootView != null) {
          normalViewHolder = (NormalViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_normal_schedule, parent, false);
          normalViewHolder = new NormalViewHolder(rootView);
          rootView.setTag(normalViewHolder);
        }
        normalViewHolder.mScheduleTitle.setText(mTalk.getTitle());
        normalViewHolder.mScheduleTime.setText(mTalk.getDate());

        return rootView;
      case 2:
        if (rootView != null) {
          lightningViewHolder = (LightningViewHolder) rootView.getTag();
        } else {
          rootView = mInflater.inflate(R.layout.row_lightning_schedule, parent, false);
          lightningViewHolder = new LightningViewHolder(rootView);
          rootView.setTag(lightningViewHolder);
        }
        lightningViewHolder.mScheduleTitle.setText(mTalk.getTitle());
        lightningViewHolder.mScheduleTime.setText(mTalk.getDate());

        return rootView;
      default:
    }

    return rootView;
  }

  static class KeynoteViewHolder {
    @InjectView(R.id.keynote_background) ForegroundImageView mKeynoteBackground;
    @InjectView(R.id.keynote_title) TextView mKeynoteTitle;

    public KeynoteViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  static class NormalViewHolder {
    @InjectView(R.id.schedule_title) TextView mScheduleTitle;
    @InjectView(R.id.schedule_time) TextView mScheduleTime;

    public NormalViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }

  static class LightningViewHolder {
    @InjectView(R.id.schedule_title) TextView mScheduleTitle;
    @InjectView(R.id.schedule_time) TextView mScheduleTime;

    public LightningViewHolder(View view) {
      ButterKnife.inject(this, view);
    }
  }
}
