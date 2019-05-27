package com.marksixinfo.base.module;

public interface IApiNetListItemMode <T> {
    T parseNetworkResponse(String response, int id);
}
