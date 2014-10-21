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
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.transformer.CircleTransformer;

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

  public SpeakerItemView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SpeakerItemView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override protected void onFinishInflate() {
    super.onFinishInflate();
    ButterKnife.inject(this);
  }

  public void bindTo(final Speaker speaker, Context context) {
    Glide.with(context)
        .load(speaker.getPhoto())
        .centerCrop()
        .error(R.drawable.person_image_empty)
        .placeholder(R.drawable.person_image_empty)
        .crossFade()
        .transform(new CircleTransformer(context))
        .into(mSpeakerImage);

    mSpeakerTitle.setText(speaker.getName());
    mSpeakerAbstract.setText(speaker.getTitle());
  }
}
