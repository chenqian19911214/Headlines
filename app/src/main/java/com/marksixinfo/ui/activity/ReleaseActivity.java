package com.marksixinfo.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.tools.PictureFileUtils;
import com.marksixinfo.R;
import com.marksixinfo.adapter.ReleaseSelectAdapter;
import com.marksixinfo.base.ArrayCallback;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.Base64UrlData;
import com.marksixinfo.bean.ReleaseLoadImage;
import com.marksixinfo.bean.ReleaseSelectClassify;
import com.marksixinfo.bean.ReleaseSelectData;
import com.marksixinfo.bean.ResultData;
import com.marksixinfo.bean.SelectClassifyData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.MainClickIndexEvent;
import com.marksixinfo.evenbus.SelectClassifyEvent;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.CommArrayImpl;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.BASE64Utils;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.CleanEditTextView;
import com.marksixinfo.widgets.ReleaseGridRvDividerDecoration;
import com.marksixinfo.widgets.easyemoji.EmotionInputDetector;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;
import com.marksixinfo.widgets.emotionkeyboard.EmotionMainFragment;
import com.marksixinfo.widgets.pictureselector.FullyGridLayoutManager;
import com.marksixinfo.widgets.pictureselector.GridImageAdapter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 发布帖子
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:14
 */
public class ReleaseActivity extends MarkSixActivity implements OnItemClickListener,
        TextWatcher, GridImageAdapter.onAddPicClickListener,
        GridImageAdapter.OnItemClickListener, SucceedCallBackListener,
        View.OnClickListener, View.OnTouchListener {

    @BindView(R.id.tv_classify)
    TextView tvClassify;
    @BindView(R.id.tv_select_title)
    TextView tvSelectTitle;
    @BindView(R.id.edit_title)
    CleanEditTextView editTitle;
    @BindView(R.id.tv_period_number)
    TextView tvPeriodNumber;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.edit_content)
    EditText editContent;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    //    @BindView(R.id.tv_release)
//    TextView mTvRelease;
    @BindView(R.id.rl_background)
    RelativeLayout viewBackground;
    //    @BindView(R.id.viewPager)
//    ViewPager viewPager;
//    @BindView(R.id.circleIndicator)
//    CircleIndicator mCircleIndicator;
//    @BindView(R.id.ll_face_container)
//    LinearLayout llFaceContainer;
//    @BindView(R.id.ll_keyboard_content)
//    LinearLayout llKeyboardContent;
    @BindView(R.id.elastic_content)
    NestedScrollView elasticContent;
    //    @BindView(R.id.iv_face)
//    ImageView mEmoJiView;
//    @BindView(R.id.fl_emotionview_main)
//    FrameLayout fl_emotionview_main;
    private CommonDialog commonDialog;
    private ReleaseSelectAdapter selectAdapter;
    private List<LocalMedia> selectList = new ArrayList<>();
    private GridImageAdapter adapter;
    private ArrayList<Base64UrlData> base64Url = new ArrayList<>();
    private InputMethodManager inputMethodManager;
    private boolean isKeyboard = true;
    private EmotionInputDetector mDetector;
    private EmotionMainFragment emotionMainFragment;
    private List<SelectClassifyData> type = new ArrayList<>();
    private List<ReleaseSelectData> period = new ArrayList<>();
    private String currentId = "";
    private String content = "";
    private String title = "";
    private String periodValue = "";
    private ReleaseUtils releaseUtils = new ReleaseUtils();
    private int column = 4;

    @Override
    public int getViewId() {
        return R.layout.activity_release;
    }


    @Override

    public void afterViews() {

        markSixTitle.init("发布头条", "", "发布", 0, this);

        tvPeriodNumber.setText("000期");
        FullyGridLayoutManager manager = new FullyGridLayoutManager(ReleaseActivity.this, 3,
                GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new ReleaseGridRvDividerDecoration(this));
        adapter = new GridImageAdapter(ReleaseActivity.this, this);
        adapter.setList(selectList);
        adapter.setSelectMax(releaseUtils.getMaxSelectNum());
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);


        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        editContent.addTextChangedListener(this);
        editTitle.addTextChangedListener(this);
        editContent.setOnTouchListener(this);
