package com.marksixinfo.ui.activity;

import android.content.Intent;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.Base64UrlData;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.ReleaseLoadImage;
import com.marksixinfo.bean.SettingData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CleanCacheEvent;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.BASE64Utils;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.CustomCoinNameFilter;
import com.marksixinfo.utils.DataCleanManager;
import com.marksixinfo.utils.EmojiFilter;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.TextChangedListener;
import com.marksixinfo.widgets.CommonInputDialog;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.SettingItemView;
import com.marksixinfo.widgets.SimpleDialog;
import com.marksixinfo.widgets.bigimageViewpage.glide.ImageLoader;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 个人中心,我的设置
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:20
 */
public class SettingActivity extends MarkSixWhiteActivity implements SucceedCallBackListener, View.OnClickListener {


    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.ll_nike_name)
    SettingItemView llNikeName;
    @BindView(R.id.tv_uid)
    SettingItemView tvUid;
    @BindView(R.id.tv_phone)
    SettingItemView tvPhone;
    @BindView(R.id.ll_email)
    SettingItemView tvEmail;
    @BindView(R.id.ll_signature)
    SettingItemView mLlSignature;
    @BindView(R.id.ll_standby)
    SettingItemView llStandby;
    @BindView(R.id.tv_last_login)
    SettingItemView tvLastLogin;
    @BindView(R.id.ll_clean_cache)
    SettingItemView tvCleanCache;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.ll_version)
    SettingItemView tvVersion;


    private String face = "";
    private CommonInputDialog commonInputDialog;
    private ReleaseUtils releaseUtils;
    private List<LocalMedia> selectList = new ArrayList<>();
    private final int nickNameLength = 20;//昵称最大字符长度
    private final int signatureLength = 20;//网址最大长度
    private final int emailLength = 50;//邮箱最大长度
    private SimpleDialog simpleDialog;

    @Override
    public int getViewId() {
        return R.layout.activity_setting;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("设置");

        commonInputDialog = new CommonInputDialog(this);
        releaseUtils = new ReleaseUtils();
        mLoadingLayout.setRetryListener(this);

        getData();
    }


    /**
     * 获取数据
     */
    private void getData() {
        new HeadlineImpl(new MarkSixNetCallBack<SettingData>(this, SettingData.class) {
            @Override
            public void onSuccess(SettingData response, int id) {
                setData(response);
            }

            @Override
            public void onError(String msg, String code) {
                mLoadingLayout.showError();
            }
        }.setNeedDialog(false)).mineSetting();
    }

    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(SettingData response) {

        if (response != null) {
            String email = response.getEmail();
            String Nickname = response.getNickname();
            face = response.getFace();
            String tel = response.getTel();
            String uid = response.getUid();
            String loginTime = response.getLast_Login();
            tvLastLogin.setValue(CommonUtils.StringNotNull(loginTime) ? loginTime : "");
            int level = response.getLevel();
            String remark = response.getRemark();


            if (level == 1 || level == 2) {
                mLlSignature.setVisibility(View.VISIBLE);
                mLlSignature.setValue(CommonUtils.StringNotNull(remark) ? CommonUtils.CommHandleText(remark) : "官方网址：无");
                llStandby.setVisibility(View.VISIBLE);
                llStandby.setValue("添加您的子域名");
            } else {
                mLlSignature.setVisibility(View.GONE);
                llStandby.setVisibility(View.GONE);
                llStandby.setValue("");
                mLlSignature.setValue("");
            }
//            llStandby.setVisibility(View.GONE);

            GlideUtil.loadCircleImage(face, ivPhoto);


            tvVersion.setValue(CommonUtils.StringNotNull(ClientInfo.VERSION) ? ClientInfo.VERSION : "");

            llNikeName.setValue(CommonUtils.StringNotNull(Nickname) ? CommonUtils.CommHandleText(Nickname) : "");

            tvUid.setValue(CommonUtils.StringNotNull(uid) ? uid : "");
            tvPhone.setValue(CommonUtils.StringNotNull(tel) ? tel : "");
            tvEmail.setValue(CommonUtils.StringNotNull(email) ? CommonUtils.CommHandleText(email) : "");
            setCacheText();
            mLoadingLayout.showContent();
        }
    }

    /**
     * 设置缓存文本
     */
    private void setCacheText() {
        String fileSize = DataCleanManager.getInternalCache(this);
        tvCleanCache.postDelayed(new Runnable() {
            @Override
            public void run() {
                tvCleanCache.setValue(CommonUtils.StringNotNull(fileSize) ? fileSize : "");
            }
        }, 500);
    }

    /**
     * 设置昵称
     *
     * @param Nickname
     */
    private void setNickname(String Nickname) {
        netSet(StringConstants.NIKE_NAME, Nickname);
    }


    /**
     * 设置邮箱
     *
     * @param mEmail
     */
    private void setEmail(String mEmail) {
        netSet(StringConstants.EMAIL, mEmail);
    }


    /**
     * 设置官方网址
     *
     * @param mEmail
     */
    private void setRemark(String mEmail) {
        netSet(StringConstants.REMARK, mEmail);
    }


    /**
     * 设置头像
     *
     * @param photo
     */
    private void setPhoto(String photo) {
        netSet(StringConstants.FACE, photo);
    }


    /**
     * 设置接口
     *
     * @param type
     * @param value
     */
    private void netSet(String type, String value) {
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("设置成功");
                getData();
            }

            @Override
            public boolean isAllowNullParams() {
                return true;
            }
        }).mineSettingProfile(type, value);
    }


    /**
     * 头像上传选择回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    showDialog();
                    toBase64();
                    break;
            }
        }
    }

    /**
     * 转base64
     */
    private void toBase64() {
        if (CommonUtils.ListNotNull(selectList)) {
            for (LocalMedia media : selectList) {
                File file = new File(media.getCompressPath());
                String prefix = releaseUtils.getPrefix(media.getPictureType());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        loadImageUrl(new Base64UrlData(BASE64Utils.fileToBase64(file, prefix), ""));
                    }
                }).start();
            }
        } else {
            closeDialog();
        }
    }

    /**
     * 上传图片
     *
     * @param base64UrlData
     */
    private void loadImageUrl(Base64UrlData base64UrlData) {
        if (base64UrlData != null) {
            new HeadlineImpl(new MarkSixNetCallBack<ReleaseLoadImage>(this, ReleaseLoadImage.class) {
                @Override
                public void onSuccess(ReleaseLoadImage response, int id) {
                    if (response != null) {
                        base64UrlData.setBase64("");
                        setPhoto(response.getThumb());
                    }
                }
            }.setNeedDialog(false)).imagesUploading(base64UrlData.getBase64());
        }
    }


    @OnClick({R.id.ll_photo, R.id.ll_nike_name, R.id.ll_email, R.id.ll_change_password
            , R.id.ll_signature, R.id.ll_clean_cache, R.id.ll_version, R.id.ll_standby})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_photo://上传图片
                releaseUtils.setPermission(this, this);
                break;
            case R.id.ll_nike_name://修改昵称
                setNickNameMaxLength(nickNameLength);
                String Nickname = llNikeName.getValue();
                EditText editText2 = commonInputDialog.getEditText();
                editText2.removeTextChangedListener(TextChangedListener.textWatcher);
                commonInputDialog.show("修改昵称", "请输入您的昵称", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = commonInputDialog.getInputContent().trim();
                        if (!CommonUtils.StringNotNull(s)) {
                            toast("请输入您的昵称");
                            return;
                        }
                        if (!s.equals(Nickname)) {
                            setNickname(s);
                        }
                        if (commonInputDialog != null && commonInputDialog.isShowing()) {
                            commonInputDialog.dismiss();
                        }
                    }
                }).setTextViewInput(Nickname);
                break;
            case R.id.ll_email://修改邮箱
                setEmailMaxLength(emailLength);
                String mEmail = tvEmail.getValue();
                EditText editText1 = commonInputDialog.getEditText();
                if (editText1 != null) {
                    //不能输入中文
                    TextChangedListener.StringWatcher(editText1);
                }
                commonInputDialog.show("修改邮箱", "请输入您的邮箱", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String s = commonInputDialog.getInputContent().trim();
                        if (!CommonUtils.StringNotNull(s)) {
                            toast("请输入您的邮箱");
                            return;
                        }
                        if (!s.equals(mEmail)) {
                            if (!CommonUtils.isEmail(s)) {
                                toast("邮箱格式不正确");
                                return;
                            }
                            setEmail(s);
                        }
                        if (commonInputDialog != null && commonInputDialog.isShowing()) {
                            commonInputDialog.dismiss();
                        }
                    }
                }).setTextViewInput("");
                break;
            case R.id.ll_change_password://修改密码
                startClass(R.string.ChangePasswordActivity);
                break;
            case R.id.ll_signature://修改官方网址
                CommonInputDialog inputDialog = new CommonInputDialog(this);
                EditText editText = inputDialog.getEditText();
                if (editText != null) {
                    //限制长度
                    editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(signatureLength), new EmojiFilter()});
                    //不能输入中文
                    TextChangedListener.StringWatcher(editText);
                }
                inputDialog.show("修改官方网址", "请输入您的官方网址", "取消",
                        "确定", null, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String s = inputDialog.getInputContent().trim();
                                setRemark(s);
                                if (inputDialog.isShowing()) {
                                    inputDialog.dismiss();
                                }
                            }
                        }).setTextViewInput("");
                break;
            case R.id.ll_clean_cache://清除缓存
                if (simpleDialog == null) {
                    simpleDialog = new SimpleDialog(this);
                }
                simpleDialog.setSelectOneTitle(true).show("确定清理所有缓存？离线内容及图片均会被清除",
                        "删除", new SucceedCallBackListener() {
                            @Override
                            public void succeedCallBack(Object o) {
                                if ("2".equals(o)) {
                                    cleanCache();
                                }
                            }
                        });
                break;
            case R.id.ll_version://版本更新
                break;
            case R.id.ll_standby://备用网址
                startClass(R.string.StandbyRemarkActivity);
