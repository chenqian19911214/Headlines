package com.marksixinfo.utils;

import android.content.Context;
import android.util.Log;

import com.marksixinfo.R;

import java.io.File;
import java.text.DecimalFormat;

/**
 * @Auther: Administrator
 * @Date: 2019/4/17 0017 16:32
 * @Description:
 */
public class DataCleanManager {
    /**
     * 清除本应用外部sd卡部缓存(/data/data/com.xxx.xxx/cache) * * @param context
     */
    public static String getInternalCache(Context context) {
        return getFilesByDirectory(getExternalCacheDirectory(context.getApplicationContext()), context);
    }

    /**
     * 获取外部缓存文件
     *
     * @param context
     * @return
     */
    public static File getExternalCacheDirectory(Context context) {
        File cacheDirectory = null;
        try {
            String diskName = context.getString(R.string.markSixImageCache);
//            cacheDirectory = context.getExternalCacheDir();
            cacheDirectory = context.getCacheDir();
            return new File(cacheDirectory, diskName);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("cacheDirectory目录为空");
        }
        return cacheDirectory;
    }


    /**
     * 获取外部缓存目录
     *
     * @param context
     * @return
     */
    public static String getExternalCacheDir(Context context) {
        String cacheDirectory = "";
        try {
            cacheDirectory = getExternalCacheDirectory(context).getAbsolutePath();
            return cacheDirectory;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d("cacheDirectory目录为空");
        }
        return cacheDirectory;
    }


    private static String getFilesByDirectory(File file, Context context) {
        long blockSize = 0;
        if (!file.exists()) {
            file = new File(context.getString(R.string.markSixImageCache));
        }
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小", "获取失败!");
        }
        return FormetFileSize(blockSize);
    }


    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */

    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }


    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */

    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}

