package cn.zyh.rxjavademot.module.cache_6.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import cn.zyh.rxjavademot.manager.AppManager;
import cn.zyh.rxjavademot.module.map_2.model.ItemModel;

/**
 * 文件存储类
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/19.
 */
public class DataFiles {

    //文件名称
    private static String DATA_FILE_NAME = "data.db";
    //文件路径 ：  /data/data/files/data.db
    File dataFile = new File(AppManager.getInstance().getFilesDir(), DATA_FILE_NAME);

    //单例
    private static DataFiles instance;

    private DataFiles() {
    }

    public static DataFiles getInstance() {
        if (instance == null) {
            instance = new DataFiles();
        }
        return instance;
    }

    private static Gson gson = new Gson();

    /**
     * 从磁盘读取文件
     * 磁盘文件类型：文件类型
     * 存储格式：gson
     *
     * @return
     */
    public List<ItemModel> readItems() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            FileReader fileReader = new FileReader(dataFile);
            return gson.fromJson(fileReader, new TypeToken<List<ItemModel>>() {
            }.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 向磁盘写文件
     * 写入格式 gson
     */
    public void writeItems(List<ItemModel> list) {
        String json = gson.toJson(list);

        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FileWriter writer = new FileWriter(dataFile);
            writer.write(json);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除文件
     */
    public void delete() {
        dataFile.delete();
    }


}
