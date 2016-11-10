package cn.zyh.rxjavademot.module.cache_6.data;

import java.util.List;

import cn.zyh.rxjavademot.R;
import cn.zyh.rxjavademot.manager.AppManager;
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;
import cn.zyh.rxjavademot.network.Network;
import cn.zyh.rxjavademot.util.GankBeautyResultToItemsMapper;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.BehaviorSubject;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/19.
 */
public class Data {

    private BehaviorSubject<List<ItemModel>> behaviorSubject;

    private static final int DATA_SOURCE_MEMORY = 1;
    private static final int DATA_SOURCE_DISK = 2;
    private static final int DATA_SOURCE_NETWORK = 3;
    private int dataSource;

    private static Data INSTANCE;

    private Data() {
    }

    public static Data getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Data();
        }
        return INSTANCE;
    }

    /**
     * 设置数据来源
     */
    public void setDataSource(int dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取数据来源类型
     */
    public String getDataSourceText() {
        int dataSourceTextRes;
        switch (dataSource) {
            case DATA_SOURCE_MEMORY:
                dataSourceTextRes = R.string.data_source_memory;
                break;
            case DATA_SOURCE_DISK:
                dataSourceTextRes = R.string.data_source_disk;
                break;
            case DATA_SOURCE_NETWORK:
                dataSourceTextRes = R.string.data_source_network;
                break;
            default:
                dataSourceTextRes = R.string.data_source_network;
        }
        return AppManager.getInstance().getString(dataSourceTextRes);
    }


    public Subscription getSubscription(Subscriber<List<ItemModel>> subscriber) {
        if (behaviorSubject == null) {
            //制作缓存
            behaviorSubject = BehaviorSubject.create();
            //2.创建被观察者
            Observable.create(new Observable.OnSubscribe<List<ItemModel>>() {
                public void call(Subscriber<? super List<ItemModel>> subscriber) {
                    //读取磁盘数据
                    List<ItemModel> itemModels = DataFiles.getInstance().readItems();
                    if (itemModels == null) {
                        //数据为空-下载网络数据并缓存
                        setDataSource(DATA_SOURCE_NETWORK);
                        loadFromNetWork();
                    } else {
                        //数据不为空-直接显示出来
                        setDataSource(DATA_SOURCE_DISK);
                        subscriber.onNext(itemModels);
                    }
                }
            })
                    .subscribeOn(Schedulers.io())
                    .subscribe(behaviorSubject);
        } else {
            setDataSource(DATA_SOURCE_MEMORY);
        }
        return behaviorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    /**
     * 从网络下载数据
     */
    private void loadFromNetWork() {
        Network.getGankApi()
                .getGankBeauties(100, 1)
                .subscribeOn(Schedulers.io())
                .map(GankBeautyResultToItemsMapper.getInstance())
                .doOnNext(new Action1<List<ItemModel>>() {
                    @Override
                    public void call(List<ItemModel> list) {
                        //缓存
                        DataFiles.getInstance().writeItems(list);
                    }
                })
                .subscribe(new Action1<List<ItemModel>>() {
                    @Override
                    public void call(List<ItemModel> list) {
                        behaviorSubject.onNext(list);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    /**
     * 清理内存
     */
    public void clearMemory() {
        behaviorSubject = null;
    }

    /**清理内存和磁盘*/
    public void clearMemoryAndDisk(){
        behaviorSubject = null;
        DataFiles.getInstance().delete();
    }
}
