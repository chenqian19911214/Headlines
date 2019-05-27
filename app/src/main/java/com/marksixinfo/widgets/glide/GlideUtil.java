package com.marksixinfo.widgets.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.marksixinfo.R;
import com.marksixinfo.utils.CommonUtils;


public class GlideUtil {

    /**
     * 默认图
     */
    private static final int PLACEHOLDER_DEFAULT = R.drawable.default_image;


    public static void loadImage(final Object src, final ImageView view) {
        if (view != null && src != null) {
            loadImage(view.getContext(), src, view);
        }
    }

    public static void loadImage(final Object src, final ImageView view, int width, int height) {
        if (view != null && src != null) {
            loadImage(view.getContext(), src, view, width, height);
        }
    }


    public static void loadCircleImage(final Object src, final ImageView view) {
        if (view != null && src != null) {
            loadCircleImage(view.getContext(), src, view, R.drawable.icon_default_photo);
        }
    }

    public static void loadCircleImage(final Object src, final ImageView view, int placeholder) {
        if (view != null && src != null) {
            loadCircleImage(view.getContext(), src, view, placeholder);
        }
    }

    public static void loadRoundedImage(final Object src, final ImageView view, int corners) {
        if (view != null && src != null) {
            loadRoundedImage(view.getContext(), src, view, corners);
        }
    }

    public static void loadRoundedImage(final Object src, final ImageView view) {
        if (view != null && src != null) {
            loadRoundedImage(view.getContext(), src, view, 8);
        }
    }

    public static void loadRoundedImage(Context context, final Object src, final ImageView view) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .placeholder(PLACEHOLDER_DEFAULT)
                    .error(PLACEHOLDER_DEFAULT)
                    .into(view);
        }
    }


    public static void loadImage(Context context, final Object src, final ImageView view, int width, int height) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .override(width, height)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(PLACEHOLDER_DEFAULT)
                    .error(PLACEHOLDER_DEFAULT)
                    .into(view);
        }
    }

    public static void loadImage(Context context, final Object src, final ImageView view) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(PLACEHOLDER_DEFAULT)
                    .error(PLACEHOLDER_DEFAULT)
                    .into(view);
        }
    }

    public static void loadCircleImage(Context context, final Object src, final ImageView view, int placeholder) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(view);
        }
    }

    public static void loadRoundedImage(Context context, final Object src, final ImageView view, int corners) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(corners)))
                    .placeholder(PLACEHOLDER_DEFAULT)
                    .error(PLACEHOLDER_DEFAULT)
                    .into(view);
        }
    }


    public static void loadImage(Context context, final Object src, final ImageView view, int placeholder) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(placeholder)
                    .error(placeholder)
                    .into(view);
        }
    }


    public static void loadImageNoError(Context context, final Object src, final ImageView view) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view);
        }
    }


    public static void loadImage(Context context, final Object src, final ImageView view, RequestListener<Drawable> listener) {
        if (getContextNotNull(context)) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(PLACEHOLDER_DEFAULT)
                    .error(PLACEHOLDER_DEFAULT)
                    .listener(listener)
                    .into(view);
        }
    }

    public static void loadImage(Context context, final Object src, final SimpleTarget<Drawable> target) {
        if (src != null) {
            GlideApp.with(context)
                    .load(checkUrl(src))
                    .thumbnail(0.1f)
                    .placeholder(PLACEHOLDER_DEFAULT)
                    .error(PLACEHOLDER_DEFAULT)
                    .into(target);
        }
    }

    /**
     *  加载网络连接判断地址
     * @param src
     * @return
     */
    private static Object checkUrl(Object src) {
        if (src != null && src instanceof String) {
            String url = (String) src;
            if (CommonUtils.StringNotNull(url) && CommonUtils.isContainsHttp(url)) {
                return url;
            }
        }
        return src;
    }


    private static boolean getContextNotNull(Context context) {
        if (context != null) {
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()
                        && !((Activity) context).isDestroyed()) {
                    return true;
                }
            } else {
                return context != null;
            }
        }
        return false;
    }


    /**
     * 需要在子线程执行
     *
     * @param context
     * @param url
     * @return
     */
    public static Bitmap load(Context context, String url) {
        try {
            return GlideApp.with(context)
                    .asBitmap()
                    .load(checkUrl(url))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
