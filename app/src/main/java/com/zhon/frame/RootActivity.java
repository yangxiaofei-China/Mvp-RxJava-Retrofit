package com.zhon.frame;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zhon.frame.mvp.test.activity.TestActivity;

/*
 * 项目名:    Mvp-RxJava-Retrofit
 * 包名       com.zhon.frame
 * 文件名:    RootActivity
 * 创建者:    ZJB
 * 创建时间:  2017/9/8 on 11:41
 * 描述:     TODO 闪屏页面
 */
public class RootActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_root);
        startActivity(new Intent(this, TestActivity.class));
    }
}
