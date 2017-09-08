package com.zhon.frame.mvp.test.presenter;

import com.zhon.baselib.mvpbase.baseImpl.BasePresenterImpl;
import com.zhon.baselib.retroft.ExceptionHelper;
import com.zhon.frame.model.Api;
import com.zhon.frame.mvp.test.bean.TestBean;
import com.zhon.frame.mvp.test.contact.TestContact;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/*
 * 项目名:    BaseFrame
 * 包名       com.zhon.frame.mvp.login.presenter
 * 文件名:    TestPresenter
 * 创建者:    ZJB
 * 创建时间:  2017/9/7 on 11:17
 * 描述:     TODO
 */
public class TestPresenter extends BasePresenterImpl<TestContact.view>
        implements TestContact.presenter {

    public TestPresenter(TestContact.view view) {
        super(view);
    }

    /**
     * 获取数据
     */
    @Override
    public void getData() {
        Api.getInstance().test()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        addDisposable(disposable);
                        view.showLoadingDialog("");
                    }
                })
                .map(new Function<TestBean, List<TestBean.StoriesBean>>() {
                    @Override
                    public List<TestBean.StoriesBean> apply(@NonNull TestBean testBean) throws Exception {
                        return testBean.getStories();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TestBean.StoriesBean>>() {
                    @Override
                    public void accept(@NonNull List<TestBean.StoriesBean> storiesBeen) throws Exception {
                        view.dismissLoadingDialog();
                        view.setData(storiesBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.dismissLoadingDialog();
                        ExceptionHelper.handleException(throwable);
                    }
                });
    }
}
