package cn.zyh.rxjavademot.network;

import java.util.Random;

import cn.zyh.rxjavademot.module.token_4.model.FakeToken;
import cn.zyh.rxjavademot.module.token_4.model.UserData;
import cn.zyh.rxjavademot.network.api.BaseApi;
import cn.zyh.rxjavademot.network.api.ElementApi;
import cn.zyh.rxjavademot.network.api.FakeApi;
import cn.zyh.rxjavademot.network.api.GankApi;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/11.
 */
public class Network {

    private static ElementApi elementApi;
    private static FakeApi fakeApi;

    /**
     * 获取基本图片
     *
     * @return
     */
    public static ElementApi getElementApi() {
        if (elementApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BaseApi.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            elementApi = retrofit.create(ElementApi.class);
        }
        return elementApi;
    }

    /**
     * 获取gank图片
     *
     * @return
     */
    public static GankApi getGankApi() {
        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseApi.BASE_URL_GANK)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GankApi.class);
    }

    /**
     * 获取fakeToken
     *
     * @return
     */
    public static FakeApi getFakeApi() {
        if (fakeApi == null) {
            fakeApi = new FakeApi();
        }
        return fakeApi;
    }


}
