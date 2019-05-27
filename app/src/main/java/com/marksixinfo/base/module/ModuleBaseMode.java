package com.marksixinfo.base.module;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.utils.CommonUtils;

import org.json.JSONObject;

import java.util.ArrayList;

import ikidou.reflect.TypeBuilder;

/**
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 14:30
 * @Description:
 */
public class ModuleBaseMode implements IApiNetListItemMode<ModuleBaseMode> {


    String key;
    long id;

    protected long position;


    @Override
    public ModuleBaseMode parseNetworkResponse(String response, int id) {
        try {
            JSONObject j = new JSONObject(response);
            setKey(j.optString("key"));
            setId(j.optLong("id"));
            if (CommonUtils.StringNotNull(getKey())) {
                return conversionMode(getKey(), response, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected ModuleBaseMode conversionMode(String key, String response, int id) {
        Class<? extends ModuleBaseMode> cmsModeClass = ModuleBaseUtils.getCMSModeClass(key);
        if (cmsModeClass != null) {
            TypeBuilder typeBuilder = TypeBuilder.newInstance(cmsModeClass);
            return BaseNetUtil.parseFromJson(response, typeBuilder.build());
        }
        return null;
    }

    /**
     * 当后台数据需要多个ViewHolder时，进行二次处理
     *
     * @param list
     */
    public void loadData(ArrayList<ModuleBaseMode> list) {
        list.add(this);
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
