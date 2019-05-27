package com.marksixinfo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;

import com.marksixinfo.bean.GalleryListData;
import com.marksixinfo.evenbus.GalleryDataEvent;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.glide.GlideUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 图库列表数据处理
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 15:06
 * @Description:
 */
public class GalleryDataService extends IntentService {

    public GalleryDataService() {
        super("");
    }

    public static void startService(Context context, List<GalleryListData.ChildBean> datas, int type) {
        Intent intent = new Intent(context, GalleryDataService.class);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) datas);
        intent.putExtra("type", type);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        List<GalleryListData.ChildBean> datas = intent.getParcelableArrayListExtra("data");
        int type = intent.getIntExtra("type", 2);
        try {
            handleGirlItemData(datas, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleGirlItemData(List<GalleryListData.ChildBean> datas, int type) {
        if (datas.size() == 0) {
            EventBusUtil.post(null);
            return;
        }
        for (GalleryListData.ChildBean data : datas) {
            Bitmap bitmap = GlideUtil.load(this, data.getPic());
            if (bitmap != null) {
                data.setWidth(bitmap.getWidth()+"");
                data.setHeight(bitmap.getHeight()+"");
            }
        }
        EventBus.getDefault().post(new GalleryDataEvent(datas, type));
    }
}
