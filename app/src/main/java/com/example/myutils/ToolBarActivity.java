package com.example.myutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.myutils.base.BaseActivity;
import com.example.myutils.utils.systemUtils.statusBar.StatusBarUtil;

/**
 * 项目名称： MyUtils
 * 创 建 人： 艺仔加油
 * 创建时间： 2017/6/20 10:51
 * 本类描述：
 */

public class ToolBarActivity extends BaseActivity {
    @Override
    public int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setNavigationIcon(R.drawable.selector_press);
        toolbar.setTitle("");
        toolbar.setNavigationOnClickListener(v -> toast("你单击我了"));
        setSupportActionBar(toolbar);//有时候需要这句话才回出现navagationIcon
        toolbar.setOnMenuItemClickListener(null);
    }

    @Override
    public void initView() {
//        StatusBarUtil.transparencyBar(this);
//        StatusBarUtil.setStatusBarColor(this,R.color.viewfinder_laser);
        StatusBarUtil.StatusBarDarkMode(this,1);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void doWork(Context context) {

    }

    private Toolbar.OnMenuItemClickListener onMenuItemClickListener = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.menu1:
//                    Toast.makeText(ToolBarActivity.this,"Item可以单击",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ToolBarActivity.this,MainActivity.class));
                    break;
            }
            return true;
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void getClick(View v){
        toast("asdasdasd");
    }


    /**
     * 按两次返回键退出程序  与   加退出
     */
    private long firstTime;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                firstTime = secondTime;
                toast("再按一次退出程序");
            } else {
                System.exit(0);
                //假退出
//                Intent i = new Intent(Intent.ACTION_MAIN);
//                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addCategory(Intent.CATEGORY_HOME);
//                startActivity(i);
                return true;
            }
        }
        return false;
    }
}
