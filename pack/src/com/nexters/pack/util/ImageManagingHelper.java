package com.nexters.pack.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageManagingHelper {
    public static void loadAndAttachCroppedImage(final ImageView iv, String url) {
        loadAndAttachCroppedImage(iv, url, null);
    }

    public static void loadAndAttachCroppedImage(final ImageView iv, String url, final Animation startAnim) {
        ImageSize targetSize = new ImageSize(iv.getWidth(), iv.getHeight());
        DisplayImageOptions options = new DisplayImageOptions.Builder().build();
        ImageLoader.getInstance().loadImage(url, targetSize, options, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                //iv.setImageResource(R.drawable.vobble_loading_icon);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                iv.setImageBitmap(getCroppedBitmap(loadedImage, iv.getWidth()));
                if (startAnim != null)
                    iv.startAnimation(startAnim);
            }
        });
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap scaledBitmap;

        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius) {
            scaledBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        } else {
            scaledBitmap = bitmap;
        }

        Bitmap output = Bitmap.createBitmap(scaledBitmap.getWidth(), scaledBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight());

        // 주의: 순서 변경하면 안 됨
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(scaledBitmap.getWidth() / 2 + 0.7f, scaledBitmap.getHeight() / 2 + 0.7f, scaledBitmap.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(scaledBitmap, rect, rect, paint);

        return output;
    }
}
