/*
 * Copyright 2014 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.devconmyanmar.apps.devcon.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedStateListDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import org.devconmyanmar.apps.devcon.R;

@TargetApi(Build.VERSION_CODES.LOLLIPOP) public class LUtils {
  private static final int[] STATE_CHECKED = new int[] { android.R.attr.state_checked };
  private static final int[] STATE_UNCHECKED = new int[] {};

  private static Typeface sMediumTypeface;

  private Handler mHandler = new Handler();
  private Context mContext;

  private LUtils(Context context) {
    this.mContext = context;
  }

  public static LUtils getInstance(Context context) {
    return new LUtils(context);
  }

  private static boolean hasL() {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
  }

  public void setOrAnimatePlusCheckIcon(final ImageView imageView, boolean isCheck,
      boolean allowAnimate) {
    if (!hasL()) {
      compatSetOrAnimatePlusCheckIcon(imageView, isCheck, allowAnimate);
      return;
    }

    Drawable drawable = imageView.getDrawable();
    if (!(drawable instanceof AnimatedStateListDrawable)) {
      drawable = mContext.getResources().getDrawable(R.drawable.add_schedule_fab_icon_anim);
      imageView.setImageDrawable(drawable);
    }
    imageView.setColorFilter(
        isCheck ? mContext.getResources().getColor(R.color.theme_accent_1) : Color.WHITE);
    if (allowAnimate) {
      imageView.setImageState(isCheck ? STATE_UNCHECKED : STATE_CHECKED, false);
      drawable.jumpToCurrentState();
      imageView.setImageState(isCheck ? STATE_CHECKED : STATE_UNCHECKED, false);
    } else {
      imageView.setImageState(isCheck ? STATE_CHECKED : STATE_UNCHECKED, false);
      drawable.jumpToCurrentState();
    }
  }

  public void compatSetOrAnimatePlusCheckIcon(final ImageView imageView, boolean isCheck,
      boolean allowAnimate) {

    final int imageResId = isCheck ? R.drawable.add_schedule_button_icon_checked
        : R.drawable.add_schedule_button_icon_unchecked;

    if (imageView.getTag() != null) {
      if (imageView.getTag() instanceof Animator) {
        Animator anim = (Animator) imageView.getTag();
        anim.end();
        imageView.setAlpha(1f);
      }
    }

    if (allowAnimate && isCheck) {
      int duration = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);

      Animator outAnimator = ObjectAnimator.ofFloat(imageView, View.ALPHA, 0f);
      outAnimator.setDuration(duration / 2);
      outAnimator.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          imageView.setImageResource(imageResId);
        }
      });

      AnimatorSet inAnimator = new AnimatorSet();
      outAnimator.setDuration(duration);
      inAnimator.playTogether(ObjectAnimator.ofFloat(imageView, View.ALPHA, 1f),
          ObjectAnimator.ofFloat(imageView, View.SCALE_X, 0f, 1f),
          ObjectAnimator.ofFloat(imageView, View.SCALE_Y, 0f, 1f));

      AnimatorSet set = new AnimatorSet();
      set.playSequentially(outAnimator, inAnimator);
      set.addListener(new AnimatorListenerAdapter() {
        @Override public void onAnimationEnd(Animator animation) {
          imageView.setTag(null);
        }
      });
      imageView.setTag(set);
      set.start();
    } else {
      mHandler.post(new Runnable() {
        @Override public void run() {
          imageView.setImageResource(imageResId);
        }
      });
    }
  }
}
