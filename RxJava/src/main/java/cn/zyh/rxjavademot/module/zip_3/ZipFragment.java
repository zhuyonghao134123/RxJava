package cn.zyh.rxjavademot.module.zip_3;

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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zyh.rxjavademot.R;
import cn.zyh.rxjavademot.adapter.ItemListAdapter;
import cn.zyh.rxjavademot.base.BaseFragment;
import cn.zyh.rxjavademot.module.elementary_1.adapter.ElementAdapter;
import cn.zyh.rxjavademot.module.elementary_1.model.ElementImageModel;
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;
import cn.zyh.rxjavademot.network.Network;
import cn.zyh.rxjavademot.network.api.BaseApi;
import cn.zyh.rxjavademot.network.api.ElementApi;
import cn.zyh.rxjavademot.util.GankBeautyResultToItemsMapper;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * 基本
 */
public class ZipFragment extends BaseFragment {

    @Bind(R.id.recycleView)
    RecyclerView recycleView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.btn_load)
    AppCompatButton btnLoad;
    private static final String tag="'ZipFragment";

    private ItemListAdapter mAdapter = new ItemListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //加载布局3种方式
//        inflater.inflate(R.layout.fragment_zip, container, false);
//        getActivity().getLayoutInflater().inflate(R.layout.fragment_zip,null);
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_zip, null);
        ButterKnife.bind(this, view);
        //recycle设置
        recycleView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycleView.setAdapter(mAdapter);
        //刷新设置
        swipeRefresh.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefresh.setEnabled(false);

        return view;
    }

    //1.创建观察者
    Observer<List<ItemModel>> observer = new Observer<List<ItemModel>>() {
        @Override
        public void onCompleted() {
            Log.i(tag,"事件序列执行完成");
        }

        @Override
        public void onError(Throwable e) {
            swipeRefresh.setRefreshing(false);
            Toast.makeText(getActivity(), "加载失败...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNext(List<ItemModel> itemModels) {
            swipeRefresh.setRefreshing(false);
            mAdapter.setItem(itemModels);
        }
    };

    @Override
    public int getTitleRes() {
        return R.string.title_zip;
    }

    @Override
    public int getDialogRes() {
        return R.layout.dialog_zip;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * 加载
     */
    @OnClick(R.id.btn_load)
    public void onClick() {
        //取消订阅
        swipeRefresh.setRefreshing(true);
        unsubscribe();
        subscription = Observable
                .zip(Network.getGankApi()
                                .getGankBeauties(200, 1)
                                .map(GankBeautyResultToItemsMapper.getInstance()),
                        Network.getElementApi().search("装逼"),
                        new Func2<List<ItemModel>, List<ElementImageModel>, List<ItemModel>>() {
                            public List<ItemModel> call(List<ItemModel> gankItems, List<ElementImageModel> elementImageModels) {
                                ArrayList<ItemModel> items = new ArrayList<>();
                                for (int i = 0; i < gankItems.size() / 2 && i < elementImageModels.size(); i++) {
                                    items.add(gankItems.get(i * 2));
                                    items.add(gankItems.get(i * 2 + 1));
                                    ElementImageModel elementImageModel = elementImageModels.get(i);
                                    ItemModel itemModel = new ItemModel();
                                    itemModel.imageUrl = elementImageModel.image_url;
                                    itemModel.description = elementImageModel.description;
                                    items.add(itemModel);
                                }
                                return items;
                            }
                        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }
}
