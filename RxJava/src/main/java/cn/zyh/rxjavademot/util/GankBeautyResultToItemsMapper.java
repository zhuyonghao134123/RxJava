package cn.zyh.rxjavademot.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.zyh.rxjavademot.module.map_2.model.GankBeautyModel;
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;
import cn.zyh.rxjavademot.network.api.GankSerializedApi;
import rx.functions.Func1;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/12.
 */
public class GankBeautyResultToItemsMapper implements Func1<GankSerializedApi, List<ItemModel>> {

    private static GankBeautyResultToItemsMapper gankBeautyResultToItemsMapper = null;

    private GankBeautyResultToItemsMapper() {
    }

    public static GankBeautyResultToItemsMapper getInstance() {
        if (gankBeautyResultToItemsMapper == null) {
            gankBeautyResultToItemsMapper = new GankBeautyResultToItemsMapper();
        }
        return gankBeautyResultToItemsMapper;
    }


    public List<ItemModel> call(GankSerializedApi gankSerializedApi) {
        List<GankBeautyModel> gankBeauties = gankSerializedApi.beauties;
        //变换-将GankBeauty 转为Item数据
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        List<ItemModel> items = new ArrayList<ItemModel>(gankBeauties.size());
        for (GankBeautyModel gankModel : gankBeauties) {
            String createdAt = gankModel.createdAt;//获取时间
            String url = gankModel.url; //获取url
            ItemModel itemModel = new ItemModel();
            //格式化时间
            try {
                Date date = inputFormat.parse(createdAt);
                itemModel.description = outputFormat.format(date);

            } catch (ParseException e) {
                e.printStackTrace();
                itemModel.description = "unknown date";
            }
            itemModel.imageUrl = url;
            items.add(itemModel);
        }
        return items;
    }
}
