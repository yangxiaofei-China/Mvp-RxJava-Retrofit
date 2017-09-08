package com.zhon.baselib.mvpbase;

import io.reactivex.disposables.Disposable;

/*
 * 项目名:    BaseLib
 * 包名       com.zhon.baselib.mvpbase
 * 文件名:    BasePresenter
 * 创建者:    ZJB
 * 创建时间:  2017/6/20 on 16:21
 * 描述:     TODO
 */
public interface BasePresenter {
    //默认初始化
    void start();

    //Activity关闭把view对象置为空
    void detach();

    //将网络请求的每一个disposable添加进入CompositeDisposable，再退出时候一并注销
    void addDisposable(Disposable subscription);

    //注销所有请求
    void unDisposable();

}
