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
