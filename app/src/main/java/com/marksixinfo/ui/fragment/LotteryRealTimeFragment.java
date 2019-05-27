package com.marksixinfo.ui.fragment;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixFragment;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.LotteryNumberConfigData;
import com.marksixinfo.bean.LotteryRealTimeData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LotteryRealTimeEvent;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.LotteryRealTimeNumber;
import com.marksixinfo.widgets.LotteryRealTimeTimeDownView;
import com.marksixinfo.widgets.ScratchView;
import com.marksixinfo.widgets.SpannableStringUtils;
import com.marksixinfo.widgets.SwitchButton;
import com.marksixinfo.widgets.TimeDownView;
import com.marksixinfo.widgets.glide.GlideUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 实时开奖
 *
 * @Auther: Administrator
 * @Date: 2019/5/18 0018 18:30
 * @Description:
 */
public class LotteryRealTimeFragment extends MarkSixFragment {


    @BindView(R.id.tv_period_number_name)
    TextView tvPeriodNumberName;
    @BindView(R.id.lotteryRealTimeNumber)
    LotteryRealTimeNumber lotteryRealTimeNumber;
    @BindView(R.id.tv_current_number)
    TextView tvCurrentNumber;
    @BindView(R.id.iv_start)
    ImageView ivStart;
    @BindView(R.id.iv_lottery_ing)
    ImageView ivLotteryIng;
    @BindView(R.id.iv_lottery_result_bg)
    ImageView ivLotteryResultBg;
    @BindView(R.id.tv_lottery_result)
    TextView tvLotteryResult;
    @BindView(R.id.rl_lottery_result)
    RelativeLayout rlLotteryResult;
    @BindView(R.id.timeDownView)
    LotteryRealTimeTimeDownView timeDownView;
    @BindView(R.id.ll_lottery_start)
    LinearLayout llLotteryStart;
    @BindView(R.id.tv_shengxiao)
    TextView tvShengxiao;
    @BindView(R.id.tv_daxiao)
    TextView tvDaxiao;
    @BindView(R.id.tv_wuxing)
    TextView tvWuxing;
    @BindView(R.id.tv_jiaye)
    TextView tvJiaye;
    @BindView(R.id.tv_danshuang)
    TextView tvDanshuang;
    @BindView(R.id.ll_lottery_detail)
    LinearLayout llLotteryDetail;
    @BindView(R.id.tv_lottery_ing)
    TextView tvLotteryIng;
    @BindView(R.id.scratchView)
    ScratchView scratchView;
    @BindView(R.id.SwitchButton_lottery)
    SwitchButton SwitchButtonLottery;

    private SucceedCallBackListener listener;
    private int current = 1;
    private String[] myNumbers = new String[]{"一", "二", "三", "四", "五", "六"};
    private String[] scoreText = {"   ", ".  ", ".. ", "..."};
    private int pauseTime = 5;//开出球停顿时间, 秒
    private int overTime = 36000000;//开奖结束,页面停留时间, 毫秒
    private int type;
    private boolean isScratch = true;//是否需要刮奖
    private List<String> lottery;

    @Override
    public int getViewId() {
        return R.layout.fragment_lottery_real_time;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            LotteryRealTimeEvent data = bundle.getParcelable(StringConstants.LOTTERY_REAL_TIME);
            if (data != null) {
                //0,开奖结束   1,准备开奖    2,开奖中
                type = data.getType();
                LotteryRealTimeData message = data.getMessage();
                if (message != null) {
                    String period = message.getPeriod();
                    tvPeriodNumberName.setText(CommonUtils.StringNotNull(period) ? "第" + period + "期" : "");
                }
                GlideUtil.loadImage(getContext(), R.drawable.gif_lottery_animator, ivLotteryIng, R.drawable.toumingtu_head);
                setTvLotteryIngAnimator();
                if (type == 1) {
                    long showTime = data.getShowTime();
                    if (showTime > 0) {
                        ivStart.setVisibility(View.VISIBLE);
                        llLotteryStart.setVisibility(View.VISIBLE);
                        ivLotteryIng.setVisibility(View.INVISIBLE);
                        tvLotteryIng.setVisibility(View.INVISIBLE);
                        timeDownView.start(showTime);
                        timeDownView.setOnTimeEndListener(new TimeDownView.OnTimeEndListener() {
                            @Override
                            public void onEnd() {
                                if (type == 1) {
                                    startLottery();
                                }
                            }
                        });
                    }
                } else if (type == 2) {
                    if (message != null) {
                        List<String> lottery = message.getLottery();
                        setIvLotteryIng(lottery);
                    }
                } else {//结束
                    if (listener != null) {
                        listener.succeedCallBack(null);
                    }
                }
            }
        }

