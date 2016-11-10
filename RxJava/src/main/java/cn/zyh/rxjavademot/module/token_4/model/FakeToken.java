package cn.zyh.rxjavademot.module.token_4.model;

/**
 * token-model
 * Project_Name:TestInternet
 * Author:朱永豪
 * Email:13838584575@139.com
 * on 2016/10/13.
 */
public class FakeToken {

    public String token;

    public boolean expired; //是否失效

    public FakeToken(){

    }

    public FakeToken(boolean expired){
        this.expired = expired;
    }

}
