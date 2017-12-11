package com.example.myutils.base;

/**
 * 项 目 名: MyTest
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/3 14:39
 * 本类描述: 根据“卓商网络科技有限公司”制定的网络请求实体类的提取
 */

public class BaseBean<E> {

    /**
     * result : true
     * msg : 访问成功
     * data : [{"id":0,"name":"苏成艺<-->0","phone":"18948299163","state":"0"},{"id":1,"name":"苏成艺<-->1","phone":"18948299163","state":"0"},{"id":2,"name":"苏成艺<-->2","phone":"18948299163","state":"0"},{"id":3,"name":"苏成艺<-->3","phone":"18948299163","state":"0"},{"id":4,"name":"苏成艺<-->4","phone":"18948299163","state":"0"},{"id":5,"name":"苏成艺<-->5","phone":"18948299163","state":"0"},{"id":6,"name":"苏成艺<-->6","phone":"18948299163","state":"0"},{"id":7,"name":"苏成艺<-->7","phone":"18948299163","state":"0"},{"id":8,"name":"苏成艺<-->8","phone":"18948299163","state":"0"},{"id":9,"name":"苏成艺<-->9","phone":"18948299163","state":"0"}]
     */

    private boolean result;
    private String msg;
    private E data;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public E getData() {
        return data;
    }

    public void setData(E data) {
        this.data = data;
    }
}