        SwitchButtonLottery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isScratch = isChecked;
            }
        });
    }

    /**
     * 进入开奖
     */
    private void startLottery() {
        llLotteryStart.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        llLotteryDetail.setVisibility(View.INVISIBLE);
        ivLotteryIng.setVisibility(View.VISIBLE);
        tvLotteryIng.setVisibility(View.VISIBLE);
        tvCurrentNumber.setText("第一球");
    }

    /**
     * 设置开奖
     *
     * @param lottery
     */
    private void setIvLotteryIng(List<String> lottery) {
        this.lottery = lottery;
        scratchView.setVisibility(View.INVISIBLE);
        if (CommonUtils.ListNotNull(lottery)) {
            if (type == 1) {
                startLottery();
            }
            type = 2;
            //当前正在开的球
            current = lottery.size();

            if (current >= 6) {//第6球开始,不允许点击特码咪
                SwitchButtonLottery.setClickable(false);
            }
            if (current >= 7) {
                setTemaData();
            } else {
                tvCurrentNumber.setText("第" + myNumbers[current - 1] + "球");
            }

            String number = lottery.get(lottery.size() - 1);

            int i = NumberUtils.stringToInt(number);
            if (i > 0) {
                LotteryNumberConfigData data = ClientInfo.lotteryConfig.get(i - 1);
                if (data != null) {
                    String tema = data.getTema();
                    String color = data.getColor();
                    String bose = data.getBose();
                    String shengxiao = data.getShengxiao();
                    String daxiao = data.getDaxiao();
                    String wuxing = data.getWuxing();
                    String jiaye = data.getJiaye();
                    String danshuang = data.getDanshuang();

                    ivLotteryIng.setVisibility(View.INVISIBLE);
                    tvLotteryIng.setVisibility(View.INVISIBLE);
                    rlLotteryResult.setVisibility(View.VISIBLE);
                    llLotteryDetail.setVisibility(View.VISIBLE);

                    tvLotteryResult.setText(tema);

                    ivLotteryResultBg.setImageResource(getNumberBg(bose));

                    setDetailTextColor(color);
                    tvShengxiao.setText("生肖：" + shengxiao);
                    tvDaxiao.setText("大小：" + daxiao);
                    tvWuxing.setText("五行：" + wuxing);
                    tvJiaye.setText("家禽野兽：" + jiaye);
                    tvDanshuang.setText("单双：" + danshuang);

                    if (current < 7) {
                        lotteryRealTimeNumber.setData(current - 1, lottery);
                    } else {
                        if (!isScratch) {//非刮奖直接显示
                            lotteryRealTimeNumber.setData(current - 1, lottery);
                        } else {
                            lotteryRealTimeNumber.setData(current - 2, lottery);
                        }
                    }

                    tvDanshuang.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (current < 7) {
                                ivLotteryIng.setVisibility(View.VISIBLE);
                                tvLotteryIng.setVisibility(View.VISIBLE);
                                rlLotteryResult.setVisibility(View.INVISIBLE);
                                llLotteryDetail.setVisibility(View.INVISIBLE);
                                String string = "";
                                if (current == 6) {
                                    string = "特码";
                                } else {
                                    string = "第" + (myNumbers[current]) + "球";
                                }
                                tvCurrentNumber.setText(string);
                            }
                        }
                    }, pauseTime * 1000);
                }
            }
        } else {
            startLottery();
        }
    }

    /**
     * 设置特码
     */
    private void setTemaData() {
        if (isScratch) {//需要刮奖
            scratchView.setVisibility(View.VISIBLE);
            SpannableStringUtils sp = new SpannableStringUtils();
            sp.addText(20, 0xffE61E27, "特码");
            sp.addText(20, 0xff333333, "已开，请手动刮奖");
            tvCurrentNumber.setText(sp.toSpannableString());
            scratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
                boolean isClear = false;

                @Override
                public void onProgress(int percent) {
                    LogUtils.d(percent + "");
                    if (percent >= 75 && !isClear) {
                        isClear = true;
                        scratchView.clear();
                        tvCurrentNumber.setText("特码");
                        lotteryRealTimeNumber.setData(current - 1, lottery);
                        scratchView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isClear = false;
                                scratchView.setVisibility(View.INVISIBLE);
                                overLottery();
                            }
                        }, 1000);
                    }
                }

                @Override
                public void onCompleted(View view) {
                }
            });
            //开奖结束,一小时内未刮奖关闭
            tvCurrentNumber.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.succeedCallBack(null);
                    }
                }
            }, overTime);
        } else {
            tvCurrentNumber.setText("特码");
            overLottery();
        }
    }

    /**
     * 结束开奖
     */
    private void overLottery() {
        if (tvCurrentNumber != null) {
            tvCurrentNumber.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (listener != null) {
                        listener.succeedCallBack(null);
                    }
                }
            }, 5000);
            toast("开奖结束,亲~中奖了吗");
            SPUtil.setStringValue(getContext(), SPUtil.LOTTERY_CURRENT, "");
        }
    }

    /**
     * 开奖推送
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LotteryRealTimeEvent event) {
        if (event != null) {
            LotteryRealTimeData data = event.getMessage();
            if (data != null) {
                String period = data.getPeriod();
                tvPeriodNumberName.setText(CommonUtils.StringNotNull(period) ? "第" + period + "期" : "");
                List<String> lottery = data.getLottery();
                setIvLotteryIng(lottery);
                SPUtil.setStringValue(getContext(), SPUtil.LOTTERY_CURRENT, lottery != null && lottery.size()
                        > 0 && lottery.size() < 7 ? JSONUtils.toJson(event) : "");
            } else {
                SPUtil.setStringValue(getContext(), SPUtil.LOTTERY_CURRENT, "");
            }
        }
    }

    public void setListener(SucceedCallBackListener listener) {
        this.listener = listener;
    }


    /**
     * 波色获取开奖球背景
     *
     * @param s
     * @return
     */
    private int getNumberBg(String s) {
        int bg = R.drawable.icon_lottery_red;
        if (s.contains("蓝")) {
            bg = R.drawable.icon_lottery_blue;
        } else if (s.contains("绿")) {
            bg = R.drawable.icon_lottery_green;
        }
        return bg;
    }

    /**
     * 波色设置开奖详情文字颜色
     *
     * @param color
     * @return
     */
    private void setDetailTextColor(String color) {
        if (CommonUtils.StringNotNull(color)) {
            tvShengxiao.setTextColor(Color.parseColor(color));
            tvDaxiao.setTextColor(Color.parseColor(color));
            tvWuxing.setTextColor(Color.parseColor(color));
            tvJiaye.setTextColor(Color.parseColor(color));
            tvDanshuang.setTextColor(Color.parseColor(color));
        }
    }

    /**
     * 设置等待中动画
     */
    private void setTvLotteryIngAnimator() {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 4).setDuration(2000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int i = (int) animation.getAnimatedValue();
                tvLotteryIng.setText("正在开奖，请稍等" + scoreText[i % scoreText.length]);
            }
        });
        valueAnimator.start();
    }


    @OnClick(R.id.ll_root_content)
    public void onViewClicked() {//避免点击事件穿透
    }
}
