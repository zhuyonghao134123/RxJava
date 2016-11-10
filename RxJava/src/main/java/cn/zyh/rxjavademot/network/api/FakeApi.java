package cn.zyh.rxjavademot.network.api;

import android.support.annotation.NonNull;

import java.util.Random;

import cn.zyh.rxjavademot.module.token_4.model.FakeToken;
import cn.zyh.rxjavademot.module.token_4.model.UserData;
import rx.Observable;
import rx.functions.Func1;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/13.
 */
public class FakeApi {

    Random random = new Random();

    /**
     * 获取token
     *
     * @param fakeAuth
     * @return
     */
    public Observable<FakeToken> getFakeToken(@NonNull String fakeAuth) {

        return Observable.just(fakeAuth)
                .map(new Func1<String, FakeToken>() { //变换 String - FakeToken
                    public FakeToken call(String fakeAuth) {
                        // Add some random delay to mock the network delay 添加一些随机延迟来模拟网络延迟
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
//                        SystemClock.sleep(fakeNetworkTimeCost);
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //获取Token
                        FakeToken fakeToken = new FakeToken();
                        fakeToken.token = createToken();
                        return fakeToken;
                    }
                });
    }

    /**
     * 创建token
     *
     * @return
     */
    private static String createToken() {
        return "fake_token_" + System.currentTimeMillis() % 10000;
    }

    /**
     * 根据token-获取用户数据
     *
     * @param fakeToken
     * @return
     */
    public Observable<UserData> getUserData(FakeToken fakeToken) {
        return Observable.just(fakeToken)
                .map(new Func1<FakeToken, UserData>() {//变换 FakeToken-UserData
                    @Override
                    public UserData call(FakeToken fakeToken) {
                        // Add some random delay to mock the network delay 添加一些随机延迟来模拟网络延迟
                        int fakeNetworkTimeCost = random.nextInt(500) + 500;
                        try {
                            Thread.sleep(fakeNetworkTimeCost);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (fakeToken.expired) {
                            throw new IllegalArgumentException("Token expired!");
                        }
                        UserData userData = new UserData();
                        userData.id = (int) (System.currentTimeMillis() % 1000);
                        userData.name = "FAKE_USER_" + userData.id;
                        userData.currentToken = fakeToken.token;
                        return userData;
                    }
                });
    }
}
