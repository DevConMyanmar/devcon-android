package org.devconmyanmar.apps.devcon.transformer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * Created by Ye Lin Aung on 14/10/15.
 */
public class CircleTransformer extends BitmapTransformation {

  public CircleTransformer(Context context) {
    super(context);
  }

  @Override protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth,
      int outHeight) {
    return getCircularBitmapImage(toTransform);
  }

  @Override public String getId() {
    return "Glide_Circle_Transformation";
  }

  public static Bitmap getCircularBitmapImage(Bitmap source) {
    int size = Math.min(source.getWidth(), source.getHeight());
    int x = (source.getWidth() - size) / 2;
    int y = (source.getHeight() - size) / 2;
    Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
    if (squaredBitmap != source) {
      source.recycle();
    }
    Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
    Canvas canvas = new Canvas(bitmap);
    Paint paint = new Paint();
    BitmapShader shader =
        new BitmapShader(squaredBitmap, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
    paint.setShader(shader);
    paint.setAntiAlias(true);
    float r = size / 2f;
    canvas.drawCircle(r, r, r, paint);
    squaredBitmap.recycle();
    return bitmap;
  }
}
