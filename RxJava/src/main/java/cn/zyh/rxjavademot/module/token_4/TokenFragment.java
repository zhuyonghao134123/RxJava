package cn.zyh.rxjavademot.module.token_4;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Random;

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
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 基本
 */
public class TokenFragment extends BaseFragment {


    @Bind(R.id.tvToken)
    AppCompatTextView tvToken;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.btn_load)
    AppCompatButton btnLoad;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        ButterKnife.bind(this, view);

        //刷新设置
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefresh.setEnabled(false);
        return view;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_token;
    }

    @Override
    public int getDialogRes() {
        return R.layout.dialog_token;
    }

    /**
     * 1.创建观察者 Observer
     */
    Action1<UserData> onNext = new Action1<UserData>() {
        @Override
        public void call(UserData userData) {
            swipeRefresh.setRefreshing(false);
            tvToken.setText("\r\n" + "获取到的数据：" + "\r\n" + "ID:" + userData.id + "\r\n"
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

    /**
     * 请求数据
     */
    @OnClick(R.id.btn_load)
    public void onClick() {
        swipeRefresh.setRefreshing(true);
        unsubscribe();


        //flatMap（）可以用较为清晰的代码实现这种连续请求
        final FakeApi fakeApi = Network.getFakeApi();
        subscription = fakeApi.getFakeToken("fake_auth_code")
                .flatMap(new Func1<FakeToken, Observable<UserData>>() {
                    public Observable<UserData> call(FakeToken fakeToken) {
                        return fakeApi.getUserData(fakeToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onNext, onError);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
