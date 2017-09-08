package com.zhon.frame.mvp.test.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zhon.baselib.mvpbase.baseImpl.BaseActivity;
import com.zhon.frame.R;
import com.zhon.frame.mvp.test.adapter.TestAdapter;
import com.zhon.frame.mvp.test.bean.TestBean;
import com.zhon.frame.mvp.test.contact.TestContact;
import com.zhon.frame.mvp.test.presenter.TestPresenter;

import java.util.ArrayList;
import java.util.List;

/*
 * 项目名:    BaseFrame
 * 包名       com.zhon.frame.mvp.login.activity
 * 文件名:    TestActivity
 * 创建者:    ZJB
 * 创建时间:  2017/9/7 on 11:21
 * 描述:     TODO 测试Activity
 */
public class TestActivity extends BaseActivity<TestContact.presenter> implements TestContact.view {

    private List<TestBean.StoriesBean> list = new ArrayList<>();//数据
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        init();
        presenter.getData();
    }

    /**
     * 初始化界面
     */
    private void init() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 初始化presenter
     *
     * @return 对应的presenter
     */
    @Override
    public TestContact.presenter initPresenter() {
        return new TestPresenter(this);
    }

    /**
     * 设置数据
     * 刷新界面
     *
     * @param dataList 数据源
     */
    @Override
    public void setData(List<TestBean.StoriesBean> dataList) {
        list.addAll(dataList);
        adapter.notifyDataSetChanged();
    }
}
