package com.marksixinfo.base.module;

import com.marksixinfo.bean.ResultData;

public interface IApiNetMode<T> {
     ResultData<T> parseNetworkResponse(String response, int id);
}