//                break;
        }
    }

    /**
     * 功能描述:  清除缓存
     *
     * @auther: Administrator
     * @date: 2019/4/17 0017 17:14
     */
    private void cleanCache() {
        ImageLoader.cleanDiskCache(this);
        EventBusUtil.post(new CleanCacheEvent());
        SPUtil.removeSearchHistory(this);
        SPUtil.cleanTaskSignInTime(this);
        toast("清除缓存成功");
        tvCleanCache.setValue("0B");
    }

    @Override
    public void succeedCallBack(@Nullable Object o) {
        PictureFileUtils.deleteCacheDirFile(this);
        releaseUtils.setConfig(this, selectList, PictureConfig.SINGLE);
    }


    /**
     * 设置输入框最大长度
     *
     * @param length
     */
    private void setEmailMaxLength(int length) {
        if (commonInputDialog != null) {
            EditText editText = commonInputDialog.getEditText();
            if (editText != null) {
                editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length), new EmojiFilter()});
            }
        }
    }


    /**
     * 设置输入框最大字符长度
     *
     * @param length
     */
    private void setNickNameMaxLength(int length) {
        if (commonInputDialog != null) {
            EditText editText = commonInputDialog.getEditText();
            if (editText != null) {
                editText.setFilters(new InputFilter[]{new CustomCoinNameFilter(length), new EmojiFilter()});
            }
        }
    }

    @Override
    public void onClick(View v) {
        mLoadingLayout.showLoading();
        getData();
    }
}
