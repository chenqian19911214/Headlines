package com.marksixinfo.widgets.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.marksixinfo.R;
import com.marksixinfo.widgets.bigimageViewpage.glide.sunfusheng.progress.ProgressManager;

import java.io.InputStream;

import androidx.annotation.NonNull;
import okhttp3.OkHttpClient;

@GlideModule
public final class OkHttpGlideModule extends AppGlideModule {

    int diskSize = 1024 * 1024 * 500;  //500mb

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        //定义图片的本地磁盘缓存
        //内部
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                context.getString(R.string.markSixImageCache), diskSize));

        // 定义图片格式
//        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认
        // 自定义内存和图片池大小
//        builder.setMemoryCache(new LruResourceCache(memorySize));
//        builder.setBitmapPool(new LruBitmapPool(memorySize));
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        super.registerComponents(context, glide, registry);
        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
//         添加拦截器到 Glide
//        OkHttpClient.Builder builder = new OkHttpClient.Builder();
//        builder.addInterceptor(new ProgressInterceptor());
//        OkHttpClient okHttpClient = builder.build();
        // 替换底层网络框架为okhttp3，这步很重要！
        // 如果您的app中已经存在了自定义的GlideModule，您只需要把这一行代码，添加到对应的重载方法中即可。
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(ProgressManager.getOkHttpClient()));
//        registry.replace(GlideUrl.class, InputStream.class,  new OkHttpUrlLoader.Factory(client));
    }
}
