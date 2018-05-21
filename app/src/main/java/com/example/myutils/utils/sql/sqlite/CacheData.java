package com.example.myutils.utils.sql.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

/**
 * 项 目 名: MyUtils
 * 创 建 人: 艺仔加油
 * 创建时间: 2018/4/2 15:13
 * 本类描述: 网络数据的存储，仅存String
 */
public class CacheData {
    public final static String CACHEDATA = "cacheData";

    public static CacheData cacheData;

    public static CacheData getInstance() {
        if (cacheData == null) cacheData = new CacheData();
        return cacheData;
    }

    private SQLiteDatabase sqLiteDatabase;

    private CacheData() {
        ProjectSQLite messagerSqlite = new ProjectSQLite(ProjectSQLite.SQLNAME, ProjectSQLite.SQLVERSION);
        sqLiteDatabase = messagerSqlite.getReadableDatabase();
    }

    //*******************上为初始化，下为操作******************************************************************************

    private int selectCount(String name) {
        int count = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + CACHEDATA + " where name = " + "'" + name + "'" + " order by _id desc", null);
        count = cursor.getCount();
        return count;
    }

    /**
     * 增
     *
     * @param name  参数名
     * @param value 数据
     */
    public void insertCacheData(String name, String value) {
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(value)) {
            throw new NullPointerException("name 和 value 存在空值");
        }

        if (selectCount(name) > 0) {
            updateCacheData(name, value);
        } else {
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("value", value);
            sqLiteDatabase.insert(CACHEDATA, null, contentValues);
        }
    }

    /**
     * 删
     *
     * @param name 参数名
     */
    public void deleteCacheData(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new NullPointerException("name 存在空值");
        }
        sqLiteDatabase.beginTransaction();
        sqLiteDatabase.delete(CACHEDATA, "name = ?", new String[]{name});
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * 查
     *
     * @param name 参数名
     * @return 整一条data数据
     */
    public String selectCacheData(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new NullPointerException("name 存在空值");
        }
        String data = null;

        Cursor cursor = sqLiteDatabase.rawQuery("select * from " + CACHEDATA + " where name = " + "'" + name + "'" + " order by _id desc", null);
        if (cursor.getCount() != 0) {
            cursor.moveToNext();
            data = cursor.getString(cursor.getColumnIndex("value"));
        }
        cursor.close();
        return data;
    }

    /**
     * 改
     *
     * @param name  参数名
     * @param value 数据
     */
    public void updateCacheData(String name, String value) {
        if (TextUtils.isEmpty(name)) {
            throw new NullPointerException("name 存在空值");
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("value", value);
        sqLiteDatabase.update(CACHEDATA, contentValues, null, null);
    }

}
