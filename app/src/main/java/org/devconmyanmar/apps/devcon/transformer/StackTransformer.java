package org.devconmyanmar.apps.devcon.transformer;

import android.view.View;

/**
 * Created by Ye Lin Aung on 14/10/12.
 */
public class StackTransformer extends BaseTransformer {

  @Override
  protected void onTransform(View view, float position) {
    view.setTranslationX(position < 0 ? 0f : -view.getWidth() * position);
  }
}
