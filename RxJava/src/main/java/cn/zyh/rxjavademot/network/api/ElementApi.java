package cn.zyh.rxjavademot.network.api;

import java.util.List;

import cn.zyh.rxjavademot.module.elementary_1.model.ElementImageModel;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/11.
 */
public interface ElementApi {

    /** retrofit形式*/
//    @GET("/user")
//    public void getUser(@Query("userId") String userId, Callback<User> callback);
//    @GET("search")
//      public void search(@Query("q"),String id,Callback<ElementImageModel> callback);
    /**RxJava形式*/
//    @GET("/user")
//    public Observable<User> getUser(@Query("userId") String userId);

    /**Element搜索图片*/
    @GET("search")
    Observable <List<ElementImageModel>> search(@Query("q") String searchKey);





}
