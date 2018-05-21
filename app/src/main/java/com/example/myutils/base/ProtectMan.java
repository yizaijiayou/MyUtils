package com.example.myutils.base;

import java.util.List;

/**
 * Created by Chen on 2017/9/19. 保护人列�?
 */
public class ProtectMan {

    /**
     * result : true
     * msg : 访问成功
     * data : [{"id":0,"name":"苏成艺<-->0","phone":"18948299163","state":"0"},{"id":1,"name":"苏成艺<-->1","phone":"18948299163","state":"0"},{"id":2,"name":"苏成艺<-->2","phone":"18948299163","state":"0"},{"id":3,"name":"苏成艺<-->3","phone":"18948299163","state":"0"},{"id":4,"name":"苏成艺<-->4","phone":"18948299163","state":"0"},{"id":5,"name":"苏成艺<-->5","phone":"18948299163","state":"0"},{"id":6,"name":"苏成艺<-->6","phone":"18948299163","state":"0"},{"id":7,"name":"苏成艺<-->7","phone":"18948299163","state":"0"},{"id":8,"name":"苏成艺<-->8","phone":"18948299163","state":"0"},{"id":9,"name":"苏成艺<-->9","phone":"18948299163","state":"0"}]
     */

    private boolean result;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 0
         * name : 苏成艺<-->0
         * phone : 18948299163
         * state : 0
         */

        private int id;
        private String name;
        private String phone;
        private String state;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }
}
