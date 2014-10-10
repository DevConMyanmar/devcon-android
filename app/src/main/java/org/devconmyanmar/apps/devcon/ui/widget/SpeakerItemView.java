package org.devconmyanmar.apps.devcon.ui.widget;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import org.devconmyanmar.apps.devcon.R;
import org.devconmyanmar.apps.devcon.model.Speaker;

/**
 * Created by Ye Lin Aung on 14/10/09.
 */
public class SpeakerItemView extends RelativeLayout {

  @InjectView(R.id.speaker_image) ImageView mSpeakerImage;
  @InjectView(R.id.speaker_title) TextView mSpeakerTitle;
  @InjectView(R.id.speaker_abstract) TextView mSpeakerAbstract;

  public SpeakerItemView(Context context) {
    super(context);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(final Speaker speaker, Context context) {
    // TODO bind image with Glide
    mSpeakerTitle.setText(speaker.getTitle());
    mSpeakerAbstract.setText(speaker.getDescription());
  }
}
