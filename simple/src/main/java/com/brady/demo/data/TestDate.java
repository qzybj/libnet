package com.brady.demo.data;

import com.qzybj.libutil.data.ListUtil;
import com.qzybj.libutil.data.RandomUtil;
import com.qzybj.libutil.test.TestDataBuilder;

/**
 * Created by Clair
 *
 * @date 2017/5/16
 * @description
 */
public class TestDate extends TestDataBuilder {
    @Override
    protected void init() {

    }
    public static String getImgUrl() {
        return ListUtil.isEmptyArray(imageUrls)?null:imageUrls[RandomUtil.getRandom(imageUrls.length - 1)];
    }
}
