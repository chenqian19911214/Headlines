package com.marksixinfo.ui.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixFragment;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.CustomViewPager;
import com.marksixinfo.widgets.emotionkeyboard.EmotiomComplateFragment;
import com.marksixinfo.widgets.emotionkeyboard.EmotionKeyboard;
import com.marksixinfo.widgets.emotionkeyboard.EmotionUtils;
import com.marksixinfo.widgets.emotionkeyboard.FragmentFactory;
import com.marksixinfo.widgets.emotionkeyboard.GlobalOnItemClickManagerUtils;
import com.marksixinfo.widgets.emotionkeyboard.HorizontalRecyclerviewAdapter;
import com.marksixinfo.widgets.emotionkeyboard.ImageModel;
import com.marksixinfo.widgets.emotionkeyboard.NoHorizontalScrollerVPAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Auther: Administrator
 * @Date: 2019/3/21 0021 14:45
 * @Description:
 */
public class EmotionMainFragment2 extends MarkSixFragment {

    @BindView(R.id.iv_keyboard)
    ImageView ivKeyboard;
    @BindView(R.id.iv_topic)
    ImageView ivTopic;
    @BindView(R.id.iv_picture)
    ImageView ivPicture;
    @BindView(R.id.iv_face)
    ImageView ivFace;
    @BindView(R.id.ll_keyboard_content)
    LinearLayout llKeyboardContent;
    @BindView(R.id.vp_emotionview_layout)
    CustomViewPager vpEmotionviewLayout;
    @BindView(R.id.recyclerview_horizontal)
    RecyclerView recyclerviewHorizontal;
    @BindView(R.id.ll_emotion_layout)
    LinearLayout llEmotionLayout;
    private EmotionKeyboard mEmotionKeyboard;

    //需要绑定的内容view
    private View contentView;

    //    //需要绑定的内容EditText
    private EditText mEditText;

    //是否绑定当前Bar的编辑框的flag
    public static final String BIND_TO_EDITTEXT = "bind_to_edittext";
    //是否隐藏bar上的编辑框和发生按钮
    public static final String HIDE_BAR_EDITTEXT_AND_BTN = "hide bar's editText and btn";

    //当前被选中底部tab
    private static final String CURRENT_POSITION_FLAG = "CURRENT_POSITION_FLAG";
    private int CurrentPosition = 0;
    private HorizontalRecyclerviewAdapter horizontalRecyclerviewAdapter;


    //    //是否绑定当前Bar的编辑框的flag
//    public static final String BIND_TO_EDITTEXT = "bind_to_edittext";
//    //是否隐藏bar上的编辑框和发生按钮
//    public static final String HIDE_BAR_EDITTEXT_AND_BTN = "hide bar's editText and btn";
//
    //是否绑定当前Bar的编辑框,默认true,即绑定。
    //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isBindToBarEditText = true;
//
//    //是否隐藏bar上的编辑框和发生按钮,默认不隐藏
//    private boolean isHidenBarEditTextAndBtn = false;

    List<Fragment> fragments = new ArrayList<>();
//    private RecyclerView recyclerviewHorizontal;

    @Override
    public int getViewId() {
        return R.layout.fragment_main_emotion;
    }


    @Override
    protected void afterViews() {
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());

//        if (isBindToBarEditText) {
        //绑定当前Bar的编辑框
        globalOnItemClickManager.attachToEditText(mEditText);

        initDatas();
    }


    /**
     * 数据操作,这里是测试数据，请自行更换数据
     */
    protected void initDatas() {
        replaceFragment();
        List<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < fragments.size(); i++) {
            if (i == 0) {
                ImageModel model1 = new ImageModel();
                model1.icon = getResources().getDrawable(R.drawable.ic_emotion);
                model1.flag = "经典笑脸";
                model1.isSelected = true;
                list.add(model1);
            } else {
                ImageModel model = new ImageModel();
                model.icon = getResources().getDrawable(R.drawable.ic_plus);
                model.flag = "其他笑脸" + i;
                model.isSelected = false;
                list.add(model);
            }
        }

        //记录底部默认选中第一个
        CurrentPosition = 0;
        SPUtil.setIntValue(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);

        //底部tab
        horizontalRecyclerviewAdapter = new HorizontalRecyclerviewAdapter(getActivity(), list);
        recyclerviewHorizontal.setHasFixedSize(true);//使RecyclerView保持固定的大小,这样会提高RecyclerView的性能
        recyclerviewHorizontal.setAdapter(horizontalRecyclerviewAdapter);
        recyclerviewHorizontal.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false));
        //初始化recyclerview_horizontal监听器
        horizontalRecyclerviewAdapter.setOnClickItemListener(new HorizontalRecyclerviewAdapter.OnClickItemListener() {
            @Override
            public void onItemClick(View view, int position, List<ImageModel> datas) {
                //获取先前被点击tab
                int oldPosition = SPUtil.getIntValue(getActivity(), CURRENT_POSITION_FLAG);
                //修改背景颜色的标记
                datas.get(oldPosition).isSelected = false;
                //记录当前被选中tab下标
                CurrentPosition = position;
                datas.get(CurrentPosition).isSelected = true;
                SPUtil.setIntValue(getActivity(), CURRENT_POSITION_FLAG, CurrentPosition);
                //通知更新，这里我们选择性更新就行了
                horizontalRecyclerviewAdapter.notifyItemChanged(oldPosition);
                horizontalRecyclerviewAdapter.notifyItemChanged(CurrentPosition);
                //viewpager界面切换
                vpEmotionviewLayout.setCurrentItem(position, false);
            }

            @Override
            public void onItemLongClick(View view, int position, List<ImageModel> datas) {
            }
        });


    }

    private void replaceFragment() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment f1 = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        fragments.add(f1);
        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), fragments);
        vpEmotionviewLayout.setAdapter(adapter);
    }


    /**
     * 是否拦截返回键操作，如果此时表情布局未隐藏，先隐藏表情布局
     *
     * @return true则隐藏表情布局，拦截返回键操作
     * false 则不拦截返回键操作
     */
    public boolean isInterceptBackPress() {
        return mEmotionKeyboard.interceptBackPress();
    }

    /**
     * 绑定内容view
     *
     * @param contentView
     * @return
     */
    public void bindToContentView(View contentView) {
        this.contentView = contentView;
    }

    /**
     * 绑定内容view
     *
     * @param mEditText
     * @return
     */
    public void bindEditTextView(EditText mEditText) {
        this.mEditText = mEditText;
    }



    @OnClick({R.id.iv_keyboard, R.id.iv_topic, R.id.iv_picture, R.id.iv_face})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_keyboard:



                break;
            case R.id.iv_topic:
                break;
            case R.id.iv_picture:
                break;
            case R.id.iv_face:



                break;
        }
    }

    /**
     * 锁定内容高度，防止跳闪
     */
    public void lockContentHeight() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        params.height = contentView.getHeight();
        params.weight = 0.0F;
    }

    /**
     * 隐藏表情布局
     *
     * @param showSoftInput 是否显示软件盘
     */
    public void hideEmotionLayout(boolean showSoftInput) {
        if (llKeyboardContent.isShown()) {
            llKeyboardContent.setVisibility(View.GONE);
            if (showSoftInput) {
                showSoftInput();
            }
        }
    }

    /**
     * 编辑框获取焦点，并显示软件盘
     */
    private void showSoftInput() {
        mEditText.requestFocus();
        mEditText.post(new Runnable() {
            @Override
            public void run() {
//                mInputManager.showSoftInput(mEditText, 0);
            }
        });
    }

}
