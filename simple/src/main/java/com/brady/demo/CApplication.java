package com.brady.demo;

import android.app.Application;

import com.qzybj.libraryimageload.LoadImageManager;
import com.qzybj.libutil.UtilsManager;

/**
 * Created by Clair
 *
 * @date 2017/5/16
 * @description
 */
public class CApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        LoadImageManager.init(this);
        UtilsManager.init(this);
    }
}
