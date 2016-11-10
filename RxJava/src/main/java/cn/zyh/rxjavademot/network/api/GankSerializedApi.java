// (c)2016 Flipboard Inc, All Rights Reserved.

package cn.zyh.rxjavademot.network.api;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import cn.zyh.rxjavademot.module.map_2.model.GankBeautyModel;

public class GankSerializedApi {
    //@SerializedName注解来将对象里的属性跟json里字段对应值匹配起来。
//    public boolean error;

    public boolean error;
    public @SerializedName("results") List<GankBeautyModel> beauties;

}
