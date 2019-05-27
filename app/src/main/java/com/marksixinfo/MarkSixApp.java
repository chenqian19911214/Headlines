package com.marksixinfo;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.base.WebSocketUtils;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.widgets.MyClassicsFooter;
import com.marksixinfo.widgets.MyClassicsHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshInitializer;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.interfaces.BetaPatchListener;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;
import com.zzhoujay.richtext.RichText;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import me.jessyan.autosize.AutoSizeConfig;

import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 16:10
 * @Description:
 */
public class MarkSixApp extends Application {

    private static final String TAG = "MarkSixApp";
    public static boolean isDebug = BuildConfig.DEBUG;
    private static MarkSixApp instance = null;

    private static final String UMengKey = "5c95d7ed61f564f5e90009d0";
    private static final String UMengMessageSecret = "35f68cc52bf073e7e75d8fa837d8f69e";
    private static final String UMengChannel = "umeng";//渠道

    private static final String QQAppId = "1108873890";
    private static final String QQAppKey = "fofJVkuk90VPb7Cb";

    private static String BUGLY_ID;
    // 注意：在微信授权的时候，必须传递appSecret
    private static final String WXAppId = "wx4311900ef003bf6d";
    private static final String WXAppSecret = "bc96c0117f164fcc59770abc5ecae6a2";

    public static MarkSixApp getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        initBuglyUpdate();
        initBugly();
    }

    private void init() {
        instance = this;
        LogUtils.setDebugMode(isDebug);
        BaseNetUtil.init();
        ClientInfo.init(getApplicationContext());
        iniUMShare();
        RichText.initCacheDir(getApplicationContext());
        initAutoSizeConfig();
        closeAndroidPDialog();
        WebSocketUtils.init(this);

        if (isDebug)
            BUGLY_ID = "2b30175ead";
        else
            BUGLY_ID = "5e01abe88f";

//        DensityUtils.setDensity(this);
//        RichText.debugMode = true;
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
    }

    private void initBugly() {
        Context context = getApplicationContext();

         //在开发测试阶段，可以在初始化Bugly之前通过以下接口把调试设备设置成“开发设备”。
         CrashReport.setIsDevelopmentDevice(context, true);

        // 获取当前包名
        // 获取当前进程名
        // 设置是否为上报进程
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(context);


        strategy.setAppChannel(""); //渠道名
        strategy.setAppVersion(ClientInfo.getVersionCode(context) + ""); //版本号
        strategy.setAppPackageName("com.marksixinfo");
        strategy.setAppReportDelay(5000);   //Bugly会在启动10s后联网同步数据。若您有特别需求，可以修改这个时间
        /*
        *
        * 第三个参数为SDK调试模式开关，调试模式的行为特性如下：

           输出详细的Bugly SDK的Log；
           每一条Crash都会被立即上报；
           自定义日志将会在Logcat中输出。
          建议在测试阶段建议设置成true，发布时设置为false。
        * */
        CrashReport.initCrashReport(this, BUGLY_ID, isDebug, strategy);

    }



    private void initBuglyUpdate() {
        Beta.enableHotfix = true;
        // 设置是否自动下载补丁，默认为true
        Beta.canAutoDownloadPatch = true;
        // 设置是否自动合成补丁，默认为true
        Beta.canAutoPatch = true;
        // 设置是否提示用户重启，默认为false
        Beta.canNotifyUserRestart = true;
        // 补丁回调接口
       // Beta.upgradeDialogLayoutId = R.layout.upgrade_dialog;

        Beta.betaPatchListener = new BetaPatchListener() {
            @Override
            public void onPatchReceived(String patchFile) {
             //   Toast.makeText(getApplication(), "补丁下载地址" + patchFile, Toast.LENGTH_SHORT).show();

           //     Log.i("chenqian:","补丁下载地址:"+patchFile);


            }

            @Override
            public void onDownloadReceived(long savedLength, long totalLength) {


               /* Log.i("chenqian:", String.format(Locale.getDefault(), "%s %d%%",
                        Beta.strNotificationDownloading,
                        (int) (totalLength == 0 ? 0 : savedLength * 100 / totalLength)));*/

            }

            @Override
            public void onDownloadSuccess(String msg) {
             /*   Toast.makeText(getApplication(), "补丁下载成功", Toast.LENGTH_SHORT).show();
                Log.i("chenqian:","补丁下载成功");*/

            }

            @Override
            public void onDownloadFailure(String msg) {
               /* Toast.makeText(getApplication(), "补丁下载失败", Toast.LENGTH_SHORT).show();
                Log.i("chenqian:","补丁下载失败:");*/

            }

            @Override
            public void onApplySuccess(String msg) {
             /*   Toast.makeText(getApplication(), "补丁应用成功", Toast.LENGTH_SHORT).show();
                Log.i("chenqian:","补丁应用成功");*/

            }

            @Override
            public void onApplyFailure(String msg) {
               /* Toast.makeText(getApplication(), "补丁应用失败", Toast.LENGTH_SHORT).show();
                Log.i("chenqian:","补丁应用失败");*/

            }

            @Override
            public void onPatchRollback() {

            }
        };

        Bugly.init(this, BUGLY_ID, isDebug);
    }

    private void initAutoSizeConfig() {
        AutoSizeConfig autoSizeConfig = AutoSizeConfig.getInstance();
        autoSizeConfig.setCustomFragment(true);
        autoSizeConfig.setExcludeFontScale(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    private void iniUMShare() {
        UMConfigure.init(this, UMengKey, UMengChannel, UMConfigure.DEVICE_TYPE_PHONE, UMengMessageSecret);
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        QueuedWork.isUseThreadPool = false;
        PlatformConfig.setWeixin(WXAppId, WXAppSecret);
        PlatformConfig.setQQZone(QQAppId, QQAppKey);
        UMShareAPI.get(getApplication());

        MobclickAgent.setScenarioType(mContext, MobclickAgent.EScenarioType.E_UM_NORMAL);

//        PushUtils.initPushWithApplication(getApplication());

    }

    /**
     * Android P 非公共API弹框关闭
     */
    private void closeAndroidPDialog() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            try {
                Class aClass = Class.forName("android.content.pm.PackageParser$Package");
                Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
                declaredConstructor.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class cls = Class.forName("android.app.ActivityThread");
                Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
                declaredMethod.setAccessible(true);
                Object activityThread = declaredMethod.invoke(null);
                Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
                mHiddenApiWarningShown.setAccessible(true);
                mHiddenApiWarningShown.setBoolean(activityThread, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局默认配置（优先级最低，会被其他设置覆盖）
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                //全局设置（优先级最低）
                layout.setPrimaryColorsId(R.color.main_bg, R.color.grey_999);//全局设置主题颜色
            }
        });
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new MyClassicsHeader(context)
                        .setTextSizeTitle(12)
                        .setEnableLastTime(false)
                        .setDrawableArrowSize(12)
                        .setDrawableProgressSize(12)
                        .setDrawableMarginRight(10)
                        .setSpinnerStyle(SpinnerStyle.Scale);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                return new MyClassicsFooter(context)
                        .setTextSizeTitle(12)
                        .setFinishDuration(0)
                        .setDrawableArrowSize(12)
                        .setDrawableProgressSize(12)
                        .setDrawableMarginRight(10)
                        .setSpinnerStyle(SpinnerStyle.Scale);
            }
        });
    }
}
