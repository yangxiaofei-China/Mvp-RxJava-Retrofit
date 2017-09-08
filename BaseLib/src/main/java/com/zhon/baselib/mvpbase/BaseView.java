package com.zhon.baselib.mvpbase;

/*
 * 项目名:    BaseLib
 * 包名       com.zhon.baselib.mvpbase
 * 文件名:    BaseView
 * 创建者:    ZJB
 * 创建时间:  2017/6/20 on 14:16
 * 描述:     TODO
 */
public interface BaseView {

    void showLoadingDialog(String msg);

    void dismissLoadingDialog();
}
