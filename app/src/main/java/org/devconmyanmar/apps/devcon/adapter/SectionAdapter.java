package org.devconmyanmar.apps.devcon.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by user on 10/31/15.
 */
public class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
  private final Context mContext;
  private static final int SECTION_TYPE = 0;
  private boolean mValid = true;
  private int mSectionResourceId;
  private int mTextResourceId;
  LayoutInflater mLayoutInflater;

  private RecyclerView.Adapter mBaseAdapter;
  private SparseArray<Section> mSections = new SparseArray<Section>();

  public SectionAdapter(Context context, int mSectionResourceId, int mTextResourceId,
      final RecyclerView.Adapter mBaseAdapter) {
    this.mContext = context;
    mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    this.mSectionResourceId = mSectionResourceId;
    this.mTextResourceId = mTextResourceId;
    this.mBaseAdapter = mBaseAdapter;
    this.mBaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
      @Override public void onChanged() {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyDataSetChanged();
      }

      @Override public void onItemRangeChanged(int positionStart, int itemCount) {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyItemRangeChanged(positionStart, itemCount);
      }

      @Override public void onItemRangeInserted(int positionStart, int itemCount) {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyItemRangeInserted(positionStart, itemCount);
      }

      @Override public void onItemRangeRemoved(int positionStart, int itemCount) {
        mValid = mBaseAdapter.getItemCount() > 0;
        notifyItemRangeRemoved(positionStart, itemCount);
      }
    });
  }

  @Override public int getItemCount() {
    return (mValid ? mBaseAdapter.getItemCount() + mSections.size() : 0);
  }

  @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    if (viewType == SECTION_TYPE) {
      final View view = LayoutInflater.from(mContext).inflate(mSectionResourceId, parent, false);
      return new SectionViewHolder(view, mTextResourceId);
    } else {
      return mBaseAdapter.onCreateViewHolder(parent, viewType);
    }
  }

  @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    if (isSectionHeaderPosition(position)) {
      ((SectionViewHolder) holder).title.setText(mSections.get(position).title);
    } else {
      mBaseAdapter.onBindViewHolder(holder, sectionedPositionToPosition(position));
    }
  }

  @Override public int getItemViewType(int position) {
    return isSectionHeaderPosition(position) ? SECTION_TYPE
        : mBaseAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
  }

  public class SectionViewHolder extends RecyclerView.ViewHolder {

    public TextView title;

    public SectionViewHolder(View itemView, int resId) {
      super(itemView);
      title = (TextView) itemView.findViewById(resId);
    }
  }

  public static class Section {
    int firstPosition;
    int sectionedPosition;
    CharSequence title;

    public Section(int firstPosition, CharSequence title) {
      this.firstPosition = firstPosition;
      this.title = title;
    }

    public CharSequence getTitle() {
      return title;
    }
  }

  public void setSections(Section[] sections) {
    mSections.clear();

    Arrays.sort(sections, new Comparator<Section>() {
      @Override public int compare(Section o, Section o1) {
        return (o.firstPosition == o1.firstPosition) ? 0
            : ((o.firstPosition < o1.firstPosition) ? -1 : 1);
      }
    });

    int offset = 0; // offset positions for the headers we're adding
    for (Section section : sections) {
      section.sectionedPosition = section.firstPosition + offset;
      mSections.append(section.sectionedPosition, section);
      ++offset;
    }

    notifyDataSetChanged();
  }

  public int positionToSectionedPosition(int position) {
    int offset = 0;
    for (int i = 0; i < mSections.size(); i++) {
      if (mSections.valueAt(i).firstPosition > position) {
        break;
      }
      ++offset;
    }
    return position + offset;
  }

  public int sectionedPositionToPosition(int sectionedPosition) {
    if (isSectionHeaderPosition(sectionedPosition)) {
      return RecyclerView.NO_POSITION;
    }

    int offset = 0;
    for (int i = 0; i < mSections.size(); i++) {
      if (mSections.valueAt(i).sectionedPosition > sectionedPosition) {
        break;
      }
      --offset;
    }
    return sectionedPosition + offset;
  }

  public boolean isSectionHeaderPosition(int position) {
    return mSections.get(position) != null;
  }

  @Override public long getItemId(int position) {
    return isSectionHeaderPosition(position) ? Integer.MAX_VALUE - mSections.indexOfKey(position)
        : mBaseAdapter.getItemId(sectionedPositionToPosition(position));
  }
}
