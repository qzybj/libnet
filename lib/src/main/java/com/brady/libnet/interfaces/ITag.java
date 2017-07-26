package com.brady.libnet.interfaces;


import com.brady.libnet.config.NetConstants;

/**
 * Created by ZhangYuanBo on 2016/9/5.
 */
public interface ITag {
    int getRequstCode();
    @NetConstants.LoadingType
    int getLoadingType();
    long getRequstTime();
}
