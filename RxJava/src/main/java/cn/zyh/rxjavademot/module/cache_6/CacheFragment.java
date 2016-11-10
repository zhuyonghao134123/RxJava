package cn.zyh.rxjavademot.module.cache_6;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zyh.rxjavademot.R;
import cn.zyh.rxjavademot.adapter.ItemListAdapter;
import cn.zyh.rxjavademot.base.BaseFragment;
import cn.zyh.rxjavademot.manager.AppManager;
import cn.zyh.rxjavademot.module.cache_6.data.Data;
import cn.zyh.rxjavademot.module.cache_6.data.DataFiles;
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;
import cn.zyh.rxjavademot.network.Network;
import cn.zyh.rxjavademot.util.GankBeautyResultToItemsMapper;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * 缓存
 */
public class CacheFragment extends BaseFragment {

    private static final String tag = "CacheFragment";
    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.tvTime)
    TextView tvTime;
    @Bind(R.id.btn_load)
    AppCompatButton btnLoad;
    @Bind(R.id.btnClearMemory)
    AppCompatButton btnClearMemory;
    @Bind(R.id.btnClearAll)
    AppCompatButton btnClearAll;

    private long startingTime;
    private ItemListAdapter mAdapter = new ItemListAdapter();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cache, container, false);
        ButterKnife.bind(this, view);

        //recycle设置
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycleView.setAdapter(mAdapter);
        //刷新设置
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefresh.setEnabled(false);
        return view;
    }

    @Override
    public int getTitleRes() {
        return R.string.title_cache;
    }

    @Override
    public int getDialogRes() {
        return R.layout.dialog_cache;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_load, R.id.btnClearMemory, R.id.btnClearAll})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load: //加载
                swipeRefresh.setRefreshing(true);
                startingTime = System.currentTimeMillis();
                unsubscribe();

                final Data data= Data.getInstance();
                //1.创建观察者
                Subscriber<List<ItemModel>> subscriber = new Subscriber<List<ItemModel>>() {
                    public void onCompleted() {
                        Log.i(tag, "onCompleted");
                    }
                    public void onError(Throwable e) {
                        Log.i(tag,"错误"+e);
                        e.printStackTrace();
                        swipeRefresh.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                    public void onNext(List<ItemModel> itemModels) {
                        swipeRefresh.setRefreshing(false);
                        tvTime.setText("耗时:" + (System.currentTimeMillis() - startingTime) + data.getDataSourceText());
                        mAdapter.setItem(itemModels);
                    }
                };
                /**
                 *  缓存(BehaviorSubject)
                 *  RxJava 中有一个较少被人用刀的类叫做Subject，它是一种及时Observable，又是Observer，
                 *  的东西，因此可以被用作中间件来做数据传递，例如,可以用它的子类BehaviorSubject来制作缓存。
                 *  代码大致形式如下：
                 *  api.getData()
                 *      .subscribe(behaviorSubject); //网络数据会被缓存
                 *   behaviorSubject.subscribe(observer) //之前的缓存见直接送达observer
                 */
                Data.getInstance().getSubscription(subscriber);
                break;
            case R.id.btnClearMemory: //清理内存缓存
                Data.getInstance().clearMemory();
                mAdapter.setItem(null);
                Toast.makeText(getActivity(), R.string.memory_cache_cleared, Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnClearAll: //清理内存和磁盘缓存
                Data.getInstance().clearMemoryAndDisk();
                mAdapter.setItem(null);
                Toast.makeText(getActivity(), R.string.memory_and_disk_cache_cleared, Toast.LENGTH_SHORT).show();
                break;
        }
    }



}
