package com.example.myutils.utils.sql.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.myutils.base.BaseApplication;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2017/11/1 17:46
 * 本类描述: 声明方法
 * ProjectSQLite sqlite = new ProjectSQLite(this,ProjectSQLite.SQLNAME,ProjectSQLite.SQLVERSION);
 * SQLiteDatabase sqLiteDatabase = sqlite.getReadableDatabase();
 * 然后使用sqLiteDatabase即可
 */

public class ProjectSQLite  extends SQLiteOpenHelper {
    public final static String SQLNAME = "ProtectSQLite";
    public final static int SQLVERSION = 4;

    public final static String TABLE_RECORD = "recordConnect";

    public ProjectSQLite( String name, int version) {
        super(BaseApplication.getAppContext(), name, null,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE IF NOT EXISTS "+TABLE_RECORD+"(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "time VARCHAR," +
                "content VARCHAR," +
                "mac VARCHAR,"+
                "name VARCHAR,"+
                "state VARCHAR,"+
                "phone VARCHAR)";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+TABLE_RECORD+"");
        onCreate(sqLiteDatabase);
    }
}
