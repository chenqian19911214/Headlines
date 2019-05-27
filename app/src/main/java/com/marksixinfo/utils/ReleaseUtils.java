package com.marksixinfo.utils;

import android.Manifest;
import android.app.Activity;
import android.widget.Toast;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.permissions.RxPermissions;
import com.marksixinfo.R;
import com.marksixinfo.bean.Base64UrlData;
import com.marksixinfo.bean.ReleaseSelectData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.SucceedCallBackListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 23:22
 * @Description:
 */
public class ReleaseUtils {


    public final int maxSelectNumber = 9;
    private int maxSelectNum = maxSelectNumber;
    private int themeId = R.style.picture_default_style;

    /**
     * 默认多选
     *
     * @param activity
     * @param selectList
     */
    public void setConfig(Activity activity, List<LocalMedia> selectList) {
        setConfig(activity, selectList, PictureConfig.MULTIPLE);
    }


    public void setConfig(Activity activity, List<LocalMedia> selectList, int selectionMode) {
        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(themeId)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(selectionMode)// 多选 or 单选
                .previewImage(true)// 是否可预览图片
//                    .previewVideo(cb_preview_video.isChecked())// 是否可预览视频
//                    .enablePreviewAudio(cb_preview_audio.isChecked()) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
                //.setOutputCameraPath("/CustomPath")// 自定义拍照保存路径
//                    .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
                //.compressSavePath(getPath())//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(240, 240)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
                .withAspectRatio(1, 1)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
                .hideBottomControls(true)// 是否显示uCrop工具栏，默认不显示
//                    .isGif(cb_isGif.isChecked())// 是否显示gif图片
                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                    .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
                .showCropFrame(true)// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
                .showCropGrid(true)// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                    .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                .previewEggs(true)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(80)// 裁剪压缩质量 默认100
                .minimumCompressSize(100)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }


    public void setItemClick(Activity activity, List<LocalMedia> selectList, int position) {
        if (selectList.size() > 0) {
            LocalMedia media = selectList.get(position);
            String pictureType = media.getPictureType();
            int mediaType = PictureMimeType.pictureToVideo(pictureType);
            switch (mediaType) {
                case 1:
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    PictureSelector.create(activity).themeStyle(themeId)
                            .openExternalPreview(position, selectList);
                    break;
                case 2:
                    // 预览视频
                    PictureSelector.create(activity).externalPictureVideo(media.getPath());
                    break;
                case 3:
                    // 预览音频
                    PictureSelector.create(activity).externalPictureAudio(media.getPath());
                    break;
            }
        }
    }


    public void setPermission(Activity activity, SucceedCallBackListener listener) {
        // 清空图片缓存，包括裁剪、压缩后的图片 注意:必须要在上传完成后调用 必须要获取权限
        RxPermissions permissions = new RxPermissions(activity);
        permissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean) {
                    if (listener != null) {
                        listener.succeedCallBack(aBoolean);
                    }
                } else {
                    Toast.makeText(activity, activity.getString(R.string.picture_jurisdiction), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onComplete() {
            }
        });
    }

    public List<String> getBase64Urls(List<Base64UrlData> base64Url) {
        ArrayList<String> list = new ArrayList<>();
        if (base64Url != null && base64Url.size() > 0) {
            for (Base64UrlData base64UrlData : base64Url) {
                if (base64UrlData != null) {
                    list.add(base64UrlData.getImageUrl());
                }
            }
        }
        return list;
    }


    public List<String> getLocalUrls(List<LocalMedia> netPic) {
        ArrayList<String> list = new ArrayList<>();
        if (CommonUtils.ListNotNull(netPic)) {
            for (LocalMedia localMedia : netPic) {
                if (localMedia != null) {
                    String path = localMedia.getPath();
                    if (path.contains(StringConstants.RELEASE_IMAGE_ORIGINAL_FIX)) {
                        path = path.replace(StringConstants.RELEASE_IMAGE_ORIGINAL_FIX, "");
                    }
                    list.add(path + "?" + localMedia.getWidth() + "_" + localMedia.getHeight());
                }
            }
        }
        return list;
    }


    public List<LocalMedia> getLocalPath(List<LocalMedia> selectList) {
        List<LocalMedia> list = new ArrayList<>();
        if (CommonUtils.ListNotNull(selectList)) {
            for (LocalMedia localMedia : selectList) {
                if (localMedia != null) {
                    if (!CommonUtils.isContainsHttp(localMedia.getPath())) {
                        list.add(localMedia);
                    }
                }
            }
        }
        return list;
    }

    public LocalMedia getOriginUrl(String url) {
        LocalMedia localMedia = new LocalMedia();
        if (CommonUtils.StringNotNull(url)) {
            try {
                String[] split = url.split("\\?");
                if (split.length >= 2) {
                    url = split[0] + StringConstants.RELEASE_IMAGE_ORIGINAL_FIX;
                    localMedia.setPath(url);
                    String[] strings = split[1].split("_");
                    if (strings.length >= 2) {
                        localMedia.setWidth(NumberUtils.stringToInt(strings[0]));
                        localMedia.setHeight(NumberUtils.stringToInt(strings[1]));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return localMedia;
    }


    public void setPeriod(List<ReleaseSelectData> period) {
        for (int i = 0; i < period.size(); i++) {
            ReleaseSelectData releaseSelectData = period.get(i);
            releaseSelectData.setCheck(i == 0);
            String value = releaseSelectData.getValue();
            releaseSelectData.setName(CommonUtils.fixThree(value) + "期");
        }
    }

    public String getPrefix(String type) {
        return new StringBuilder("data:").append(type).append(";base64,").toString();
    }


    public int getMaxSelectNum() {
        return maxSelectNum;
    }

    public void setMaxSelectNum(int maxSelectNum) {
        this.maxSelectNum = maxSelectNum;
    }

    public int getThemeId() {
        return themeId;
    }

    public void setThemeId(int themeId) {
        this.themeId = themeId;
    }
}
