![Travis](https://img.shields.io/badge/RxJava2-2.1.1-brightgreen.svg)&nbsp;&nbsp;&nbsp;![Travis](https://img.shields.io/badge/Retrofit2-2.3.0-ff69b4.svg)&nbsp;&nbsp;&nbsp;![Travis](https://img.shields.io/badge/compileSdkVersion-25-blue.svg)

> *许多不管怎么做、怎么想都没结果的事，要懂得交给时间。有些事无论你怎么努力怎么勉强，时间不够，还是耐心的等待吧。*


# 1.序言

> 2016年安卓热门词汇MVP，RxJava，Retrofit。时隔一年这些框架依然是很常用的，现在来把这几个关键词整合起来，搭建一个快速开发框架。。。


----------


### MVP？？？
对于一些刚学安卓的朋友们应该还不是太熟悉，我们先来温习一下吧！
![这里写图片描述](https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/c0=baike80,5,5,80,26/sign=0d3000fa9c25bc313f5009ca3fb6e6d4/8b82b9014a90f603534849733c12b31bb051ed0e.jpg)
这张图可以说是看烂了，这张图对于懂了点MVP的人可以说是把中间几个字去掉，都能一眼看穿。这张图到底是什么意思呢？


----------


#### 举个例子：
**需求：需要点击一个按钮通过访问网络获取一条数据展示在页面上**

**普通做法：**
一个Activity中写一个方法访问网络获取数据，点击按钮调用它，然后获取数据完成了再拿到对应的控件设置数据，完事了。。。

**MVP：**
在图中有三个模块**view（界面），presenter（控制层），model（数据源）**。他们在这个需求中需要做什么呢？
**view（界面）**：显示数据
**presenter（控制层）**：1.通知model我要取数据 2.取到了数据再传递给view
**model（数据源）**：访问网络获取数据

它的过程是这样的，

>  1. view告诉presenter我要数据
>  2. presenter告诉model我要数据
>  3. model访问网络得到了数据再通知presenter给你我取到的数据
>  4. presenter **处理好数据** 再把数据传递给view
>  5. 最后view显示出来用户可以观看。

有些人说这不是脱了裤子放屁啊？一点代码能写完的东西为啥分了这么多东西？
这确实有点复杂，在面向对象中有几个原则   单一职责原则，开闭原则，里氏代换原则，依赖倒转原则，接口隔离原则，合成复用原则，迪米特法则。这我就不一一介绍了，自行百度。。普通做法中一个Activity即有访问网络，又有更新界面，第一条单一职责原则就违背了，然而在mvp中view只做和界面相关的事情。
再者一个Activity中如果逻辑太多了。一个Activity几千行代码，逻辑判断，更新界面，查询数据库，访问网络，如果第二个人需要修改，怎么看？？
这时候再看看mvp 逻辑在P里面一个类，数据在Model层，界面相关的在V层。清晰明了，也方便单元测试。

> 程序猿如果不最求代码质量，那和咸鱼有什么区别？

### RxJava2+Retrofit2整合
#### 1.玩框架第一步compile ：

```
    compile 'io.reactivex.rxjava2:rxjava:2.1.1'
    compile 'io.reactivex.rxjava2:rxandroid:2.0.1'
    compile 'com.squareup.retrofit2:retrofit:2.3.0'
    compile 'com.squareup.retrofit2:converter-gson:2.3.0'
    compile 'com.squareup.retrofit2:converter-scalars:2.3.0'
    compile 'com.squareup.retrofit2:adapter-rxjava2:2.3.0'//配合rxjava2
    compile 'com.squareup.okhttp3:logging-interceptor:3.8.1'//拦截器
```
#### 2.创建service

```
public interface RequestService {

    String BASE_URL = "https://news-at.zhihu.com/api/4/";

    /**
     * 测试接口
     *
     * @return
     */
    @GET("news/latest")
    Observable<TestBean> test();
}
```

> 单独使用retrofit是返回call，配合RxJava这里我们返回Observable

#### 3.封装一个工具类

```
public class RetrofitFactory {

    //访问超时
    private static final long TIMEOUT = 30;

    // Retrofit是基于OkHttpClient的，可以创建一个OkHttpClient进行一些配置
    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            //打印接口信息，方便接口调试
            .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.e("TAG", "log: " + message);
                }
            }).setLevel(HttpLoggingInterceptor.Level.BASIC))
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .build();

    private static RetrofitService retrofitService = new Retrofit.Builder()
            .baseUrl(RetrofitService.BASE_URL)
            // 添加Gson转换器
            .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                    .setLenient()
                    .create()
            ))
            // 添加Retrofit到RxJava的转换器
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(httpClient)
            .build()
            .create(RetrofitService.class);
	
	//获得RetrofitService对象
    public static RetrofitService getInstance() {
        return retrofitService;
    }
}
```
#### 使用
我们整合好了，最后我们看下怎么使用吧！访问个网络获取一个数据

```
 RetrofitFactory.getInstance()//获取retrofitService对象
				.test()//测试接口
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        //将这个请求的Disposable添加进入CompositeDisposable同一管理（在封装的presenter中）
                        addDisposable(disposable);
                        //访问网络显示dialog
                        view.showLoadingDialog("");
                    }
                })
                .map(new Function<TestBean, List<TestBean.StoriesBean>>() {
                    @Override
                    public List<TestBean.StoriesBean> apply(@NonNull TestBean testBean) throws Exception {
	                    //转化数据
                        return testBean.getStories();
                    }
                })
                //获得的数据返回主线程去更新界面
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<TestBean.StoriesBean>>() {
                    @Override
                    public void accept(@NonNull List<TestBean.StoriesBean> storiesBeen) throws Exception {
	                    //消失dialog
                        view.dismissLoadingDialog();
                        //设置数据
                        view.setData(storiesBeen);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        view.dismissLoadingDialog();
                        String exception = ExceptionHelper.handleException(throwable);
                        //打印出错误信息
                        Log.e("TAG", "exception: " + exception);
                    }
                });
```
好我们来分析一下，

>  1. 首先先获得一个retrofitService对象
>  2. 然后调用test接口。
>  3. 访问网络在子线程
>  4. 在访问网络的时候显示等待对话框，将这个请求加入CompositeDisposable中（在basePresenter封装了统一管理的方法，调用addDisposable(disposable);最后Activity关闭，取消所有网络请求，防止内存泄漏）
>  5. 将网络获取的数据转换成你需要的数据
>  6. 线程卡点结果返回主线程
>  7. 订阅得到数据更新界面，处理错误信息

RxJava2+retrofit2就是这么简单封装好了一条线路下来非常清晰。没用过的朋友看下有可能一脸懵逼，不过没关系，你只要拿着我的项目看下就能懂了。


----------


# 打造MVP
先看下我们的成果里面有什么东西吧！没错 就是下面几个类就ok

![这里写图片描述](http://img.blog.csdn.net/20170907155129462?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvejk1NzI1MDI1NA==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 1.分析
好我们来分析一下mvp  

> 1.view需要找presenter拿数据，那么view里面需要一个presenter对象。
> 2.presenter需要给view数据，那么presenter也需要一个view对象。
> 3.model层访问网络使用RxJava+retrofit，数据回调给presenter（后面分析）

### 2.思考

> 所有的view里面都需要什么操作呢？ 所有的presenter里面都需要什么操作呢？
> 暂时在我的需求中view和presenter只有如下这么几个功能，当然，如果你还有其他的功能可以再加上去。

```
public interface BaseView {

	//显示dialog
    void showLoadingDialog(String msg);

	//取消dialog
    void dismissLoadingDialog();
}

```

```
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
```


### 3.接下来编写view和presenter的实现类

> 由于每一个view都对应不同的presenter。当然对应的每个presenter也同样对应一个view。所有我们使用接口和泛型来封装了。

所以我们先看下代码：

```
public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected P presenter;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityManager.getAppInstance().addActivity(this);//将当前activity添加进入管理栈
        presenter = initPresenter();
    }

    @Override
    protected void onDestroy() {
        ActivityManager.getAppInstance().removeActivity(this);//将当前activity移除管理栈
        if (presenter != null) {
            presenter.detach();//在presenter中解绑释放view
            presenter = null;
        }
        super.onDestroy();
    }

    /**
     * 在子类中初始化对应的presenter
     *
     * @return 相应的presenter
     */
    public abstract P initPresenter();


    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void showLoadingDialog(String msg) {

    }
}

```

```
public abstract class BasePresenterImpl<V extends BaseView> implements BasePresenter {
    public BasePresenterImpl(V view) {
        this.view = view;
        start();
    }

    protected V view;//给子类使用view


    @Override
    public void detach() {
        this.view = null;
        unDisposable();
    }

    @Override
    public void start() {

    }

/////////////////////////////////////////////////////////////////////////////////

	//以下下为配合RxJava2+retrofit2使用的

    //将所有正在处理的Subscription都添加到CompositeSubscription中。统一退出的时候注销观察
    private CompositeDisposable mCompositeDisposable;

    /**
     * 将Disposable添加
     *
     * @param subscription
     */
    @Override
    public void addDisposable(Disposable subscription) {
        //csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
     */
    @Override
    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
    
}

```

>  - 创建activity中泛型传入相应的view接口，presenter中泛型传入相应的presenter接口  
>  -  activity中onCreate中初始化presenter，onDestroy中调用detach，将presenter中正在执行的任务取消，将view对象置为空。
> 
>  -  presenter中通过构造传递参数。将view的实例传递进入presenter


----------


### 4.使用
好的接下来我们来使用一下吧
首先我们先来个简单的**需求：**
> 打开一个页面请求网络获取数据，将数据显示在界面上

#### 1.创建Contact管理接口

> 首先先思考view需要设置数据所有view中需要一个setData方法
> presenter需要去访问网络所以需要一个getData方法。代码如下：

```
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
```
#### 创建Activity和presenter

> 创建一个Activity继承BaseActivity它的泛型对应presenter的接口。实现对应的view接口
> 创建一个TestPresenter继承BasePresenterImpl，泛型对应view的接口。并实现对应的presenter接口

代码如下：

```
public class TestActivity extends BaseActivity<TestContact.presenter> implements TestContact.view {

    private List<TestBean.StoriesBean> list = new ArrayList<>();//数据
    private RecyclerView recyclerView;
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
        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
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

```

```
public class TestPresenter extends BasePresenterImpl<TestContact.view> implements TestContact.presenter {
    public TestPresenter(TestContact.view view) {
        super(view);
    }

    /**
     * 获取数据
     */
    @Override
    public void getData() {
        Api.getInstance()
		        .test()//测试接口
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        addDisposable(disposable);//请求加入管理
                        view.showLoadingDialog("");
                    }
                })
                .map(new Function<TestBean, List<TestBean.StoriesBean>>() {
                    @Override
                    public List<TestBean.StoriesBean> apply(@NonNull TestBean testBean) throws Exception {
                        return testBean.getStories();//转换数据
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
```
#### 分析
好了相信大部分朋友看了代码都看懂了，简要的分析一下过程吧


>  - 创建对应的类，实现对应的方法
>  - Activity中只有一个recyclerView初始化它。
>  - 在onCreate中调用presenter中的getData()方法
>  - 在presenter中使用RxJava2+retrofit2访问网络。获取数据返回给view
>  - view拿到数据更新界面

