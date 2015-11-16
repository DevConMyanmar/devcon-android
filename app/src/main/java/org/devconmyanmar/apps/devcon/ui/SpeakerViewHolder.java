package org.devconmyanmar.apps.devcon.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import org.devconmyanmar.apps.devcon.model.Speaker;
import org.devconmyanmar.apps.devcon.ui.widget.SpeakerItemView;

/**
 * Created by yelinaung on 11/16/15.
 */
public class SpeakerViewHolder extends RecyclerView.ViewHolder {
  public final SpeakerItemView itemView;
  private Speaker speaker;
  private Context mContext;

  public SpeakerViewHolder(Context context, SpeakerItemView speakerItemView,
      final SpeakerClickListener speakerClickListener) {
    super(speakerItemView);
    this.itemView = speakerItemView;
    this.mContext = context;

    this.itemView.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        speakerClickListener.onSpeakerClick(speaker, v, getAdapterPosition());
      }
    });
  }

  public void bindTo(Speaker speaker) {
    this.speaker = speaker;
    itemView.bindTo(speaker, mContext);
  }
}
