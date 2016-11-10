package cn.zyh.rxjavademot.network.api;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/12.
 */
public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<GankSerializedApi> getGankBeauties(@Path("number") int number, @Path("page") int page);

}
