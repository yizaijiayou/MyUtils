package com.example.myutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.myutils.base.BaseActivity;

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
//        toolbar.setNavigationIcon(R.mipmap.left_guide2);
        toolbar.setNavigationOnClickListener(v -> toast("你单击我了"));

        setSupportActionBar(toolbar);//有时候需要这句话才回出现navagationIcon
        toolbar.setOnMenuItemClickListener(onMenuItemClickListener);
    }

    @Override
    public void initView() {

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
}
