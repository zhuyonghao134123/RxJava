package cn.zyh.rxjavademot.manager;

import android.app.Application;

/**
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/19.
 */
public class AppManager extends Application {

    private static AppManager INSTANCE;

    public static AppManager getInstance(){
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
