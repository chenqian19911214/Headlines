package com.marksixinfo.base;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.marksixinfo.bean.ResultData;

import java.util.ArrayList;

import ikidou.reflect.TypeBuilder;
import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:24
 * @Description:
 */
public abstract class MarkSixNetCallBack<T> extends CallbackBase<T> {

    String resultString;
    Class tType;

    public MarkSixNetCallBack(IBaseView ui, Class tType) {
        super(ui);
        this.tType = tType;
    }


    /**
     * Thread Pool Thread
     *
     * @param response
     * @param id
     */

    @Override
    public ResultData<T> parseNetworkResponse(Response response, int id) throws Exception {
        if (response != null && response.body() != null) {
            HttpUrl httpUrl = response.request().url();
            String url = "";
            if (response.request().url() != null) {
                url = httpUrl.url().toString();
            }

            resultString = response.body().string();
            //JSONObject s=new JSONObject(getResultString());
            JsonObject s = new JsonParser().parse(getResultString()).getAsJsonObject();

            ResultData<T> resultData = BaseNetUtil.parseFromJson(s.toString(), new TypeToken<ResultData<T>>() {
            }.getType());


//            JsonObject jsonObject = null;
            JsonArray jsonArray = null;
            if (s.has("result")) {
                if (s.get("result").isJsonArray()) {
                    jsonArray = s.getAsJsonArray("result");
                    s.remove("result");
                    if (jsonArray != null && resultData != null) {
                        resultData.setData(BaseNetUtil.parseFromJson(jsonArray.toString(),
                                TypeBuilder.newInstance(ArrayList.class).addTypeParam(tType).build()));
                    }
                } else {
//                    jsonObject = s.getAsJsonObject("result");
//                    s.remove("result");
                    JsonElement result = s.remove("result");
                    if (result != null && resultData != null) {
                        resultData.setData(BaseNetUtil.parseFromJson(result.toString(), TypeBuilder.newInstance(tType).build()));
                    }
                }
            }
            return resultData;
        }

        return null;

    }

    public String getResultString() {
        return resultString;
    }

}
