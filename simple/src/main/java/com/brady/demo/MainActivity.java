package com.brady.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.brady.demo.data.TestDate;
import com.qzybj.demo.R;
import com.qzybj.libraryimageload.LoadImageManager;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.iv_show)
    public ImageView iv_show;
    @BindView(R.id.iv_show1)
    public ImageView iv_show1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        initData();
    }

    private void initUI() {
        iv_show.setImageResource(R.mipmap.ic_launcher);
        iv_show1.setImageResource(R.mipmap.ic_launcher);
        LoadImageManager.instance().
                loadImage(iv_show, TestDate.getImgUrl(),R.mipmap.ic_launcher);
        LoadImageManager.instance().
                loadImage(iv_show1, TestDate.getImgUrl(),R.mipmap.ic_launcher);
    }
    private void initData() {

    }


}
