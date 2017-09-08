package com.zhon.frame.mvp.test.contact;

import com.zhon.baselib.mvpbase.BasePresenter;
import com.zhon.baselib.mvpbase.BaseView;
import com.zhon.frame.mvp.test.bean.TestBean;

import java.util.List;

/*
 * 项目名:    BaseFrame
 * 包名       com.zhon.frame.mvp.login.contact
 * 文件名:    TestContact
 * 创建者:    ZJB
 * 创建时间:  2017/9/7 on 11:13
 * 描述:     TODO  接口
 */
public interface TestContact {

    interface view extends BaseView {
        /**
         * 设置数据
         *
         * @param dataList
         */
        void setData(List<TestBean.StoriesBean> dataList);
    }

    interface presenter extends BasePresenter {
        /**
         * 获取数据
         */
        void getData();
    }
}
