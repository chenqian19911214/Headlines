package com.marksixinfo.widgets.umengshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.LoadingDialog;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.ToastShow;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMVideo;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.media.UMusic;

import java.io.File;

import androidx.annotation.DrawableRes;

/**
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 13:52
 * @Description:
 */
public class UMengShareUtils {
    public static UMengShareUtils getInstance() {
        return Holder.INSTANCE;
    }


    private UMengShareUtils() {

    }

    private static class Holder {
        private static UMengShareUtils INSTANCE = new UMengShareUtils();
    }


    /**
     * 纯文本分享
     *
     * @param activity
     * @param text
     * @param shareMedia
     * @param shareListener
     */
    public void shareText(Activity activity, String text, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        new ShareAction(activity)
                .withText(text)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }

    /**
     * 纯图片分享
     */
    public void shareImage(Activity activity, UMImage umImage, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        new ShareAction(activity)
                .withMedia(umImage)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }


    /**
     * 分享图片文字链接
     *
     * @param activity
     */
    public void shareUMWeb(Activity activity, ShareParams shareParams, SHARE_MEDIA shareMedia) {
        UMWeb web = new UMWeb(shareParams.getUrl());
        web.setTitle(shareParams.getTitle());
        web.setThumb(shareParams.getUmImage());
        web.setDescription(shareParams.getContent());
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareParams.getListener())
                .share();
    }


    /**
     * 分享图片文字链接
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param umImage
     * @param shareMedia
     * @param shareListener
     */
    public static void shareUMWeb(Activity activity, String shareUrl, String title, String content, UMImage umImage, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);
        web.setThumb(umImage);
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }

    /**
     * 分享图片文字链接
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param icon
     * @param shareMedia
     * @param shareListener
     */
    public static void shareUMWeb(Activity activity, String shareUrl, String title, String content, String icon, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);
        web.setThumb(getImageIcon(activity, icon));
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }


    /**
     * 分享图片文字链接
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param icon
     * @param shareMedia
     * @param shareListener
     */
    public static void shareUMWeb(Activity activity, String shareUrl, String title, String content, @DrawableRes int icon, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);
        web.setThumb(getImageIcon(activity, icon));
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }


    /**
     * 分享图片文字链接
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param icon
     * @param shareMedia
     * @param shareListener
     */
    public static void shareUMWeb(Activity activity, String shareUrl, String title, String content, Bitmap icon, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);
        web.setThumb(getImageIcon(activity, icon));
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }

    /**
     * 分享图片文字链接
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param icon
     * @param shareMedia
     * @param shareListener
     */
    public static void shareUMWeb(Activity activity, String shareUrl, String title, String content, File icon, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);
        web.setThumb(getImageIcon(activity, icon));
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }


    public static UMImage getImageIcon(Activity activity, String icon) {
        if (!TextUtils.isEmpty(icon)) {
            try {
                return new UMImage(activity, icon);
            } catch (Exception e) {
                e.printStackTrace();
                return new UMImage(activity, R.mipmap.ic_launcher);
            }
        } else {
            return new UMImage(activity, R.mipmap.ic_launcher);
        }
    }

    public static UMImage getImageIcon(Activity activity, @DrawableRes int icon) {
        if (icon > 0) {
            try {
                return new UMImage(activity, icon);
            } catch (Exception e) {
                e.printStackTrace();
                return new UMImage(activity, R.mipmap.ic_launcher);
            }
        } else {
            return new UMImage(activity, R.mipmap.ic_launcher);
        }
    }


    public static UMImage getImageIcon(Activity activity, Bitmap iconBitmap) {
        if (iconBitmap != null) {
            try {
                return new UMImage(activity, iconBitmap);
            } catch (Exception e) {
                e.printStackTrace();
                return new UMImage(activity, R.mipmap.ic_launcher);
            }
        } else {
            return new UMImage(activity, R.mipmap.ic_launcher);
        }

    }

    public static UMImage getImageIcon(Activity activity, File iconFile) {
        if (iconFile != null) {
            try {
                return new UMImage(activity, iconFile);
            } catch (Exception e) {
                e.printStackTrace();
                return new UMImage(activity, R.mipmap.ic_launcher);
            }
        } else {
            return new UMImage(activity, R.mipmap.ic_launcher);
        }
    }


    /**
     * 分享图片文字链接
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param umImage
     * @param shareMedia
     */
    public void shareUMWeb(Activity activity, String shareUrl, String title, String content, UMImage umImage, SHARE_MEDIA shareMedia) {
        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(title);
        web.setThumb(umImage);
        web.setDescription(content);
        new ShareAction(activity)
                .withMedia(web)
                .setPlatform(shareMedia)
                .setCallback(new MyShareListener(activity) {
                    @Override
                    public void onResult(SHARE_MEDIA platform) {
                        super.onResult(platform);
                    }

                    @Override
                    public void onError(SHARE_MEDIA platform, Throwable t) {
                        super.onError(platform, t);
                    }
                })
                .share();
    }


    /**
     * 分享音乐
     *
     * @param activity
     * @param musicurl
     * @param shareUrl
     * @param title
     * @param content
     * @param umImage
     * @param shareMedia
     * @param shareListener
     */
    public void shareMusic(Activity activity, String musicurl, String shareUrl, String title, String content, UMImage umImage, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMusic music = new UMusic(musicurl);
        music.setTitle(title);
        music.setThumb(umImage);
        music.setDescription(content);
        music.setmTargetUrl(shareUrl);
        new ShareAction(activity)
                .withMedia(music)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }


    /**
     * 分享视频
     *
     * @param activity
     * @param videoUrl
     * @param title
     * @param content
     * @param umImage
     * @param shareMedia
     * @param shareListener
     */
    public void shareVideo(Activity activity, String videoUrl, String title, String content, UMImage umImage, SHARE_MEDIA shareMedia, UMShareListener shareListener) {
        UMVideo video = new UMVideo(videoUrl);
        video.setThumb(umImage);
        video.setTitle(title);
        video.setDescription(content);
        new ShareAction(activity)
                .withMedia(video)
                .setPlatform(shareMedia)
                .setCallback(shareListener)
                .share();
    }

    public static class MyShareListener implements UMShareListener {

        private Context context;
        private LoadingDialog loadingDialog;
        private boolean isShowToast = true;

        public MyShareListener(Context context) {
            this.context = context;
            loadingDialog = new LoadingDialog(context);
        }

        public MyShareListener(Context context, boolean isShowToast) {
            this.context = context;
            this.isShowToast = isShowToast;
            loadingDialog = new LoadingDialog(context);
        }

        /**
         * @param platform 平台类型
         * @descrption 分享开始的回调
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
//            if (!SHARE_MEDIA.WEIXIN.equals(platform)) {
//                SocializeUtils.safeShowDialog(loadingDialog);
//            }
        }

        /**
         * @param platform 平台类型
         * @descrption 分享成功的回调
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            SocializeUtils.safeCloseDialog(loadingDialog);
            if (isShowToast) {
                ToastShow.toastShow(context, "分享成功");
            }
        }

        /**
         * @param platform 平台类型
         * @param t        错误原因
         * @descrption 分享失败的回调
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            SocializeUtils.safeCloseDialog(loadingDialog);
            LogUtils.d("" + t.getMessage());
            if (isShowToast) {
                ToastShow.toastShow(context, "分享失败");
            }
        }

        /**
         * @param platform 平台类型
         * @descrption 分享取消的回调
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (isShowToast) {
                ToastShow.toastShow(context, "分享取消");
            }
//            SocializeUtils.safeCloseDialog(loadingDialog);
        }
    }


    /**
     * 返回分享渠道标识，用于统计
     *
     * @param media
     * @return
     */
    public static String getShareTag(SHARE_MEDIA media) {
        if (media == null)
            return "";
        if (media.equals(SHARE_MEDIA.WEIXIN)) {
            return "hy";
        } else if (media.equals(SHARE_MEDIA.WEIXIN_CIRCLE)) {
            return "pyq";
        } else if (media.equals(SHARE_MEDIA.SINA)) {
            return "sina";
        } else if (media.equals(SHARE_MEDIA.QQ)) {
            return "qq";
        } else {
            return media + "";
        }
    }


    /**
     * 自定义分享弹出框
     */
    public void doCustomShare(Activity activity, ShareParams shareParams, MyShareListener shareListener, UMShareDialog uMShareDialog) {
        doCustomShare(activity, shareParams.getUrl(), shareParams.getTitle(), shareParams.getContent(),
                shareParams.getUmImage(), shareListener, uMShareDialog);
    }

    /**
     * 自定义分享弹出框
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param umImage
     * @param shareListener
     */
    public void doCustomShare(final Activity activity, final String shareUrl,
                              final String title, final String content, final UMImage umImage,
                              final MyShareListener shareListener,
                              final UMShareDialog uMShareDialog) {
        doCustomShare(activity, shareUrl, title, content, umImage, shareListener, uMShareDialog, null);

    }

    /**
     * 自定义分享弹出框
     *
     * @param activity
     * @param shareUrl
     * @param title
     * @param content
     * @param umImage
     * @param shareListener
     */
    public void doCustomShare(final Activity activity, final String shareUrl,
                              final String title, final String content, final UMImage umImage,
                              final MyShareListener shareListener,
                              final UMShareDialog uMShareDialog, final SucceedCallBackListener<Integer> listener) {
        uMShareDialog.setCancelable(true);
        uMShareDialog.setCanceledOnTouchOutside(true);
        uMShareDialog.setOnListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                if (id == R.id.custom_share_wechat_view) { //微信
                    shareUMWeb(activity, shareUrl, title, content, umImage, SHARE_MEDIA.WEIXIN, shareListener);
                } else if (id == R.id.custom_share_friends_view) { //朋友圈
                    shareUMWeb(activity, shareUrl, title, content, umImage, SHARE_MEDIA.WEIXIN_CIRCLE, shareListener);
                } else if (id == R.id.custom_share_qq_view) {//QQ好友
                    shareUMWeb(activity, shareUrl, title, content, umImage, SHARE_MEDIA.QQ, shareListener);
                } else if (id == R.id.custom_share_copy_view) {//复制
                    CommonUtils.copyText(activity, shareUrl);
                } else if (id == R.id.custom_share_cancel_view) {
                    //取消关闭dialog
                }
                if (id != R.id.custom_share_cancel_view && id != R.id.custom_share_copy_view && listener != null) {
                    listener.succeedCallBack(0);
                }
                if (uMShareDialog.isShowing()) {
                    uMShareDialog.dismiss();
                }
            }
        });
        uMShareDialog.showDialog();
    }

    /**
     * 获取分享成功回调关闭
     *
     * @param activity
     * @param uMShareDialog
     * @return
     */
    public MyShareListener getMySuccessShareListener(final Activity activity, final UMShareDialog uMShareDialog) {
        return new MyShareListener(activity) {
            /**
             *  分享 成功关闭分享页面
             * @param platform 平台类型
             */
            public void onResult(SHARE_MEDIA platform) {
                super.onResult(platform);
                if (uMShareDialog != null && uMShareDialog.isShowing()) {
                    uMShareDialog.dismiss();
                }
            }
        };
    }


    public void release(Context mContext) {
        UMShareAPI.get(mContext).release();
    }

    /**
     * 调用此方法的Activity（如果fragment则容器Activity）必须调用 UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
     */
    public static void weixinLogin(Activity context, UMAuthListener umAuthListener) {
        UMShareAPI mShareAPI = UMShareAPI.get(context);
        mShareAPI.getPlatformInfo(context, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

}