//        editContent.setOnKeyBoardHideListener(this);
//        KeyBoardUtils.toggleInput(this);
//        emotionMainFragment = EmotionMainFragment.newInstance(EmotionMainFragment.class, null);
////        emotionMainFragment = new EmotionMainFragment();
//        emotionMainFragment.bindEditTextView(editContent);
//        emotionMainFragment.bindToContentView(elasticContent);
//        replace(R.id.fl_emotionview_main, emotionMainFragment);
//        showInput();
//        setWindowSoftInputMode(true);

        //                .bindSendButton(mSendButton)
//        mDetector = EmotionInputDetector.with(this)
////                .bindSendButton(mSendButton)
//                .bindToEditText(editContent)
//                .setEmotionView(llFaceContainer)
//                .bindToContent(elasticContent)
//                .bindToEmotionButton(mEmoJiView);
//
//        EmoJiHelper emojiHelper = new EmoJiHelper(1, this, editContent);
//        EmoJiContainerAdapter mAdapter = new EmoJiContainerAdapter(emojiHelper.getPagers());
//        viewPager.setAdapter(mAdapter);
//        mCircleIndicator.setViewPager(viewPager);
        KeyBoardUtils.setEditTextState(editContent);
        getData();
    }

    /**
     * 获取期数及分类
     */
    private void getData() {
        new HeadlineImpl(new MarkSixNetCallBack<ReleaseSelectClassify>(this, ReleaseSelectClassify.class) {
            @Override
            public void onSuccess(ReleaseSelectClassify response, int id) {
                if (response != null && CommonUtils.ListNotNull(response.getPeriod())) {
                    period = response.getPeriod();
                    handleType();
//                    period.addAll(new ArrayList<>(period));
//                    period.addAll(new ArrayList<>(period));
                    releaseUtils.setPeriod(period);
                    onItemClick(null, 0);
                    setClassifyByPosition(false);
                }
            }
        }.setNeedDialog(false)).getSelectClassify();
    }


    /**
     * 处理分类
     */
    private void handleType() {
        type.clear();
        type.add(0, CommonUtils.getDefault());
    }


    /**
     * 设置选中的分类,并对比是否有删除
     *
     * @param isContrastId
     */
    private void setClassifyByPosition(boolean isContrastId) {
        if (CommonUtils.ListNotNull(type)) {
            try {
                if (isContrastId) {
                    boolean isContains = false;
                    String name = "";
                    if ("0".equals(currentId)) {
                        isContains = true;
                        name = type.get(0).getName();
                    } else {
                        for (SelectClassifyData selectClassifyData : type) {
                            if (selectClassifyData != null) {
                                String id = selectClassifyData.getId();
                                if (currentId.equals(id)) {
                                    isContains = true;
                                    name = selectClassifyData.getName();
                                    break;
                                }
                            }
                        }
                    }
                    if (!isContains) {
                        setClassifyByPosition(false);
                    } else {
                        if (tvClassify != null) {
                            tvClassify.setText(CommonUtils.StringNotNull(name) ? name : "");
                        }
                    }
                } else {
                    SelectClassifyData selectClassifyData = type.get(0);
                    if (selectClassifyData != null && tvClassify != null) {
                        String name = selectClassifyData.getName();
                        currentId = selectClassifyData.getId();
                        tvClassify.setText(CommonUtils.StringNotNull(name) ? name : "");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        KeyBoardUtils.toggleInput(this);
//    }
//
//
//    @Override
//    public void onBackPressed() {
//        if (!mDetector.interceptBackPress()) {
//            super.onBackPressed();
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    adapter.setList(selectList);
                    adapter.notifyDataSetChanged();
                    break;
                case NumberConstants.INTENT_CODE_CLASSIFY_SELECT:
                    if (data != null) {
                        currentId = data.getStringExtra(StringConstants.RELEASE_CLASSIFY_SELECT);
                        setClassifyByPosition(true);
                    }
                    break;
            }
        }
    }


    /**
     * 显示期数弹框
     */
    private void showPopup() {
        if (period.size() <= 1) {
            return;
        }
        int[] location = new int[2];
        viewLine.getLocationInWindow(location); //获取在当前窗口内的绝对坐标
        viewLine.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
        int screenHeight = UIUtils.getScreenHeight(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout
                .LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight - location[1]);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        viewBackground.setLayoutParams(layoutParams);
        if (selectAdapter == null) {
            int screenWidth = UIUtils.getScreenWidth(getContext());
            selectAdapter = new ReleaseSelectAdapter(getContext(), period, this);
            View view = LayoutInflater.from(this).inflate(R.layout.view_popup_select, null);
            viewBackground.addView(view);
            GridView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.getLayoutParams().width = screenWidth;
            int size = period.size() >= 12 ? 12 : period.size();
            int height = (int) ((size / 4 == 0 ? 1 : Math.ceil(size / 4f)) * 64);
            recyclerView.getLayoutParams().height = UIUtils.dip2px(this, height);
            recyclerView.setAdapter(selectAdapter);
        }
        viewBackground.setVisibility(View.VISIBLE);
        setSelectStatus(true);

        viewBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewBackground.setVisibility(View.GONE);
                setSelectStatus(false);
            }
        });
    }

    /**
     * 选择期数弹框,期数颜色
     *
     * @param isSelect
     */
    private void setSelectStatus(boolean isSelect) {
        if (isSelect) {
            UIUtils.setRightDrawable(this, tvPeriodNumber, R.drawable.icon_pull_red);
            tvPeriodNumber.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            UIUtils.setRightDrawable(this, tvPeriodNumber, R.drawable.icon_pull_gray2);
            tvPeriodNumber.setTextColor(getResources().getColor(R.color.grey_999));
        }
    }


    /**
     * 选择期数数据填充
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        for (ReleaseSelectData data : period) {
            data.setCheck(false);
        }
        ReleaseSelectData selectData = period.get(position);
        if (selectData != null) {
            String name = selectData.getName();
            selectData.setCheck(true);
            tvPeriodNumber.setText(name);
            periodValue = selectData.getValue();
            if (selectAdapter != null) {
                selectAdapter.notifyUI(period);
            }
        }
        if (viewBackground != null) {
            viewBackground.setVisibility(View.GONE);
            setSelectStatus(false);
        }
    }


    @OnClick({R.id.tv_classify, R.id.tv_select_title, R.id.tv_period_number
          /*, R.id.iv_keyboard, R.id.iv_topic, R.id.iv_picture, R.id.iv_face*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_classify://选择分类
                startClassForResult(getString(R.string.SelectClassifyActivity),
                        null, NumberConstants.INTENT_CODE_CLASSIFY_SELECT);
                break;
            case R.id.tv_select_title://添加标题
                if (commonDialog == null) {
                    commonDialog = new CommonDialog(this);
                }
                commonDialog.show("是否添加标题？", "", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tvSelectTitle.setVisibility(View.GONE);
                        editTitle.setVisibility(View.VISIBLE);
                        editTitle.setText("");
                        if (commonDialog != null && commonDialog.isShowing()) {
                            commonDialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.tv_period_number://选择期数
                showPopup();
                break;
//            case R.id.iv_keyboard:
////                if (isKeyboard) {
////                    hideInput();
////                } else {
////                    showInput();
////                }
//                KeyBoardUtils.toggleInput(this);
////                setWindowSoftInputMode(!isKeyboard);
//                break;
//            case R.id.iv_topic:
//                break;
//            case R.id.iv_picture:
//                break;
//            case R.id.iv_face:
//
//                break;
        }
    }


    /**
     * 设置键盘弹出状态
     *
     * @param isAlwaysVisible 是否一直显示键盘
     */
    private void setWindowSoftInputMode(boolean isAlwaysVisible) {
        isKeyboard = isAlwaysVisible;
        if (isAlwaysVisible) {
            //取消强制隐藏键盘
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        } else {
            //强制隐藏键盘，即使点击Edit也不会弹出。覆盖输入法窗口,如果需要可在edit的touch里清空这个属性
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setWindowSoftInputMode(true);
    }

    /**
     * 显示键盘
     */
    public void showInput() {
        LogUtils.d("显示");
        isKeyboard = true;
        //打开软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editContent, 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        et.requestFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * 隐藏键盘
     */
    protected void hideInput() {
        LogUtils.d("隐藏");
        isKeyboard = false;
        //关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editContent.getWindowToken(), 0);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        View v = getWindow().peekDecorView();
//        if (null != v) {
//            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//        }
    }

//    @Override
//    public void onKeyHide(int keyCode, KeyEvent event) {
//        LogUtils.d("隐藏2");
//        isKeyboard = false;
//    }

//    @Override
//    public void onBackPressed() {
//        /**
//         * 判断是否拦截返回键操作
//         */
//        if (!emotionMainFragment.isInterceptBackPress()) {
//            super.onBackPressed();
//        }
//    }

    /**
     * 选择分类分类发生变化
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SelectClassifyEvent event) {
        if (event != null && CommonUtils.ListNotNull(event.getType())) {
//            type.clear();
//            type.addAll(event.getType());
            handleType();
            type.addAll(event.getType());
            setClassifyByPosition(true);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }


    /**
     * 输入变化改变发布按钮颜色
     *
     * @param charSequence
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        content = editContent.getText().toString().trim();
        title = editTitle.getText().toString().trim();
        if (CommonUtils.StringNotNull(title)) {
            editTitle.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            editTitle.setTypeface(Typeface.DEFAULT);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }


    /**
     * 发布
     */
    private void release() {
        showDialog();
        toBase64();
    }


    /**
     * 开线程转base64
     */
    private void toBase64() {
        base64Url.clear();
        if (CommonUtils.ListNotNull(selectList)) {
            for (LocalMedia media : selectList) {
                String compressPath = media.getCompressPath();
                if (CommonUtils.StringNotNull(compressPath)) {
                    File file = new File(compressPath);
                    String prefix = releaseUtils.getPrefix(media.getPictureType());
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            base64Url.add(new Base64UrlData(BASE64Utils.fileToBase64(file, prefix), ""));
                            loadImageUrl();
                        }
                    }).start();
                }
            }
        } else {
            releaseContent();
        }
    }

    /**
     * 全部图片转换成功后,开始上传图片
     */
    private void loadImageUrl() {
        if (CommonUtils.ListNotNull(base64Url) && base64Url.size() == selectList.size()) {
            loadImageUrl(-1);
        }
    }

    /**
     * 递归上传图片(单张上传)
     *
     * @param position
     */
    private void loadImageUrl(int position) {
        int i = position;
        if (i >= base64Url.size() - 1) {
            releaseContent();
            return;
        }
        Base64UrlData base64UrlData = base64Url.get(++i);
        if (base64UrlData != null) {
            int finalPosition = i;
            new HeadlineImpl(new MarkSixNetCallBack<ReleaseLoadImage>(this, ReleaseLoadImage.class) {
                @Override
                public void onSuccess(ReleaseLoadImage response, int id) {
                    if (response != null) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //设置图片url,清除base64
                                base64UrlData.setImageUrl(response.getThumb());
                                base64UrlData.setBase64("");
                                loadImageUrl(finalPosition);
                            }
                        }).start();
                    }
                }
            }.setNeedDialog(false)).imagesUploading(base64UrlData.getBase64());
        }
    }

    /**
     * 发布
     */
    private void releaseContent() {
        List<String> list = releaseUtils.getBase64Urls(base64Url);
        new CommArrayImpl(new ArrayCallback<String>(this) {
            @Override
            public void onSuccess(ResultData<String> response) {
                toast("发布成功");
                EventBusUtil.post(new MainClickIndexEvent(0));
                //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
                if (CommonUtils.ListNotNull(base64Url)) {
                    base64Url.clear();
                    PictureFileUtils.deleteExternalCacheDirFile(ReleaseActivity.this);
                }
                finish();
            }
        }).headlineRelease(list, title, content, currentId, periodValue);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //包括裁剪和压缩后的缓存，要在上传成功后调用，注意：需要系统sd卡权限
        if (CommonUtils.ListNotNull(selectList)) {
            selectList.clear();
            PictureFileUtils.deleteExternalCacheDirFile(ReleaseActivity.this);
        }
    }

    /**
     * 添加图片
     */
    @Override
    public void onAddPicClick() {
        releaseUtils.setPermission(this, this);
    }

    /**
     * 点击已选图片
     *
     * @param position
     * @param v
     */
    @Override
    public void onItemClick(int position, View v) {
        releaseUtils.setItemClick(this, selectList, position);
    }

    /**
     * 删除
     *
     * @param position
     */
    @Override
    public void moveItem(int position) {
        if (position != RecyclerView.NO_POSITION) {
            selectList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, selectList.size());
        }
    }

    @Override
    public void succeedCallBack(@Nullable Object o) {
        PictureFileUtils.deleteCacheDirFile(this);
        releaseUtils.setConfig(this, selectList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://开始发布
                if (!CommonUtils.StringNotNull(content)) {
                    toast("请输入内容");
                    return;
                }
                release();
                break;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
        if ((view.getId() == R.id.edit_content && CommonUtils.canVerticalScroll(editContent))) {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view.getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return false;
    }


}
