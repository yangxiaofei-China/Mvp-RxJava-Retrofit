package com.zhon.frame.api;


import com.zhon.frame.mvp.test.bean.TestBean;

import io.reactivex.Observable;
import retrofit2.http.GET;

/*
 * 项目名:    playstock
 * 包名       com.hjtech.playstock.Api
 * 文件名:    RetrofitService
 * 创建者:    ZJB
 * 创建时间:  2017/7/6 on 14:30 
 * 描述:     TODO
 */
public interface RetrofitService {


    String BASE_URL = "https://news-at.zhihu.com/api/4/";


    /**
     * 测试接口
     *
     * @return
     */
    @GET("news/latest")
    Observable<TestBean> test();


}