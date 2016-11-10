package cn.zyh.rxjavademot.module.elementary_1;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import cn.zyh.rxjavademot.R;
import cn.zyh.rxjavademot.base.BaseFragment;
import cn.zyh.rxjavademot.module.elementary_1.adapter.ElementAdapter;
import cn.zyh.rxjavademot.module.elementary_1.model.ElementImageModel;
import cn.zyh.rxjavademot.network.Network;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 基本
 */
public class ElementaryFragment extends BaseFragment {

    @Bind(R.id.radiogroup)
    RadioGroup radiogroup;
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private static final String tag = "ElementaryFragment";

    private ElementAdapter mAdapter = new ElementAdapter();

//    /**
//     * 单例设计模式
//     */
//    private ElementaryFragment() {
//        super();
//    }

//    private static ElementaryFragment instance = null;
//
//    public static ElementaryFragment newInstance() {
//        if (instance == null) {
//            instance = new ElementaryFragment();
//        }
//        return instance;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        ButterKnife.bind(this, view);
        //recycle设置
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycleView.setAdapter(mAdapter);

        //刷新设置
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefresh.setEnabled(false);

        return view;
    }

    //1.创建观察者 observer
    Observer<List<ElementImageModel>> subscriber = new Observer<List<ElementImageModel>>() {
        @Override
        public void onCompleted() {
            Log.i(tag, "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.i(tag, "onError");
            swipeRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), "加载数据失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ElementImageModel> elementImageModels) {
            swipeRefresh.setRefreshing(false);
            mAdapter.setImages(elementImageModels);
        }
    };

        /**方式一*/
//    @OnClick({R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4})
//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.rb1://可爱
//                break;
//            case R.id.rb2: //110
//                break;
//            case R.id.rb3: //在下
//                break;
//            case R.id.rb4: //装逼
//                break;
//        }
//    }

        /**方式二*/
    @OnCheckedChanged({R.id.rb1, R.id.rb2, R.id.rb3, R.id.rb4})
    void onTagChecked(RadioButton searchRb, boolean checked) {
        if (checked){
            //2.创建被观察者observable 3.订阅
            unsubscribe();
            mAdapter.setImages(null);
            swipeRefresh.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    /**
     * 根据关键字 调用接口数据
     * @param key
     */
    private void search(String key) {
        subscription = Network.getElementApi().search(key)
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 所发生的线程
                .observeOn(AndroidSchedulers.mainThread()) //指定 Subscriber 所运行在的线程。或者叫做事件消费的线程。
                .subscribe(subscriber);
    }



    @Override
    public int getTitleRes() {
        return R.string.title_elementary;
    }

    @Override
    public int getDialogRes() {
        return R.layout.dialog_elementary;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
