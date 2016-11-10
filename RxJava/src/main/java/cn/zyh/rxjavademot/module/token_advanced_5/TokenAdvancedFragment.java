package cn.zyh.rxjavademot.module.token_advanced_5;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zyh.rxjavademot.R;
import cn.zyh.rxjavademot.base.BaseFragment;
import cn.zyh.rxjavademot.module.token_4.model.FakeToken;
import cn.zyh.rxjavademot.module.token_4.model.UserData;
import cn.zyh.rxjavademot.network.Network;
import cn.zyh.rxjavademot.network.api.FakeApi;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 基本
 */
public class TokenAdvancedFragment extends BaseFragment {

    @Bind(R.id.tvToken)
    AppCompatTextView tvToken;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.btn_load)
    AppCompatButton btnLoad;
    @Bind(R.id.btn_destory)
    AppCompatButton btnDestory;


    final FakeToken catheFakeToken = new FakeToken(true);
    boolean tokenUpdate; //更新token

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_token_advanced, container, false);
        ButterKnife.bind(this, view);
        //刷新设置
        swipeRefresh.setEnabled(false);
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        return view;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_token_advanced;
    }

    @Override
    public int getDialogRes() {
        return R.layout.dialog_token_advanced;
    }


    @OnClick({R.id.btn_load, R.id.btn_destory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load: //请求数据
                tokenUpdate = false;
                swipeRefresh.setRefreshing(true);
                unsubscribe();

                //1.创建observer
                Action1<UserData> onNext = new Action1<UserData>() {
                    public void call(UserData userData) {
                        swipeRefresh.setRefreshing(false);
                        String token = catheFakeToken.token;
                        if (tokenUpdate) {
                            token += "已更新";
                        }
                        Log.i("onNext", tokenUpdate + "" + token);
                        tvToken.setText("当前token：" + token + "\r\n" + "获取到的数据：" + "\r\n" + "ID:" + userData.id + "\r\n"
                                + "name：" + userData.name);
                    }
                };
                Action1<Throwable> onError = new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                };

                final FakeApi fakeApi = Network.getFakeApi();

                Observable.just(null)
                        .flatMap(new Func1<Object, Observable<UserData>>() {
                            @Override
                            public Observable<UserData> call(Object o) {
                                Log.i("flatMap",catheFakeToken.token);
                                return catheFakeToken.token == null
                                        ? Observable.<UserData>error(new NullPointerException("Token is null!"))
                                        : fakeApi.getUserData(catheFakeToken); //根据token-获取用户数据
                            }
                        })
                        .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
                            @Override
                            public Observable<?> call(Observable<? extends Throwable> observable) {
                                return observable.flatMap(new Func1<Throwable, Observable<?>>() {
                                    @Override
                                    public Observable<?> call(Throwable throwable) {
                                        if (throwable instanceof IllegalArgumentException || throwable instanceof NullPointerException) {
                                            return fakeApi.getFakeToken("fake_auth_code")
                                                    .doOnNext(new Action1<FakeToken>() {
                                                        @Override
                                                        public void call(FakeToken fakeToken) {
                                                            //获取token
                                                            tokenUpdate = true;
                                                            catheFakeToken.token = fakeToken.token;
                                                            catheFakeToken.expired = fakeToken.expired;
                                                            Log.i("retryWhen",tokenUpdate+""+catheFakeToken.token+catheFakeToken.expired);
                                                        }
                                                    });
                                        }
                                        return Observable.error(throwable);
                                    }
                                });
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(onNext, onError);

                break;
            case R.id.btn_destory: //销毁token
                Toast.makeText(getActivity(), "token销毁", Toast.LENGTH_SHORT).show();
                catheFakeToken.expired = true; //token过期
                Log.i("taggg", "btn_destory" + catheFakeToken.expired);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
