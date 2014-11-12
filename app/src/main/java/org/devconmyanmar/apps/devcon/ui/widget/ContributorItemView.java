package org.devconmyanmar.apps.devcon.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.bumptech.glide.Glide;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Contributor;
import org.devconmyanmar.apps.devcon.transformer.CircleTransformer;

import static org.devconmyanmar.apps.devcon.utils.LogUtils.LOGD;

/**
 * Created by Ye Lin Aung on 14/10/09.
 */
public class ContributorItemView extends RelativeLayout {

  @InjectView(R.id.contributor_image) ImageView mSpeakerImage;
  @InjectView(R.id.contributor_name) TextView mSpeakerTitle;

  public ContributorItemView(Context context) {
    super(context);
  }

  public ContributorItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ContributorItemView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(final Contributor contributor, Context context) {
    LOGD("img ", contributor.getAvatarUrl());
    Glide.with(context)
        .load(contributor.getAvatarUrl())
        .centerCrop()
        .error(R.drawable.person_image_empty)
        .placeholder(R.drawable.person_image_empty)
        .crossFade()
        .transform(new CircleTransformer(context))
        .into(mSpeakerImage);

    mSpeakerTitle.setText(contributor.getLogin().toUpperCase());
  }
}
