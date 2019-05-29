package com.marksixinfo.ui.fragment;

import android.animation.ValueAnimator;
import android.app.Activity;
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
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.LotteryCountDownData;
import com.marksixinfo.bean.LotteryNumberConfigData;
import com.marksixinfo.bean.LotteryRealTimeData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LotteryRealTimeEvent;
import com.marksixinfo.evenbus.LotteryRealTimeOverEvent;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.LotteryImpl;
import com.marksixinfo.ui.activity.MainActivity;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
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
    private int overTime = 600;//开奖结束,页面停留时间, 秒
    private int type;
    private boolean isScratch = true;//是否需要刮奖
    private List<String> lottery;
    private ValueAnimator valueAnimator;

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
                setData(data);
            }
        }

        SwitchButtonLottery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isScratch = isChecked;
            }
        });
    }

    private void setData(LotteryRealTimeEvent data) {
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
            setShowTimeStatus();
            setShowTime(showTime);
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


    /**
     * 进入开奖
     */
    private void startLottery() {
        scratchView.setVisibility(View.INVISIBLE);
        rlLotteryResult.setVisibility(View.INVISIBLE);
        llLotteryStart.setVisibility(View.INVISIBLE);
        ivStart.setVisibility(View.INVISIBLE);
        llLotteryDetail.setVisibility(View.INVISIBLE);
        ivLotteryIng.setVisibility(View.VISIBLE);
        tvLotteryIng.setVisibility(View.VISIBLE);
        tvCurrentNumber.setVisibility(View.VISIBLE);
        tvCurrentNumber.setText("第一球");
    }

    /**
     * 设置倒计时
     *
     * @param showTime
     */
    private void setShowTime(long showTime) {
        if (showTime > 0) {
            timeDownView.stop();
            timeDownView.start(showTime);
            timeDownView.setOnTimeEndListener(new TimeDownView.OnTimeEndListener() {
                @Override
                public void onEnd() {
                    startLottery();
                }
            });
        } else {
            startLottery();
        }
    }

    /**
     * 设置倒计时状态
     */
    private void setShowTimeStatus() {
        if (type != 1) {
            type = 1;
        }
        timeDownView.setStopTimeInit();
        ivStart.setVisibility(View.VISIBLE);
        scratchView.setVisibility(View.INVISIBLE);
        tvCurrentNumber.setVisibility(View.INVISIBLE);
        llLotteryStart.setVisibility(View.VISIBLE);
        rlLotteryResult.setVisibility(View.INVISIBLE);
        ivLotteryIng.setVisibility(View.INVISIBLE);
        llLotteryDetail.setVisibility(View.INVISIBLE);
        tvLotteryIng.setVisibility(View.INVISIBLE);
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
                            if (current < 7 && type == 2) {
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
            scratchView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scratchView.reset();
                }
            }, 200);
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
            }, overTime * 1000);
        } else {
            tvCurrentNumber.setText("特码");
            overLottery();
        }
        //实时开奖结束通知
        EventBusUtil.post(new LotteryRealTimeOverEvent());
    }

    /**
     * 结束开奖
     */
    private void overLottery() {
        if (tvCurrentNumber != null) {
            tvCurrentNumber.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (current < 7) {//刮奖结束重新开奖
                        return;
                    }
                    if (listener != null) {
                        listener.succeedCallBack(null);
                    }
                }
            }, 4000);
            //开奖页面才提示
            Activity currentActivity = ActivityManager.getActivityManager().getCurrentActivity();
            if (currentActivity != null && MainActivity.class.equals(currentActivity.getClass())) {
                if (((MainActivity) currentActivity).getPosition() == 2) {
                    toast("开奖结束,亲~中奖了吗");
                }
            }
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
                setPeriodText(period);
                int isOpen = data.getIsOpen();
                setCurrentLotteryType(isOpen, event);
            } else {
                SPUtil.setStringValue(getContext(), SPUtil.LOTTERY_CURRENT, "");
            }
        }
    }

    /**
     * 设置开奖期数显示
     *
     * @param period
     */
    private void setPeriodText(String period) {
        if (tvPeriodNumberName != null) {
            tvPeriodNumberName.setText(CommonUtils.StringNotNull(period) ? "第" + period + "期" : "");
        }
    }

    /**
     * 获取距离开奖秒数
     */
    public void getCountDown() {
        new LotteryImpl(new MarkSixNetCallBack<LotteryCountDownData>(this, LotteryCountDownData.class) {
            @Override
            public void onSuccess(LotteryCountDownData response, int id) {
                setCountDown(response);
            }
        }.setNeedDialog(false)).getCountdown();
    }


    /**
     * 重新开奖获取倒计时
     *
     * @param response
     */
    private void setCountDown(LotteryCountDownData response) {
        if (response != null) {
            long now = response.getNow();
            long time = CommonUtils.getTodayNineTime(now);
            setShowTime(time);
        }
    }

    /**
     * 设置当前开奖type
     *
     * @param type 0,晚上9点20 后台开始重置  1,准备开奖,弹框提醒  2,开球中   3,开奖结束
     * @param data
     */
    private void setCurrentLotteryType(int type, LotteryRealTimeEvent data) {
        switch (type) {
            case 0://重置,倒计时
                SwitchButtonLottery.setChecked(true);
                reSetNumberDataStatus();
                setShowTimeStatus();
                getCountDown();
                break;
            case 1://弹框提醒,准备开球,如果有球,直接开球
                ((MainActivity) getActivity()).setRemindDialog();
            case 2://开球中
                data.setType(2);
                LotteryRealTimeData message = data.getMessage();
                List<String> lottery = message.getLottery();
                setIvLotteryIng(lottery);
                SPUtil.setStringValue(getContext(), SPUtil.LOTTERY_CURRENT, JSONUtils.toJson(data));
                break;
            case 3://开奖结束
                current = 7;
                overLottery();
                break;
        }
    }

    /**
     * 重置号码状态
     */
    private void reSetNumberDataStatus() {
        if (lotteryRealTimeNumber != null) {
            lotteryRealTimeNumber.reSetStatus();
        }
        SwitchButtonLottery.setClickable(true);
        timeDownView.stop();
        current = 0;
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
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofInt(0, 4).setDuration(2000);
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
