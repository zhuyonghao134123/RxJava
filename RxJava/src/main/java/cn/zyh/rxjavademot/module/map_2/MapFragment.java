package cn.zyh.rxjavademot.module.map_2;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;
import cn.zyh.rxjavademot.network.Network;
import cn.zyh.rxjavademot.util.GankBeautyResultToItemsMapper;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 基本
 */
public class MapFragment extends BaseFragment {

    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.prePageBtn)
    AppCompatButton prePageBtn;
    @Bind(R.id.pageNumber)
    TextView pageNumber;
    @Bind(R.id.nextPageBtn)
    AppCompatButton nextPageBtn;

    private ItemListAdapter mAdapter = new ItemListAdapter();
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        //recycleView设置
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycleView.setAdapter(mAdapter);
        //刷新设置
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefresh.setEnabled(false);
        return view;
    }

    //1.创建观察者
    Observer<List<ItemModel>> observer = new Observer<List<ItemModel>>() {
        public void onCompleted() {
        }

        public void onError(Throwable e) {
            swipeRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), "数据加载失败...", Toast.LENGTH_SHORT).show();
        }

        public void onNext(List<ItemModel> items) {
            swipeRefresh.setRefreshing(false);
            mAdapter.setItem(items);
            pageNumber.setText("第" + page + "页");
            if (items == null || items.size() == 0) {
                nextPageBtn.setEnabled(false);
            } else {
                nextPageBtn.setEnabled(true);
            }
        }
    };

    @OnClick({R.id.prePageBtn, R.id.nextPageBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.prePageBtn:
                //上一页
                loadPage(--page);
                if (page == 1) {
                    prePageBtn.setEnabled(false);
                }
                break;
            case R.id.nextPageBtn:
                //下一页
                loadPage(++page);
                if (page == 2) {
                    prePageBtn.setEnabled(true);
                }
                break;
        }
    }

    /**
     * 加载页面
     *
     * @param i 页面索引
     */
    private void loadPage(int i) {
        swipeRefresh.setRefreshing(true);
        //取消订阅
        unsubscribe();

        //2.创建被观察者Observable  3.订阅
        Network.getGankApi()
                .getGankBeauties(10, i)
                .map(GankBeautyResultToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    @Override
    public int getTitleRes() {
        return R.string.title_map;
    }

    @Override
    public int getDialogRes() {
        return R.layout.dialog_map;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
