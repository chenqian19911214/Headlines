package com.marksixinfo.widgets.emotionkeyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.marksixinfo.R;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.CustomViewPager;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.OnClick;

/**
 * @Auther: Administrator
 * @Date: 2019/3/21 0021 14:45
 * @Description:
 */
public class EmotionMainFragment extends BaseFragment {


    //    @BindView(R.id.iv_keyboard)
//    ImageView ivKeyboard;
//    @BindView(R.id.iv_topic)
//    ImageView ivTopic;
//    @BindView(R.id.iv_picture)
//    ImageView ivPicture;
//    @BindView(R.id.iv_face)
//    ImageView ivFace;
//    @BindView(R.id.ll_keyboard_content)
//    LinearLayout llKeyboardContent;
//    @BindView(R.id.vp_emotionview_layout)
//    CustomViewPager vpEmotionviewLayout;
//    @BindView(R.id.recyclerview_horizontal)
//    RecyclerView recyclerviewHorizontal;
//    @BindView(R.id.ll_emotion_layout)
//    LinearLayout llEmotionLayout;
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
    private RecyclerView recyclerviewHorizontal;

    /**
     * 创建与Fragment对象关联的View视图时调用
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_emotion, container, false);
//        isHidenBarEditTextAndBtn= args.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
//        //获取判断绑定对象的参数
//        isBindToBarEditText=args.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);
        initView(rootView);
        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
                .setEmotionView(rootView.findViewById(R.id.ll_emotion_layout))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(mEditText)//判断绑定那种EditView
                .bindToEmotionButton(rootView.findViewById(R.id.iv_face))//绑定表情按钮
                .build();
//        initListener();
        initDatas();
        //创建全局监听
        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());

        if (isBindToBarEditText) {
            //绑定当前Bar的编辑框
            globalOnItemClickManager.attachToEditText(mEditText);
        } else {
            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
            globalOnItemClickManager.attachToEditText(mEditText);
//            mEmotionKeyboard.bindToEditText(mEditText);
        }
        return rootView;
    }


    //    @BindView(R.id.iv_keyboard)
//    ImageView ivKeyboard;
//    @BindView(R.id.iv_topic)
//    ImageView ivTopic;
//    @BindView(R.id.iv_picture)
//    ImageView ivPicture;
//    @BindView(R.id.iv_face)
//    ImageView ivFace;
//    @BindView(R.id.ll_keyboard_content)
//    LinearLayout llKeyboardContent;
//    @BindView(R.id.vp_emotionview_layout)
    CustomViewPager vpEmotionviewLayout;
//    @BindView(R.id.recyclerview_horizontal)
//    RecyclerView recyclerviewHorizontal;
//    @BindView(R.id.ll_emotion_layout)
//    LinearLayout llEmotionLayout;

    private void initView(View rootView) {
        ImageView ivKeyboard = rootView.findViewById(R.id.iv_keyboard);
        ImageView ivTopic = rootView.findViewById(R.id.iv_topic);
        ImageView ivPicture = rootView.findViewById(R.id.iv_picture);
//        ImageView ivFace = rootView.findViewById(R.id.iv_face);
        recyclerviewHorizontal = rootView.findViewById(R.id.recyclerview_horizontal);
        vpEmotionviewLayout = rootView.findViewById(R.id.vp_emotionview_layout);

//        viewPager= (NoHorizontalScrollerViewPager) rootView.findViewById(R.id.vp_emotionview_layout);
//        recyclerview_horizontal= (RecyclerView) rootView.findViewById(R.id.recyclerview_horizontal);
//        bar_edit_text= (EditText) rootView.findViewById(R.id.bar_edit_text);
//        bar_image_add_btn= (ImageView) rootView.findViewById(R.id.bar_image_add_btn);
//        bar_btn_send= (Button) rootView.findViewById(R.id.bar_btn_send);
//        rl_editbar_bg= (LinearLayout) rootView.findViewById(R.id.rl_editbar_bg);
    }


//    @Override
//    public int getViewId() {
//        return R.layout.fragment_main_emotion;
//    }
//
//    @Override
//    protected void afterViews() {

//        Bundle params = getBundleParams();
//
//
//        isHidenBarEditTextAndBtn = params.getBoolean(EmotionMainFragment.HIDE_BAR_EDITTEXT_AND_BTN);
//        //获取判断绑定对象的参数
//        isBindToBarEditText = params.getBoolean(EmotionMainFragment.BIND_TO_EDITTEXT);


//        mEmotionKeyboard = EmotionKeyboard.with(getActivity())
//                .setEmotionView(llEmotionLayout)//绑定表情面板
//                .bindToContent(contentView)//绑定内容view
//                .bindToEditText(mEditText)//判断绑定那种EditView
//                .bindToEmotionButton(ivFace)//绑定表情按钮
//                .build();
//        initDatas();
//
//        //创建全局监听
//        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());
//
//        if (isBindToBarEditText) {
//            //绑定当前Bar的编辑框
//            globalOnItemClickManager.attachToEditText(mEditText);
//        } else {
//            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
//            globalOnItemClickManager.attachToEditText(mEditText);
//            mEmotionKeyboard.bindToEditText(mEditText);
//        }

//        KeyBoardUtils.toggleInput(getContext());
//    }


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
//        Bundle b = null;
//        for (int i = 0; i < 7; i++) {
//            b = new Bundle();
//            b.putString("Interge", "Fragment-" + i);
//            Fragment1 fg = Fragment1.newInstance(Fragment1.class, b);
//            fragments.add(fg);
//        }

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


    @OnClick({R.id.iv_keyboard, R.id.iv_topic, R.id.iv_picture/*, R.id.iv_face*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_keyboard:
//                if (isKeyboard) {
//                    hideInput();
//                } else {
//                    showInput();
//                }
//                KeyBoardUtils.toggleInput(getContext());
//                setWindowSoftInputMode(!isKeyboard);
//                if (mEmotionKeyboard.isSoftInputShown()) {
//                    //隐藏
////                    mEmotionKeyboard.showEmotionLayout();
//                    mEmotionKeyboard.hideEmotionLayout(false);
//                    mEmotionKeyboard.hideSoftInput();
//                } else {
//                    //打开
////                    mEmotionKeyboard.hideEmotionLayout(false);
//                    mEmotionKeyboard.lockContentHeight();//显示软件盘时，锁定内容高度，防止跳闪。
//                    mEmotionKeyboard.hideEmotionLayout(true);//隐藏表情布局，显示软件盘
//                }
//                KeyBoardUtils.toggleInput(getContext());
                break;
            case R.id.iv_topic:
                break;
            case R.id.iv_picture:
                break;
//            case R.id.iv_face:
//
//                break;
        }
    }
}
